package duskdn.plantz_ex.entity.plant.init

import duskdn.plantz_ex.entity.Balloon
import duskdn.plantz_ex.init.PazTags
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Base class for all plant entities that attack.
 * Provides basic behavior for all attacking plants.
 */
abstract class AttackingPlant(type: EntityType<out AttackingPlant>, level: Level) : PazPlant(type, level) {
    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(AttackingPlant::class.java)
    }

    open fun mustSeeTarget(): Boolean {
        return true
    }

    override fun registerGoals() {
        super.registerGoals()

        registerTargetGoal();
    }

    open fun attacksPlayers(): Boolean {
        return false
    }

    override fun considersEntityAsAlly(other: Entity): Boolean {

        if (other is PazPlant) return true

        if (isTame) {
            val owner = rootOwner
            if (other === owner) return true
        }

        return false
    }

    override fun canAttack(target: LivingEntity): Boolean {
        return target.isAlive && target !is PazPlant
    }

    override fun wantsToAttack(target: LivingEntity, owner: LivingEntity): Boolean {
        return target.isAlive && target !is PazPlant
    }

    fun enemyCheck(target: LivingEntity): Boolean {
        return target.isAlive && target !is PazPlant // target is not plant
                && (
                target is Enemy // target is a enemy
                        || ( target is Player && !isTame && attacksPlayers() ) // or a player, IF they are not tame
                        || ( BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(target.type).`is`(PazTags.EntityTypes.ATTACKS_PLANTS) )
                        || (target is Balloon && target.leashHolder != null && target.leashHolder is LivingEntity && enemyCheck(
                    target.leashHolder as LivingEntity
                ))
                )
    }

    override fun asValidTarget(target: LivingEntity?): LivingEntity? {
        if (target is Player) {
            if (target.isCreative || target.isSpectator) {
                return null
            }
        }

        return target
    }

    open fun registerTargetGoal() {

//        debugPrint("follow range = ${this.getAttributeValue(Attributes.FOLLOW_RANGE)}")

        this.targetSelector.addGoal(4,
            NearestAttackableTargetGoal(this, LivingEntity::class.java, 5, mustSeeTarget(), false) { target, level ->
                enemyCheck(target) || target is Balloon
            })
    }
}