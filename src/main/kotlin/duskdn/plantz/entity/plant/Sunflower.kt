package duskdn.plantz.entity.plant

import duskdn.plantz.init.PazEntities
import duskdn.plantz.ai.goal.GenerateSunGoal
import duskdn.plantz.entity.plant.init.PazPlant
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