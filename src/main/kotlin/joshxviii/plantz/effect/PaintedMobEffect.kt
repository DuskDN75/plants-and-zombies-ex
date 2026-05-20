package joshxviii.plantz.effect

import net.minecraft.core.particles.ParticleOptions
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.DyeColor

class PaintedMobEffect(
    category: MobEffectCategory,
    var paintColor: Int = DyeColor.WHITE.fireworkColor
) : MobEffect(category, paintColor) {
    companion object {
        const val CHECK_INTERVAL: Int = 100
    }

    override fun shouldApplyEffectTickThisTick(tickCount: Int, amplification: Int): Boolean {
        return super.shouldApplyEffectTickThisTick(tickCount, amplification)
    }

    override fun applyEffectTick(serverLevel: ServerLevel, mob: LivingEntity, amplification: Int): Boolean {
        return true
    }

    override fun onEffectStarted(effectInstance: MobEffectInstance, entity: LivingEntity) {
        super.onEffectStarted(effectInstance, entity)
    }

    override fun onEffectRemoved(effectInstance: MobEffectInstance, entity: LivingEntity) {
        super.onEffectRemoved(effectInstance, entity)
    }

    override fun createParticleOptions(mobEffectInstance: MobEffectInstance): ParticleOptions {
        return super.createParticleOptions(mobEffectInstance)
    }

}