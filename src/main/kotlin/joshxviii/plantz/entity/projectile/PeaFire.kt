package joshxviii.plantz.entity.projectile

import joshxviii.plantz.init.PazDamageTypes
import joshxviii.plantz.init.PazEntities
import joshxviii.plantz.init.PazServerParticles
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec2

class PeaFire(
    level: Level,
    owner: LivingEntity? = null,
    spawnOffset: Vec2 = Vec2.ZERO,
) : PazProjectile(PazEntities.PEA_FIRE, level, owner, spawnOffset,
    PazDamageTypes.PLANT_FIRE,
) {
    override fun afterHitEntityEffect(target: LivingEntity) {
        super.afterHitEntityEffect(target)
        target.igniteForSeconds(3.5f);
    }

    override fun onHit(hitResult: HitResult) {
        super.onHit(hitResult)
        spawnParticle(PazServerParticles.FIRE_PEA_HIT)
    }
}