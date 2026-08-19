package duskdn.plantz_ex.entity.zombie

import duskdn.plantz_ex.init.PazBlocks
import duskdn.plantz_ex.init.PazSounds
import duskdn.plantz_ex.init.PazDataSerializers.BROWN_COAT_VARIANT
import duskdn.plantz_ex.init.PazTags
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.StructureTags
import net.minecraft.util.RandomSource
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.*
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.level.ServerLevelAccessor
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import kotlin.jvm.optionals.getOrDefault

class BrownCoat(type: EntityType<out BrownCoat>, level: Level) : PazZombie(type, level) {
    companion object {
        val DATA_VARIANT_ID: EntityDataAccessor<BrownCoatVariant> = SynchedEntityData.defineId(BrownCoat::class.java, BROWN_COAT_VARIANT)
    }

    var variant: BrownCoatVariant
        get() = this.entityData.get(DATA_VARIANT_ID)
        set(value) = this.entityData.set(DATA_VARIANT_ID, value)

    override fun defineSynchedData(entityData: SynchedEntityData.Builder) {
        super.defineSynchedData(entityData)
        entityData.define(DATA_VARIANT_ID, BrownCoatVariant.getDefault())
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

    override fun isBaby(): Boolean = isBabyZombie()
    override fun populateDefaultEquipmentSlots(random: RandomSource, difficulty: DifficultyInstance) {
        randomEquip(random, difficulty)
    }

    override fun addAdditionalSaveData(output: ValueOutput) {
        super.addAdditionalSaveData(output)
        output.store("variant", BrownCoatVariant.CODEC, variant)
    }

    override fun readAdditionalSaveData(input: ValueInput) {
        super.readAdditionalSaveData(input)
        variant = input.read<BrownCoatVariant>("variant", BrownCoatVariant.CODEC).getOrDefault(BrownCoatVariant.getDefault())
    }

    override fun canFreeze(): Boolean {
        return if (variant == BrownCoatVariant.SNOW) false
        else super.canFreeze()
    }

    override fun finalizeSpawn(
        level: ServerLevelAccessor,
        difficulty: DifficultyInstance,
        spawnReason: EntitySpawnReason,
        groupData: SpawnGroupData?
    ): SpawnGroupData? {
        val data = super.finalizeSpawn(level, difficulty, spawnReason, groupData)
        val random = level.random
        val difficultyModifier = difficulty.specialMultiplier
        setCanPickUpLoot(true)
        setCanBreakDoors(true)
        val structureManager = (level as ServerLevel).structureManager()
        val isShipwreckSpawn = structureManager.getStructureWithPieceAt(blockPosition(), StructureTags.SHIPWRECK).isValid
        variant = BrownCoatVariant.pickForBiome(
            level.getBiome(blockPosition()).`is`(PazTags.Biomes.HAS_BROWNCOAT_SNOW),
            level.getBiome(blockPosition()).`is`(PazTags.Biomes.HAS_BROWNCOAT_DESERT),
            level.getBiome(blockPosition()).`is`(PazTags.Biomes.HAS_BROWNCOAT_BEACH),
            isShipwreckSpawn,
            random
        )

        if (spawnReason != EntitySpawnReason.COMMAND && spawnReason != EntitySpawnReason.SPAWN_ITEM_USE){

            val headSlot = getItemBySlot(EquipmentSlot.HEAD)

            val mainHandSlot = getItemBySlot(EquipmentSlot.MAINHAND)

            var screenDoorChance = 0.05

            if (headSlot.isEmpty) {

                if (random.nextFloat() < 0.25) {
                    setItemSlot(EquipmentSlot.HEAD, PazBlocks.CONE.asItem().defaultInstance)
                    screenDoorChance *= 0.1
                    setDropChance(EquipmentSlot.HEAD, 0.2f)
                }
                else if (random.nextFloat() < 0.1) {
                    setItemSlot(EquipmentSlot.HEAD, Items.BUCKET.defaultInstance)
                    screenDoorChance *= 0.01
                    setDropChance(EquipmentSlot.HEAD, 0.1f)
                }

            }

            if (mainHandSlot.isEmpty) {

                if (random.nextFloat() < screenDoorChance) {
                    setItemSlot(EquipmentSlot.MAINHAND, PazBlocks.SCREEN_DOOR.asItem().defaultInstance)
                    setDropChance(EquipmentSlot.MAINHAND, 0.1f)
                }
            }
        }

        return data
    }
}

enum class HatVariant(val hat: Item?, val hatName: String?) {
    NONE(null, "basic"),
    CONE(PazBlocks.CONE.asItem(), "cone"),
    BUCKET(Items.BUCKET, "bucket"),
}