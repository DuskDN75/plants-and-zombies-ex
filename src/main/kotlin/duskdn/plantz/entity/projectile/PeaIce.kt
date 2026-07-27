package duskdn.plantz.entity.projectile

import duskdn.plantz.init.PazDamageTypes
import duskdn.plantz.init.PazEffects
import duskdn.plantz.init.PazEntities
import duskdn.plantz.init.PazServerParticles
import duskdn.plantz.init.PazTags
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec2

class PeaIce(
    level: Level,
    owner: LivingEntity? = null,
    spawnOffset: Vec2 = Vec2.ZERO,
) : PazProjectile(PazEntities.PEA_ICE, level, owner, spawnOffset,
    PazDamageTypes.PLANT_FREEZE
) {
    override fun afterHitEntityEffect(target: LivingEntity) {
        super.afterHitEntityEffect(target)
        val cannotChill = (target.`is`(PazTags.EntityTypes.CANNOT_CHILL))
        println("APPLYING ENTITY AFFECT. $cannotChill")
        if (cannotChill) return
        target.addEffect(MobEffectInstance(PazEffects.CHILLED, 100, 0, false, false))
    }

    override fun onHit(hitResult: HitResult) {
        super.onHit(hitResult)
        spawnParticle(PazServerParticles.ICE_PEA_HIT)
    }
}