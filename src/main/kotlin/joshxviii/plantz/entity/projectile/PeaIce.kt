package joshxviii.plantz.entity.projectile

import joshxviii.plantz.init.PazDamageTypes
import joshxviii.plantz.init.PazEntities
import joshxviii.plantz.init.PazServerParticles
import net.minecraft.tags.EntityTypeTags
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
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
        if (target.`is`(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)) return
        target.addEffect(MobEffectInstance(MobEffects.SLOWNESS, 100, 0))
        target.addEffect(MobEffectInstance(MobEffects.WEAKNESS, 100, 0))
    }

    override fun onHit(hitResult: HitResult) {
        super.onHit(hitResult)
        spawnParticle(PazServerParticles.ICE_PEA_HIT)
    }
}