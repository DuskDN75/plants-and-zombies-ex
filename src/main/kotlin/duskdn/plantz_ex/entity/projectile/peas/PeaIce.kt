package duskdn.plantz_ex.entity.projectile.peas

import duskdn.plantz_ex.entity.projectile.init.BasePea
import duskdn.plantz_ex.init.PazDamageTypes
import duskdn.plantz_ex.init.PazEffects
import duskdn.plantz_ex.init.PazEntities
import duskdn.plantz_ex.init.PazServerParticles
import duskdn.plantz_ex.init.PazTags
import duskdn.plantz_ex.util.debugPrint
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec2

class PeaIce(
    level: Level,
    owner: LivingEntity? = null,
    spawnOffset: Vec2 = Vec2.ZERO,
) : BasePea(PazEntities.PEA_ICE, level, owner, spawnOffset,
    PazDamageTypes.PLANT_FREEZE
) {

    override fun peaCanTransform(): Boolean = true

    override fun getTransformedPea(): BasePea {
        return PeaWater(level = level(), owner = entityOwner)
    }

    override fun afterHitEntityEffect(target: LivingEntity) {
        super.afterHitEntityEffect(target)
        val cannotChill = (target.`is`(PazTags.EntityTypes.CANNOT_CHILL))
        debugPrint("APPLYING ENTITY AFFECT. $cannotChill")
        if (cannotChill) return
        target.addEffect(MobEffectInstance(PazEffects.CHILLED, 100, 0, false, false))
    }

    override fun onHit(hitResult: HitResult) {
        super.onHit(hitResult)
        spawnParticle(PazServerParticles.ICE_PEA_HIT)
    }
}