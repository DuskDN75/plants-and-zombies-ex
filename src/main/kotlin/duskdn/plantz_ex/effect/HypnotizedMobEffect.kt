package duskdn.plantz_ex.effect

import duskdn.plantz_ex.init.PazEffects.HYPNOTIZED_GOAL_ATTACHMENT
import duskdn.plantz_ex.init.PazTags
import duskdn.plantz_ex.entity.plant.init.PazPlant
import duskdn.plantz_ex.mixin.MobAccessor
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.monster.Enemy

/**
 * A [MobEffect] that causes the user to attack [PazTags.EntityTypes.ZOMBIE_RAIDERS].
 */
class HypnotizedMobEffect(
    category: MobEffectCategory,
    color: Int,
    particleOptions: ParticleOptions
) : MobEffect(category, color, particleOptions) {
    companion object {
        const val CHECK_INTERVAL: Int = 100
    }

    override fun shouldApplyEffectTickThisTick(tickCount: Int, amplification: Int): Boolean {
        val interval = CHECK_INTERVAL shr amplification
        return if (interval > 0) tickCount % interval == 0 else true
    }

    override fun applyEffectTick(serverLevel: ServerLevel, mob: LivingEntity, amplification: Int): Boolean {
        tryAddGoal(mob)
        return true
    }

    override fun onEffectStarted(effectInstance: MobEffectInstance, entity: LivingEntity) {
        super.onEffectStarted(effectInstance, entity)
        tryAddGoal(entity)
    }

    override fun onEffectRemoved(effectInstance: MobEffectInstance, entity: LivingEntity) {
        super.onEffectRemoved(effectInstance, entity)
        if (entity !is PazPlant) {
            if(entity is Mob) {
                val goal = entity.getAttached<Goal>(HYPNOTIZED_GOAL_ATTACHMENT)
                if (goal != null) (entity as MobAccessor).targetSelector.removeGoal(goal)
                entity.setAttached(HYPNOTIZED_GOAL_ATTACHMENT, null)
            }
        }
    }

    private fun tryAddGoal(entity: LivingEntity) {
        if (entity !is PazPlant) {
            if(entity is Mob) {
                val oldGoal = entity.getAttached<Goal>(HYPNOTIZED_GOAL_ATTACHMENT)
                if (oldGoal != null) return

                val goal = NearestAttackableTargetGoal(entity, LivingEntity::class.java, true) { target, level ->
                    target.`is`(PazTags.EntityTypes.ZOMBIE_RAIDERS) || (target is Enemy ) }

                (entity as MobAccessor).targetSelector.addGoal(0, goal)
                entity.setAttached(HYPNOTIZED_GOAL_ATTACHMENT,goal)
            }
        }
    }
}