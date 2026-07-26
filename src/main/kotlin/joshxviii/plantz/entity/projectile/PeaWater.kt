package joshxviii.plantz.entity.projectile

import joshxviii.plantz.entity.plant.WaterPeaShooter
import joshxviii.plantz.init.PazDamageTypes
import joshxviii.plantz.init.PazEffects
import joshxviii.plantz.init.PazEntities
import joshxviii.plantz.init.PazServerParticles
import joshxviii.plantz.init.PazTags
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.EntityTypeTags
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.TamableAnimal
import net.minecraft.world.level.Level
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3

class PeaWater(
    level: Level,
    owner: LivingEntity? = null,
    spawnOffset: Vec2 = Vec2.ZERO,
) : PazProjectile(PazEntities.PEA_WATER, level, owner, spawnOffset,
    PazDamageTypes.PLANT_DRENCH
) {

    override fun getDefaultGravity(): Double = 0.03
    override fun ignoreWaterDrag(): Boolean = true

    override fun afterHitEntityEffect(target: LivingEntity) {
        super.afterHitEntityEffect(target)
        if (target.`is`(PazTags.EntityTypes.CANNOT_DRENCH)) return

        if (!waterCheck() && !isInWater) target.addEffect(MobEffectInstance(PazEffects.DRENCHED, 100, 0, false, false))
    }

    override fun getKnockback(): Float = if (waterCheck()) 0.0f else 0.3f

    override fun getPierceLevel(): Byte = if (waterCheck()) 2 else 0

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