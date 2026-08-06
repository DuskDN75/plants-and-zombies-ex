package duskdn.plantz.entity.plant.all.aerial

import duskdn.plantz.ai.goal.BalloonPriorityProjectileAttackGoal
import duskdn.plantz.ai.goal.FloatingPathfindGoal
import duskdn.plantz.entity.interfaces.FloatingMob
import duskdn.plantz.entity.plant.init.AttackingPlant
import duskdn.plantz.entity.plant.utils.airSurvivalCheck
import duskdn.plantz.entity.projectile.peas.Pea
import duskdn.plantz.entity.zombie.BalloonZombie
import duskdn.plantz.init.PazEntities
import net.minecraft.server.level.ServerLevel
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
import kotlin.math.sign

class SkyPeaShooter(type: EntityType<out AttackingPlant>, level: Level) : AttackingPlant(PazEntities.SKY_PEA_SHOOTER, level),
    FloatingMob {
    override fun registerGoals() {
        super.registerGoals()

        this.goalSelector.addGoal(2, BalloonPriorityProjectileAttackGoal(
            usingEntity = this,
            projectileFactory = { Pea(level(), this)},
            cooldownTime = 15,
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
    }

    override fun addAdditionalSaveData(output: ValueOutput) {
        super.addAdditionalSaveData(output)

        output.putBoolean("isFloating", isFloating)
        output.putBoolean("checkedSpawn", checkedSpawn)
    }

    override var isFloating: Boolean = true

    override lateinit var flyingNavigation: PathNavigation

    override var checkedSpawn = false

    override var spawnPos: Vec3? = null



    override fun createNavigation(level: Level): PathNavigation {
        flyingNavigation = FlyingPathNavigation(this, level)

        this.moveControl = FlyingMoveControl(this, 20, true)
        return flyingNavigation
    }



    override fun tick() {
        super.tick()

        if (!checkedSpawn && !firstTick) {
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

        override fun canUse(): Boolean {
            return entity.nutrientSupply >= NUTRIENT_SUPPLY_MAX - 1
        }

        override val spawnPullDistance: Double = 5.0
    }
}