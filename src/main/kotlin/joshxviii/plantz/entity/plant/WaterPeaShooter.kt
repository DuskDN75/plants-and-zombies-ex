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
import net.minecraft.tags.FluidTags
import net.minecraft.util.RandomSource
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.monster.zombie.Zombie
import net.minecraft.world.level.Level
import net.minecraft.world.level.ServerLevelAccessor
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.abs

class WaterPeaShooter(type: EntityType<out AttackingPlant>, level: Level) : AttackingPlant(PazEntities.PEA_SHOOTER, level) {

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

    override fun tick() {
        super.tick()

        if (this.isInWater) {

            val level = this.level()
            val blockPos = this.blockPosition()
            val fluidState = level.getFluidState(blockPos)

            val fluidHeight = fluidState.getHeight(level, blockPos)
            if (fluidHeight <= 0.0f || fluidState.isEmpty) {
                return
            }

            val waterSurfaceY = blockPos.y.toDouble() + fluidHeight.toDouble()

            val distance = waterSurfaceY - this.y

            if (abs(distance) < 0.2) {

                this.setDeltaMovement(this.x, 0.0, this.z)

                this.setPosRaw(this.x, waterSurfaceY, this.z)
            }
        }
    }

    override fun canBreatheUnderwater(): Boolean = true

    override fun registerGoals() {
        super.registerGoals()

        this.goalSelector.addGoal(2, ProjectileAttackGoal(
            usingEntity = this,
            projectileFactory = { PeaWater(level(), this)},
            cooldownTime = 30,
            velocity = 0.5,
            actionDelay = 3))
    }

    override fun canSurviveOn(block: BlockState): Boolean {
        val waterSurvival = waterSurvivalCheck(block)
//        println("Can survive in water: $waterSurvival")
        return super.canSurviveOn(block) || waterSurvival
    }

    override fun actuallyHurt(level: ServerLevel, source: DamageSource, damage: Float) {
        super.actuallyHurt(level, source, damage)
    }
}
