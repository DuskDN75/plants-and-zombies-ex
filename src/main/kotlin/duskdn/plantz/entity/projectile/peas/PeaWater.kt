package duskdn.plantz.entity.projectile.peas

import duskdn.plantz.entity.projectile.init.BasePea
import duskdn.plantz.init.PazDamageTypes
import duskdn.plantz.init.PazEffects
import duskdn.plantz.init.PazEntities
import duskdn.plantz.init.PazServerParticles
import duskdn.plantz.init.PazTags
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3

class PeaWater(
    level: Level,
    owner: LivingEntity? = null,
    spawnOffset: Vec2 = Vec2.ZERO,
) : BasePea(PazEntities.PEA_WATER, level, owner, spawnOffset,
    PazDamageTypes.PLANT_DRENCH
) {

    override fun peaCanTransform(): Boolean = true

    override fun getDefaultGravity(): Double = 0.03
    override fun ignoreWaterDrag(): Boolean = true

    override fun transformPea() {
        super.transformPea()

        spawnParticle(
            ParticleTypes.SMOKE,
            amount = 10,
            spread = Vec3(0.01,0.01,0.01),
            speed = 0.1
        )

        playSound(SoundEvents.LAVA_EXTINGUISH, 1f, 1f)
    }

    override fun afterHitEntityEffect(target: LivingEntity) {
        super.afterHitEntityEffect(target)
        val cannotDrench = (target.`is`(PazTags.EntityTypes.CANNOT_DRENCH))
        if (cannotDrench) return

        if (!waterCheck() && !isInWater) target.addEffect(MobEffectInstance(PazEffects.DRENCHED, 100, 0, false, false))
    }

    override fun getKnockback(): Float = if (waterCheck()) 0.0f else 0.1f

    override fun getPierceLevel(): Byte = if (waterCheck()) 2 else super.getPierceLevel()

    override fun tick() {
    super.tick()
        spawnParticle(
            ParticleTypes.BUBBLE_POP,
            amount = 5,
            spread = Vec3(0.01,0.01,0.01),
            speed = 0.1
        )
    }

    override fun getHitSound(): SoundEvent = SoundEvents.AXOLOTL_SPLASH

    override fun onHit(hitResult: HitResult) {
        super.onHit(hitResult)
        spawnParticle(PazServerParticles.WATER_PEA_HIT)
        spawnParticle(
            ParticleTypes.SPLASH,
            amount = 4,
            speed = 0.1,
            spread = Vec3(0.1, 0.1, 0.1)
        )
    }
}