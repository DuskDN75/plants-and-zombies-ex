package duskdn.plantz_ex.entity.plant.all

import duskdn.plantz_ex.init.PazEntities
import duskdn.plantz_ex.ai.goal.GenerateSunGoal
import duskdn.plantz_ex.entity.plant.init.PazPlant
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level

class Sunflower(
    type: EntityType<out PazPlant>,
    level: Level,
) : PazPlant(PazEntities.SUNFLOWER, level) {
    override fun attackGoals() {}

    override fun sleepsDuringNight(): Boolean = true

    override fun registerGoals() {
        super.registerGoals()
        this.goalSelector.addGoal(1, GenerateSunGoal(
            usingEntity = this,
            actionDelay = 10,
            generatesAtNight = true
        ))
    }
}