package joshxviii.plantz.entity.plant.utils

import joshxviii.plantz.entity.plant.init.CarrierPlant
import joshxviii.plantz.entity.plant.init.Plant
import joshxviii.plantz.entity.plant.init.Plant.Companion.hasAdjacentPlant
import joshxviii.plantz.entity.plant.traits.PlantHabitatTraits
import joshxviii.plantz.entity.plant.utils.PlantSpawnUtils.validVehicle
import joshxviii.plantz.init.PazBlocks
import joshxviii.plantz.init.PazComponents
import joshxviii.plantz.init.PazTags
import joshxviii.plantz.util.getTotalSun
import joshxviii.plantz.util.removeSunFromStorageAndInventory
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.FluidTags
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.TamableAnimal
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

object PlantSpawnUtils {

    fun tryPlant(
        level: Level,
        player: Player?,
        itemStack: ItemStack,
        pos: BlockPos,
        face: Direction? = null,
        horizontalDir: Direction? = null,
        checkWater: Boolean = false,
        carrier: Plant? = null
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
            !checkWater,
            face == Direction.UP
        )?: return InteractionResult.FAIL

        if (entity is Plant) {
            val spawnBlockCollisionShape = level.getBlockState(spawnPos).getCollisionShape(level, spawnPos).let { if (it.isEmpty.not()) it.bounds() else null }
            val entityBox = entity.boundingBox.move(spawnPos.multiply(-1))

            val validGround = entity.onValidGround(spawnPos.x.toDouble(), spawnPos.y.toDouble(), spawnPos.z.toDouble(), carrier)

            val invalidSpace = !(validGround == null || checkWater)
                    || !(spawnBlockCollisionShape==null || !entityBox.intersects(spawnBlockCollisionShape))

//            println("ValidGround: $validGround, InvalidSpace: $invalidSpace")

            if (validGround != null) {
                player.sendOverlayMessage(
                    Component.translatable("message.plantz.cannot_survive").withStyle(ChatFormatting.RED)
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
            val existingPlants = level.getEntitiesOfClass(Plant::class.java, AABB(it.blockPosition()))
            if (existingPlants.isNotEmpty() && (carrier == null || carrier::class.java == entity::class.java)) {
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

        if (carrier != null && carrier is CarrierPlant && entity is Plant) carrier.setRider(entity)

        return InteractionResult.SUCCESS
    }

    fun validVehicle(type: EntityType<*>, carrier: Plant? = null): Boolean {

        if (carrier == null) {
//            println("Carrier doesn't exist")
            return false
        } // if no carrier, return true

        if (carrier !is CarrierPlant) {
//            println("Carrier is not a Carrier Plant")
            return false
        }

        // if carrier and is not plantable on water, return true
        // else only return true if the carrier allows water plants
        if (type == PazTags.EntityTypes.PLANTABLE_ON_WATER) {
            val waterAllowed = carrier.`is`(PazTags.EntityTypes.CARRIER_ALLOW_WATER)

//            println("Plant is plantable on water. Carrier allows water plants? : $waterAllowed")

            return waterAllowed
        }

//        println("Plant is normal")

        return true

    }

}

data class InvalidGroundReasons (
    val tooClose: Boolean = false,
    val invalidSpace: Boolean = false,
    val invalidCarrier: Boolean = false
)

// PLANT SPAWN CHECKING

fun Plant.checkValidGround(x: Double = this.x, y: Double = this.y, z: Double = this.z, carrier: Plant? = vehicle as Plant?) : InvalidGroundReasons {

    val belowBlock = getBlockBelow(x, y, z)

    val hasValidVehicle = validVehicle(this.type, carrier)

    val blockPosition = BlockPos.containing(x,y,z)

    val canSurvive = canSurviveOn(belowBlock) || canSurviveOn(belowBlock)

    val hasAdjacent = hasAdjacentPlant(level(), blockPosition, this)

//    println("Plant: ${this.type}, attachedEntity: $attachedEntity, canSurvive: $canSurvive, hasValidVehicle: $hasValidVehicle, hasAdjacent: $hasAdjacent, carrier: $carrier")

    return InvalidGroundReasons(hasAdjacent, !canSurvive, !hasValidVehicle)
}

fun Plant.onValidGround(x: Double = this.x, y: Double = this.y, z: Double = this.z, carrier: Plant? = vehicle as Plant?) : InvalidGroundReasons? {
    val reasons = checkValidGround(x,y,z,carrier)

    val invalidGround = (reasons.invalidSpace && reasons.invalidCarrier) || reasons.tooClose

//    println("Plant: ${this.type}, validGround: ${!invalidGround}, reasons: $reasons")

    if (invalidGround) return reasons

    return null
}

fun Plant.waterSurvivalCheck(block: BlockState): Boolean {
    if (block.`is`(PazBlocks.ZEN_PLANT_POT)) {
        return true
    }

    if (block.`is`(PazBlocks.WATER_POT)) {
        return true
    }

    if (block.`is`(Blocks.WATER_CAULDRON)) {
        return block.getValue(BlockStateProperties.LEVEL_CAULDRON) > 0
    }

    val fluidState = level().getFluidState(blockPosition())

    return fluidState.`is`(FluidTags.WATER)
}