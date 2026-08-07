package duskdn.plantz.ai.goal

import duskdn.plantz.entity.Balloon
import duskdn.plantz.entity.interfaces.FloatingMob
import net.minecraft.advancements.criterion.MovementPredicate.speed
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Leashable
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.phys.Vec3
import kotlin.math.absoluteValue
import kotlin.math.sign

open class FloatingPathfindGoal<T> (
    val entity: T
): Goal() where T: PathfinderMob, T: FloatingMob {

    override fun canUse(): Boolean {
        return entity.isFloating && entity.spawnPos != null
    }

    open fun setEntityControls() {

        if (entity.target == null) return

        val target = entity.target as LivingEntity

        entity.lookControl.setLookAt(target, 30f, 30f)

        entity.moveControl.setWantedPosition(target.x, target.y, target.z, 1.5)
    }

    open fun avoidGround(block: BlockState, distance: Double): Boolean {
        return false
    }

    open val pullTowardsSpawn: Boolean = false

    open val spawnPullDistance: Double = 0.0

    open val targetPullDistance: Double = 0.0

    open val targetAvoidDistance: Double = 10.0

    open val targetAvoid: Boolean = false

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

        val spawnDirection = spawnPos.subtract(entity.position())



        var direction: Vec3 = spawnDirection

        val spawnDistance = spawnDirection.length()



        val ogFlyingSpeed = entity.getAttribute(Attributes.FLYING_SPEED)?.value ?: 0.0

        var flyingSpeed = ogFlyingSpeed

        var targetDirection: Vec3 = Vec3.ZERO

        var targetDistance: Double = targetAvoidDistance+1



        var forceVec: Vec3 = Vec3.ZERO



        if (entity.target != null && (entity.target as LivingEntity).isAlive) {

            val target = entity.target as LivingEntity

            targetDirection = target.position().subtract(entity.position())

            direction = targetDirection

            targetDistance = targetDirection.length()

            val searcherTarget = if (target !is PathfinderMob && target is Leashable) target.leashHolder else target

            if (targetDistance <= targetAvoidDistance && targetAvoid && searcherTarget is PathfinderMob && searcherTarget.target == this) {

                val offset = if (target.position().y > spawnPos.y) -10.0 else 10.0

                val offsetDirection = direction.add(0.0,offset,0.0)

                direction = offsetDirection

                flyingSpeed *= 2
            }

            if (targetPullDistance != 0.0 && targetDistance <= targetPullDistance.absoluteValue && !targetAvoid) {
                val targetPull = if (!targetAvoid) (targetDistance/targetPullDistance).coerceIn(0.0, 1.0) else 0.0

                flyingSpeed *= 1 - targetPull
            }
        }



        val groundHeight = entity.level().getHeight(Heightmap.Types.WORLD_SURFACE, entity.blockPosition())

        val groundDistance = entity.y - groundHeight.toDouble()

        val groundBlock = entity.level().getBlockState(entity.blockPosition().atY(groundHeight))

        val disLength = direction.length()

        if (disLength <= 1.0) flyingSpeed *= disLength

        if (pullTowardsSpawn) {

            val spawnPull = (spawnDistance/(spawnPullDistance)).coerceIn(0.0, 1.0)

            flyingSpeed *= 1 - spawnPull

        }

        if (avoidGround(groundBlock, groundDistance)) {
            direction = Vec3(
                direction.x,
                50.0,
                direction.z
            )

            flyingSpeed = ogFlyingSpeed
        }

        setEntityDelta(direction, flyingSpeed)

        setEntityControls()
    }

}