package duskdn.plantz.ai.goal

import duskdn.plantz.entity.interfaces.IFloatingMob
import duskdn.plantz.entity.plant.init.PazPlant
import duskdn.plantz.entity.plant.interfaces.IInstantPlant
import duskdn.plantz.init.PazSounds
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.level.gameevent.GameEvent
import java.util.*
import java.util.function.Predicate

abstract class InstantUseGoal<T>(
    override val usingEntity: T,
    cooldownTime: Int = 20,
    actionDelay: Int = 0,
    actionStartEffect: () -> Unit = {},
    actionSuccessEffect: () -> Unit = {},
    actionEndEffect: () -> Unit = {},
    actionPredicate: Predicate<PathfinderMob> = Predicate { true },
    var projectileFactory: () -> Entity,
    var attackRadius: Float = usingEntity.attributes.getValue(Attributes.FOLLOW_RANGE).toFloat(),
    var velocity : Double = 1.2,
    val soundEvent: SoundEvent? = PazSounds.PROJECTILE_FIRE,
    val discard
) : ActionGoal(usingEntity, cooldownTime, actionDelay, actionStartEffect, actionSuccessEffect, actionEndEffect, actionPredicate) where T: PazPlant, T: IInstantPlant {

    init {
        flags = EnumSet.of<Flag>(Flag.MOVE)
    }

    override fun canUse(): Boolean {
        if (!actionPredicate.test(usingEntity)) return false
        if ((usingEntity.isAsleep || usingEntity.isGrowingSeeds)) return false
        if (usingEntity.activeDirection>=0) return true
        return false
    }

    override fun start() {
        usingEntity.getNavigation().stop()
    }

    override fun stop() {
        usingEntity.activeDirection = -1
    }

    override fun requiresUpdateEveryTick(): Boolean {
        return true
    }

    override fun canDoAction(): Boolean {
        return canUse()
    }

    fun startAction(): Boolean {
        
    }

    override fun doAction(): Boolean {
        val result = usingEntity.activate()

        if (result && usingEntity.discardOnExplode()) usingEntity.discard()

        return result
    }

    override fun tick() {

        if (!canUse()) return

        if (usingEntity.activeDirection != 2) usingEntity.activeDirection = 1

        if (usingEntity.activeDirection > 0 && usingEntity.activeTime == 0) {
            startAction()
            usingEntity.playSound(usingEntity.getActiveSound())
            usingEntity.gameEvent(GameEvent.PRIME_FUSE)
        }

        if (usingEntity.activeTime == usingEntity.getMaxActiveTime()) {
            actionEndEffect()
            doAction()
        }
    }

}