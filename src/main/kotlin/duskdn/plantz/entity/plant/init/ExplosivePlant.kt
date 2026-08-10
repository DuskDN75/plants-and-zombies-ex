package duskdn.plantz.entity.plant.init

import duskdn.plantz.ai.goal.ExplodeGoal.Companion.DESTRUCTIVE_EXPLOSION_CALCULATOR
import duskdn.plantz.ai.goal.ExplodeGoal.Companion.EXPLOSION_CALCULATOR
import duskdn.plantz.entity.plant.interfaces.IExplosivePlant
import duskdn.plantz.entity.plant.interfaces.IExplosivePlant.Companion.SWELL
import duskdn.plantz.entity.plant.interfaces.IExplosivePlant.Companion.SWELL_OLD
import duskdn.plantz.entity.plant.interfaces.IInstantPlant.Companion.ACTIVE
import duskdn.plantz.init.PazConfig
import duskdn.plantz.init.PazDamageTypes
import duskdn.plantz.init.PazSounds
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.chat.Component
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.random.WeightedList
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level

abstract class ExplosivePlant(type: EntityType<out ExplosivePlant>, level: Level) : PazPlant(type, level), IExplosivePlant {
    override fun mobInteract(player: Player, hand: InteractionHand): InteractionResult {
        return if (getTriggered(player, hand)) InteractionResult.SUCCESS_SERVER else super.mobInteract(player, hand)
    }

    override var swellSpeed: Int = 1
}