package duskdn.plantz_ex.entity.plant.all.igenous

import duskdn.plantz_ex.entity.plant.init.CarrierPlant
import duskdn.plantz_ex.entity.plant.init.PazPlant
import duskdn.plantz_ex.entity.plant.interfaces.IIgneousPlant
import duskdn.plantz_ex.entity.plant.utils.lavaSurvivalCheck
import duskdn.plantz_ex.init.PazEntities
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class LavaLily(type: EntityType<out CarrierPlant>, level: Level) : CarrierPlant(PazEntities.LAVALILY, level), IIgneousPlant {

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

//    override fun canStandOnFluid(fluid: FluidState): Boolean {
//        return fluid.`is`(FluidTags.LAVA) || super.canStandOnFluid(fluid)
//    }

    override var buoyancyHeight: Double = 0.9

    override fun registerGoals() {
        super.registerGoals()
    }

    override fun aiStep() {
        applyBuoyancy()

        super.aiStep()
    }

    override fun canBreatheUnderwater(): Boolean = true

    override fun canSurviveOn(block: BlockState): Boolean {
        return lavaSurvivalCheck(block)
    }
}