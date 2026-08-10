package duskdn.plantz.ai.goal

import duskdn.plantz.entity.interfaces.IFloatingMob
import duskdn.plantz.entity.plant.init.PazPlant
import duskdn.plantz.entity.plant.interfaces.IInstantPlant
import duskdn.plantz.init.PazConfig
import duskdn.plantz.init.PazDamageTypes
import duskdn.plantz.init.PazSounds
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.Mth
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.level.gameevent.GameEvent
import java.util.*
import java.util.function.Predicate

abstract class InstantUseGoal<T>(
    override val usingEntity: T,
    cooldownTime: Int = 20,
    actionDelay: Int = usingEntity.getMaxActiveTime(),
    actionStartEffect: (ActionData?) -> Unit = {},
    actionSuccessEffect: (ActionData?) -> Unit = {},
    actionEndEffect: (ActionData?) -> Unit = {},
    actionPredicate: Predicate<PathfinderMob> = Predicate { true },
    var attackRadius: Float = usingEntity.attributes.getValue(Attributes.FOLLOW_RANGE).toFloat(),
    val soundEvent: Holder.Reference<SoundEvent> = PazSounds.PLANT_EXPLODE,
    val damageType: ResourceKey<DamageType> = PazDamageTypes.PLANT_AOE,
    val requireTarget: Boolean = false,
    val activateRange: Double = 3.0,
    override var maxActionTime: Int = usingEntity.getMaxActiveTime(),
) : ActionGoal(usingEntity, cooldownTime, actionDelay, actionStartEffect, actionSuccessEffect, actionEndEffect, actionPredicate) where T: PazPlant, T: IInstantPlant {

    init {
        flags = EnumSet.of<Flag>(Flag.MOVE)
    }

    fun getTargets(): MutableList<LivingEntity> {
        return usingEntity.level().getEntitiesOfClass(
            LivingEntity::class.java,
            usingEntity.boundingBox.inflate(attackRadius.toDouble())
        ).filter { it != usingEntity }.toMutableList()
    }

    override fun getData(): ActionData? {
        return ActionData(getTargets())
    }

    override fun canUse(): Boolean {

        if (requireTarget) {
            target = usingEntity.target
            target?.let {
                return (!it.isDeadOrDying && usingEntity.distanceToSqr(it) < activateRange * activateRange) || actionTimer < maxActionTime
            }
        }

        if (!actionPredicate.test(usingEntity)) return false
        if ((usingEntity.isAsleep || usingEntity.isGrowingSeeds)) return false

        return true
    }

    override fun start() {
        super.start()
        usingEntity.getNavigation().stop()
    }

    override fun stop() {
        super.stop()
        target = null
    }

    override fun requiresUpdateEveryTick(): Boolean {
        return true
    }

    override fun canDoAction(): Boolean {

        println("ENTITY IS ACTIVE: ${usingEntity.active}")

        return usingEntity.active
    }

    override fun doAction(): Boolean {
        return true
    }

    override fun startAction() {

        val sound = usingEntity.getActiveSound()

        if (sound != null) {
            usingEntity.playSound(sound)
        }
    }

    override fun preAction() {

    }

    override fun postAction() {
        if (usingEntity.discardOnActivate()) usingEntity.discard()
    }

}