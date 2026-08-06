package duskdn.plantz.ai.goal

import com.sun.xml.internal.stream.Entity
import duskdn.plantz.entity.zombie.BalloonZombie
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.entity.ai.goal.Goal.Flag
import net.minecraft.world.phys.Vec3
import java.util.EnumSet

open class FloatingPathfindGoal (
    private val entity: PathfinderMob
): Goal() {

    init {
        this.flags = EnumSet.of(Flag.MOVE, Flag.LOOK)
    }

    override fun canUse(): Boolean {
        return !entity.onGround()
    }

    open fun setEntityDelta(targetPosition: Vec3, distance: Double, speed: Double) {
        entity.deltaMovement = Vec3(
            (targetPosition.x / distance) * speed,
            (targetPosition.y / distance) * speed * 2,
            (targetPosition.z / distance) * speed
        )
    }

    override fun tick() {

        val target = entity.target ?: return

        entity.lookControl.setLookAt(target, 30f, 30f)

        var targetPosition = target.position().subtract(entity.position())

        var distance = targetPosition.length()

        var flyingSpeed = entity.getAttribute(Attributes.FLYING_SPEED)?.value ?: 0.0

        if (distance <= 0.5) flyingSpeed *= distance

        println("flyingSpeed = $flyingSpeed")

        setEntityDelta(targetPosition, distance, flyingSpeed)

        entity.moveControl.setWantedPosition(target.x, target.y, target.z, 1.5)
    }

}