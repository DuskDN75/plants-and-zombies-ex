package duskdn.plantz.worldgen.zombie.rules.ground

import duskdn.plantz.worldgen.SpawnRules
import duskdn.plantz.worldgen.init.SpawnContext
import duskdn.plantz.worldgen.plant.rules.PlantNormalSpawnRules

open class PlantFireSpawnRules(): PlantNormalSpawnRules() {

    override var strict = false

    override fun getPlantableRule(context: SpawnContext): Boolean {
        return super.getPlantableRule(context) && SpawnRules.IS_PLANTABLE_FIRE.testRule(context)
    }

}