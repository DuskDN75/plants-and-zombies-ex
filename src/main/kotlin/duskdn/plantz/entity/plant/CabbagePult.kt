package duskdn.plantz.entity.plant

import duskdn.plantz.init.PazEntities
import duskdn.plantz.ai.goal.ProjectileAttackGoal
import duskdn.plantz.entity.plant.init.PultPlant
import duskdn.plantz.entity.projectile.Cabbage
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec2

class CabbagePult(type: EntityType<out PultPlant>, level: Level) : PultPlant(PazEntities.CABBAGE_PULT, level) {

    override fun registerGoals() {
        super.registerGoals()

        this.goalSelector.addGoal(2, ProjectileAttackGoal(
            usingEntity = this,
            projectileFactory = { Cabbage(level(), this, spawnOffset = Vec2(-1f, 1f)) },
            useHighArc = true,
            velocity = 1.0,
            cooldownTime = 30,
            actionDelay = 9))
    }
}