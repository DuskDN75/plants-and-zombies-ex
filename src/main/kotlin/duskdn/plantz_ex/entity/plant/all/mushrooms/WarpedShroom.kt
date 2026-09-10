package duskdn.plantz_ex.entity.plant.all.mushrooms

import duskdn.plantz_ex.init.PazEntities
import duskdn.plantz_ex.ai.goal.ExplodeGoal
import duskdn.plantz_ex.ai.goal.ProjectileAttackGoal
import duskdn.plantz_ex.entity.plant.init.ExplosivePlant
import duskdn.plantz_ex.entity.plant.init.PazPlant
import duskdn.plantz_ex.entity.plant.init.PultPlant
import duskdn.plantz_ex.entity.plant.interfaces.IIgneousPlant
import duskdn.plantz_ex.entity.plant.interfaces.IWarmingPlant
import duskdn.plantz_ex.entity.plant.utils.fireSurvivalCheck
import duskdn.plantz_ex.entity.plant.utils.lavaSurvivalCheck
import duskdn.plantz_ex.entity.plant.utils.mushroomSurvivalCheck
import duskdn.plantz_ex.entity.plant.utils.stoneSurvivalCheck
import duskdn.plantz_ex.entity.projectile.shroomlights.CrimsonShroomLight
import duskdn.plantz_ex.entity.projectile.shroomlights.WarpedShroomLight
import duskdn.plantz_ex.init.PazConfig
import duskdn.plantz_ex.init.PazEffects
import net.minecraft.core.Holder
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.AreaEffectCloud
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec2

class WarpedShroom(level: Level) : PultPlant(PazEntities.WARPED_SHROOM, level), IWarmingPlant {

    override fun getLightLevel(): Int {
        return if (isAsleep) 5 else 12
    }

    override fun isPushedByFluid(): Boolean {
        return false
    }

    override fun allowPlayerCollision(): Boolean {
        return true
    }

    override var buoyancyHeight: Double = 0.9

    override fun aiStep() {
        super.aiStep()
    }

    override fun canSurviveOn(block: BlockState): Boolean {
        return super.canSurviveOn(block) || fireSurvivalCheck(block)
    }

    override fun getMeltRadius(): Double = 3.0

    override fun getMeltChance(): Double = 0.2

    override fun registerGoals() {
        super.registerGoals()

        this.goalSelector.addGoal(2, ProjectileAttackGoal(
            usingEntity = this,
            projectileFactory = { WarpedShroomLight(level(), this, spawnOffset = Vec2(0f, 0f)) },
            useHighArc = true,
            velocity = 1.0,
            cooldownTime = 45,
            actionDelay = 8))
    }

}