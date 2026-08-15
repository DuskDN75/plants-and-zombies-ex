package duskdn.plantz_ex.entity.plant.all

import duskdn.plantz_ex.init.ElectricArcParticleOptions
import duskdn.plantz_ex.init.PazDamageTypes
import duskdn.plantz_ex.init.PazEffects
import duskdn.plantz_ex.init.PazEntities
import duskdn.plantz_ex.init.PazServerParticles
import duskdn.plantz_ex.ai.goal.MeleeAttackActionGoal
import duskdn.plantz_ex.entity.plant.init.PazPlant
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.monster.zombie.Zombie
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

class LightningReed(type: EntityType<out PazPlant>, level: Level) : PazPlant(PazEntities.LIGHTNING_REED, level) {

    override fun registerGoals() {
        super.registerGoals()

        this.goalSelector.addGoal(2, MeleeAttackActionGoal(
            usingEntity = this,
            actionDelay = 6,
            cooldownTime = 16,
            damageType = PazDamageTypes.PLANT_ELECTRIC,
            beforeHitEntityEffect = {
                it.addEffect(MobEffectInstance(PazEffects.ELECTRIFIED, 100, 1), this)
                val eyeHeight = eyeHeight.toDouble()
                val direction = this.headLookAngle.scale(0.5)
                (level() as? ServerLevel)?.sendParticles(
                    ElectricArcParticleOptions(
                        Vec3(it.getRandomX(0.2), it.randomY, it.getRandomZ(0.2)),
                        color = 0xBDFDF5,
                        thickness = 0.15f
                    ),
                    x + direction.x, y + eyeHeight, z + direction.z,
                    1, 0.0, 0.0, 0.0, 0.0
                )
            }
        ))
        this.targetSelector.addGoal(4, NearestAttackableTargetGoal(this, LivingEntity::class.java, 5, true, false) { target, level ->
            target !is PazPlant
                    
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
