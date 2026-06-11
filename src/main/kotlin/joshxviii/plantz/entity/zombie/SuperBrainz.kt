package joshxviii.plantz.entity.zombie

import joshxviii.plantz.PazDataSerializers.DATA_DYE_COLOR
import joshxviii.plantz.PazDataSerializers.GNOME_VARIANT
import joshxviii.plantz.PazDataSerializers.SUPER_BRAINZ_VARIANT
import joshxviii.plantz.PazItems
import joshxviii.plantz.PazSounds
import joshxviii.plantz.ai.goal.ProjectileAttackGoal
import joshxviii.plantz.entity.gnome.Gnome
import joshxviii.plantz.entity.gnome.GnomeVariant
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
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import kotlin.jvm.optionals.getOrDefault

class SuperBrainz(type: EntityType<out SuperBrainz>, level: Level) : PazZombie(type, level) {

    companion object {
        val DATA_VARIANT_ID: EntityDataAccessor<SuperBrainzVariant> = SynchedEntityData.defineId(SuperBrainz::class.java, SUPER_BRAINZ_VARIANT)
    }

    init {
        xpReward = 40
    }

    var variant: SuperBrainzVariant
        get() = this.entityData.get(DATA_VARIANT_ID)
        set(value) = this.entityData.set(DATA_VARIANT_ID, value)

    override fun defineSynchedData(entityData: SynchedEntityData.Builder) {
        super.defineSynchedData(entityData)
        entityData.define(DATA_VARIANT_ID, SuperBrainzVariant.pickRandomVariant())
    }

    override fun addAdditionalSaveData(output: ValueOutput) {
        super.addAdditionalSaveData(output)
        output.store("variant", SuperBrainzVariant.CODEC, variant)
    }

    override fun readAdditionalSaveData(input: ValueInput) {
        super.readAdditionalSaveData(input)
        variant = input.read<SuperBrainzVariant>("variant", SuperBrainzVariant.CODEC).getOrDefault(SuperBrainzVariant.pickRandomVariant())
    }

    override fun registerGoals() {
        super.registerGoals()
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