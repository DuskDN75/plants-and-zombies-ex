package joshxviii.plantz.entity.plant

import joshxviii.plantz.*
import joshxviii.plantz.ai.goal.MeleeAttackActionGoal
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.Mth
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.monster.zombie.Zombie
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

class BonkChoy(type: EntityType<out Plant>, level: Level) : Plant(PazEntities.BONK_CHOY, level) {

    companion object {
        val USE_MEGA_PUNCH: EntityDataAccessor<Boolean> = SynchedEntityData.defineId<Boolean>(BonkChoy::class.java, EntityDataSerializers.BOOLEAN)
    }

    var useMegaPunch: Boolean
        get() = this.entityData.get(USE_MEGA_PUNCH)
        set(value) = this.entityData.set(USE_MEGA_PUNCH, value)

    override fun defineSynchedData(entityData: SynchedEntityData.Builder) {
        super.defineSynchedData(entityData)
        entityData.define(USE_MEGA_PUNCH, false)
    }

    override fun registerGoals() {
        super.registerGoals()
        // normal attack
        this.goalSelector.addGoal(1, MeleeAttackActionGoal(
            usingEntity = this,
            actionDelay = 7,
            cooldownTime = 20,
            actionPredicate = { !useMegaPunch }
        ))
        // mega punch
        this.goalSelector.addGoal(1, MeleeAttackActionGoal(
            usingEntity = this,
            actionDelay = 18,
            cooldownTime = 60,
            actionPredicate = { useMegaPunch },
            actionSuccessEffect = {
                //TODO custom sounds
                playSound(SoundEvents.WIND_CHARGE_BURST.value(), 1.0f, 1.3f)
            },
            afterHitEntityEffect = {
                val lookDirection = calculateViewVector(-45f, yHeadRot)
                it.applyImpulse(lookDirection.x, lookDirection.y, lookDirection.z, 1.5f, 0.3f)
            }
        ))
        this.targetSelector.addGoal(4, NearestAttackableTargetGoal(this, LivingEntity::class.java, 5, true, false) { target, level ->
            target !is Plant
                    && (target is Zombie
                    || (target is Enemy && isTame)
                    || (target is Player && !isTame))
        })
    }

    override fun cooldownFinished() {
        useMegaPunch = random.nextFloat() < 0.45f
    }
}