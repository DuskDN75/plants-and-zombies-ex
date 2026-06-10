package joshxviii.plantz.entity.zombie

import joshxviii.plantz.PazDataSerializers.DATA_DYE_COLOR
import joshxviii.plantz.PazItems
import joshxviii.plantz.PazSounds
import joshxviii.plantz.ai.goal.ProjectileAttackGoal
import joshxviii.plantz.entity.plant.Repeater
import joshxviii.plantz.entity.projectile.Missile
import joshxviii.plantz.entity.projectile.PaintBall
import joshxviii.plantz.entity.zombie.Gargantuar.Companion.HAS_IMP_ID
import joshxviii.plantz.entity.zombie.Gargantuar.Companion.SMASH_ATTACK_TIME_ID
import joshxviii.plantz.entity.zombie.Gargantuar.Companion.THROW_TIME_ID
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.*
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.Level
import net.minecraft.world.level.ServerLevelAccessor

class RoboZombie(type: EntityType<out RoboZombie>, level: Level) : PazZombie(type, level) {

    companion object {

    }

    init {
        xpReward = 10
    }

    override fun defineSynchedData(entityData: SynchedEntityData.Builder) {
        super.defineSynchedData(entityData)
    }

    override fun registerGoals() {
        super.registerGoals()
        this.goalSelector.addGoal(2, ProjectileAttackGoal(
            usingEntity = this,
            projectileFactory =  { Missile(level(), this) },
            velocity = 1.3,
            actionDelay = 8,
            soundEvent = null,
            actionEndEffect = {

            }))
    }

    override fun addBehaviourGoals() {
        behaviourGoalsNoMelee()
    }

    //TODO custom sounds
    override fun getAmbientSound(): SoundEvent {
        return PazSounds.GNOME_JUMP
    }
    override fun getHurtSound(source: DamageSource): SoundEvent {
        return PazSounds.GNOME_JUMP
    }
    override fun getDeathSound(): SoundEvent {
        return PazSounds.GNOME_JUMP
    }
    override fun getStepSound(): SoundEvent {
        return PazSounds.GNOME_JUMP
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