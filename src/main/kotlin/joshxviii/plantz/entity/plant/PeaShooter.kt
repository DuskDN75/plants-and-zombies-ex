package joshxviii.plantz.entity.plant

import joshxviii.plantz.init.PazEntities
import joshxviii.plantz.ai.goal.ProjectileAttackGoal
import joshxviii.plantz.entity.plant.init.AttackingPlant
import joshxviii.plantz.entity.plant.init.Plant
import joshxviii.plantz.entity.projectile.Pea
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.monster.zombie.Zombie
import net.minecraft.world.level.Level

class PeaShooter(type: EntityType<out AttackingPlant>, level: Level) : AttackingPlant(PazEntities.PEA_SHOOTER, level) {
    override fun registerGoals() {
        super.registerGoals()

        this.goalSelector.addGoal(2, ProjectileAttackGoal(
            usingEntity = this,
            projectileFactory = { Pea(level(), this)},
            cooldownTime = 19,
            actionDelay = 3))
    }

    override fun getZenGrownSeedType(): EntityType<*> = if (random.nextFloat() < 0.1f) PazEntities.REPEATER else super.getZenGrownSeedType()

    override fun actuallyHurt(level: ServerLevel, source: DamageSource, damage: Float) {
        super.actuallyHurt(level, source, damage)
        if (source.`is`(DamageTypes.LIGHTNING_BOLT)) {
            convertToPlantType(PazEntities.ELECTRIC_PEA_SHOOTER)
        }
    }
}
