package joshxviii.plantz.entity.plant

import joshxviii.plantz.entity.plant.init.CarrierPlant
import joshxviii.plantz.entity.plant.init.Plant
import joshxviii.plantz.entity.plant.utils.waterSurvivalCheck
import joshxviii.plantz.init.PazEntities
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags
import net.minecraft.core.BlockPos
import net.minecraft.tags.BiomeTags
import net.minecraft.tags.FluidTags
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.ServerLevelAccessor
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import kotlin.math.abs

class LilyPad(type: EntityType<out CarrierPlant>, level: Level) : CarrierPlant(PazEntities.LILYPAD, level) {

    companion object {
        fun checkLilyPadSpawnRules(
            type: EntityType<out Plant>,
            level: ServerLevelAccessor,
            spawnReason: EntitySpawnReason,
            pos: BlockPos,
            random: RandomSource
        ): Boolean {
            val isRaining = level.level.isRaining
            val inWater = level.getFluidState(pos).`is`(FluidTags.WATER)
            val rainBonus = if (isRaining) 2.25f else 1f

            val biome = level.getBiome(pos)

            val swampBonus = if (biome.`is`(ConventionalBiomeTags.IS_SWAMP)) 2.25f else 1f

            return checkValidSpawn(level, pos, spawnReason)
                    && inWater && random.nextFloat() < (0.5 * rainBonus * swampBonus)
        }
    }

    override fun isPushedByFluid(): Boolean {
        return false
    }

    override fun registerGoals() {
        super.registerGoals()
    }

    override fun doWaterSplashEffect() {

    }



    override fun tick() {
        super.tick()

        if (this.isInWater) {

            val level = this.level()
            val blockPos = this.blockPosition()
            val fluidState = level.getFluidState(blockPos)

            val fluidHeight = fluidState.getHeight(level, blockPos)
            if (fluidHeight <= 0.0f || fluidState.isEmpty) {
                return
            }

            val waterSurfaceY = blockPos.y.toDouble() + fluidHeight.toDouble()

            val distance = waterSurfaceY - this.y

            if (abs(distance) < 0.2) {

                this.setDeltaMovement(this.x, 0.0, this.z)

                this.setPosRaw(this.x, waterSurfaceY, this.z)
            }
        }
    }

    override fun canBreatheUnderwater(): Boolean = true

    override fun canSurviveOn(block: BlockState): Boolean {
        val waterSurvival = waterSurvivalCheck(block)
//        println("Can survive in water: $waterSurvival")
        return waterSurvival
    }

//    override fun isNoGravity(): Boolean {
//        return this.isInWater || super.isNoGravity()
//    }
}