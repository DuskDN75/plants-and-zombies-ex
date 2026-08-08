package duskdn.plantz.ai.goal

import duskdn.plantz.entity.Balloon
import duskdn.plantz.entity.interfaces.FloatingMob
import duskdn.plantz.util.blueDustParticle
import duskdn.plantz.util.orangeDustParticle
import duskdn.plantz.util.redDustParticle
import duskdn.plantz.util.trackVariable
import duskdn.plantz.util.trackVector
import duskdn.plantz.util.updateTrackers
import net.minecraft.advancements.criterion.MovementPredicate
import net.minecraft.advancements.criterion.MovementPredicate.speed
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
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
        return entity.isFloating
    }

//    init {
//        trackVariable("currentVelocity", color = Vec3(1.0, 0.5, 0.2))
//    }

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

//    open var currentVelocity: Vec3 = Vec3.ZERO

    open fun setEntityDelta(direction: Vec3, force: Vec3) {

        entity.deltaMovement = direction.add(force)

    }

    open fun getEntityDelta(direction: Vec3, speed: Double): Vec3 {

        val dir = direction.normalize()

        val delta = Vec3(
            dir.x * speed,
            dir.y * speed,
            dir.z * speed
        )

        return delta

    }

    open fun getTargetDirection(target: LivingEntity, spawnPos: Vec3): Pair<Vec3, Double> {

        val target = entity.target as LivingEntity

        var targetDirection = target.position().subtract(entity.position())

        val targetDistance = targetDirection.length()

        val searcherTarget = if (target !is PathfinderMob && target is Leashable) target.leashHolder else target

        var targetSpeed: Double = 1.0

        val avoidTarget = targetDistance <= targetAvoidDistance && targetAvoid && searcherTarget is PathfinderMob && searcherTarget.target == this

        if (avoidTarget) {

            val offset = if (target.position().y > spawnPos.y) -10.0 else 10.0

            targetDirection = targetDirection.add(0.0,offset,0.0)

            targetSpeed *= 2
        }

        if (targetPullDistance != 0.0 && targetDistance <= targetPullDistance.absoluteValue && !avoidTarget) {
            val targetPull = if (!targetAvoid) (targetDistance/targetPullDistance).coerceIn(0.0, 1.0) else 0.0

            targetSpeed -= targetPull
        }

        return targetDirection to targetSpeed

    }

    open fun getSpawnDirection(spawnPos: Vec3): Pair<Vec3, Double> {
        val spawnDirection = spawnPos.subtract(entity.position())

        val spawnDistance = spawnDirection.length()

        var spawnSpeed: Double = 1.0



        if (pullTowardsSpawn) {

            val spawnPull = (spawnDistance/(spawnPullDistance)).coerceIn(0.0, 1.0)

            spawnSpeed -= spawnPull

        }

        return spawnDirection to spawnSpeed
    }

    open fun getAvoidGroundDirection(): Pair<Vec3, Double> {

        val groundHeight = entity.level().getHeight(Heightmap.Types.WORLD_SURFACE, entity.blockPosition())

        val groundDistance = entity.y - groundHeight.toDouble()

        val groundBlock = entity.level().getBlockState(entity.blockPosition().atY(groundHeight))

        var groundAvoidDirection = Vec3.ZERO

        var groundSpeed = 1.0

        if (avoidGround(groundBlock, groundDistance)) {
            groundAvoidDirection = Vec3(
                0.0,
                50.0,
                0.0
            )

            groundSpeed *= 2
        }

        return groundAvoidDirection to groundSpeed

    }

    open fun slowApproach(distance: Double): Double {

        if (distance <= 1.0) return distance

        return 1.0

    }

    override fun tick() {

        updateTrackers(entity.level())

        if (!canUse()) return

        val spawnPos: Vec3 = if (entity.spawnPos != null) entity.spawnPos as Vec3 else entity.position()



        var velocityVector: Vec3 = Vec3.ZERO

        var forceVector: Vec3 = Vec3.ZERO

        var speedMult: Double = 1.0



        val flyingSpeed = entity.getAttribute(Attributes.FLYING_SPEED)?.value ?: 0.0



        if (entity.target != null && entity.target?.isAlive == true) {

            val target = entity.target!!

            val (targetDirection, targetSpeed) = getTargetDirection(target, spawnPos)

            speedMult *= targetSpeed

            velocityVector = velocityVector.add(targetDirection)
        }



        val (spawnDirection, spawnSpeed) = getSpawnDirection(spawnPos)

        speedMult *= spawnSpeed

        velocityVector = velocityVector.add(spawnDirection)



        val (groundDirection, groundSpeed) = getAvoidGroundDirection()

        speedMult *= groundSpeed

        forceVector = forceVector.add(groundDirection)



        val velLength = velocityVector.length()

        speedMult *= slowApproach(velLength)

        val speed = flyingSpeed*speedMult

        val delta = getEntityDelta(velocityVector, speed)

        val entityPos = entity.position()

//        currentVelocity = entityPos.add(delta.scale(speed*100))

        setEntityDelta(delta, forceVector)

        setEntityControls()



        trackVector(entity.level(), orangeDustParticle, entityPos.add(delta.scale(2.0)))

        trackVector(entity.level(), redDustParticle, entityPos.add(spawnDirection.scale(0.5)))

    }

