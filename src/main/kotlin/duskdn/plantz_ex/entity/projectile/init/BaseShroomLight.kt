package duskdn.plantz_ex.entity.projectile.init

import duskdn.plantz_ex.init.PazDamageTypes
import duskdn.plantz_ex.init.PazEntities
import net.minecraft.core.particles.BlockParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.resources.ResourceKey
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
    override fun getDefaultGravity(): Double = 0.04

    override fun getLightLevel(): Int {
        return 8
    }

    override fun onHit(hitResult: HitResult) {
        super.onHit(hitResult)
        spawnParticle(
            BlockParticleOption(
                ParticleTypes.BLOCK,
                Blocks.HONEYCOMB_BLOCK.defaultBlockState()
            ),
            amount = 4,
            speed = 0.1,
            spread = Vec3(0.1, 0.1, 0.1)
        )
    }
}