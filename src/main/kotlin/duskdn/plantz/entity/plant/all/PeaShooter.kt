package duskdn.plantz.entity.plant.all

import duskdn.plantz.init.PazEntities
import duskdn.plantz.ai.goal.ProjectileAttackGoal
import duskdn.plantz.entity.plant.init.AttackingPlant
import duskdn.plantz.entity.projectile.peas.Pea
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level

class PeaShooter(type: EntityType<out AttackingPlant>, level: Level) : AttackingPlant(PazEntities.PEA_SHOOTER, level) {
    override fun registerGoals() {
        super.registerGoals()

        this.goalSelector.addGoal(2, ProjectileAttackGoal(
            usingEntity = this,
            projectileFactory = { Pea(level(), this)},
            cooldownTime = 20,
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