//    override fun tick() {
//
//        var spawnPos: Vec3 = if (entity.spawnPos != null) entity.spawnPos as Vec3 else return
//
//        val spawnDirection = spawnPos.subtract(entity.position())
//
//
//
//        var direction: Vec3 = spawnDirection
//
//        val spawnDistance = spawnDirection.length()
//
//
//
//        val ogFlyingSpeed = entity.getAttribute(Attributes.FLYING_SPEED)?.value ?: 0.0
//
//        var flyingSpeed = ogFlyingSpeed
//
//        var targetDirection: Vec3 = Vec3.ZERO
//
//        var targetDistance: Double = targetAvoidDistance+1
//
//
//
//        var forceVec: Vec3 = Vec3.ZERO
//
//
//
//        if (entity.target != null && (entity.target as LivingEntity).isAlive) {
//
//            val target = entity.target as LivingEntity
//
//            targetDirection = target.position().subtract(entity.position())
//
//            direction = targetDirection
//
//            targetDistance = targetDirection.length()
//
//            val searcherTarget = if (target !is PathfinderMob && target is Leashable) target.leashHolder else target
//
//            if (targetDistance <= targetAvoidDistance && targetAvoid && searcherTarget is PathfinderMob && searcherTarget.target == this) {
//
//                val offset = if (target.position().y > spawnPos.y) -10.0 else 10.0
//
//                val offsetDirection = direction.add(0.0,offset,0.0)
//
//                direction = offsetDirection
//
//                flyingSpeed *= 2
//            }
//
//            if (targetPullDistance != 0.0 && targetDistance <= targetPullDistance.absoluteValue && !targetAvoid) {
//                val targetPull = if (!targetAvoid) (targetDistance/targetPullDistance).coerceIn(0.0, 1.0) else 0.0
//
//                flyingSpeed *= 1 - targetPull
//            }
//        }
//
//
//
//        val groundHeight = entity.level().getHeight(Heightmap.Types.WORLD_SURFACE, entity.blockPosition())
//
//        val groundDistance = entity.y - groundHeight.toDouble()
//
//        val groundBlock = entity.level().getBlockState(entity.blockPosition().atY(groundHeight))
//
//        val disLength = direction.length()
//
//        if (disLength <= 1.0) flyingSpeed *= disLength
//
//        if (pullTowardsSpawn) {
//
//            val spawnPull = (spawnDistance/(spawnPullDistance)).coerceIn(0.0, 1.0)
//
//            flyingSpeed *= 1 - spawnPull
//
//        }
//
//        if (avoidGround(groundBlock, groundDistance)) {
//            direction = Vec3(
//                direction.x,
//                50.0,
//                direction.z
//            )
//
//            flyingSpeed = ogFlyingSpeed
//        }
//
//        setEntityDelta(direction, flyingSpeed, forceVec)
//
//        setEntityControls()
//    }

}