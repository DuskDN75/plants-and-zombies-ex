package duskdn.plantz.entity.plant

import duskdn.plantz.init.PazEntities
import duskdn.plantz.ai.goal.ProjectileAttackGoal
import duskdn.plantz.entity.plant.init.PazPlant
import duskdn.plantz.entity.plant.utils.waterSurvivalCheck
import duskdn.plantz.entity.projectile.WaterSpore
import net.minecraft.core.BlockPos
import net.minecraft.tags.FluidTags
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.monster.zombie.Zombie
import net.minecraft.world.level.Level
import net.minecraft.world.level.ServerLevelAccessor
import net.minecraft.world.level.block.state.BlockState

class SeaShroom(type: EntityType<out PazPlant>, level: Level) : PazPlant(PazEntities.SEA_SHROOM, level) {

    override fun isPushedByFluid(): Boolean {
        return false
    }

    override fun registerGoals() {
        super.registerGoals()

        this.goalSelector.addGoal(2, ProjectileAttackGoal(
            usingEntity = this,
            projectileFactory = { WaterSpore(level(), this) },
            cooldownTime = 20))
        this.targetSelector.addGoal(4, NearestAttackableTargetGoal(this, LivingEntity::class.java, 5, true, false) { target, level ->
            target !is PazPlant
                    && (target is Zombie
                    || (target is Enemy && isTame))
        })
    }

    override fun tick() {
        super.tick()

        if (this.isInWater) {

            this.setDeltaMovement(this.deltaMovement.x, 0.0, this.deltaMovement.z)
        }
    }

    override fun canBreatheUnderwater(): Boolean = true

    override fun canSurviveOn(block: BlockState): Boolean {
        return waterSurvivalCheck(block)
    }
}