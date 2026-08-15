package duskdn.plantz_ex.ai.goal

import duskdn.plantz_ex.entity.plant.init.PazPlant
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.goal.target.TargetGoal
import net.minecraft.world.entity.ai.targeting.TargetingConditions
import net.minecraft.world.phys.AABB
import java.util.EnumSet
import java.util.function.Predicate

open class NearestPlantTargetGoal(
    usingEntity: PathfinderMob,
    randomInterval: Int,
    mustSee: Boolean,
    mustReach: Boolean,
    selector: TargetingConditions.Selector?
) : TargetGoal(usingEntity, mustSee, mustReach) {
    protected val randomInterval: Int = reducedTickDelay(randomInterval)
    protected var target: PazPlant? = null
    protected val targetConditions: TargetingConditions

    init {
        this.setFlags(EnumSet.of<Flag>(Flag.TARGET))
        this.targetConditions = TargetingConditions.forNonCombat().range(this.followDistance).selector(selector)
    }

    override fun canUse(): Boolean {
        if (this.randomInterval > 0 && this.mob.getRandom().nextInt(this.randomInterval) != 0) {
            return false
        } else {
            this.findTarget()
            return this.target != null
        }
    }

    protected open fun getTargetSearchArea(followDistance: Double): AABB {
        return this.mob.boundingBox.inflate(followDistance, followDistance, followDistance)
    }

    protected fun findTarget() {
        val level = getServerLevel(this.mob)
        this.target = level.getNearestEntity(
            this.mob.level().getEntitiesOfClass(
                PazPlant::class.java,
                this.getTargetSearchArea(this.followDistance),
                Predicate { entity: PazPlant? -> true }),
            this.targetConditions,
            this.mob,
            this.mob.x,
            this.mob.eyeY,
            this.mob.z
        )
    }

    override fun start() {
        this.mob.target = this.target
        super.start()
    }
}