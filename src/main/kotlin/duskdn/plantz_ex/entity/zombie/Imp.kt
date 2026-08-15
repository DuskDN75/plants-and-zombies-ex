package duskdn.plantz_ex.entity.zombie

import duskdn.plantz_ex.init.PazBlocks
import duskdn.plantz_ex.init.PazDataSerializers.IMP_VARIANT
import duskdn.plantz_ex.init.PazEffects
import duskdn.plantz_ex.init.PazEntities
import duskdn.plantz_ex.init.PazSounds
import duskdn.plantz_ex.init.PazTags
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.Difficulty
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.*
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.level.ServerLevelAccessor
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import kotlin.jvm.optionals.getOrDefault

class Imp(type: EntityType<out Imp> = PazEntities.IMP, level: Level) : PazZombie(type, level) {

    companion object {
        val DATA_VARIANT_ID: EntityDataAccessor<ImpVariant> = SynchedEntityData.defineId(Imp::class.java, IMP_VARIANT)
    }

    init {

    }

    var variant: ImpVariant
        get() = this.entityData.get(DATA_VARIANT_ID)
        set(value) = this.entityData.set(DATA_VARIANT_ID, value)

    override fun defineSynchedData(entityData: SynchedEntityData.Builder) {
        super.defineSynchedData(entityData)
        entityData.define(DATA_VARIANT_ID, ImpVariant.getDefault())
    }

    override fun addAdditionalSaveData(output: ValueOutput) {
        super.addAdditionalSaveData(output)
        output.store("variant", ImpVariant.CODEC, variant)
    }

    override fun readAdditionalSaveData(input: ValueInput) {
        super.readAdditionalSaveData(input)
        variant = input.read<ImpVariant>("variant", ImpVariant.CODEC).getOrDefault(ImpVariant.getDefault())
    }

    override fun getAmbientSound(): SoundEvent {
        return PazSounds.IMP_AMBIENT
    }
    override fun getHurtSound(source: DamageSource): SoundEvent {
        return PazSounds.IMP_HURT
    }
    override fun getDeathSound(): SoundEvent {
        return PazSounds.IMP_DEATH
    }
    override fun getStepSound(): SoundEvent {
        return SoundEvents.ZOMBIE_STEP
    }

    override fun isBaby(): Boolean = true
    override fun canPickUpLoot(): Boolean = false

    override fun canFreeze(): Boolean {
        return if (variant == ImpVariant.YETI) false
        else super.canFreeze()
    }

    override fun actuallyHurt(level: ServerLevel, source: DamageSource, damage: Float) {
        super.actuallyHurt(level, source, damage)
        val entity = source.entity
        if (source.directEntity == entity) {// apply toxic effect attacked directly
            if (entity is LivingEntity && entity.weaponItem.isEmpty && !entity.hasInfiniteMaterials()) {
                when (variant) {
                    ImpVariant.IMP -> {
                        entity.addEffect(MobEffectInstance(PazEffects.TOXIC, 200, 0), this)
                    }
                    else -> {}
                }
            }
        }
    }

    override fun doHurtTarget(level: ServerLevel, target: Entity): Boolean {
        val wasHurt = super.doHurtTarget(level, target)
        if (wasHurt && target is LivingEntity) {
            val effectTime = when (level().difficulty) {
                Difficulty.NORMAL -> 5
                Difficulty.HARD -> 12
                else -> 0
            }
            val chance = random.nextFloat()
            when (variant) {
                ImpVariant.IMP -> if (chance < 0.2f) target.addEffect(MobEffectInstance(PazEffects.TOXIC, effectTime * 20, 0), this)
                ImpVariant.YETI -> if (chance < 0.5f) target.addEffect(MobEffectInstance(PazEffects.CHILLED, (effectTime * 30).coerceAtLeast(20), 1), this)
                else -> {}
            }
        }
        return wasHurt
    }

    override fun finalizeSpawn(
        level: ServerLevelAccessor,
        difficulty: DifficultyInstance,
        spawnReason: EntitySpawnReason,
        groupData: SpawnGroupData?
    ): SpawnGroupData? {
        val data = super.finalizeSpawn(level, difficulty, spawnReason, ZombieGroupData(false, false))
        val random = level.random
        if (spawnReason != EntitySpawnReason.CONVERSION) {
            setCanPickUpLoot(false)
            setCanBreakDoors(true)

            variant = ImpVariant.pickForBiome(
                level.getBiome(blockPosition()).`is`(PazTags.Biomes.HAS_BROWNCOAT_SNOW),
                random
            )

            if (getItemBySlot(EquipmentSlot.HEAD).isEmpty){
                if (random.nextFloat() < 0.05) {
                    setItemSlot(EquipmentSlot.HEAD, PazBlocks.CONE.asItem().defaultInstance)
                }
                else if (random.nextFloat() < 0.01 && getItemBySlot(EquipmentSlot.HEAD).isEmpty) {
                    setItemSlot(EquipmentSlot.HEAD, Items.BUCKET.defaultInstance)
                }
            }
        }

        return data
    }
}