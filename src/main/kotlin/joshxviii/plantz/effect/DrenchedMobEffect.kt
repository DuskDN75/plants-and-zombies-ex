package joshxviii.plantz.effect

import joshxviii.plantz.effect.ElectrifyMobEffect.Companion.ZAP_DAMAGE
import joshxviii.plantz.init.PazDamageTypes
import joshxviii.plantz.init.PazEffects
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

/**
 *
 */
class DrenchedMobEffect(
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

        if (mob.remainingFireTicks > 0) {
            mob.removeEffect(PazEffects.CHILLED)
            return false
        }

        particles(level, mob)
        
        return true
    }

    private fun particles(level: ServerLevel, target: LivingEntity) {
        level.sendParticles(
            ParticleTypes.FALLING_WATER,
            target.x, target.y + target.boundingBox.ysize*0.5, target.z, 10,
            target.boundingBox.xsize*0.55,
            target.boundingBox.ysize*0.25,
            target.boundingBox.zsize*0.55,
            0.0
        )
    }
}