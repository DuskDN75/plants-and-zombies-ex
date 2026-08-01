package duskdn.plantz.entity.plant

import duskdn.plantz.entity.plant.init.CarrierPlant
import duskdn.plantz.entity.plant.utils.PlantSpawnUtils
import duskdn.plantz.entity.plant.utils.PlantUtils
import duskdn.plantz.init.PazEntities
import net.minecraft.world.entity.Entity
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