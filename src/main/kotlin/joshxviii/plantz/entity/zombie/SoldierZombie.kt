package joshxviii.plantz.entity.zombie

import joshxviii.plantz.init.PazDataSerializers.DATA_DYE_COLOR
import joshxviii.plantz.init.PazItems
import joshxviii.plantz.init.PazSounds
import joshxviii.plantz.ai.goal.ProjectileAttackGoal
import joshxviii.plantz.entity.projectile.PaintBall
import net.minecraft.network.syncher.EntityDataAccessor
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

class SoldierZombie(type: EntityType<out SoldierZombie>, level: Level) : PazZombie(type, level) {

    companion object {
        val DYE_COLOR: EntityDataAccessor<DyeColor> = SynchedEntityData.defineId<DyeColor>(SoldierZombie::class.java, DATA_DYE_COLOR)
    }

    init {
        xpReward = 10
    }

    var dyeColor: DyeColor
        get() = this.entityData.get(DYE_COLOR)
        set(value) = this.entityData.set(DYE_COLOR, value)

    override fun defineSynchedData(entityData: SynchedEntityData.Builder) {
        super.defineSynchedData(entityData)
        entityData.define(DYE_COLOR, DyeColor.WHITE)
    }

    override fun registerGoals() {
        super.registerGoals()
        this.goalSelector.addGoal(2, ProjectileAttackGoal(
            usingEntity = this,
            projectileFactory =  { PaintBall(level(), this, color = dyeColor) },
            velocity = 1.3,
            actionDelay = 8,
            soundEvent = null,
            actionEndEffect = {

            }))
    }

    override fun addBehaviourGoals() {
        behaviourGoalsNoMelee()
    }

    override fun getAmbientSound(): SoundEvent {
        return PazSounds.BROWNCOAT_AMBIENT
    }
    override fun getHurtSound(source: DamageSource): SoundEvent {
        return PazSounds.BROWNCOAT_HURT
    }
    override fun getDeathSound(): SoundEvent {
        return PazSounds.BROWNCOAT_DEATH
    }
    override fun getStepSound(): SoundEvent {
        return SoundEvents.ZOMBIE_STEP
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

        setItemSlot(EquipmentSlot.MAINHAND, PazItems.DYE_BLASTER.defaultInstance)
        dyeColor = DyeColor.VALUES.filter { it != DyeColor.WHITE && it != DyeColor.BLACK }.random()
        setDropChance(EquipmentSlot.MAINHAND, 0.0f)
        if (spawnReason != EntitySpawnReason.CONVERSION) {
            setCanBreakDoors(true)
        }

        return data
    }
}