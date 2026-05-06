package joshxviii.plantz.ai.goal

import joshxviii.plantz.entity.plant.Plant
import joshxviii.plantz.entity.plant.explode
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.entity.ai.targeting.TargetingConditions
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.monster.zombie.Zombie
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.gameevent.GameEvent
import java.util.*
import java.util.function.Predicate

class ExplodeGoal(
    private val plantEntity: Plant,
    val explosionRadius: Float = 2.5f,
    val sound: Holder.Reference<SoundEvent> = SoundEvents.GENERIC_EXPLODE,
    val destroyBlocks: Boolean = false,
    val activateRange: Double = 3.0,
    val actionPredicate: Predicate<PathfinderMob> = Predicate { true },
    val actionEndEffect: () -> Unit = {},
    selector: TargetingConditions.Selector? = { target, level ->
        target !is Plant
            && target is Zombie
            || (target is Enemy && plantEntity.isTame)
            || (target is Player && !plantEntity.isTame)
    }
) : Goal() {
    protected val targetConditions: TargetingConditions

    companion object {
        private const val DISTANCE_SQR = 49.0
    }

    private var target: LivingEntity? = null

    init {
        setFlags(EnumSet.of<Flag>(Flag.MOVE))
        targetConditions = TargetingConditions.forCombat().range(activateRange).selector(selector)
    }

    override fun canUse(): Boolean {
        if (!actionPredicate.test(plantEntity)) return false
        if ((plantEntity.isAsleep || plantEntity.isGrowingSeeds)) return false
        val level = plantEntity.level() as ServerLevel
        target = level.getNearestEntity(LivingEntity::class.java, targetConditions, plantEntity, plantEntity.x, plantEntity.y, plantEntity.z, plantEntity.boundingBox.inflate(activateRange))
        val t = target
        return (t != null && !t.isDeadOrDying && plantEntity.distanceToSqr(t) < activateRange * activateRange) || plantEntity.swell > 0
    }

    override fun start() {
        plantEntity.getNavigation().stop()
    }

    override fun stop() {
        target = null
        plantEntity.swellDir = -1
    }

    override fun requiresUpdateEveryTick(): Boolean {
        return true
    }

    override fun tick() {
        val currentTarget = target

        plantEntity.swellDir = when {
            currentTarget == null -> -1
            currentTarget.isDeadOrDying -> -1
            plantEntity.distanceToSqr(currentTarget) > DISTANCE_SQR -> -1
            else -> 1
        }

        if (plantEntity.swellDir > 0 && plantEntity.swell == 0) {
            plantEntity.playSound(SoundEvents.CREEPER_PRIMED, 1.0f, 1f + (1-plantEntity.getMaxSwellTime() / 30))
            plantEntity.gameEvent(GameEvent.PRIME_FUSE)
        }

        if (plantEntity.swell == plantEntity.getMaxSwellTime()) {
            actionEndEffect()
            plantEntity.explode(
                radius = explosionRadius,
                sound = sound,
                destroyBlocks = destroyBlocks,
            )
        }
    }
}