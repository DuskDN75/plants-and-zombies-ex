package duskdn.plantz_ex.entity.projectile.peas

import duskdn.plantz_ex.entity.projectile.init.BasePea
import duskdn.plantz_ex.init.PazDamageTypes
import duskdn.plantz_ex.init.PazEntities
import duskdn.plantz_ex.init.PazServerParticles
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec2

class Pea(
    level: Level,
    owner: LivingEntity? = null,
    spawnOffset: Vec2 = Vec2.ZERO,
) : BasePea(PazEntities.PEA, level, owner, spawnOffset,
    PazDamageTypes.PLANT
) {

    override fun peaCanTransform(): Boolean = true

    override fun getTransformedPea(): BasePea {
        return PeaFire(level(), entityOwner)
    }

    override fun onHit(hitResult: HitResult) {
        super.onHit(hitResult)
        spawnParticle(PazServerParticles.PEA_HIT)
    }
}