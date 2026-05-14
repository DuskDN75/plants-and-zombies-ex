package joshxviii.plantz.entity.projectile

import joshxviii.plantz.PazDamageTypes
import joshxviii.plantz.PazEffects
import joshxviii.plantz.PazEntities
import joshxviii.plantz.PazServerParticles
import net.minecraft.tags.EntityTypeTags
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.animal.sheep.Sheep
import net.minecraft.world.level.Level
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec2

class PeaElectric(
    level: Level,
    owner: LivingEntity? = null,
    spawnOffset: Vec2 = Vec2.ZERO,
) : PazProjectile(PazEntities.PEA_ELECTRIC, level, owner, spawnOffset,
    PazDamageTypes.PLANT_ELECTRIC
) {
    override fun afterHitEntityEffect(target: LivingEntity) {
        super.afterHitEntityEffect(target)
    }

    override fun onHitEntity(hitResult: EntityHitResult) {
        val target = hitResult.entity
        (target as? LivingEntity)?.addEffect(MobEffectInstance(PazEffects.ELECTRIFIED, 50, 1))
        super.onHitEntity(hitResult)
    }

    override fun onHit(hitResult: HitResult) {
        super.onHit(hitResult)
        spawnParticle(PazServerParticles.ELECTRIC_PEA_HIT)
    }
}