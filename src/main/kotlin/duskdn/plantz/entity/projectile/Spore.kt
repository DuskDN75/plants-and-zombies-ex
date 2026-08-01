package duskdn.plantz.entity.projectile

import duskdn.plantz.entity.projectile.init.PazProjectile
import duskdn.plantz.init.PazDamageTypes
import duskdn.plantz.init.PazEffects
import duskdn.plantz.init.PazEntities
import duskdn.plantz.init.PazServerParticles
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3

class Spore(
    level: Level,
    owner: LivingEntity? = null,
    spawnOffset: Vec2 = Vec2.ZERO,
) : PazProjectile(PazEntities.SPORE, level, owner, spawnOffset,
    PazDamageTypes.PLANT,
) {

    override fun tick() {
        super.tick()
        spawnParticle(
            PazServerParticles.SPORE,
            spread = Vec3(0.01,0.01,0.01),
            speed = 0.1
        )
    }

    override fun afterHitEntityEffect(target: LivingEntity) {
        target.addEffect(MobEffectInstance(PazEffects.TOXIC, 50, 0, false, false))
    }

    override fun onHit(hitResult: HitResult) {
        super.onHit(hitResult)
        spawnParticle(
            PazServerParticles.SPORE_HIT,
            amount = 18,
            speed = 0.4
        )
    }

}