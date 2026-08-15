package duskdn.plantz_ex.entity.plant.init

import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Base class for all plant entities that attack.
 * Provides basic behavior for all attacking plants.
 */
abstract class PultPlant(type: EntityType<out PultPlant>, level: Level) : AttackingPlant(type, level) {
    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(PultPlant::class.java)
    }

    override fun mustSeeTarget(): Boolean {
        return false
    }
}