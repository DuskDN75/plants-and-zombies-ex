package duskdn.plantz_ex.entity.plant.all

import duskdn.plantz_ex.init.PazEntities
import duskdn.plantz_ex.ai.goal.ProjectileAttackGoal
import duskdn.plantz_ex.entity.plant.init.PultPlant
import duskdn.plantz_ex.entity.projectile.Melon
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec2

class MelonPult(type: EntityType<out PultPlant>, level: Level) : PultPlant(PazEntities.MELON_PULT, level) {

    override fun registerGoals() {
        super.registerGoals()

        this.goalSelector.addGoal(2, ProjectileAttackGoal(
            usingEntity = this,
            projectileFactory = { Melon(level(), this, spawnOffset = Vec2(-1f, 1f))},
            useHighArc = true,
            velocity = 1.5,
            cooldownTime = 65,
            actionDelay = 12))
    }
}