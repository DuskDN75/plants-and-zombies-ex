package joshxviii.plantz.entity.plant

import joshxviii.plantz.entity.plant.init.CarrierPlant
import joshxviii.plantz.entity.plant.init.Plant
import joshxviii.plantz.init.PazEntities
import net.minecraft.tags.FluidTags
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import kotlin.math.abs

class LilyPad(type: EntityType<out CarrierPlant>, level: Level) : CarrierPlant(PazEntities.LILYPAD, level) {

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
            }
        }
    }

    override fun canBreatheUnderwater(): Boolean = true

    override fun canSurviveOn(block: BlockState): Boolean {
        return waterSurvivalCheck(block)
    }

//    override fun isNoGravity(): Boolean {
//        return this.isInWater || super.isNoGravity()
//    }
}