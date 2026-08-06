package duskdn.plantz.ai.goal

import duskdn.plantz.entity.interfaces.FloatingMob
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.phys.Vec3
import kotlin.math.absoluteValue

open class FloatingPathfindGoal<T> (
    val entity: T
): Goal() where T: PathfinderMob, T: FloatingMob {

    override fun canUse(): Boolean {
        return entity.isFloating && entity.spawnPos != null
    }

    open fun setEntityControls(target: LivingEntity) {
        entity.lookControl.setLookAt(target, 30f, 30f)

        entity.moveControl.setWantedPosition(target.x, target.y, target.z, 1.5)
    }

    open val spawnPullDistance: Double = 0.0

    open val targetPullDistance: Double = 0.0

    open fun setEntityDelta(distance: Vec3, speed: Double) {

        val dis = distance.normalize()

        entity.deltaMovement = Vec3(
            dis.x * speed,
            dis.y * speed,
            dis.z * speed
        )

    }

    override fun tick() {

        var spawnPos: Vec3 = if (entity.spawnPos != null) entity.spawnPos as Vec3 else return

        val spawnDistance = spawnPos.subtract(entity.position())



        var distance: Vec3 = spawnDistance



        var flyingSpeed = entity.getAttribute(Attributes.FLYING_SPEED)?.value ?: 0.0

        var targetDistance: Vec3 = Vec3.ZERO

        if (entity.target != null) {

            val target = entity.target ?: return

            targetDistance = target.position().subtract(entity.position())

            distance = targetDistance
        }



        val disLength = distance.length()

        if (disLength <= 1.0) flyingSpeed *= disLength

        if (spawnPullDistance.absoluteValue >= 0.01) {

            val spawnPull = (spawnDistance.length()/(spawnPullDistance)).coerceIn(0.0, 1.0)

            flyingSpeed *= 1 - spawnPull

        }

        if (entity.target != null && targetPullDistance != 0.0 && targetDistance.length() <= targetPullDistance.absoluteValue) {

            val targetPull = (targetDistance.length()/targetPullDistance).coerceIn(0.0, 1.0)

            flyingSpeed *= 1 - targetPull

        }

        setEntityDelta(distance, flyingSpeed)
    }

}