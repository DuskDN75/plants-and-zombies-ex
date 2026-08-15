package duskdn.plantz_ex.entity.plant.all.mushrooms

import duskdn.plantz_ex.init.PazEntities
import duskdn.plantz_ex.ai.goal.ExplodeGoal
import duskdn.plantz_ex.entity.plant.init.ExplosivePlant
import duskdn.plantz_ex.entity.plant.init.PazPlant
import duskdn.plantz_ex.entity.plant.utils.mushroomSurvivalCheck
import duskdn.plantz_ex.entity.plant.utils.stoneSurvivalCheck
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

class IceShroom(type: EntityType<out PazPlant>, level: Level) : ExplosivePlant(PazEntities.ICE_SHROOM, level) {

    var explodeGoal: ExplodeGoal<IceShroom>? = null

    override fun registerGoals() {
        super.registerGoals()

        explodeGoal = ExplodeGoal(
            usingEntity = this,
            attackRadius = 7f,
            destroyBlocks = true,
            actionEndEffect = { _ ->
                addParticlesAroundSelf(
                    particle = ParticleTypes.ITEM_SNOWBALL,
                    amount = 58..60,
                    speed = 0.15,
                )
                val level = level() as? ServerLevel ?: return@ExplodeGoal
                playSound(SoundEvents.DRAGON_FIREBALL_EXPLODE)
                level.sendParticles(ParticleTypes.SNOWFLAKE,
                    x, y, z, 1, 0.0, 0.0, 0.0, 0.0
                )
                level.sendParticles(ParticleTypes.SNOWFLAKE,
                    x, y+2.5, z, 17, 0.0, 1.0, 0.0, 0.0
                )
            }
        )

        this.goalSelector.addGoal(1, explodeGoal as ExplodeGoal)
    }

    override fun getMaxActiveTime(): Int {
        return 20
    }

    override fun explode(
        radius: Float,
        sound: Holder.Reference<SoundEvent>,
        damageType: ResourceKey<DamageType>,
        destroyBlocks: Boolean,
        causeFire: Boolean
    ) {
        val level = entity.level()
        val source = entity.damageSources().source(damageType, entity,
            if (PazConfig.PLAYER_CREDIT_FOR_PLANT_KILLS) entity.rootOwner else entity)

//        val targets = explodeGoal?.getTargets()

        val cloud = AreaEffectCloud(level(), x, y, z)
        cloud.radius = radius
        cloud.radiusOnUse = 0f
        cloud.waitTime = 0
        cloud.duration = 10
        cloud.radiusPerTick = -cloud.radius / cloud.duration.toFloat()
        cloud.owner = owner
        cloud.addEffect(MobEffectInstance(PazEffects.CHILLED, 165, 1))
        level().addFreshEntity(cloud)

//        if (targets != null) {
//            for (target in targets) {
//                target.addEffect(
//                    MobEffectInstance(PazEffects.CHILLED, 100, 0, false, false)
//                )
//            }
//        }

    }

    override fun canSurviveOn(block: BlockState): Boolean {
        return super.canSurviveOn(block) || mushroomSurvivalCheck(block) || stoneSurvivalCheck(block)
    }
}