package joshxviii.plantz.entity.plant

import joshxviii.plantz.PazBlocks
import joshxviii.plantz.PazEntities
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class GraveBuster(type: EntityType<out Plant>, level: Level) : Plant(PazEntities.GRAVE_BUSTER, level) {

    override fun registerGoals() {
        super.registerGoals()
    }
    override fun attackGoals() {}

    override fun canSurviveOn(block: BlockState): Boolean {
        return block.`is`(PazBlocks.ZEN_PLANT_POT) || block.`is`(PazBlocks.GRAVESTONE)
    }
}