package duskdn.plantz_ex.entity.projectile.shroomlights

import duskdn.plantz_ex.common.PazTeleportRandomlyAwayEffect
import duskdn.plantz_ex.entity.projectile.init.BaseShroomLight
import duskdn.plantz_ex.entity.projectile.init.PazProjectile
import duskdn.plantz_ex.init.PazDamageTypes
import duskdn.plantz_ex.init.PazEntities
import duskdn.plantz_ex.init.PazServerParticles
import net.minecraft.core.particles.BlockParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.consume_effects.TeleportRandomlyConsumeEffect
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3

class WarpedShroomLight(
    level: Level,
    owner: LivingEntity? = null,
    spawnOffset: Vec2 = Vec2.ZERO,
) : BaseShroomLight(
    PazEntities.WARPED_SHROOMLIGHT, level, owner, spawnOffset,
    PazDamageTypes.PLANT,
) {

    companion object {
        val randomTeleportEffect = PazTeleportRandomlyAwayEffect(8f, 60f)
    }

    override fun getMovingParticle(): SimpleParticleType = PazServerParticles.WARPED_SHROOMLIGHT

    override fun getHitParticle(): SimpleParticleType = PazServerParticles.WARPED_SHROOMLIGHT_HIT

    override fun afterHitEntityEffect(target: LivingEntity) {
        super.afterHitEntityEffect(target)
        randomTeleportEffect.apply(level(), target, this.deltaMovement)
    }

}