package joshxviii.plantz.entity.zombie

import joshxviii.plantz.PazBlocks
import joshxviii.plantz.PazDamageTypes
import joshxviii.plantz.PazEffects
import joshxviii.plantz.PazEntities
import joshxviii.plantz.PazSounds
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

class Imp(type: EntityType<out Imp> = PazEntities.IMP, level: Level) : PazZombie(type, level) {

    init {

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

    override fun actuallyHurt(level: ServerLevel, source: DamageSource, damage: Float) {
        super.actuallyHurt(level, source, damage)
        val entity = source.entity
        if (source.directEntity == entity) {// apply toxic effect attacked directly
            if (entity is LivingEntity && entity.weaponItem.isEmpty && !entity.hasInfiniteMaterials()) entity.addEffect(MobEffectInstance(PazEffects.TOXIC, 200, 0), this)
        }
    }

    override fun doHurtTarget(level: ServerLevel, target: Entity): Boolean {
        val wasHurt = super.doHurtTarget(level, target)
        if (wasHurt && target is LivingEntity) {
            val toxicTime = when (level().difficulty) {
                Difficulty.NORMAL -> 8
                Difficulty.HARD -> 15
                else -> 0
            }
            if (random.nextFloat() > 0.25) target.addEffect(MobEffectInstance(PazEffects.TOXIC, toxicTime * 20, 0), this)
        }
        return wasHurt
    }

    override fun finalizeSpawn(
        level: ServerLevelAccessor,
        difficulty: DifficultyInstance,
        spawnReason: EntitySpawnReason,
        groupData: SpawnGroupData?
    ): SpawnGroupData? {
        val data = super.finalizeSpawn(level, difficulty, spawnReason, ZombieGroupData(true, false))
        val random = level.random
        if (spawnReason != EntitySpawnReason.CONVERSION) {
            setCanPickUpLoot(false)
            setCanBreakDoors(true)

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