package duskdn.plantz.entity.projectile.peas

import duskdn.plantz.entity.projectile.init.BasePea
import duskdn.plantz.init.PazDamageTypes
import duskdn.plantz.init.PazEntities
import duskdn.plantz.init.PazServerParticles
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec2

class PeaFire(
    level: Level,
    owner: LivingEntity? = null,
    spawnOffset: Vec2 = Vec2.ZERO
) : BasePea(PazEntities.PEA_FIRE, level, owner, spawnOffset,
    PazDamageTypes.PLANT_FIRE,
) {

    override fun peaCanTransform(): Boolean {
        return false
    }

    override fun afterHitEntityEffect(target: LivingEntity) {
        super.afterHitEntityEffect(target)
        target.igniteForSeconds(3.5f);
    }

    override fun onHit(hitResult: HitResult) {
        super.onHit(hitResult)
        spawnParticle(PazServerParticles.FIRE_PEA_HIT)
    }
}