package duskdn.plantz.entity.plant.all.aquatic

import duskdn.plantz.init.PazEntities
import duskdn.plantz.ai.goal.ProjectileAttackGoal
import duskdn.plantz.entity.plant.init.AttackingPlant
import duskdn.plantz.entity.plant.init.PazPlant
import duskdn.plantz.entity.plant.utils.sandSurvivalCheck
import duskdn.plantz.entity.plant.utils.waterSurvivalCheck
import duskdn.plantz.entity.projectile.peas.PeaWater
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.BlockTags
import net.minecraft.tags.FluidTags
import net.minecraft.util.RandomSource
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.level.Level
import net.minecraft.world.level.ServerLevelAccessor
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState

class WaterPeaShooter(type: EntityType<out AttackingPlant>, level: Level) : AttackingPlant(PazEntities.WATER_PEA_SHOOTER, level) {

    override fun isPushedByFluid(): Boolean {
        return false
    }

    override fun doWaterSplashEffect() {

    }

    var inWater = false

    fun waterStateChanged() {

        println("Water state: $inWater")

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
