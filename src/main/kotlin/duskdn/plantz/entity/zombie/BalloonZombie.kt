package duskdn.plantz.entity.zombie

import duskdn.plantz.entity.Balloon
import duskdn.plantz.entity.plant.init.PazPlant
import duskdn.plantz.init.PazEntities
import duskdn.plantz.init.PazSounds
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.control.FlyingMoveControl
import net.minecraft.world.entity.ai.control.MoveControl
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.ai.goal.target.TargetGoal
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation
import net.minecraft.world.entity.ai.navigation.PathNavigation
import net.minecraft.world.entity.ai.targeting.TargetingConditions
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.Level
import net.minecraft.world.level.ServerLevelAccessor
import net.minecraft.world.phys.Vec3
import java.util.EnumSet
import kotlin.math.abs

class BalloonZombie(type: EntityType<out BalloonZombie> = PazEntities.BALLOON_ZOMBIE, level: Level) : PazZombie(type, level) {

    init {

    }

    override fun getAmbientSound(): SoundEvent {
        return PazSounds.BROWNCOAT_AMBIENT
    }
    override fun getHurtSound(source: DamageSource): SoundEvent {
        return PazSounds.BROWNCOAT_HURT
    }
    override fun getDeathSound(): SoundEvent {
        return PazSounds.BROWNCOAT_DEATH
    }
    override fun getStepSound(): SoundEvent {
        return SoundEvents.ZOMBIE_STEP
    }

    override fun doHurtTarget(level: ServerLevel, target: Entity): Boolean {
        val result = super.doHurtTarget(level, target)
        return result
    }

    override fun isBaby(): Boolean = isBabyZombie()

    lateinit var flyingNavigation: PathNavigation
    lateinit var groundNavigation: PathNavigation

    override fun createNavigation(level: Level): PathNavigation {
        flyingNavigation = FlyingPathNavigation(this, level)
        groundNavigation = GroundPathNavigation(this, level)

        return if (hasBalloon) flyingNavigation else groundNavigation
    }

    override fun getMoveControl(): MoveControl {
        return if (hasBalloon) FlyingMoveControl(this, 20, true) else super.getMoveControl()
    }

    var hasBalloon: Boolean = true

    var balloon : Balloon? = null

    override fun tick() {
        super.tick()

        if (!level().isClientSide && (hasBalloon && balloon?.leashHolder != this || balloon == null || !balloon!!.isAlive)) {
            stopFloating()
        }
    }

    fun stopFloating() {

        println("STOPPING FLOATING")

        hasBalloon = false

        if (groundNavigation != null) {
            navigation = groundNavigation
        }

        moveControl = MoveControl(this)

        this.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.FLYING_SPEED)?.baseValue = 0.0
    }

    override fun registerGoals() {
        super.registerGoals()

        this.goalSelector.addGoal(1, BalloonZombieChaseGoal(this))
    }

//    override fun getDefaultGravity(): Double {
//        return if (hasBalloon) 0.0 else super.getDefaultGravity()
//    }

    override fun finalizeSpawn(
        level: ServerLevelAccessor,
        difficulty: DifficultyInstance,
        spawnReason: EntitySpawnReason,
        groupData: SpawnGroupData?
    ): SpawnGroupData? {
        val data = super.finalizeSpawn(level, difficulty, spawnReason, groupData)

        if (level is ServerLevel) {

            balloon = PazEntities.BALLOON.create(level, spawnReason)

            if (balloon != null && balloon is Balloon) {

                balloon!!.dyeColor = DyeColor.RED

                val randomX = (random.nextDouble() - 0.5) * 2
                val randomZ = (random.nextDouble() - 0.5) * 2

                balloon!!.snapTo(this.x, this.y + 2.0, this.z)

                level.addFreshEntity(balloon!!)

                balloon!!.setLeashedTo(this, true)

                hasBalloon = true

            }

        }

        return data
    }

    override fun actuallyHurt(level: ServerLevel, source: DamageSource, damage: Float) {
        super.actuallyHurt(level, source, damage)
    }

    private class BalloonZombieChaseGoal(
        private val entity: BalloonZombie
    ): Goal() {

        init {
            this.flags = EnumSet.of(Flag.MOVE, Flag.LOOK)
        }

        override fun canUse(): Boolean {
            return entity.hasBalloon && entity.target != null && entity.target?.isAlive == true
        }

        override fun tick() {
            val target = entity.target ?: return

            entity.lookControl.setLookAt(target, 30f, 30f)

            var targetPosition = target.position().subtract(entity.position())

            var distance = targetPosition.length()

            var speed = 0.05

            entity.gravity

            entity.deltaMovement = Vec3(
                (targetPosition.x / distance) * speed,
                (targetPosition.y / distance) * speed * 2,
                (targetPosition.z / distance) * speed
            )

            entity.moveControl.setWantedPosition(target.x, target.y, target.z, 1.5)
        }

    }
}