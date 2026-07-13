package joshxviii.plantz.entity.projectile

import joshxviii.plantz.init.PazDamageTypes
import joshxviii.plantz.init.PazEntities
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec2

class Missile(
    level: Level,
    owner: LivingEntity? = null,
    spawnOffset: Vec2 = Vec2.ZERO,
) : PazProjectile(PazEntities.MISSILE, level, owner, spawnOffset,
    PazDamageTypes.PLANT
) {
    override fun onHit(hitResult: HitResult) {
        super.onHit(hitResult)
    }
}