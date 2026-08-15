package duskdn.plantz_ex.ai.goal

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.ai.targeting.TargetingConditions

class PlantTargetGoal<T : LivingEntity>(
    mob: Mob,
    targetType: Class<T>,
    randomInterval: Int,
    mustSee: Boolean,
    mustReach: Boolean,
    selector: TargetingConditions.Selector
) : NearestAttackableTargetGoal<T>(mob, targetType, randomInterval, mustSee, mustReach, selector) {

    override fun canContinueToUse(): Boolean {
        return mob.target != null && mob.target!!.isAlive
    }

}