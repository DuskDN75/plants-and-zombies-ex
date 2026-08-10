package duskdn.plantz.effect

import duskdn.plantz.entity.plant.init.PazPlant
import duskdn.plantz.init.PazDamageTypes
import duskdn.plantz.init.PazEffects
import duskdn.plantz.util.PazEntityData
import duskdn.plantz.util.pazResource
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.resources.Identifier
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.EntityTypeTags
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import java.lang.classfile.Attribute

/**
 *
 */
class ChilledMobEffect(
    category: MobEffectCategory,
    color: Int
) : MobEffect(category, color) {
    companion object {
        const val PARTICLE_INTERVAL: Int = 12
    }

    val effectModifier: Identifier = pazResource("effect.chilled")

    val attributesList = listOf(
        Attributes.MOVEMENT_SPEED,
        Attributes.FLYING_SPEED,
        Attributes.ATTACK_SPEED,
        Attributes.BLOCK_BREAK_SPEED
    )

    override fun shouldApplyEffectTickThisTick(tickCount: Int, amplification: Int): Boolean {
        return true
    }

    fun applyAttributes(entity: LivingEntity, mult: Double = 1.0) {

        removeAttributes(entity)

        for (attribute in attributesList) {
            entity.getAttribute(attribute)?.let { instance ->
                if (!instance.hasModifier(effectModifier)) {
                    instance.addTransientModifier(
                        AttributeModifier(
                            effectModifier,
                            -0.5 * mult,
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                        )
                    )
                }
            }
        }

    }

    fun removeAttributes(entity: LivingEntity) {

        for (attribute in attributesList) {
            entity.getAttribute(attribute)?.removeModifier(effectModifier)
        }

    }

    override fun onEffectRemoved(effectInstance: MobEffectInstance, entity: LivingEntity) {
        removeAttributes(entity)

        super.onEffectRemoved(effectInstance, entity)
    }

    override fun onEffectStarted(effectInstance: MobEffectInstance, entity: LivingEntity) {
        applyAttributes(entity)

        super.onEffectStarted(effectInstance, entity)
    }

    override fun applyEffectTick(level: ServerLevel, mob: LivingEntity, amplification: Int): Boolean {

        val duration = mob.getEffect(PazEffects.CHILLED)?.duration!!

        val frozenCap = (1 shl amplification-1).toDouble()

        val currentPercentage: Double = if (amplification > 0) (duration / 100.0) else 0.0

        if (mob.remainingFireTicks > 0) {
            mob.removeEffect(PazEffects.CHILLED)
            return false
        }

        val isFrozen = (mob as PazEntityData).`plantz$getFrozenId`()

        println("isFrozen = $isFrozen, current = $currentPercentage, duration = $duration, frozenCap: $frozenCap")

        if (currentPercentage > frozenCap && amplification > 0) {

            if (!isFrozen) {
                setFreezeEntity(mob, true)
            }

        } else if (isFrozen) setFreezeEntity(mob, false)

        if (mob.tickCount % PARTICLE_INTERVAL == 0) particles(level, mob)

        return true
    }

    fun setFreezeEntity(entity: LivingEntity, isFrozen: Boolean) {
        print("FREEZE FOR: $entity SET TO $isFrozen")

        var mult = 1.0

        if (isFrozen) mult = 2.0

        applyAttributes(entity,mult)

        if (entity is Mob) entity.setNoAi(isFrozen)

        (entity as PazEntityData).`plantz$setFrozenId`(isFrozen)
    }

    override fun onEffectStarted(mob: LivingEntity, amplifier: Int) {

        val level = mob.level()

        if (!level.isClientSide && level is ServerLevel) {
            particles(level, mob)
        }

        super.onEffectStarted(mob, amplifier)
    }

    private fun particles(level: ServerLevel, target: LivingEntity) {
        level.sendParticles(
            ParticleTypes.SNOWFLAKE,
            target.x, target.y + target.boundingBox.ysize*0.5, target.z, 10,
            target.boundingBox.xsize*0.55,
            target.boundingBox.ysize*0.25,
            target.boundingBox.zsize*0.55,
            0.0
        )

        if (target.type == EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES) {
            val source = target.damageSources().source(PazDamageTypes.PLANT_FREEZE,null)
            target.hurtServer(level, source, (PazPlant.PEA_DAMAGE).toFloat())
        }
    }
}