package duskdn.plantz.entity.plant.all.aerial

import duskdn.plantz.ai.goal.BalloonPriorityProjectileAttackGoal
import duskdn.plantz.ai.goal.FloatingPathfindGoal
import duskdn.plantz.entity.interfaces.FloatingMob
import duskdn.plantz.entity.plant.init.AttackingPlant
import duskdn.plantz.entity.plant.utils.airSurvivalCheck
import duskdn.plantz.entity.plant.utils.onValidGround
import duskdn.plantz.entity.projectile.peas.Pea
import duskdn.plantz.entity.zombie.BalloonZombie
import duskdn.plantz.init.PazEntities
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.control.FlyingMoveControl
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation
import net.minecraft.world.entity.ai.navigation.PathNavigation
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.world.phys.Vec3
import java.util.EnumSet
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import kotlin.math.sign

class SkyPeaShooter(type: EntityType<out SkyPeaShooter>, level: Level) : AttackingPlant(PazEntities.SKY_PEA_SHOOTER, level),
    FloatingMob {
    override fun registerGoals() {
        super.registerGoals()

        this.goalSelector.addGoal(2, BalloonPriorityProjectileAttackGoal(
            usingEntity = this,
            projectileFactory = { Pea(level(), this)},
            cooldownTime = 35,
            actionDelay = 3))

        this.goalSelector.addGoal(3, SkyPeaChaseGoal(this))
    }

    override fun registerAttackGoal() {

        this.targetSelector.addGoal(4, NearestAttackableTargetGoal(this, LivingEntity::class.java, 5, mustSeeTarget(), false) { target, level ->

            (enemyCheck(target) && !(target is BalloonZombie && target.balloons.isNotEmpty()))

        })
    }

    override fun readAdditionalSaveData(input: ValueInput) {
        super.readAdditionalSaveData(input)

        this.isFloating = input.getBooleanOr("isFloating", true)
        this.checkedSpawn = input.getBooleanOr("checkedSpawn", false)

        val hasSpawnPos = input.getBooleanOr("hasSpawnPos", false)

        if (hasSpawnPos) {
            val spawnPosX = input.getDoubleOr("spawnPosX", 0.0)
            val spawnPosY = input.getDoubleOr("spawnPosY", 0.0)
            val spawnPosZ = input.getDoubleOr("spawnPosZ", 0.0)

            spawnPos = Vec3(spawnPosX, spawnPosY, spawnPosZ)
        } else {
            spawnPos = null
        }
    }

    override fun addAdditionalSaveData(output: ValueOutput) {
        super.addAdditionalSaveData(output)

        output.putBoolean("isFloating", isFloating)
        output.putBoolean("checkedSpawn", checkedSpawn)

        val hasSpawnPos = spawnPos != null

        if (hasSpawnPos) {
            output.putBoolean("hasSpawnPos", true)
            output.putDouble("spawnPosX", spawnPos!!.x)
            output.putDouble("spawnPosY", spawnPos!!.y)
            output.putDouble("spawnPosZ", spawnPos!!.z)
        } else {
            output.putBoolean("hasSpawnPos", false)
        }
    }

    override var isFloating: Boolean = true

    override var flyingNavigation: PathNavigation? = null

    override var checkedSpawn = false

    override var spawnPos: Vec3? = null



    override fun createNavigation(level: Level): PathNavigation {
        flyingNavigation = FlyingPathNavigation(this, level)

        this.moveControl = FlyingMoveControl(this, 20, true)
        return flyingNavigation as PathNavigation
    }



    override fun tick() {
        super.tick()

        if (!checkedSpawn) {
            checkedSpawn = true

            println("MAKING SPAWN POS")

            spawnPos = position()
        }
    }

    fun getGroundDistance(): Double {
        val groundHeight = level().getHeight(Heightmap.Types.WORLD_SURFACE, blockPosition()).toDouble()

        return y - groundHeight
    }

    override fun actuallyHurt(level: ServerLevel, source: DamageSource, damage: Float) {
        super.actuallyHurt(level, source, damage)
        if (source.`is`(DamageTypes.LIGHTNING_BOLT)) {
            convertToPlantType(PazEntities.ELECTRIC_PEA_SHOOTER)
        }
    }

    override fun canSurviveOn(block: BlockState): Boolean {
        return super.canSurviveOn(block) || airSurvivalCheck(block)
    }

    override fun causeFallDamage(fallDistance: Double, damageModifier: Float, damageSource: DamageSource): Boolean {
        return false
    }

    fun checkTarget(): Boolean {
        return target != null && (target as LivingEntity).isAlive && (((target as LivingEntity).y-y).absoluteValue >= 1.0 || (target as LivingEntity).distanceTo(this) <= 12.0f)
    }

    override fun onGround(): Boolean {
        return super.onGround() && !checkTarget()
    }

    override fun isNoGravity(): Boolean {
        return (!super.onGround() && checkTarget()) || spawnPos == null
    }

    private class SkyPeaChaseGoal(
        entity: SkyPeaShooter
    ) : FloatingPathfindGoal<SkyPeaShooter>(entity) {

        init {
            this.flags = EnumSet.of(Flag.MOVE)
        }

        override fun setEntityDelta(distance: Vec3, speed: Double) {

            println("spawnPos: ${entity.spawnPos}, distance: $distance, speed: $speed")

            entity.deltaMovement = Vec3(
                0.0,
                (distance.y.sign) * speed * 2,
                0.0
            )

        }

        override val pullTowardsSpawn: Boolean = true

        override val targetAvoidDistance: Double = 5.0

        override val targetAvoid: Boolean = true

        override fun avoidGround(block: BlockState, distance: Double): Boolean {
            return !entity.canSurviveOn(block) && distance >= 2.0
        }

        override fun setEntityControls() {}

        override fun canUse(): Boolean {
            return true
        }

        override val spawnPullDistance: Double = 5.0
    }
}