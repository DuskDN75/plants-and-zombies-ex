package duskdn.plantz_ex.ai.goal

import duskdn.plantz_ex.entity.plant.init.PazPlant
import duskdn.plantz_ex.entity.plant.interfaces.IExplosivePlant
import duskdn.plantz_ex.init.PazDamageTypes
import duskdn.plantz_ex.init.PazSounds
import duskdn.plantz_ex.util.debugPrint
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.level.ExplosionDamageCalculator
import net.minecraft.world.level.SimpleExplosionDamageCalculator
import java.util.*
import java.util.function.Predicate

class ExplodeGoal<T>(
    override val usingEntity: T,
    cooldownTime: Int = 20,
    actionDelay: Int = usingEntity.getMaxActiveTime(),
    actionStartEffect: (ActionData?) -> Unit = {},
    actionSuccessEffect: (ActionData?) -> Unit = {},
    actionEndEffect: (ActionData?) -> Unit = {},
    actionPredicate: Predicate<PathfinderMob> = Predicate { true },
    attackRadius: Float = usingEntity.attributes.getValue(Attributes.FOLLOW_RANGE).toFloat(),
    soundEvent: Holder.Reference<SoundEvent> = PazSounds.PLANT_EXPLODE,
    damageType: ResourceKey<DamageType> = PazDamageTypes.PLANT_AOE,
    requireTarget: Boolean = false,
    activateRange: Double = 3.0,
    active: Boolean = true,
    val destroyBlocks: Boolean = false,
    val causeFire: Boolean = false,
) : InstantUseGoal<T>(
    usingEntity, cooldownTime, actionDelay, actionStartEffect, actionSuccessEffect, actionEndEffect, actionPredicate,
    attackRadius = attackRadius,
    soundEvent = soundEvent,
    damageType = damageType,
    requireTarget = requireTarget,
    activateRange = activateRange,
) where T: PazPlant, T: IExplosivePlant {
    companion object {
        val EXPLOSION_CALCULATOR: ExplosionDamageCalculator =
            SimpleExplosionDamageCalculator(false, true, Optional.of<Float>(1f), Optional.ofNullable(null))
        val DESTRUCTIVE_EXPLOSION_CALCULATOR: ExplosionDamageCalculator =
            SimpleExplosionDamageCalculator(true, false, Optional.of<Float>(1.5f), Optional.ofNullable(null))
    }

    override fun calculateActionTime() {
        oldActionTime = actionTimer
        actionTimer = (actionTimer+(actionDirection*usingEntity.swellSpeed)).coerceIn(0, maxActionTime)
        debugPrint("SWELLING SHOULD BE: ${maxActionTime-actionTimer}, MAX ACTION: $maxActionTime")
        usingEntity.oldSwelling = maxActionTime-oldActionTime
        usingEntity.swelling = maxActionTime-actionTimer
    }

    override fun doAction(): Boolean {
        usingEntity.explode(
            radius = attackRadius,
            sound = soundEvent,
            damageType = damageType,
            destroyBlocks = destroyBlocks,
            causeFire = causeFire,
        )

        return true
    }
}