package duskdn.plantz.ai.goal

import duskdn.plantz.entity.plant.init.PazPlant
import net.minecraft.util.Mth
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.goal.Goal
import java.util.function.Predicate

data class ActionData(
    val targets: MutableList<LivingEntity> = mutableListOf(),
)

/**
 * Defines an action goal for plants.
 * Used for triggering animations and action timing
 * @param actionDelay amount of time in ticks before [doAction] is called from when the action started.
 * @param actionStartEffect Callback function used to add effects at the start of the action
 * @param actionSuccessEffect Callback function used to add effects at the end of the action
 */
abstract class ActionGoal(
    open val usingEntity: PathfinderMob,
    open var cooldownTime: Int = 20,
    open var actionDelay: Int = 0,
    open val actionStartEffect: (ActionData?) -> Unit = {},
    open val actionSuccessEffect: (ActionData?) -> Unit = {},
    open val actionEndEffect: (ActionData?) -> Unit = {},
    open val actionPredicate: Predicate<PathfinderMob> = Predicate { true },
    open val cooldownVariationRange: IntRange = 0..0
): Goal() {
    open var isDoingAction = false
    open var oldActionTime = -1
    open var actionTimer = -1
    open var actionDirection = -1
    open var maxActionTime = 999

    open var target: LivingEntity? = null

    override fun stop() {
        isDoingAction = false
        actionTimer = -1
    }

    open fun getData(): ActionData? {
        return null
    }

    override fun requiresUpdateEveryTick(): Boolean = true
    final override fun canContinueToUse(): Boolean = canUse()

    override fun tick() {

//        println("ACTION TIMER IS: $actionTimer")

        if (
            canDoAction()
            && !(usingEntity is PazPlant && (usingEntity as PazPlant).cooldown > -1)
            && actionTimer == -1
            && actionDelay >= 0
        ) {
            (usingEntity as? PazPlant)?.cooldown = Mth.floor(
                (cooldownTime+cooldownVariationRange.random()) *
                        if ((usingEntity as PazPlant).poweredUp) 0.8 else 1.0
            ).coerceAtLeast(actionDelay)
            startAction()
            actionTimer = actionDelay.coerceAtLeast(0)
            actionStartEffect(getData())
            isDoingAction = true
        }

        if (actionTimer > 0) {
            calculateActionTime()
        }
        if (actionTimer == 0) {// do action
            runAction()
        }
    }

    open fun calculateActionTime() {
        oldActionTime = actionTimer
        actionTimer = (actionTimer+actionDirection).coerceIn(0, maxActionTime)
    }

    open fun runAction() {
        if (actionPredicate.test(usingEntity)) {
            preAction()
            if (doAction()) actionSuccessEffect(getData())
        }
        isDoingAction = false
        actionTimer = -1
        actionEndEffect(getData())
        postAction()
    }

    open fun startAction() {

    }

    open fun preAction() {

    }

    open fun postAction() {

    }

    abstract fun canDoAction() : Boolean
    abstract fun doAction() : Boolean
}