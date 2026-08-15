package duskdn.plantz_ex.entity.plant.all

import duskdn.plantz_ex.entity.plant.init.CarrierPlant
import duskdn.plantz_ex.entity.plant.utils.PlantSpawnUtils
import duskdn.plantz_ex.init.PazEntities
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class FlowerPot(type: EntityType<out CarrierPlant>, level: Level) : CarrierPlant(PazEntities.FLOWER_POT, level) {

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