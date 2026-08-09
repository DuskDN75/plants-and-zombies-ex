package duskdn.plantz.ai.goal

import duskdn.plantz.entity.plant.init.ExplosivePlant
import duskdn.plantz.init.PazConfig
import duskdn.plantz.init.PazDamageTypes
import duskdn.plantz.init.PazSounds
import net.minecraft.core.Holder
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.random.WeightedList
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.level.ExplosionDamageCalculator
import net.minecraft.world.level.Level
import net.minecraft.world.level.SimpleExplosionDamageCalculator
import java.util.*
import java.util.function.Predicate

class ExplodeGoal(
    override val usingEntity: ExplosivePlant,
    cooldownTime: Int = 20,
    actionDelay: Int = 0,
    actionStartEffect: () -> Unit = {},
    actionSuccessEffect: () -> Unit = {},
    actionEndEffect: () -> Unit = {},
    actionPredicate: Predicate<PathfinderMob> = Predicate { true },
    attackRadius: Float = usingEntity.attributes.getValue(Attributes.FOLLOW_RANGE).toFloat(),
    velocity : Double = 1.2,
    soundEvent: Holder.Reference<SoundEvent> = PazSounds.PLANT_EXPLODE,
    damageType: ResourceKey<DamageType> = PazDamageTypes.PLANT_AOE,
    beforeActionEntityEffect: (targets: MutableList<LivingEntity>) -> Unit = {},
    afterActionEntityEffect: (targets: MutableList<LivingEntity>) -> Unit = {},
    requireTarget: Boolean = false,
    activateRange: Double = 3.0,
    val destroyBlocks: Boolean = false,
    val causeFire: Boolean = false,
) : InstantUseGoal<ExplosivePlant>(
    usingEntity, cooldownTime, actionDelay, actionStartEffect, actionSuccessEffect, actionEndEffect, actionPredicate,
    velocity = velocity,
    attackRadius = attackRadius,
    soundEvent = soundEvent,
    damageType = damageType,
    beforeActionEntityEffect = beforeActionEntityEffect,
    afterActionEntityEffect = afterActionEntityEffect,
    requireTarget = requireTarget,
    activateRange = activateRange,
) {

    companion object {
        val EXPLOSION_CALCULATOR: ExplosionDamageCalculator =
            SimpleExplosionDamageCalculator(false, true, Optional.of<Float>(1f), Optional.ofNullable(null))
        val DESTRUCTIVE_EXPLOSION_CALCULATOR: ExplosionDamageCalculator =
            SimpleExplosionDamageCalculator(true, false, Optional.of<Float>(1.5f), Optional.ofNullable(null))
    }

    override fun doAction(): Boolean {
        val result = super.doAction()

        val level = usingEntity.level()
        val source = usingEntity.damageSources().source(damageType, usingEntity,
            if (PazConfig.PLAYER_CREDIT_FOR_PLANT_KILLS) usingEntity.rootOwner else usingEntity)

        level.explode(
            usingEntity,
            source,
            EXPLOSION_CALCULATOR,
            usingEntity.x, usingEntity.y, usingEntity.z,
            attackRadius,
            causeFire,
            Level.ExplosionInteraction.MOB,
            ParticleTypes.SMOKE,
            ParticleTypes.EXPLOSION,
            WeightedList.of(),
            soundEvent
        )
        if (destroyBlocks) level.explode(
            usingEntity,
            null,
            DESTRUCTIVE_EXPLOSION_CALCULATOR,
            usingEntity.x, usingEntity.y, usingEntity.z,
            attackRadius*.5f,
            causeFire,
            Level.ExplosionInteraction.MOB,
            ParticleTypes.SMOKE,
            ParticleTypes.EXPLOSION,
            WeightedList.of(),
            SoundEvents.ITEM_BREAK
        )

        return result
    }
}