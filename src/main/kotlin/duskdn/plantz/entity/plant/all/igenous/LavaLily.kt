package duskdn.plantz.entity.plant.all.igenous

import duskdn.plantz.entity.plant.init.CarrierPlant
import duskdn.plantz.entity.plant.init.PazPlant
import duskdn.plantz.entity.plant.utils.lavaSurvivalCheck
import duskdn.plantz.init.PazEntities
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.abs

class LavaLily(type: EntityType<out CarrierPlant>, level: Level) : CarrierPlant(PazEntities.LAVALILY, level) {

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
        return lavaSurvivalCheck(block)
    }
}