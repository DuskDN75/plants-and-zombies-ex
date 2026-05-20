package joshxviii.plantz.entity.projectile

import joshxviii.plantz.PaintParticleOptions
import joshxviii.plantz.PazDamageTypes
import joshxviii.plantz.PazEffects
import joshxviii.plantz.PazEntities
import joshxviii.plantz.effect.PaintedMobEffect
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.util.ARGB
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.animal.sheep.Sheep
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.Level
import net.minecraft.world.phys.EntityHitResult
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec2
import net.minecraft.world.phys.Vec3

class PaintBall(
    level: Level,
    owner: LivingEntity? = null,
    spawnOffset: Vec2 = Vec2.ZERO,
    color: DyeColor = DyeColor.WHITE
) : PazProjectile(PazEntities.PAINT_BALL, level, owner, spawnOffset,
    PazDamageTypes.PAINT, damage = 0.5f, knockback = 0.01
) {
    companion object {
        val COLOR: EntityDataAccessor<Int> = SynchedEntityData.defineId(PaintBall::class.java, EntityDataSerializers.INT)
    }

    var dyeColor: DyeColor
        get() = DyeColor.byId(entityData.get(COLOR))
        set(value) = entityData.set(COLOR, value.id)

    init {
        dyeColor = color
    }

    override fun defineSynchedData(entityData: SynchedEntityData.Builder) {
        super.defineSynchedData(entityData)
        entityData.define(COLOR,  DyeColor.WHITE.id)
    }

    override fun tick() {
        super.tick()
        spawnParticle(
            PaintParticleOptions(dyeColor.fireworkColor, 0.95f),
            spread = Vec3(0.01,0.01,0.01),
            speed = 0.015
        )
    }

    override fun onHit(hitResult: HitResult) {
        super.onHit(hitResult)
        spawnParticle(
            PaintParticleOptions(dyeColor.fireworkColor, 1.95f),
            amount = 18,
            speed = 0.25
        )
    }

    override fun afterHitEntityEffect(target: LivingEntity) {
        super.afterHitEntityEffect(target)
        val oldEffect = target.getEffect(PazEffects.PAINTED)?.apply { effect.value().let {
            if (it is PaintedMobEffect) it.paintColor = this@PaintBall.dyeColor.fireworkColor
        } }
        val paintedEffect = oldEffect?.effect?.value() as? PaintedMobEffect
        if (paintedEffect is PaintedMobEffect) {
            oldEffect.mapDuration { it + 40 }
            paintedEffect.paintColor = ARGB.opaque(this@PaintBall.dyeColor.fireworkColor)
            target.addEffect(oldEffect)
        }
        else {
            val effectInstance = MobEffectInstance(PazEffects.PAINTED, 600, 0).apply { effect.value().let {
                if (it is PaintedMobEffect) it.paintColor = ARGB.opaque(this@PaintBall.dyeColor.fireworkColor)
            } }
            target.addEffect(effectInstance)
        }

        if (target is Sheep && target.hurtMarked) target.color = dyeColor
    }
}