package duskdn.plantz.ai.goal

import duskdn.plantz.entity.Sun
import duskdn.plantz.entity.plant.init.PazPlant
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.PathfinderMob
import java.util.function.Predicate

class GenerateSunGoal(
    usingEntity: PazPlant,
    cooldownTime: Int = 500,
    actionDelay: Int = 0,
    actionStartEffect: (ActionData?) -> Unit = {},
    actionSuccessEffect: (ActionData?) -> Unit = {},
    actionEndEffect: (ActionData?) -> Unit = {},
    actionPredicate: Predicate<PathfinderMob> = Predicate { true },
    val sunAmount: Int = 1,
    val generatesAtNight : Boolean = false,
): ActionGoal(usingEntity, cooldownTime, actionDelay, actionStartEffect, actionSuccessEffect, actionEndEffect, actionPredicate, -10..20) {
    override fun canUse(): Boolean = (
        usingEntity.tickCount>1
            && usingEntity.isAlive
            && !(usingEntity is PazPlant && (usingEntity.isAsleep || usingEntity.isGrowingSeeds))
    )

    override var actionTimer: Int = 140

    override fun stop() {
        isDoingAction = false
        actionTimer = 140
    }

    override fun canDoAction(): Boolean = (generatesAtNight || (usingEntity as? PazPlant)?.sunIsVisible() == true)

    override fun doAction() : Boolean {
        val serverLevel = usingEntity.level() as? ServerLevel ?: return false
        Sun.award(serverLevel, usingEntity.position(), if (usingEntity.isBaby) (sunAmount/2).coerceAtLeast(1) else sunAmount )
        usingEntity.playSound(SoundEvents.CHICKEN_EGG, 1.0f, 0.5f)
        return true
    }
}