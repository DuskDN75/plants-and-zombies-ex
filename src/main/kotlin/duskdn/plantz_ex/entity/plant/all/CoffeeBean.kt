package duskdn.plantz_ex.entity.plant.all

import duskdn.plantz_ex.init.PazEntities
import duskdn.plantz_ex.ai.goal.WakeUpSleepingPlantsGoal
import duskdn.plantz_ex.entity.plant.init.PazPlant
import net.minecraft.core.particles.DustParticleOptions
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class CoffeeBean(type: EntityType<out PazPlant>, level: Level) : PazPlant(PazEntities.COFFEE_BEAN, level) {

    override fun registerGoals() {
        super.registerGoals()
        this.goalSelector.addGoal(1, WakeUpSleepingPlantsGoal(
            this,
            actionDelay = 9,
            actionSuccessEffect = {
                addParticlesAroundSelf(
                    level(),
                    DustParticleOptions(8606770, 1f),
                    amount = 12..14,
                    horizontalSpreadScale = 0.1,
                    verticalSpreadScale = 0.3,
                    height = 0.25f,
                    speed = 0.02
                )
                discard()
            }
        ))
    }
    override fun attackGoals() {}

    override fun canSurviveOn(block: BlockState): Boolean {
        return super.canSurviveOn(block) || !block.getCollisionShape(level(), blockPosition().below()).isEmpty
    }
}