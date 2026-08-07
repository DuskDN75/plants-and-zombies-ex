package duskdn.plantz.entity

import duskdn.plantz.entity.plant.init.PazPlant.Companion.PEA_DAMAGE
import duskdn.plantz.init.PazDataSerializers.DATA_DYE_COLOR
import duskdn.plantz.init.PazEffects
import duskdn.plantz.init.PazServerParticles
import duskdn.plantz.init.PazSounds
import net.minecraft.core.BlockPos
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.util.Mth
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.*
import net.minecraft.world.entity.Leashable.LeashData
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.Level
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.world.phys.Vec3
import java.util.function.Consumer
import kotlin.math.atan2
import kotlin.math.sqrt

class Balloon(
    type: EntityType<out LivingEntity>,
    level: Level
) : LivingEntity(type, level), Leashable {
    companion object {
        val DYE_COLOR: EntityDataAccessor<DyeColor> = SynchedEntityData.defineId(Balloon::class.java, DATA_DYE_COLOR)

        private const val MAX_PULL_PITCH = 25.0f
        private const val PITCH_SPEED_MULTIPLIER = 180.0f
        private const val PITCH_LERP_SPEED = 0.25f
        private const val YAW_LERP_SPEED = 0.5f
        private const val MIN_ROTATION_SPEED = 0.001f
        private const val HOLDER_PULL_STIFFNESS = 0.0075F
        private const val HOLDER_GRAVITY_LIFT_MULTIPLIER = 1
        private const val MAX_HOLDER_PULL_FORCE = 0.16
        private const val MAX_HOLDER_UPWARD_VELOCITY = 0.5

        data class BalloonAttributes(
            val maxHealth: Double = PEA_DAMAGE*6
        ) {
            fun apply(builder: AttributeSupplier.Builder): AttributeSupplier.Builder {
                return builder
                    .add(Attributes.MAX_HEALTH, maxHealth)
            }
        }
    }

    private val interpolation = InterpolationHandler(this)

    private var balloonLeashData: LeashData? = null
    var dyeColor: DyeColor
        get() = this.entityData.get(DYE_COLOR)
        set(value) = this.entityData.set(DYE_COLOR, value)

    override fun getInterpolation(): InterpolationHandler = interpolation


    override fun defineSynchedData(entityData: SynchedEntityData.Builder) {
        super.defineSynchedData(entityData)
        entityData.define(DYE_COLOR, DyeColor.WHITE)
    }

    var didBalloonSound = false

    override fun baseTick() {
        super.baseTick()
        yRotO = yRot
        xRotO = xRot
    }

    override fun tick() {
        super.tick()
        while (yRot - yRotO < -180.0f) yRotO -= 360.0f
        while (yRot - yRotO >= 180.0f) yRotO += 360.0f

        if (tickCount == 2 && !didBalloonSound) {

            didBalloonSound = true

//            level().playLocalSound(this.blockPosition(), PazSounds.BALLOON_INFLATE, SoundSource.NEUTRAL, 1.0f, 1.0f, true)

            val randomPitch = random.nextInt(80,120).toFloat()/100

            playSound(PazSounds.BALLOON_INFLATE, 1f, randomPitch)
        }

        val horizontalSpeed = sqrt(deltaMovement.x * deltaMovement.x + deltaMovement.z * deltaMovement.z).toFloat()
        val targetPitch = (horizontalSpeed * PITCH_SPEED_MULTIPLIER).coerceAtMost(MAX_PULL_PITCH)

        xRot += (targetPitch - xRot) * PITCH_LERP_SPEED

        if (horizontalSpeed > MIN_ROTATION_SPEED) {
            val targetYaw = (atan2(deltaMovement.z, deltaMovement.x) * Mth.RAD_TO_DEG).toFloat() - 90.0f
            yRot += Mth.wrapDegrees(targetYaw - yRot) * YAW_LERP_SPEED * horizontalSpeed
        }

        if (!level().isClientSide) {
            pushCollidingEntities()
        }

        if (isRemoved) return
        if (isInterpolating) {
            getInterpolation().interpolate()
        } else if (canSimulateMovement()) {
            move(MoverType.SELF, deltaMovement)
            applyGravity()
            deltaMovement = deltaMovement.scale(0.98)
        } else {
            deltaMovement = deltaMovement.scale(0.98)
        }

        val holder = this.leashHolder as? LivingEntity ?: return
        if ((holder as? Player)?.abilities?.flying == true) return
        if (y < holder.y) return
        val verticalStretch = y - holder.y - leashElasticDistance()
        if (verticalStretch <= 0.0) return



        val groundHeight = level().getHeight(Heightmap.Types.WORLD_SURFACE, blockPosition()).toDouble()

        val groundDistance = y - groundHeight

        val groundPull = groundDistance / 100

        val groundPullForce = groundPull.coerceIn(0.0, 1.0)

//        println("groundHeight: $groundHeight, groundDistance: $groundDistance, groundPullForce: $groundPullForce")

        val mult = if (holder.hasEffect(PazEffects.CHILLED)) 0.5 else 0.0

        val crouchMultiplier = if (holder.isCrouching) 0.5 else 1.0
        val gravityLift = holder.getAttributeValue(Attributes.GRAVITY) * HOLDER_GRAVITY_LIFT_MULTIPLIER
        val springLift = verticalStretch * HOLDER_PULL_STIFFNESS
        val totalLift = ((gravityLift + springLift) * (crouchMultiplier - groundPullForce - mult))
            .coerceAtMost(MAX_HOLDER_PULL_FORCE)

        val currentYVelocity = holder.deltaMovement.y
        val availableLift = (MAX_HOLDER_UPWARD_VELOCITY - currentYVelocity).coerceAtLeast(0.0)
        val appliedLift = totalLift.coerceAtMost(availableLift)

//        if (appliedLift <= 0.0) appliedLift = 0.0

        holder.addDeltaMovement(Vec3(0.0, appliedLift, 0.0))
        holder.needsSync = true
        holder.checkFallDistanceAccumulation()
        holder.checkFallDistanceAccumulation()
    }

    private fun pushCollidingEntities() {
        val entities = level().getPushableEntities(this, boundingBox.inflate(0.25))
        for (entity in entities) {
            push(entity)
        }
    }

    override fun push(entity: Entity) {
        if (level().isClientSide) return
        if (entity.noPhysics || noPhysics) return
        if (hasPassenger(entity)) return

        var xa = entity.x - x
        var za = entity.z - z
        var distanceSquared = xa * xa + za * za

        if (distanceSquared < 1.0E-4) return

        distanceSquared = sqrt(distanceSquared)
        xa /= distanceSquared
        za /= distanceSquared

        var strength = 1.0 / distanceSquared
        if (strength > 1.0) strength = 1.0

        xa *= strength * 0.05
        za *= strength * 0.05

        if (entity is Balloon) entity.push(xa*0.075, 0.0, za*0.075)
        else {
            push(-xa*2, 0.0, -za*2)
            entity.push(xa, 0.0, za)
        }
    }


    override fun isPushable(): Boolean = true
    override fun getMainArm(): HumanoidArm {
        return HumanoidArm.RIGHT
    }

    override fun isPickable(): Boolean = true
    override fun getDefaultGravity(): Double = -0.005
    override fun leashElasticDistance(): Double = 3.0
    override fun leashTooFarBehaviour() {
        super.leashTooFarBehaviour()
    }
    override fun closeRangeLeashBehaviour(leashHolder: Entity) {
        super.closeRangeLeashBehaviour(leashHolder)
    }

    override fun onElasticLeashPull() {
        super.onElasticLeashPull()
    }

    override fun setLeashedTo(holder: Entity, synch: Boolean) {
        super.setLeashedTo(holder, synch)
    }

    override fun onLeashRemoved() {
        val holder = this.leashHolder
        super.onLeashRemoved()
    }

    override fun hurtServer(
        level: ServerLevel,
        source: DamageSource,
        damage: Float
    ): Boolean {

        if (isRemoved) return true

        return super.hurtServer(level, source, damage)
    }

    override fun actuallyHurt(level: ServerLevel, source: DamageSource, dmg: Float) {

        if (this.isInvulnerableToBase(source)) return

        if (health - dmg <= 0){
            level.sendParticles(
                PazServerParticles.POP,
                x, y + boundingBox.ysize * 0.5, z,
                1,
                0.0, 0.0, 0.0, 0.0
            )

            makeSound(PazSounds.BALLOON_POP)

            discard()
        }

        super.actuallyHurt(level, source, dmg)
    }

    override fun getDeathSound(): SoundEvent {
        return PazSounds.BALLOON_POP
    }

    override fun getHurtSound(source: DamageSource): SoundEvent {
        return PazSounds.BALLOON_HIT
    }

    override fun lerpPositionAndRotationStep(stepsToTarget: Int, targetX: Double, targetY: Double, targetZ: Double, targetYRot: Double, targetXRot: Double) {
        val wrappedTargetYRot = yRot + Mth.wrapDegrees(targetYRot.toFloat() - yRot)
        super.lerpPositionAndRotationStep(stepsToTarget, targetX, targetY, targetZ, wrappedTargetYRot.toDouble(), targetXRot)
    }

    override fun addAdditionalSaveData(output: ValueOutput) {
        this.writeLeashData(output, balloonLeashData)
        this.entityData.set(DYE_COLOR, dyeColor)
        output.store("plantz:Color", DyeColor.CODEC, dyeColor)
    }

    override fun readAdditionalSaveData(input: ValueInput) {
        this.readLeashData(input)
        this.dyeColor = this.entityData.get(DYE_COLOR)
        input.read("plantz:Color", DyeColor.CODEC).ifPresent { dyeColor -> this.dyeColor = dyeColor }
    }

    override fun getLeashData(): LeashData? {
        return balloonLeashData
    }

    override fun setLeashData(leashData: LeashData?) {
        balloonLeashData = leashData
    }

    override fun getLeashOffset(): Vec3 {
        return Vec3.ZERO
    }
}