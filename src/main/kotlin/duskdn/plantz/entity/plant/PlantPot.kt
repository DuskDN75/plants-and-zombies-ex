package duskdn.plantz.entity.plant

import duskdn.plantz.entity.plant.init.PazPlant
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PlantPot(type: EntityType<out PlantPot>, level: Level) : PazPlant(type, level) {
    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(PazPlant::class.java)
    }
}