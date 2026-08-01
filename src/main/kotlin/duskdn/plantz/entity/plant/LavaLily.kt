package duskdn.plantz.entity.plant

import duskdn.plantz.entity.plant.init.CarrierPlant
import duskdn.plantz.entity.plant.init.PazPlant
import duskdn.plantz.entity.plant.utils.PlantSpawnUtils
import duskdn.plantz.entity.plant.utils.PlantUtils
import duskdn.plantz.entity.plant.utils.lavaSurvivalCheck
import duskdn.plantz.entity.plant.utils.waterSurvivalCheck
import duskdn.plantz.init.PazEntities
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.FluidTags
import net.minecraft.util.RandomSource
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import net.minecraft.world.level.ServerLevelAccessor
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.abs

class LavaLily(type: EntityType<out CarrierPlant>, level: Level) : CarrierPlant(PazEntities.LAVALILY, level) {

    companion object {
        fun checkLavaLilySpawnRules(
            type: EntityType<out PazPlant>,
            level: ServerLevelAccessor,
            spawnReason: EntitySpawnReason,
            pos: BlockPos,
            random: RandomSource
        ): Boolean {
            val inLava = level.getFluidState(pos).`is`(FluidTags.LAVA)

            return checkValidSpawn(level, pos, spawnReason)
                    && inLava && random.nextFloat() < 0.5
        }
    }

    override fun setRider(plant: PazPlant) {
        super.setRider(plant)
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

    override fun tick() {
        super.tick()

        if (this.isInLava) {

            val level = this.level()
            val blockPos = this.blockPosition()
            val fluidState = level.getFluidState(blockPos)

            val fluidHeight = fluidState.getHeight(level, blockPos)
            if (fluidHeight <= 0.0f || fluidState.isEmpty) {
                return
            }

            val waterSurfaceY = blockPos.y.toDouble() + fluidHeight.toDouble() - 0.01

            val distance = waterSurfaceY - this.y

            if (abs(distance) < 0.2) {

                this.setDeltaMovement(this.x, 0.0, this.z)

                this.setPosRaw(this.x, waterSurfaceY, this.z)
            }
        }
    }

    override fun canBreatheUnderwater(): Boolean = true

    override fun canSurviveOn(block: BlockState): Boolean {
        val lavaSurvival = lavaSurvivalCheck(block)
//        println("Can survive in water: $waterSurvival")
        return lavaSurvival
    }

//    override fun isNoGravity(): Boolean {
//        return this.isInWater || super.isNoGravity()
//    }
}