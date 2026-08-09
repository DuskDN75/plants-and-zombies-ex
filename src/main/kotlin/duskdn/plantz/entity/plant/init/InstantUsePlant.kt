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

abstract class InstantUsePlant(type: EntityType<out InstantUsePlant>, level: Level) : PazPlant(type, level), IInstantPlant {
    companion object {
        val ACTIVE_DIRECTION: EntityDataAccessor<Int> = SynchedEntityData.defineId<Int>(InstantUsePlant::class.java,
            PazDataSerializers.ACTIVE_DIRECTION
        )
    }

    override fun defineSynchedData(entityData: SynchedEntityData.Builder) {
        super.defineSynchedData(entityData)
        entityData.define(ACTIVE_DIRECTION, 0)
    }

    override fun tick() {
        super.tick()
        if (activeTime == getMaxActiveTime() && !isRemoved) activate()
        calculateActiveTime()
    }

    override var oldActiveTime = 0
    override var activeTime = 0

    open fun activate(
        radius: Float = 4.0f,
        sound: Holder.Reference<SoundEvent> = PazSounds.PLANT_EXPLODE,
        damageType: ResourceKey<DamageType> = PazDamageTypes.PLANT_AOE,
        destroyBlocks: Boolean = false,
        discardOnFinish: Boolean = discardOnFinish()
    ) {
        activeDirection = -1
        activeTime = 0
        val level = this.level()
        val source = this.damageSources().source(damageType, this,
            if (PazConfig.PLAYER_CREDIT_FOR_PLANT_KILLS) this.rootOwner else this)
        activateFunction()
        if (discardOnFinish) discard()
    }

    open fun activate() {
        activeDirection = -1
        activeTime = 0
        val level = this.level()
        val source = this.damageSources().source(damageType, this,
            if (PazConfig.PLAYER_CREDIT_FOR_PLANT_KILLS) this.rootOwner else this)
        activateFunction()
        if (discardOnFinish) discard()
    }

    open fun discardOnFinish(): Boolean = true
}