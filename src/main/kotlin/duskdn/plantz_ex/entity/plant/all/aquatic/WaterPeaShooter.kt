package duskdn.plantz_ex.entity.plant.all.aquatic

import duskdn.plantz_ex.init.PazEntities
import duskdn.plantz_ex.ai.goal.ProjectileAttackGoal
import duskdn.plantz_ex.entity.plant.init.AttackingPlant
import duskdn.plantz_ex.entity.plant.interfaces.IAquaticPlant
import duskdn.plantz_ex.entity.plant.utils.sandSurvivalCheck
import duskdn.plantz_ex.entity.plant.utils.waterSurvivalCheck
import duskdn.plantz_ex.entity.projectile.peas.PeaWater
import duskdn.plantz_ex.util.debugPrint
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class WaterPeaShooter(type: EntityType<out AttackingPlant>, level: Level) : AttackingPlant(PazEntities.WATER_PEA_SHOOTER, level), IAquaticPlant {

    override fun isPushedByFluid(): Boolean {
        return false
    }

    override fun doWaterSplashEffect() {

    }

    var inWater = false

    fun waterStateChanged() {

        debugPrint("Water state: $inWater")

        if (inWater) {
            projectileAttackGoal?.velocity = 1.8
        } else {
            projectileAttackGoal?.velocity = 0.5
        }

    }

    override fun tick() {
        super.tick()

        if (this.isInWater != inWater) {
            inWater = this.isInWater
            waterStateChanged()
        }

        if (this.isInWater) {

            this.setDeltaMovement(this.deltaMovement.x, 0.0, this.deltaMovement.z)
        }
    }

    override fun canBreatheUnderwater(): Boolean = true

    var projectileAttackGoal: ProjectileAttackGoal? = null

    override fun registerGoals() {
        super.registerGoals()

        projectileAttackGoal = ProjectileAttackGoal(
            usingEntity = this,
            projectileFactory = { PeaWater(level(), this)},
            cooldownTime = 25,
            velocity = 0.5,
            actionDelay = 3)

        this.goalSelector.addGoal(2, projectileAttackGoal as Goal)
    }

    override fun actuallyHurt(level: ServerLevel, source: DamageSource, damage: Float) {
        super.actuallyHurt(level, source, damage)
    }

    override fun canSurviveOn(block: BlockState): Boolean {
        return super.canSurviveOn(block) || waterSurvivalCheck(block) || sandSurvivalCheck(block)
    }
}
