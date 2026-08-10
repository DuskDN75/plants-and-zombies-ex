package duskdn.plantz.entity.plant.interfaces

import duskdn.plantz.ai.goal.ExplodeGoal.Companion.DESTRUCTIVE_EXPLOSION_CALCULATOR
import duskdn.plantz.ai.goal.ExplodeGoal.Companion.EXPLOSION_CALCULATOR
import duskdn.plantz.entity.plant.init.InstantUsePlant
import duskdn.plantz.entity.plant.init.PazPlant
import duskdn.plantz.entity.plant.init.PlantAbilities
import duskdn.plantz.init.PazConfig
import duskdn.plantz.init.PazDamageTypes
import duskdn.plantz.init.PazDataSerializers.DATA_SWELL
import duskdn.plantz.init.PazDataSerializers.DATA_SWELL_OLD
import duskdn.plantz.init.PazSounds
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.chat.Component
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.Mth
import net.minecraft.util.Unit
import net.minecraft.util.random.WeightedList
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level

interface IExplosivePlant: IInstantPlant {

    companion object {
        val SWELL: EntityDataAccessor<Int> = SynchedEntityData.defineId<Int>(PazPlant::class.java, DATA_SWELL)

        val SWELL_OLD: EntityDataAccessor<Int> = SynchedEntityData.defineId<Int>(PazPlant::class.java, DATA_SWELL_OLD)
    }

    override fun getActiveSound(): SoundEvent {
        return SoundEvents.CREEPER_PRIMED
    }

    fun explode(
        radius: Float = 4.0f,
        sound: Holder.Reference<SoundEvent> = PazSounds.PLANT_EXPLODE,
        damageType: ResourceKey<DamageType> = PazDamageTypes.PLANT_AOE,
        destroyBlocks: Boolean = false,
        causeFire: Boolean = false,
    ) {
        val level = entity.level()
        val source = entity.damageSources().source(damageType, entity,
            if (PazConfig.PLAYER_CREDIT_FOR_PLANT_KILLS) entity.rootOwner else entity)
        level.explode(
            entity,
            source,
            EXPLOSION_CALCULATOR,
            entity.x, entity.y, entity.z,
            radius,
            causeFire,
            Level.ExplosionInteraction.MOB,
            ParticleTypes.SMOKE,
            ParticleTypes.EXPLOSION,
            WeightedList.of(),
            sound
        )
        if (destroyBlocks) level.explode(
            entity,
            null,
            DESTRUCTIVE_EXPLOSION_CALCULATOR,
            entity.x, entity.y, entity.z,
            radius*.5f,
            causeFire,
            Level.ExplosionInteraction.MOB,
            ParticleTypes.SMOKE,
            ParticleTypes.EXPLOSION,
            WeightedList.of(),
            SoundEvents.ITEM_BREAK
        )
    }

    fun getTriggered(player: Player, hand: InteractionHand): Boolean {
        val itemStack = player.getItemInHand(hand)
        val level = entity.level()
        if (level is ServerLevel) {
            // flint and steel interaction
            if (itemStack.`is`(Items.FLINT_AND_STEEL)) {
                if (entity.cooldown<0) {
                    if (entity.isAsleep) {
                        player.sendOverlayMessage(
                            Component.translatable("message.plantz.sleeping", entity.name.copy().withStyle(
                                ChatFormatting.RED)).withStyle(ChatFormatting.DARK_RED))
                        return false
                    }
                    swellSpeed=2
                    entity.playSound(SoundEvents.FLINTANDSTEEL_USE)
                    return true
                }
            }
        }
        return false
    }

    var swelling: Int
        get() = entity.entityData.get(SWELL)
        set(value) {
            entity.entityData.set(SWELL, value)
        }

    var oldSwelling: Int
        get() = entity.entityData.get(SWELL_OLD)
        set(value) {
            entity.entityData.set(SWELL_OLD, value)
        }

    var swellSpeed: Int

    fun getSwelling(a: Float): Float {
        val swelling = Mth.lerp(a, oldSwelling.toFloat(), swelling.toFloat()) / (getMaxActiveTime() - 2).toFloat()
        return swelling
    }

}