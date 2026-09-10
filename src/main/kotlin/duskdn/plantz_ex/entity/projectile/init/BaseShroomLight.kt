package duskdn.plantz_ex.entity.projectile.init

import duskdn.plantz_ex.init.PazDamageTypes
import duskdn.plantz_ex.init.PazEntities
import duskdn.plantz_ex.init.PazServerParticles
import net.minecraft.core.particles.BlockParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3

open class BaseShroomLight(
    type: EntityType<out BaseShroomLight> = PazEntities.SHROOMLIGHT,
    level: Level,
    entityOwner: LivingEntity? = null,
    spawnOffset: Vec2 = Vec2.ZERO,
    damageType: ResourceKey<DamageType> = PazDamageTypes.PLANT,
) : PazProjectile(
    type = type,
    level = level,
    entityOwner = entityOwner,
    spawnOffset = spawnOffset,
    damageType = damageType
) {
    override fun getDefaultGravity(): Double = 0.05

    override fun getLightLevel(): Int {
        return 8
    }

    override fun tick() {
        super.tick()
        movingParticles()
    }

    fun movingParticles() {
        spawnParticle(
            getMovingParticle(),
            spread = Vec3(0.01,0.01,0.01),
            speed = 0.1
        )
    }

    open fun getMovingParticle(): SimpleParticleType = PazServerParticles.SHROOMLIGHT

    open fun getHitParticle(): SimpleParticleType = PazServerParticles.SHROOMLIGHT_HIT

    override fun getHitSound(): SoundEvent = SoundEvents.SHROOMLIGHT_BREAK

    override fun getKnockback(): Float = 0.15f

    override fun onHit(hitResult: HitResult) {
        super.onHit(hitResult)
        knockbackNearby(0.25f)
        spawnParticle(
            getHitParticle(),
            amount = 18,
            speed = 0.4,
            spread = Vec3(0.1, 0.1, 0.1)
        )
    }

}