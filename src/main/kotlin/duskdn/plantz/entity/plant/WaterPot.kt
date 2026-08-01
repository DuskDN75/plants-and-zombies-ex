package duskdn.plantz.entity.plant

import duskdn.plantz.entity.plant.init.CarrierPlant
import duskdn.plantz.entity.plant.init.PazPlant
import duskdn.plantz.entity.plant.utils.PlantSpawnUtils
import duskdn.plantz.entity.plant.utils.PlantUtils
import duskdn.plantz.entity.plant.utils.waterSurvivalCheck
import duskdn.plantz.init.PazBlocks
import duskdn.plantz.init.PazEntities
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags
import net.minecraft.core.BlockPos
import net.minecraft.tags.FluidTags
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import net.minecraft.world.level.ServerLevelAccessor
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluids
import kotlin.math.abs

class WaterPot(type: EntityType<out CarrierPlant>, level: Level) : CarrierPlant(PazEntities.WATER_POT, level) {

    companion object {
    }

    override fun attackGoals() {}

    override fun isPushedByFluid(): Boolean {
        return false
    }

    override fun allowPlayerCollision(): Boolean {
        return true
    }

    override fun registerGoals() {
        super.registerGoals()
    }

    override fun canSurviveOn(block: BlockState): Boolean {
        return super.canSurviveOn(block) || PlantSpawnUtils.solidFloorCheck(level(), blockPosition().below(), block)
    }
}