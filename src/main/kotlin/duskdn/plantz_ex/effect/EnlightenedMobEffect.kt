package duskdn.plantz_ex.effect

import duskdn.plantz_ex.entity.Sun
import duskdn.plantz_ex.entity.plant.init.PazPlant
import duskdn.plantz_ex.init.PazServerParticles
import duskdn.plantz_ex.util.debugPrint
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.CombatRules
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity

/**
 * A [MobEffect] that causes the user to spawn sun on death.
 */
class EnlightenedMobEffect(
    category: MobEffectCategory,
    color: Int,
) : MobEffect(category, color, PazServerParticles.EMPTY) {
    companion object {
    }

    override fun shouldApplyEffectTickThisTick(tickCount: Int, amplification: Int): Boolean {
        return tickCount % 20 == 0
    }

    override fun onEffectAdded(effectInstance: MobEffectInstance, entity: LivingEntity) {
        super.onEffectAdded(effectInstance, entity)

        if (entity is PazPlant) entity.funnyBounce()
    }

    override fun applyEffectTick(serverLevel: ServerLevel, mob: LivingEntity, amplification: Int): Boolean {

        if (mob is PazPlant) {

            mob.heal(0.25f)

            mob.addParticlesAroundSelf(
                particle = PazServerParticles.ENLIGHTENED,
                amount = 10..20
            )

        } else {
            particles(serverLevel, mob)
        }

        return true
    }

    override fun onMobHurt(level: ServerLevel, mob: LivingEntity, amplifier: Int, source: DamageSource, damage: Float) {

        debugPrint("MOB IS HURT!!! DAMAGE IS: $damage")

        val finalDamage = CombatRules.getDamageAfterAbsorb(mob, damage, source, mob.armorValue.toFloat(), mob.getAttributeValue(
            net.minecraft.world.entity.ai.attributes.Attributes.ARMOR_TOUGHNESS).toFloat()
        )

        if (mob.isDeadOrDying) {

            debugPrint("MOB IS HURT!!! AWARDING SUN!!")

            Sun.award(level, mob.position(), (amplifier+1) )
        }
    }

    private fun particles(level: ServerLevel, target: LivingEntity) {
        level.sendParticles(
            PazServerParticles.ENLIGHTENED,
            target.x, target.y + target.boundingBox.ysize*0.5, target.z, 5,
            target.boundingBox.xsize*0.55,
            target.boundingBox.ysize*0.25,
            target.boundingBox.zsize*0.55,
            0.0
        )

    }

//    private fun tryAddGoal(entity: LivingEntity) {
//        if (entity !is PazPlant) {
//            if(entity is Mob) {
//                val oldGoal = entity.getAttached<Goal>(HYPNOTIZED_GOAL_ATTACHMENT)
//                if (oldGoal != null) return
//
//                val goal = NearestAttackableTargetGoal(entity, LivingEntity::class.java, true) { target, level ->
//                    target.`is`(PazTags.EntityTypes.ZOMBIE_RAIDERS) || (target is Enemy ) }
//
//                (entity as MobAccessor).targetSelector.addGoal(0, goal)
//                entity.setAttached(HYPNOTIZED_GOAL_ATTACHMENT,goal)
//            }
//        }
//    }
}