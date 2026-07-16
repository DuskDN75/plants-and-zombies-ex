package joshxviii.plantz.entity.plant

import joshxviii.plantz.init.PazEntities
import joshxviii.plantz.ai.goal.ProjectileAttackGoal
import joshxviii.plantz.entity.plant.init.Plant
import joshxviii.plantz.entity.plant.init.PultPlant
import joshxviii.plantz.entity.projectile.Cabbage
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.monster.zombie.Zombie
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec2

class CabbagePult(type: EntityType<out PultPlant>, level: Level) : PultPlant(PazEntities.CABBAGE_PULT, level) {

    override fun registerGoals() {
        super.registerGoals()

        this.goalSelector.addGoal(2, ProjectileAttackGoal(
            usingEntity = this,
            projectileFactory = { Cabbage(level(), this, spawnOffset = Vec2(-1f, 1f)) },
            useHighArc = true,
            velocity = 0.85,
            cooldownTime = 30,
            actionDelay = 9))
    }
}