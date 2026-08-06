package duskdn.plantz.entity.plant.all.aquatic

import duskdn.plantz.ai.goal.MeleeAttackActionGoal
import duskdn.plantz.entity.plant.init.AttackingPlant
import duskdn.plantz.entity.plant.init.PazPlant
import duskdn.plantz.entity.plant.utils.waterSurvivalCheck
import duskdn.plantz.init.PazDamageTypes
import duskdn.plantz.init.PazEntities
import duskdn.plantz.init.PazSounds
import duskdn.plantz.init.PazTags.EntityTypes.CANNOT_CHOMP
import duskdn.plantz.util.pazResource
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.animal.fish.AbstractFish
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.monster.zombie.Zombie
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class TangleKelp(type: EntityType<out AttackingPlant>, level: Level) : AttackingPlant(PazEntities.TANGLE_KELP, level) {

    companion object {
        private val TANGLE_ATTACK_MODIFIER = AttributeModifier(
            pazResource("tangle_attack"), 100.0, AttributeModifier.Operation.ADD_MULTIPLIED_BASE
        )
    }

    override fun isPushable(): Boolean = false

    override fun registerGoals() {
        super.registerGoals()
        this.goalSelector.addGoal(1, TangleAttackGoal(this))
        this.targetSelector.addGoal(4, NearestAttackableTargetGoal(this, LivingEntity::class.java, 5, true, false) { target, level ->
            target !is PazPlant
                    && (target is Zombie
                    || target is AbstractFish
                    || (target is Enemy && isTame)
                    || (target is Player && !isTame))
        })
    }

    override fun canBreatheUnderwater(): Boolean = true

    override fun canSurviveOn(block: BlockState): Boolean {
        return waterSurvivalCheck(block)
    }

    class TangleAttackGoal(
        val tangleKelpEntity: TangleKelp,
    ) : MeleeAttackActionGoal(
        usingEntity = tangleKelpEntity,
        cooldownTime = 0,
        actionDelay = 1,
        damageType = PazDamageTypes.PLANT_TANGLE,
        actionStartEffect = {
            tangleKelpEntity.playSound(PazSounds.ZOMBIE_EATS)
        }
    ) {

        override fun doAction() : Boolean {
            val target = usingEntity.target?: return false
            if(!target.`is`(CANNOT_CHOMP)) {
                //Add modifier to increase damage for insta kills
                usingEntity.getAttribute(Attributes.ATTACK_DAMAGE)?.addOrUpdateTransientModifier(TANGLE_ATTACK_MODIFIER)
            }

            !super.doAction()

            //remove modifier if it was added
            usingEntity.getAttribute(Attributes.ATTACK_DAMAGE)?.removeModifier(TANGLE_ATTACK_MODIFIER)
            if (!target.isAlive) {
                (usingEntity.level() as ServerLevel).sendParticles(
                    ParticleTypes.BUBBLE, target.x,target.y+target.eyeHeight,target.z,
                    30,
                    0.2, 0.2, 0.2,
                    0.32
                )
                target.discard()
                usingEntity.discard()
            }

            return true
        }
    }
}