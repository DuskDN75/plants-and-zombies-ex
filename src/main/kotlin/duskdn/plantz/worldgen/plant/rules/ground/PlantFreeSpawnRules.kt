package duskdn.plantz.worldgen.plant.rules.ground

import duskdn.plantz.worldgen.SpawnRules.IS_PLANTABLE_FREE
import duskdn.plantz.worldgen.init.SpawnContext
import duskdn.plantz.worldgen.plant.rules.PlantNormalSpawnRules

class PlantFreeSpawnRules(): PlantNormalSpawnRules() {

    override var strict = false

    override fun getPlantableRule(context: SpawnContext): Boolean {
        return IS_PLANTABLE_FREE.testRule(context)
    }

}