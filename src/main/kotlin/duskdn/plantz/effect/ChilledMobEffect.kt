package duskdn.plantz.effect

import duskdn.plantz.entity.plant.init.PazPlant
import duskdn.plantz.init.PazDamageTypes
import duskdn.plantz.init.PazEffects
import duskdn.plantz.util.PazEntityData
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.EntityTypeTags
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

/**
 *
 */
class ChilledMobEffect(
    category: MobEffectCategory,
    color: Int
) : MobEffect(category, color) {
    companion object {
        const val PARTICLE_INTERVAL: Int = 12
    }

    override fun shouldApplyEffectTickThisTick(tickCount: Int, amplification: Int): Boolean {
        return tickCount % PARTICLE_INTERVAL == 0
    }

    override fun applyEffectTick(level: ServerLevel, mob: LivingEntity, amplification: Int): Boolean {

        val duration = mob.getEffect(PazEffects.CHILLED)?.duration!!

        val currentPercentage: Double = if (amplification > 0) (duration / (100.0/amplification)) else 0.0

        if (mob.remainingFireTicks > 0) {
            mob.removeEffect(PazEffects.CHILLED)
            return false
        }

        val isFrozen = (mob as PazEntityData).`plantz$getFrozenId`()

        println("isFrozen = $isFrozen, current = ${currentPercentage*100}%, duration = $duration")

        if (currentPercentage > 1.0 && !isFrozen) {

            setFreezeMob(mob, true)

        } else if (isFrozen) setFreezeMob(mob, false)

        particles(level, mob)

        return true
    }

    fun setFreezeMob(mob: LivingEntity, isFrozen: Boolean) {
        if (mob is Mob) mob.setNoAi(isFrozen)
        (mob as PazEntityData).`plantz$setFrozenId`(isFrozen)
    }

    override fun onEffectStarted(mob: LivingEntity, amplifier: Int) {

        val level = mob.level()

        if (!level.isClientSide && level is ServerLevel) {
            particles(level, mob)
        }

        super.onEffectStarted(mob, amplifier)
    }

    private fun particles(level: ServerLevel, target: LivingEntity) {
        level.sendParticles(
            ParticleTypes.SNOWFLAKE,
            target.x, target.y + target.boundingBox.ysize*0.5, target.z, 10,
            target.boundingBox.xsize*0.55,
            target.boundingBox.ysize*0.25,
            target.boundingBox.zsize*0.55,
            0.0
        )

        if (target.type == EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES) {
            val source = target.damageSources().source(PazDamageTypes.PLANT_FREEZE,null)
            target.hurtServer(level, source, (PazPlant.PEA_DAMAGE).toFloat())
        }
    }
}