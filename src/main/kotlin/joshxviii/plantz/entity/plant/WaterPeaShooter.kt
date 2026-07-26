package joshxviii.plantz.entity.plant

import joshxviii.plantz.init.PazEntities
import joshxviii.plantz.ai.goal.ProjectileAttackGoal
import joshxviii.plantz.entity.plant.init.AttackingPlant
import joshxviii.plantz.entity.plant.init.Plant
import joshxviii.plantz.entity.plant.utils.waterSurvivalCheck
import joshxviii.plantz.entity.projectile.Pea
import joshxviii.plantz.entity.projectile.PeaWater
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.BlockTags
import net.minecraft.tags.FluidTags
import net.minecraft.util.RandomSource
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.monster.zombie.Zombie
import net.minecraft.world.level.Level
import net.minecraft.world.level.ServerLevelAccessor
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.abs

class WaterPeaShooter(type: EntityType<out AttackingPlant>, level: Level) : AttackingPlant(PazEntities.WATER_PEA_SHOOTER, level) {

    companion object {
        fun checkWaterPeaSpawnRules(
            type: EntityType<out Plant>,
            level: ServerLevelAccessor,
            spawnReason: EntitySpawnReason,
            pos: BlockPos,
            random: RandomSource
        ): Boolean {
            val isRaining = level.level.isRaining
            val inWater = level.getFluidState(pos).`is`(FluidTags.WATER)
            val rainBonus = if (isRaining) 2.25f else 1f

            return checkValidSpawn(level, pos, spawnReason)
                    && inWater && random.nextFloat() < (0.2 * rainBonus)
        }
    }

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
            cooldownTime = 19,
            velocity = 0.5,
            actionDelay = 3)

        this.goalSelector.addGoal(2, projectileAttackGoal as Goal)
    }

    override fun canSurviveOn(block: BlockState): Boolean {
        val waterSurvival = waterSurvivalCheck(block)
//        println("Can survive in water: $waterSurvival")
        return waterSurvival || super.canSurviveOn(block) || block.`is`(BlockTags.SAND) || block.`is`(Blocks.SOUL_SAND)
    }

    override fun actuallyHurt(level: ServerLevel, source: DamageSource, damage: Float) {
        super.actuallyHurt(level, source, damage)
    }
}
