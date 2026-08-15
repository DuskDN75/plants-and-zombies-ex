package duskdn.plantz_ex.entity.projectile

import duskdn.plantz_ex.entity.plant.init.PazPlant
import duskdn.plantz_ex.entity.projectile.init.PazProjectile
import duskdn.plantz_ex.init.PazDamageTypes
import duskdn.plantz_ex.init.PazEntities
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec2

class Needle(
    level: Level,
    owner: LivingEntity? = null,
    spawnOffset: Vec2 = Vec2.ZERO,
) : PazProjectile(PazEntities.NEEDLE, level, owner, spawnOffset,
    PazDamageTypes.PLANT,
    (PazPlant.PEA_DAMAGE).toFloat()*2
) {
    override fun stickInGroundTime(): Int = 100
    override fun getPierceLevel(): Byte = 4

    override fun onHit(hitResult: HitResult) {
        super.onHit(hitResult)
    }
}