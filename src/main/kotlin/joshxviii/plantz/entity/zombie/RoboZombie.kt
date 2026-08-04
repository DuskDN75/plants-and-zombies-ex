package joshxviii.plantz.entity.zombie

import joshxviii.plantz.ai.goal.ProjectileAttackGoal
import joshxviii.plantz.entity.projectile.Missile
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.*
import net.minecraft.world.level.Level
import net.minecraft.world.level.ServerLevelAccessor

class RoboZombie(type: EntityType<out RoboZombie>, level: Level) : PazZombie(type, level) {

    companion object {
        val TANK_TRANSFORMATION: EntityDataAccessor<Boolean> = SynchedEntityData.defineId<Boolean>(RoboZombie::class.java, EntityDataSerializers.BOOLEAN)
    }

    init {
        xpReward = 30
    }

    var isTransformed: Boolean
        get() = this.entityData.get(TANK_TRANSFORMATION)
        set(value) = this.entityData.set(TANK_TRANSFORMATION, value)

    val idleAnimation : AnimationState = AnimationState()
    val meleeAttackAnimation : AnimationState = AnimationState()
    val shootAnimation : AnimationState = AnimationState()

    override fun defineSynchedData(entityData: SynchedEntityData.Builder) {
        super.defineSynchedData(entityData)
        entityData.define(TANK_TRANSFORMATION, false)
    }

    override fun registerGoals() {
        super.registerGoals()
        this.goalSelector.addGoal(2, ProjectileAttackGoal(
            usingEntity = this,
            projectileFactory =  { Missile(level(), this) },
            velocity = 1.3,
            actionDelay = 50,
            soundEvent = null,
            actionPredicate = {
                false
            },
            actionEndEffect = {

            }))
    }

    override fun addBehaviourGoals() {
        //addBehaviourGoalsNoMelee()
        super.addBehaviourGoals()
    }

    override fun canEquipDuckyInWater(): Boolean = false
    override fun canPickUpLoot(): Boolean = false

    override fun tick() {
        super.tick()
        if (!this.isNoAi) { updateAnimationState() }
    }

    fun updateAnimationState() {
        idleAnimation.startIfStopped(0)
    }

    //TODO custom sounds
    override fun getAmbientSound(): SoundEvent {
        return SoundEvents.EMPTY
    }
    override fun getHurtSound(source: DamageSource): SoundEvent {
        return SoundEvents.EMPTY
    }
    override fun getDeathSound(): SoundEvent {
        return SoundEvents.EMPTY
    }
    override fun getStepSound(): SoundEvent {
        return SoundEvents.EMPTY
    }

    override fun doHurtTarget(level: ServerLevel, target: Entity): Boolean {
        val result = super.doHurtTarget(level, target)
        return result
    }

    override fun finalizeSpawn(
        level: ServerLevelAccessor,
        difficulty: DifficultyInstance,
        spawnReason: EntitySpawnReason,
        groupData: SpawnGroupData?
    ): SpawnGroupData? {
        val data = super.finalizeSpawn(level, difficulty, spawnReason, ZombieGroupData(false, false))

        if (spawnReason != EntitySpawnReason.CONVERSION) {
            setCanBreakDoors(true)
        }

        return data
    }
}