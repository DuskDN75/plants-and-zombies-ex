package duskdn.plantz_ex.entity.plant.all.mushrooms

import duskdn.plantz_ex.init.PazEntities
import duskdn.plantz_ex.ai.PlantState
import duskdn.plantz_ex.ai.goal.GenerateSunGoal
import duskdn.plantz_ex.entity.plant.init.PazPlant
import duskdn.plantz_ex.entity.plant.utils.mushroomSurvivalCheck
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class SunShroom(
    type: EntityType<out PazPlant>,
    level: Level,
) : PazPlant(PazEntities.SUN_SHROOM, level) {
    override fun attackGoals() {}

    override fun registerGoals() {
        super.registerGoals()
        this.goalSelector.addGoal(1, GenerateSunGoal(
            usingEntity = this,
            actionDelay = 10,
            generatesAtNight = true,
            sunAmount = 2
        ))
    }

    override fun stateUpdated(state: PlantState) {
        if (state == PlantState.INIT) {
            isBaby = true
            age = -3400
        }
        super.stateUpdated(state)
    }

    override fun canSurviveOn(block: BlockState): Boolean {
        return super.canSurviveOn(block) || mushroomSurvivalCheck(block)
    }

}