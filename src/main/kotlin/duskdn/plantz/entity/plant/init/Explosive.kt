package duskdn.plantz.entity.plant.init

import duskdn.plantz.entity.plant.interfaces.IInstantPlant
import duskdn.plantz.init.PazConfig
import duskdn.plantz.init.PazDamageTypes
import duskdn.plantz.init.PazDataSerializers
import duskdn.plantz.init.PazSounds
import net.minecraft.ChatFormatting
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
import net.minecraft.util.random.WeightedList
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Items
import net.minecraft.world.level.ExplosionDamageCalculator
import net.minecraft.world.level.Level
import net.minecraft.world.level.SimpleExplosionDamageCalculator
import java.util.Optional

abstract class Explosive(type: EntityType<out Explosive>, level: Level) : InstantUsePlant(type, level) {
    override fun mobInteract(player: Player, hand: InteractionHand): InteractionResult {
        val itemStack = player.getItemInHand(hand)
        val level = level()
        if (level is ServerLevel) {
            // flint and steel interaction
            if (itemStack.`is`(Items.FLINT_AND_STEEL)) {
                if (cooldown<0) {
                    if (isAsleep) {
                        player.sendOverlayMessage(
                            Component.translatable("message.plantz.sleeping", name.copy().withStyle(
                                ChatFormatting.RED)).withStyle(ChatFormatting.DARK_RED))
                        return InteractionResult.FAIL
                    }
                    activeDirection=2
                    playSound(SoundEvents.FLINTANDSTEEL_USE)
                    return InteractionResult.SUCCESS_SERVER
                }
            }
        }
        return super.mobInteract(player, hand)
    }

    override fun getActiveSound(): SoundEvent {
        return SoundEvents.CREEPER_PRIMED
    }

    open fun discardOnExplode(): Boolean = true

    override fun beginActivate(): Boolean {
        return true
    }
}