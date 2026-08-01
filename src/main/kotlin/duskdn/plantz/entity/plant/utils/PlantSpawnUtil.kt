package duskdn.plantz.entity.plant.utils

import duskdn.plantz.entity.Sun
import duskdn.plantz.entity.plant.init.CarrierPlant
import duskdn.plantz.entity.plant.init.PazPlant
import duskdn.plantz.entity.plant.init.PazPlant.Companion.hasAdjacentPlant
import duskdn.plantz.entity.plant.utils.PlantSpawnUtils.validVehicle
import duskdn.plantz.init.PazBlocks
import duskdn.plantz.init.PazComponents
import duskdn.plantz.init.PazTags
import duskdn.plantz.util.getTotalSun
import duskdn.plantz.util.removeSunFromStorageAndInventory
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.FluidTags
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.TamableAnimal
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.monster.zombie.Zombie
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.phys.AABB

object PlantSpawnUtils {

    fun tryPlant(
        level: Level,
        player: Player?,
        itemStack: ItemStack,
        pos: BlockPos,
        face: Direction? = null,
        horizontalDir: Direction? = null,
        checkWater: Boolean = false,
        checkLava: Boolean = false,
        carrier: PazPlant? = null
    ): InteractionResult {

        println(carrier)

        if (level !is ServerLevel || player == null) return InteractionResult.PASS

        val component = itemStack.get(DataComponents.ENTITY_DATA)
        val entityType = component?.type()

        val spawnPos = if (level.getBlockState(pos).getCollisionShape(level, pos).isEmpty) pos
        else if (face != null) pos.relative(face) else pos

        val availableSun = player.getTotalSun()
        val sunCost = itemStack.get(PazComponents.SUN_COST)?.getSunCost(entityType) ?: 0
        if (sunCost > availableSun && !player.hasInfiniteMaterials()) {
            player.sendOverlayMessage(
                Component.translatable("message.plantz.not_enough_sun", availableSun, sunCost).withStyle(ChatFormatting.RED)
            )
            return InteractionResult.FAIL
        }

        val entity = entityType?.create(
            level,
            EntityType.createDefaultStackConfig(level, itemStack, player),
            spawnPos,
            EntitySpawnReason.SPAWN_ITEM_USE,
            !checkWater && !checkLava,
            face == Direction.UP
        )?: return InteractionResult.FAIL

        if (entity is PazPlant) {
            val spawnBlockCollisionShape = level.getBlockState(spawnPos).getCollisionShape(level, spawnPos).let { if (it.isEmpty.not()) it.bounds() else null }
            val entityBox = entity.boundingBox.move(spawnPos.multiply(-1))

            val validGround = entity.onValidGround(spawnPos.x.toDouble(), spawnPos.y.toDouble(), spawnPos.z.toDouble(), carrier)

            val invalidSpace = !(validGround == null || checkWater || checkLava)
                    || !(spawnBlockCollisionShape==null || !entityBox.intersects(spawnBlockCollisionShape))

            println("ValidGround: $validGround, InvalidSpace: $invalidSpace")

            if (validGround != null) {

                var messageKey = when {
                    validGround.tooClose -> "message.plantz.too_close"
                    validGround.invalidCarrier -> "message.plantz.cannot_be_placed"
                    else -> {"message.plantz.cannot_survive"}
                }

                when {
                    validGround.tooClose -> messageKey = "message.plantz.too_close"
                    validGround.invalidCarrier -> messageKey = "message.plantz.cannot_be_placed"
                }

                player.sendOverlayMessage(
                    Component.translatable(messageKey).withStyle(ChatFormatting.RED)
                )
                return InteractionResult.FAIL
            }

            if (horizontalDir != null) {
                val yaw = horizontalDir.opposite.toYRot()
                entity.yHeadRot = yaw
                entity.yBodyRot = yaw
                entity.yRot = yaw
            }

        }

        entity.let {
            val existingPlants = level.getEntitiesOfClass(PazPlant::class.java, AABB(it.blockPosition()))
            if (existingPlants.isNotEmpty() && (carrier == null || carrier::class.java == entity::class.java || carrier.passengers.isNotEmpty())) {
                player.sendOverlayMessage(
                    Component.translatable("message.plantz.already_planted").withStyle(ChatFormatting.RED)
                )
                return InteractionResult.FAIL
            }
        }

        if (!level.addFreshEntity(entity)) {
            entity.discard()
            return InteractionResult.FAIL
        }

        itemStack.consume(1, player)
        if (!player.hasInfiniteMaterials()) {
            player.removeSunFromStorageAndInventory(sunCost)
        }
        entity.playSound(SoundEvents.BIG_DRIPLEAF_PLACE)
        if (entity is TamableAnimal) entity.tame(player)
        level.gameEvent(player, GameEvent.ENTITY_PLACE, spawnPos)

        println("CARRIER IS: $carrier")

        if (carrier != null && carrier is CarrierPlant && entity is PazPlant) carrier.setRider(entity)

        return InteractionResult.SUCCESS
    }

