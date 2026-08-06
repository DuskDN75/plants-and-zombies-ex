package duskdn.plantz.worldgen.zombie.rules.ground

import duskdn.plantz.worldgen.SpawnRules.IS_PLANTABLE_SAND
import duskdn.plantz.worldgen.init.SpawnContext
import duskdn.plantz.worldgen.plant.rules.PlantNormalSpawnRules

open class PlantSandSpawnRules(): PlantNormalSpawnRules() {

    override fun getPlantableRule(context: SpawnContext): Boolean {
        return super.getPlantableRule(context) && IS_PLANTABLE_SAND.testRule(context)
    }

}