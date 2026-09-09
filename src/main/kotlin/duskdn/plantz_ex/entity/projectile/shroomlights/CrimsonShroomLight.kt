package duskdn.plantz_ex.entity.projectile.shroomlights

import duskdn.plantz_ex.entity.projectile.init.BaseShroomLight
import duskdn.plantz_ex.entity.projectile.init.PazProjectile
import duskdn.plantz_ex.init.PazDamageTypes
import duskdn.plantz_ex.init.PazEntities
import duskdn.plantz_ex.init.PazServerParticles
import net.minecraft.core.particles.BlockParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3

class CrimsonShroomLight(
    level: Level,
    owner: LivingEntity? = null,
    spawnOffset: Vec2 = Vec2.ZERO,
) : BaseShroomLight(
    PazEntities.CRIMSON_SHROOMLIGHT, level, owner, spawnOffset,
    PazDamageTypes.PLANT_FIRE,
) {

    override fun getHitSound(): SoundEvent = SoundEvents.BIG_DRIPLEAF_BREAK

    override fun getMovingParticle(): SimpleParticleType = PazServerParticles.CRIMSON_SHROOMLIGHT

    override fun getHitParticle(): SimpleParticleType = PazServerParticles.CRIMSON_SHROOMLIGHT_HIT

    override fun afterHitEntityEffect(target: LivingEntity) {
        super.afterHitEntityEffect(target)
        target.igniteForSeconds(3.5f);
    }

    override fun onHit(hitResult: HitResult) {
        super.onHit(hitResult)
        spawnParticle(
            BlockParticleOption(
                ParticleTypes.BLOCK,
                Blocks.NETHER_WART_BLOCK.defaultBlockState()
            ),
            amount = 4,
            speed = 0.1,
            spread = Vec3(0.1, 0.1, 0.1)
        )
    }

}