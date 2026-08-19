package duskdn.plantz_ex.entity.plant.utils

import duskdn.plantz_ex.entity.plant.init.CarrierPlant
import duskdn.plantz_ex.entity.plant.init.PazPlant
import duskdn.plantz_ex.entity.plant.utils.PlantSpawnUtils.hasAdjacentPlant
import duskdn.plantz_ex.entity.plant.utils.PlantSpawnUtils.validVehicle
import duskdn.plantz_ex.init.PazBlocks
import duskdn.plantz_ex.init.PazComponents
import duskdn.plantz_ex.init.PazTags
import duskdn.plantz_ex.item.SeedPacketItem
import duskdn.plantz_ex.util.debugPrint
import duskdn.plantz_ex.util.getTotalSun
import duskdn.plantz_ex.util.removeSunFromStorageAndInventory
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.BlockTags
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
        checkFluid: Boolean = false,
        carrier: PazPlant? = null
    ): InteractionResult {

        debugPrint(carrier)

        if (level !is ServerLevel || player == null) return InteractionResult.PASS

        val component = itemStack.get(DataComponents.ENTITY_DATA)
        val entityType = component?.type()

        val spawnPos = if (level.getBlockState(pos).getCollisionShape(level, pos).isEmpty) pos
        else if (face != null) pos.relative(face) else pos

        val availableSun = player.getTotalSun()
        val sunCost = itemStack.get(PazComponents.SUN_COST)?.getSunCost(entityType) ?: 0
        if (sunCost > availableSun && !player.hasInfiniteMaterials()) {
            player.sendOverlayMessage(
                Component.translatable("message.plantz_ex.not_enough_sun", availableSun, sunCost).withStyle(ChatFormatting.RED)
            )
            return InteractionResult.FAIL
        }

        val entity = entityType?.create(
            level,
            EntityType.createDefaultStackConfig(level, itemStack, player),
            spawnPos,
            EntitySpawnReason.SPAWN_ITEM_USE,
            !checkFluid,
            face == Direction.UP
        )?: return InteractionResult.FAIL

        if (entity is PazPlant) {
            val spawnBlockCollisionShape = level.getBlockState(spawnPos).getCollisionShape(level, spawnPos).let { if (it.isEmpty.not()) it.bounds() else null }
            val entityBox = entity.boundingBox.move(spawnPos.multiply(-1))

            val validGround = entity.onValidGround(spawnPos.x.toDouble(), spawnPos.y.toDouble(), spawnPos.z.toDouble(), carrier)

            val belowBlock = entity.getBlockBelow(spawnPos.x.toDouble(), spawnPos.y.toDouble(), spawnPos.z.toDouble())

            val canPlace = entity.canPlaceOn(belowBlock, carrier)

            val invalidSpace = !(validGround == null || checkFluid)
                    || !(spawnBlockCollisionShape==null || !entityBox.intersects(spawnBlockCollisionShape))

            debugPrint("ValidGround: $validGround, InvalidSpace: $invalidSpace")

            if (validGround != null && canPlace) {

                var messageKey = when {
                    validGround.tooClose -> "message.plantz_ex.too_close"
                    validGround.invalidCarrier -> "message.plantz_ex.cannot_be_placed"
                    else -> {"message.plantz_ex.cannot_survive"}
                }

                when {
                    validGround.tooClose -> messageKey = "message.plantz_ex.too_close"
                    validGround.invalidCarrier -> messageKey = "message.plantz_ex.cannot_be_placed"
                }

                player.sendOverlayMessage(
                    Component.translatable(messageKey, entity.name.copy().withStyle(
                        ChatFormatting.RED)).withStyle(ChatFormatting.DARK_RED)
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
                    Component.translatable("message.plantz_ex.already_planted").withStyle(ChatFormatting.RED)
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
            val success = player.removeSunFromStorageAndInventory(sunCost)

            if (!success) return InteractionResult.FAIL
        }
        entity.playSound(SoundEvents.BIG_DRIPLEAF_PLACE)
        if (entity is TamableAnimal) entity.tame(player)
        level.gameEvent(player, GameEvent.ENTITY_PLACE, spawnPos)

        debugPrint("CARRIER IS: $carrier")

        if (carrier != null && carrier is CarrierPlant && entity is PazPlant) carrier.setRider(entity)

        (itemStack.item as SeedPacketItem).applyCooldown(itemStack, player)

        return InteractionResult.SUCCESS
    }

    fun validVehicle(plant: PazPlant, carrier: PazPlant? = null): Boolean {

        val plantType = plant.type

        if (carrier == null) {
//            debugPrint("Carrier doesn't exist")
            return false
        } // if no carrier, return true

        val carrierType = carrier.type

        if (carrier !is CarrierPlant) {
//            debugPrint("Carrier is not a Carrier Plant")
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

//        debugPrint("PlantType: $plantType, carrier: $carrier, PlantableOnWater: $plantableOnWater, Amphibious: $amphibious")

        val waterAllowed = BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(carrierType).`is`(PazTags.EntityTypes.CARRIER_ALLOW_WATER)

        // if carrier and is not plantable on water, return true
        // else only return true if the carrier allows water plants
        if (plantableOnWater && !amphibious) {
//            debugPrint("Plant is plantable on water. Carrier allows water plants? : $waterAllowed")

            return waterAllowed
        }

        if (waterAllowed && !plantableOnWater) {
            return false
        }

//        debugPrint("Plant is normal")

        return carrier.checkRider(plant)

    }

    fun solidFloorCheck(level: Level, pos: BlockPos, block: BlockState): Boolean {
        val isWater = block.`is`(Blocks.WATER) || block.fluidState.`is`(Fluids.WATER)

        val isLava = block.`is`(Blocks.LAVA) || block.fluidState.`is`(Fluids.LAVA)

        val isAir = block.`is`(Blocks.AIR) || block.getCollisionShape(level, pos).isEmpty

        return !(isWater || isLava || isAir)
    }

    /**
     * Checks for nearby plants in a 3x3 radius, and excludes itself.
     */
    fun hasAdjacentPlant(level: Level, pos: BlockPos, ogPlant: PazPlant? = null) : Boolean {

        val searchBox = AABB(pos).inflate(1.0, 0.0, 1.0)

        val plants = level.getEntitiesOfClass(PazPlant::class.java, searchBox) { plant ->
            plant.isAlive && (ogPlant != null && (ogPlant != plant && plant != ogPlant.vehicle) || plant.blockPosition() != pos)
        }

        return plants.isNotEmpty()
    }

    fun canSurviveDefault(block: BlockState): Boolean {
        return block.`is`(PazTags.BlockTags.PLANTABLE)
    }

    fun canSurviveFire(block: BlockState): Boolean {
        return block.`is`(BlockTags.BASE_STONE_NETHER) || block.`is`(Blocks.BASALT) || block.`is`(Blocks.GRAVEL)
    }

    fun canSurviveSand(block: BlockState): Boolean {
        return block.`is`(BlockTags.SAND) || block.`is`(Blocks.SOUL_SAND)
    }

    fun canSurviveGravel(block: BlockState): Boolean {
        return block.`is`(BlockTags.CONCRETE_POWDERS) || block.`is`(Blocks.GRAVEL)
    }

    fun canSurviveSnow(block: BlockState): Boolean {
        return block.`is`(BlockTags.SNOW)
    }

    fun canSurviveStone(block: BlockState): Boolean {
        return block.`is`(BlockTags.BASE_STONE_OVERWORLD) || block.`is`(Blocks.COBBLESTONE)
    }

    fun canSurviveFree(block: BlockState): Boolean {
        return !block.`is`(BlockTags.AIR)
    }

    fun canSurviveWater(type: EntityType<out Entity>, level: Level, block: BlockState, pos: BlockPos): Boolean {
        if (block.`is`(PazBlocks.ZEN_POT)) {
            return true
        }

        if (block.`is`(Blocks.WATER_CAULDRON)) {
            return block.getValue(BlockStateProperties.LEVEL_CAULDRON) > 0
        }

        val waterAllowed = BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(type).`is`(PazTags.EntityTypes.PLANTABLE_ON_WATER)

        val fluidState = level.getFluidState(pos)

        return fluidState.`is`(FluidTags.WATER) && waterAllowed
    }

    fun canSurviveLava(type: EntityType<out Entity>, level: Level, block: BlockState, pos: BlockPos): Boolean {
        if (block.`is`(PazBlocks.ZEN_POT)) {
            return true
        }

        if (block.`is`(Blocks.LAVA_CAULDRON)) {
            return block.getValue(BlockStateProperties.LEVEL_CAULDRON) > 0
        }

        val lavaAllowed = BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(type).`is`(PazTags.EntityTypes.PLANTABLE_ON_LAVA)

        val fluidState = level.getFluidState(pos)

        return fluidState.`is`(FluidTags.LAVA) && lavaAllowed
    }

    fun canSurviveAir(type: EntityType<out Entity>, level: Level, block: BlockState, pos: BlockPos): Boolean {

        val airAllowed = BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(type).`is`(PazTags.EntityTypes.PLANTABLE_ON_AIR)

        return (block.`is`(Blocks.AIR) || block.getCollisionShape(level, pos).isEmpty) && airAllowed
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

    val hasAdjacent = hasAdjacentPlant(level(), blockPosition, this) && checkForAdjacent && !isGrowingSeeds

//    debugPrint("Plant: ${this.type}, attachedEntity: $attachedEntity, canSurvive: $canSurvive, hasValidVehicle: $hasValidVehicle, hasAdjacent: $hasAdjacent, carrier: $carrier")

    return InvalidGroundReasons(hasAdjacent, !canSurvive, !hasValidVehicle)
}

fun PazPlant.onValidGround(x: Double = this.x, y: Double = this.y, z: Double = this.z, carrier: PazPlant? = vehicle as PazPlant?, print: Boolean = false) : InvalidGroundReasons? {
    val reasons = checkValidGround(x,y,z,carrier)

    val validSpace = !reasons.invalidSpace

    val validCarrier = !reasons.invalidCarrier

    val validDistance = !reasons.tooClose

    val validGround = (validSpace || validCarrier) && validDistance

//    debugPrint("type: ${this.type}, validSpace: $validSpace, validCarrier: $validCarrier, validDistance: $validDistance, validGround: $validGround")

    if (!validGround) return reasons

    return null
}

fun PazPlant.waterSurvivalCheck(block: BlockState): Boolean {
    return PlantSpawnUtils.canSurviveWater(this.type, level(),block,blockPosition())
}

fun PazPlant.lavaSurvivalCheck(block: BlockState): Boolean {
    return PlantSpawnUtils.canSurviveLava(this.type, level(),block,blockPosition())
}

fun PazPlant.airSurvivalCheck(block: BlockState): Boolean {
    return PlantSpawnUtils.canSurviveAir(this.type, level(),block,blockPosition())
}

fun PazPlant.sandSurvivalCheck(block: BlockState): Boolean {
    return PlantSpawnUtils.canSurviveSand(block)
}

fun PazPlant.gravelSurvivalCheck(block: BlockState): Boolean {
    return PlantSpawnUtils.canSurviveGravel(block)
}

fun PazPlant.snowSurvivalCheck(block: BlockState): Boolean {
    return PlantSpawnUtils.canSurviveSnow(block)
}

fun PazPlant.fireSurvivalCheck(block: BlockState): Boolean {
    return PlantSpawnUtils.canSurviveFire(block)
}

fun PazPlant.stoneSurvivalCheck(block: BlockState): Boolean {
    return PlantSpawnUtils.canSurviveStone(block) || PlantSpawnUtils.canSurviveFire(block)
}

fun PazPlant.mushroomSurvivalCheck(block: BlockState): Boolean {
    return PlantSpawnUtils.canSurviveSnow(block) || PlantSpawnUtils.canSurviveGravel(block)
}