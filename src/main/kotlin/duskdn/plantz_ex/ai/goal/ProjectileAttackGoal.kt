package duskdn.plantz_ex.ai.goal

import duskdn.plantz_ex.init.PazSounds
import duskdn.plantz_ex.util.applyImpulse
import duskdn.plantz_ex.entity.plant.init.PazPlant
import duskdn.plantz_ex.init.PazConfig.SHOW_DEBUG_INFO
import duskdn.plantz_ex.util.blueDustParticle
import duskdn.plantz_ex.util.debugPrint
import duskdn.plantz_ex.util.trackVector
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.entity.projectile.ProjectileUtil
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.phys.Vec3
import java.util.function.Predicate
import kotlin.math.abs
import kotlin.math.atan
import kotlin.math.sqrt

open class ProjectileAttackGoal(
    usingEntity: PathfinderMob,
    cooldownTime: Int = 20,
    actionDelay: Int = 0,
    actionStartEffect: (ActionData?) -> Unit = {},
    actionSuccessEffect: (ActionData?) -> Unit = {},
    actionEndEffect: (ActionData?) -> Unit = {},
    actionPredicate: Predicate<PathfinderMob> = Predicate { true },
    var projectileFactory: () -> Entity,
    var inaccuracy: Float = 0.0f,
    var attackRadius: Float = usingEntity.attributes.getValue(Attributes.FOLLOW_RANGE).toFloat(),
    var velocity : Double = 1.2,
    var useHighArc: Boolean = false,
    val soundEvent: SoundEvent? = PazSounds.PROJECTILE_FIRE,
) : ActionGoal(usingEntity, cooldownTime, actionDelay, actionStartEffect, actionSuccessEffect, actionEndEffect, actionPredicate) {
    var distanceSqr: Double = 0.0

    var targetMoveVelocity: Vec3 = Vec3(0.0, 0.0, 0.0)

    var lastTarget: LivingEntity? = null

    var targetDistance: Double = 0.0

    var lastTargetDistance: Double = 0.0

    var updateRate: Int = 3

//    var curTarget: LivingEntity? = null

    override fun canUse(): Boolean = (
        usingEntity.tickCount>cooldownTime
            && usingEntity.target?.isAlive == true
            && !(usingEntity is PazPlant && ((usingEntity as PazPlant).isAsleep || (usingEntity as PazPlant).isGrowingSeeds))
    )

    override fun stop() {
        super.stop()
        usingEntity.isAggressive = false
        usingEntity.stopUsingItem()
        usingEntity.target = null
    }

    override fun canDoAction(): Boolean {// check distance and line of sight
        val target = usingEntity.target ?: return false
        if (!target.isAlive) return false

        distanceSqr = usingEntity.distanceToSqr(target)

        usingEntity.lookControl.setLookAt(target, 30f, 30f)
        usingEntity.isAggressive = true

        return distanceSqr <= (attackRadius * attackRadius)
    }

    override fun tick() {
        super.tick()

        if (usingEntity.isUsingItem) {
            if (!canDoAction()) {
                usingEntity.isAggressive = false
                usingEntity.stopUsingItem()
            }
            else {
                val pullTime: Int = usingEntity.ticksUsingItem
                if (pullTime >= actionDelay-1) {
                    usingEntity.stopUsingItem()
                }
            }
        } else usingEntity.startUsingItem(ProjectileUtil.getWeaponHoldingHand(usingEntity, Items.BOW))

        if (usingEntity.tickCount % updateRate == 0) {
            updateTargetVelocity(usingEntity.target)
        }
    }

    fun fire(target: Entity = usingEntity.target as Entity) : Boolean {
        if (!target.isAlive) return false

        val level = usingEntity.level() as ServerLevel
        val projectile = projectileFactory()

        /**
         * Pretends that the plant is at 0, 0, 0 and gets the relative position of the target
         */
        val targetPosNow = Vec3(
            target.x - projectile.x,
            target.boundingBox.minY + (target.bbHeight * .5) - projectile.y, // gets the y at the target's feet, raises it up by half the height to get it centered, and makes it relative to the plant
            target.z - projectile.z
        )

        targetDistance = targetPosNow.horizontalDistance()

//        val fullDistance = targetPosNow.distanceTo(Vec3(0.0, 0.0, 0.0))
//
////        val sqrtDistance = sqrt(fullDistance)

        val gravity = projectile.gravity

        val velocityRequired = sqrt(
            gravity*(targetDistance+(targetPosNow.y * 1.2))
        )

//        debugPrint("velocity = $velocity, distance = $targetDistance, velocityRequired = $velocityRequired")

        val finalVel = if(useHighArc) {
            velocityRequired*velocity*1.2
        } else velocity

        val targetPos = calculateMovingTargetPosition(targetPosNow,target, projectile, finalVel)
        val arcs = calculateProjectileArcs(targetPos, projectile.gravity, finalVel)

        debugPrint(arcs)

        if (arcs==null) {// lose target if unreachable
            projectile.discard()
            usingEntity.target = null
            return false
        }

        val finalAngle = if(useHighArc) arcs.first else arcs.second

        val horizDist = targetPos.horizontalDistance()

        lastTargetDistance = targetDistance

        val horizUnitX = targetPos.x / horizDist
        val horizUnitZ = targetPos.z / horizDist
        val horizComp = Mth.cos(finalAngle)

        val shootX = (horizUnitX * horizComp)
        val shootY = Mth.sin(finalAngle).toDouble()
        val shootZ = (horizUnitZ * horizComp)

        if (projectile is Projectile) {
            Projectile.spawnProjectile(projectile, level, ItemStack.EMPTY)
            projectile.shoot(shootX, shootY, shootZ, finalVel.toFloat(), inaccuracy)
        } else {
            level.addFreshEntity(projectile)
            projectile.applyImpulse(shootX, shootY, shootZ, finalVel.toFloat(), inaccuracy)
        }

        if (soundEvent!=null) usingEntity.playSound(soundEvent, 0.7f, 0.4f / (usingEntity.random.nextFloat() * 0.4f + 0.8f))
        return true
    }

    override fun doAction() : Boolean {// fire projectile
        val target = usingEntity.target?: return false

        return fire()
    }

    val minAngle = Math.toRadians(70.0)

    fun updateTargetVelocity(target: Entity?) {

        val target = usingEntity.target?: return

        if (!target.isAlive) return

        val targetVel = Vec3(
            target.x - target.xo,
            target.y - target.yo,
            target.z - target.zo
        )

        val entityVel = Vec3(
            usingEntity.x - usingEntity.xo,
            usingEntity.y - usingEntity.yo,
            usingEntity.z - usingEntity.zo
        )

        val relativeVel = targetVel.subtract(entityVel)

        var alphaMult = 0.5

        if (target != lastTarget || targetDistance < lastTargetDistance) {
            alphaMult= 3.0
        } else {
            alphaMult=0.2
        }

        val distanceChange = abs((lastTargetDistance - targetDistance))

        val distanceAlpha = Math.clamp(distanceChange*alphaMult,0.0,1.0)

        targetMoveVelocity = targetMoveVelocity.lerp(relativeVel, distanceAlpha)


    }

    private fun calculateMovingTargetPosition(basePos: Vec3, target: Entity, projectile: Entity, v: Double): Vec3 {

        updateTargetVelocity(target)

        if (targetMoveVelocity.lengthSqr() <= 0.000001) return basePos



        val g = projectile.gravity

        var time = basePos.horizontalDistance() / v

        repeat(8) {
            val predicted = basePos.add(targetMoveVelocity.scale(time))

            val arcs = calculateProjectileArcs(predicted, g, v) ?: return predicted
            val angle = if (useHighArc) {
                arcs.first
            } else arcs.second

            val horizontalSpeed = v * Mth.cos(angle)

            val horizontalDistance = predicted.horizontalDistance()
            val flightTime = horizontalDistance / horizontalSpeed

            time = Mth.lerp(0.5f, time.toFloat(), flightTime.toFloat()).toDouble()

//            debugPrint("vel=$targetMoveVelocity time=$time prediction=$predicted")
        }

        val targetPos = basePos.add(targetMoveVelocity.scale(time))

        val testingPos = projectile.position().add(targetPos)

        println("POS IS: $testingPos")

        if (SHOW_DEBUG_INFO) trackVector(projectile.level(), blueDustParticle, testingPos)

        return targetPos
    }

    /**
     * Calculates the high-arc (φ₁, steeper) and low-arc (φ₂, flatter) elevation angles (radians)
     * for a projectile to hit the target position with given initial velocity and gravity.
     *
     * @param targetPos Relative target position from launch point (Vec3)
     * @param g Projectile's gravity
     * @param velocity Initial projectile speed (blocks/tick)
     * @return Pair(highArcAngle, lowArcAngle)
     */
    private fun calculateProjectileArcs(targetPos: Vec3, g: Double, velocity: Double): Pair<Double, Double>? {
        val dx = targetPos.x
        val dy = targetPos.y
        val dz = targetPos.z

        val horizDist = sqrt(dx * dx + dz * dz) // horizontal distance
        if (horizDist <= 0f) return null

        val v2: Double = velocity*velocity
        val v4 = v2 * v2
        val horiz2_d = horizDist * horizDist
        val discriminant = v4 - g * (g * horiz2_d + 2.0 * v2 * dy)

        debugPrint(discriminant)

        //impossible shot if discriminant is < 0

        val sqrtDisc = sqrt(maxOf(0.0, discriminant))
        val denom = g * horizDist

        val phi1 = atan((v2 + sqrtDisc) / denom) // high arc (parabola)
        val phi2 = atan((v2 - sqrtDisc) / denom) // low arc (straight)

        return phi1 to phi2
    }
}