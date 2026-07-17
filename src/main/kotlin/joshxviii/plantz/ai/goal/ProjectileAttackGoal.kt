package joshxviii.plantz.ai.goal

import joshxviii.plantz.init.PazSounds
import joshxviii.plantz.util.applyImpulse
import joshxviii.plantz.entity.plant.init.Plant
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
import kotlin.math.atan
import kotlin.math.sqrt

class ProjectileAttackGoal(
    usingEntity: PathfinderMob,
    cooldownTime: Int = 20,
    actionDelay: Int = 0,
    actionStartEffect: () -> Unit = {},
    actionSuccessEffect: () -> Unit = {},
    actionEndEffect: () -> Unit = {},
    actionPredicate: Predicate<PathfinderMob> = Predicate { true },
    val projectileFactory: () -> Entity,
    val velocity : Double = 0.9,
    val inaccuracy: Float = 0.0f,
    val attackRadius: Float = usingEntity.attributes.getValue(Attributes.FOLLOW_RANGE).toFloat(),
    val useHighArc: Boolean = false,
    val soundEvent: SoundEvent? = PazSounds.PROJECTILE_FIRE,
) : ActionGoal(usingEntity, cooldownTime, actionDelay, actionStartEffect, actionSuccessEffect, actionEndEffect, actionPredicate) {
    var distanceSqr: Double = 0.0

    override fun canUse(): Boolean = (
        usingEntity.tickCount>cooldownTime
            && usingEntity.target?.isAlive == true
            && !(usingEntity is Plant && (usingEntity.isAsleep || usingEntity.isGrowingSeeds))
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
    }

    override fun doAction() : Boolean {// fire projectile
        val target = usingEntity.target?: return false

        val level = usingEntity.level() as ServerLevel
        val projectile = projectileFactory()
        if (projectile is Projectile) Projectile.spawnProjectile(projectile, level, ItemStack.EMPTY)
        else level.addFreshEntity(projectile)

        /**
         * Pretends that the plant is at 0, 0, 0 and gets the relative position of the target
         */
        val targetPosNow = Vec3(
            target.x - projectile.x,
            target.boundingBox.minY + (target.bbHeight * .5) - projectile.y, // gets the y at the target's feet, raises it up by half the height to get it centered, and makes it relative to the plant
            target.z - projectile.z
        )

        val horzDistance = targetPosNow.horizontalDistance()

        val distanceRatio = (horzDistance / attackRadius).coerceIn(0.0, 1.0)
        val finalVel = if(useHighArc) Mth.lerp(distanceRatio, 0.0, velocity) else velocity

        val targetPos = calculateMovingTargetPosition(targetPosNow,target, projectile, finalVel)
        val arcs = calculateProjectileArcs(targetPos, projectile.gravity, finalVel)
        if (arcs==null) {// lose target if unreachable
            projectile.discard()
            usingEntity.target = null
            return false
        }

        val finalAngle = if(useHighArc) arcs.first else arcs.second

        val horizDist = targetPos.horizontalDistance()

        val horizUnitX = targetPos.x / horizDist
        val horizUnitZ = targetPos.z / horizDist
        val horizComp = Mth.cos(finalAngle)

        val shootX = (horizUnitX * horizComp)
        val shootY = Mth.sin(finalAngle).toDouble()
        val shootZ = (horizUnitZ * horizComp)

        if (projectile is Projectile) projectile.shoot(shootX, shootY, shootZ, finalVel.toFloat(), inaccuracy)
        else projectile.applyImpulse(shootX, shootY, shootZ, finalVel.toFloat(), inaccuracy)

        if (soundEvent!=null) usingEntity.playSound(soundEvent, 0.7f, 0.4f / (usingEntity.random.nextFloat() * 0.4f + 0.8f))
        return true
    }

    private fun calculateMovingTargetPosition(basePos: Vec3, target: LivingEntity, projectile: Entity, v: Double): Vec3 {

        val targetVel = Vec3(target.deltaMovement.x, 0.0, target.deltaMovement.z)
        if (targetVel.lengthSqr() <= 0.000001) return basePos

        val g = projectile.gravity

        println(projectile.position())
        println(usingEntity.eyePosition)

        var predicted = basePos

        repeat(4) {
            val arcs = calculateProjectileArcs(predicted, g, v) ?: return predicted
            val angle = if (useHighArc) arcs.first else arcs.second

            val horizontalSpeed = v * Mth.cos(angle)
            if (horizontalSpeed <= 0.000001) return predicted

            val horizontalDistance = predicted.horizontalDistance()
            val flightTime = horizontalDistance / horizontalSpeed

            predicted = basePos.add(targetVel.scale(flightTime))
        }

        return predicted
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
        var discriminant = v4 - g * (g * horiz2_d + 2.0 * v2 * dy)

        //impossible shot if discriminant is < 0
        if (discriminant < 0.0) discriminant = 0.0

        val sqrtDisc = sqrt(discriminant)
        val denom = g * horizDist

        val phi1 = atan((v2 + sqrtDisc) / denom) // high arc (parabola)
        val phi2 = atan((v2 - sqrtDisc) / denom) // low arc (straight)

        return phi1 to phi2
    }
}