    fun validVehicle(plant: PazPlant, carrier: PazPlant? = null): Boolean {

        val plantType = plant.type

        if (carrier == null) {
//            println("Carrier doesn't exist")
            return false
        } // if no carrier, return true

        val carrierType = carrier.type

        if (carrier !is CarrierPlant) {
//            println("Carrier is not a Carrier Plant")
            return false
        }

        if (carrier.passengers.isNotEmpty() && carrier.firstPassenger != plant) {
            return false
        }

        if (carrierType == plantType) {
            return false
        }

        val plantableOnWater = BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(plantType).`is`(PazTags.EntityTypes.PLANTABLE_ON_WATER)

        val amphibious = BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(plantType).`is`(PazTags.EntityTypes.AMPHIBIOUS)

        println("PlantType: $plantType, carrier: $carrier, PlantableOnWater: $plantableOnWater, Amphibious: $amphibious")

        val waterAllowed = BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(carrierType).`is`(PazTags.EntityTypes.CARRIER_ALLOW_WATER)

        // if carrier and is not plantable on water, return true
        // else only return true if the carrier allows water plants
        if (plantableOnWater && !amphibious) {
            println("Plant is plantable on water. Carrier allows water plants? : $waterAllowed")

            return waterAllowed
        }

        if (waterAllowed && !plantableOnWater) {
            return false
        }

//        println("Plant is normal")

        return true

    }

    fun solidFloorCheck(level: Level, belowPos: BlockPos, block: BlockState): Boolean {
        val isWater = block.`is`(Blocks.WATER) || block.fluidState.`is`(Fluids.WATER)

        val isLava = block.`is`(Blocks.LAVA) || block.fluidState.`is`(Fluids.LAVA)

        if (isWater || isLava) return false

        return !block.getCollisionShape(level, belowPos).isEmpty
    }

}

data class InvalidGroundReasons (
    val tooClose: Boolean = false,
    val invalidSpace: Boolean = false,
    val invalidCarrier: Boolean = false
)

// PLANT SPAWN CHECKING

fun PazPlant.checkValidGround(x: Double = this.x, y: Double = this.y, z: Double = this.z, carrier: PazPlant? = vehicle as PazPlant?) : InvalidGroundReasons {

    val belowBlock = getBlockBelow(x, y, z)

    val hasValidVehicle = validVehicle(this, carrier)

    val blockPosition = BlockPos.containing(x,y,z)

    val canSurvive = if (carrier == null) canSurviveOn(belowBlock) else false

    val hasAdjacent = hasAdjacentPlant(level(), blockPosition, this)

//    println("Plant: ${this.type}, attachedEntity: $attachedEntity, canSurvive: $canSurvive, hasValidVehicle: $hasValidVehicle, hasAdjacent: $hasAdjacent, carrier: $carrier")

    return InvalidGroundReasons(hasAdjacent, !canSurvive, !hasValidVehicle)
}

fun PazPlant.onValidGround(x: Double = this.x, y: Double = this.y, z: Double = this.z, carrier: PazPlant? = vehicle as PazPlant?, print: Boolean = false) : InvalidGroundReasons? {
    val reasons = checkValidGround(x,y,z,carrier)

    val validSpace = !reasons.invalidSpace

    val validCarrier = !reasons.invalidCarrier

    val validDistance = !reasons.tooClose

    val validGround = (validSpace || validCarrier) && validDistance

//    println("type: ${this.type}, validSpace: $validSpace, validCarrier: $validCarrier, validDistance: $validDistance, validGround: $validGround")

    if (!validGround) return reasons

    return null
}

fun PazPlant.waterSurvivalCheck(block: BlockState): Boolean {
    if (block.`is`(PazBlocks.ZEN_POT)) {
        return true
    }

    if (block.`is`(Blocks.WATER_CAULDRON)) {
        return block.getValue(BlockStateProperties.LEVEL_CAULDRON) > 0
    }

    val fluidState = level().getFluidState(blockPosition())

    return fluidState.`is`(FluidTags.WATER)
}

fun PazPlant.lavaSurvivalCheck(block: BlockState): Boolean {
    if (block.`is`(PazBlocks.ZEN_POT)) {
        return true
    }

    if (block.`is`(Blocks.LAVA_CAULDRON)) {
        return block.getValue(BlockStateProperties.LEVEL_CAULDRON) > 0
    }

    val fluidState = level().getFluidState(blockPosition())

    return fluidState.`is`(FluidTags.LAVA)
}