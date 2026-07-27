package duskdn.plantz.entity.plant.init

import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.monster.zombie.Zombie
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

        registerAttackGoal();
    }

    open fun attacksPlayers(): Boolean {
        return false
    }

    fun registerAttackGoal() {

        println("follow range = ${this.getAttributeValue(Attributes.FOLLOW_RANGE)}")

        this.targetSelector.addGoal(4, NearestAttackableTargetGoal(this, LivingEntity::class.java, 5, mustSeeTarget(), false) { target, level ->
            target !is PazPlant // target is not plant
            && (target is Zombie // target is a zombie
            || (target is Enemy && isTame) // or an enemy, IF they are tame
            || (target is Player && !isTame && attacksPlayers())) // or a player, IF they are not tame
        })
    }
}