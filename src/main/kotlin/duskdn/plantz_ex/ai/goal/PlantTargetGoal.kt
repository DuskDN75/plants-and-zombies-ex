package duskdn.plantz_ex.ai.goal

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.ai.goal.target.TargetGoal
import net.minecraft.world.entity.ai.targeting.TargetingConditions
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.AABB
import java.util.*
import java.util.function.Predicate

open class PlantTargetGoal<T : LivingEntity>(
    mob: Mob,
    protected val targetType: Class<T>,
    randomInterval: Int,
    mustSee: Boolean,
    mustReach: Boolean,
    selector: TargetingConditions.Selector?
) : TargetGoal(mob, mustSee, mustReach) {
    protected val randomInterval: Int = reducedTickDelay(randomInterval)
    protected var target: LivingEntity? = null
    protected val targetConditions: TargetingConditions

    constructor(mob: Mob, targetType: Class<T>, mustSee: Boolean) : this(
        mob,
        targetType,
        10,
        mustSee,
        false,
        null as TargetingConditions.Selector?
    )

    constructor(mob: Mob, targetType: Class<T>, mustSee: Boolean, selector: TargetingConditions.Selector?) : this(
        mob,
        targetType,
        10,
        mustSee,
        false,
        selector
    )

    constructor(mob: Mob, targetType: Class<T>, mustSee: Boolean, mustReach: Boolean) : this(
        mob,
        targetType,
        10,
        mustSee,
        mustReach,
        null as TargetingConditions.Selector?
    )

    init {
        this.flags = EnumSet.of<Flag?>(Flag.TARGET)
        this.targetConditions = TargetingConditions.forCombat().range(this.getFollowDistance()).selector(selector)
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
        if (this.targetType != Player::class.java && this.targetType != ServerPlayer::class.java) {
            this.target = level.getNearestEntity(
                this.mob.level().getEntitiesOfClass<T>(
                    this.targetType,
                    this.getTargetSearchArea(this.followDistance),
                    Predicate { entity: T? -> true }),
                this.getTargetConditions(),
                this.mob,
                this.mob.x,
                this.mob.eyeY,
                this.mob.z
            )
        } else {
            this.target = level.getNearestPlayer(
                this.getTargetConditions(),
                this.mob,
                this.mob.getX(),
                this.mob.getEyeY(),
                this.mob.getZ()
            )
        }
    }

    override fun start() {
        this.mob.target = this.target
        super.start()
    }

    private fun getTargetConditions(): TargetingConditions {
        return this.targetConditions.range(this.followDistance)
    }

    companion object {
        private const val DEFAULT_RANDOM_INTERVAL = 10
    }
}