package joshxviii.plantz.entity.plant

import joshxviii.plantz.ElectricArcParticleOptions
import joshxviii.plantz.PazDamageTypes
import joshxviii.plantz.PazEffects
import joshxviii.plantz.PazEntities
import joshxviii.plantz.PazServerParticles
import joshxviii.plantz.ai.goal.BeamAttackGoal
import joshxviii.plantz.ai.goal.MeleeAttackActionGoal
import joshxviii.plantz.ai.goal.ProjectileAttackGoal
import joshxviii.plantz.entity.projectile.PeaElectric
import joshxviii.plantz.entity.projectile.PeaFire
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.monster.Creeper
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.monster.zombie.Zombie
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

class LightningReed(type: EntityType<out Plant>, level: Level) : Plant(PazEntities.LIGHTNING_REED, level) {
    override fun registerGoals() {
        super.registerGoals()

        this.goalSelector.addGoal(2, MeleeAttackActionGoal(
            usingEntity = this,
            actionDelay = 6,
            cooldownTime = 16,
            damageType = PazDamageTypes.PLANT_ELECTRIC,
            afterHitEntityEffect = {
                it.addEffect(MobEffectInstance(PazEffects.ELECTRIFIED, 100, 3))
                val eyeHeight = eyeHeight.toDouble()
                val direction = this.headLookAngle.scale(0.4)
                (level() as? ServerLevel)?.sendParticles(
                    ElectricArcParticleOptions(
                        Vec3(it.getRandomX(0.2), it.randomY*0.75, it.getRandomZ(0.2)),
                        color = 0xFFFFFF,
                        thickness = 0.1f
                    ),
                    x + direction.x, y + eyeHeight - 0.1, z + direction.z,
                    1, 0.0, 0.0, 0.0, 0.0
                )
            }
        ))
        this.targetSelector.addGoal(4, NearestAttackableTargetGoal(this, LivingEntity::class.java, 5, true, false) { target, level ->
            target !is Plant
                    && target !is Creeper
                    && (target is Zombie
                    || (target is Enemy && isTame)
                    || (target is Player && !isTame))
        })
    }

    override fun tick() {
        super.tick()

        if (tickCount % 3 == 0 && tickCount > 18 && isAlive) {

            val direction = calculateUpVector(this.xRot - 50, this.yHeadRot).scale(0.3)
            this.level().addParticle(
                PazServerParticles.ELECTRIFIED,
                direction.x.toFloat() + this.getRandomX(0.2),
                direction.y.toFloat() + this.y + eyeHeight.toDouble() - 0.1,
                direction.z.toFloat() + this.getRandomZ(0.2),
                0.0, 0.0, 0.0,
            )

        }
    }
}
