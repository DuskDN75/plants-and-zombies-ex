package duskdn.plantz_ex.entity.plant.all.mushrooms

import duskdn.plantz_ex.entity.plant.init.PazPlant
import duskdn.plantz_ex.entity.plant.utils.mushroomSurvivalCheck
import duskdn.plantz_ex.init.PazCriteria
import duskdn.plantz_ex.init.PazEffects
import duskdn.plantz_ex.init.PazEntities
import duskdn.plantz_ex.init.PazServerParticles
import duskdn.plantz_ex.init.PazSounds
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.AreaEffectCloud
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.monster.zombie.Zombie
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class HypnoShroom(type: EntityType<out PazPlant>, level: Level) : PazPlant(PazEntities.HYPNOSHROOM, level) {
    override fun registerGoals() {
        super.registerGoals()

        this.targetSelector.addGoal(4, NearestAttackableTargetGoal(this, LivingEntity::class.java, 5, true, false) { target, level ->
            target !is PazPlant
                    && (target is Zombie
                    || (target is Enemy && isTame))
        })
    }

    override fun actuallyHurt(level: ServerLevel, source: DamageSource, damage: Float) {
        super.actuallyHurt(level, source, damage)
        if (isAsleep) return
        val attacker = source.entity
        if (attacker is LivingEntity && !attacker.isInvulnerable) {
            addParticlesAroundSelf(
                particle = PazServerParticles.HYPNO_SPORE,
                amount = 25..30,
                horizontalSpreadScale = 0.6,
                verticalSpreadScale = 0.6,
                height = 0.6f
            )
            attacker.addEffect(MobEffectInstance(PazEffects.HYPNOTIZE, 800, 0))
            val owner = owner
            if (owner is ServerPlayer) PazCriteria.DISCO_HYPNO.trigger(owner, attacker.`is`(PazEntities.DISCO_ZOMBIE))
            playSound(PazSounds.HYPNOTIZED)
        }
    }

    override fun die(source: DamageSource) {
        super.die(source)
        spawnHypnosisCloud()
    }

    private fun spawnHypnosisCloud() {
        val cloud = AreaEffectCloud(level(), x, y, z)
        cloud.radius = 2.5f
        cloud.radiusOnUse = -0.5f
        cloud.waitTime = 10
        cloud.duration = 300
        cloud.setPotionDurationScale(0.25f)
        cloud.radiusPerTick = -cloud.radius / cloud.duration.toFloat()
        cloud.addEffect(MobEffectInstance(PazEffects.HYPNOTIZE, 1000, 0))
        level().addFreshEntity(cloud)
    }

    override fun canSurviveOn(block: BlockState): Boolean {
        return super.canSurviveOn(block) || mushroomSurvivalCheck(block)
    }
}