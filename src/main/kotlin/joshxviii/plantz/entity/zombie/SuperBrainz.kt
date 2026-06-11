package joshxviii.plantz.entity.zombie

import joshxviii.plantz.PazDataSerializers.SUPER_BRAINZ_VARIANT
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.*
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

    var xCape = 0.0f
    var yCape = 0.0f
    var zCape = 0.0f
    var xCapeO = 0.0f
    var yCapeO = 0.0f
    var zCapeO = 0.0f

    var walkDist = 0f
    var walkDistO = 0f

    var variant: SuperBrainzVariant
        get() = this.entityData.get(DATA_VARIANT_ID)
        set(value) = this.entityData.set(DATA_VARIANT_ID, value)

    override fun tick() {
        super.tick()
        updateCapeState()
    }

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

    fun updateCapeState() {
        walkDistO = walkDist
        val delta = deltaMovement

        xCapeO = xCape
        yCapeO = yCape
        zCapeO = zCape

        val dx = (x - xCape).toFloat()
        val dy = (y - yCape).toFloat()
        val dz = (z - zCape).toFloat()

        if (dx * dx + dy * dy + dz * dz > 100.0) {
            xCape = x.toFloat()
            yCape = y.toFloat()
            zCape = z.toFloat()
            xCapeO = xCape
            yCapeO = yCape
            zCapeO = zCape
        } else {
            xCape += dx * 0.25f
            yCape += dy * 0.25f
            zCape += dz * 0.25f
        }

        val horizontalSpeed = delta.horizontalDistance()
        walkDist += horizontalSpeed.toFloat()
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