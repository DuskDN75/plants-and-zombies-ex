package duskdn.plantz.entity.projectile.init

import duskdn.plantz.init.PazDamageTypes
import duskdn.plantz.init.PazEntities
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec2

open class BasePea(
    type: EntityType<out BasePea> = PazEntities.PEA,
    level: Level,
    entityOwner: LivingEntity? = null,
    spawnOffset: Vec2 = Vec2.ZERO,
    damageType: ResourceKey<DamageType> = PazDamageTypes.PLANT,
) : PazProjectile(
    type = type,
    level = level,
    entityOwner = entityOwner,
    spawnOffset = spawnOffset,
    damageType = damageType
) {

    open fun peaCanTransform(): Boolean = false

    open fun getTransformedPea(): BasePea? {
        return null
    }

    override fun tick() {
        super.tick()
    }

    open fun transformPea() {
        val level = level() as ServerLevel
        val p = getTransformedPea()

        if (p != null) {

            p.extinguishFire()

            p.remainingFireTicks = -10

            spawnProjectile(p, level, ItemStack.EMPTY)
            p.setPos(position())
            p.deltaMovement = deltaMovement
        }

        discard()
    }

    override fun isOnFire(): Boolean {
        val onFire = super.isOnFire()
        if (onFire && peaCanTransform()) {
            transformPea()
        }
        return onFire
    }
}