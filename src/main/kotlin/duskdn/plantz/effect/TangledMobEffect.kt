package duskdn.plantz.effect

import duskdn.plantz.entity.plant.init.PazPlant
import duskdn.plantz.init.PazDamageTypes
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

/**
 *
 */
class TangledMobEffect(
    category: MobEffectCategory,
    color: Int,
    particleOptions: ParticleOptions
) : MobEffect(category, color, particleOptions) {

    companion object {
        const val DAMAGE_INTERVAL: Int = 8
        const val DAMAGE_AMOUNT: Float = PazPlant.PEA_DAMAGE.toFloat() / 10f
    }

    override fun applyEffectTick(level: ServerLevel, mob: LivingEntity, amplification: Int): Boolean {
        if (mob.health > 0) {
            mob.hurtServer(level, mob.damageSources().source(PazDamageTypes.PLANT_TANGLE), DAMAGE_AMOUNT)
        }

        return true
    }

    override fun shouldApplyEffectTickThisTick(tickCount: Int, amplification: Int): Boolean {
        val interval = DAMAGE_INTERVAL shr amplification
        return if (interval > 0) tickCount % interval == 0 else true
    }

}