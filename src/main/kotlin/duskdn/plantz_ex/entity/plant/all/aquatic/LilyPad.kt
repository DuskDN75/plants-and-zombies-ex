package duskdn.plantz_ex.entity.plant.all.aquatic

import duskdn.plantz_ex.entity.plant.init.CarrierPlant
import duskdn.plantz_ex.entity.plant.interfaces.IAquaticPlant
import duskdn.plantz_ex.entity.plant.utils.waterSurvivalCheck
import duskdn.plantz_ex.init.PazEntities
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class LilyPad(type: EntityType<out CarrierPlant>, level: Level) : CarrierPlant(PazEntities.LILYPAD, level), IAquaticPlant {

    override fun attackGoals() {}

    override fun isPushedByFluid(): Boolean {
        return false
    }

//    override fun canStandOnFluid(fluid: FluidState): Boolean {
//        return fluid.`is`(FluidTags.WATER) || super.canStandOnFluid(fluid)
//    }

    override fun allowPlayerCollision(): Boolean {
        return true
    }

    override var buoyancyHeight: Double = 0.9

    override fun registerGoals() {
        super.registerGoals()
    }



    override fun doWaterSplashEffect() {

    }


    override fun aiStep() {
        applyBuoyancy()

        super.aiStep()
    }

    override fun canBreatheUnderwater(): Boolean = true

    override fun canSurviveOn(block: BlockState): Boolean {
        return waterSurvivalCheck(block)
    }

//    override fun isNoGravity(): Boolean {
//        return this.isInWater || super.isNoGravity()
//    }
}