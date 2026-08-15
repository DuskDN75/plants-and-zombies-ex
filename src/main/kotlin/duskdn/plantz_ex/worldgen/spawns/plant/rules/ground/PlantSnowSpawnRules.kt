package duskdn.plantz_ex.worldgen.spawns.plant.rules.ground

import duskdn.plantz_ex.worldgen.spawns.SpawnRules
import duskdn.plantz_ex.worldgen.spawns.init.SpawnContext
import duskdn.plantz_ex.worldgen.spawns.plant.rules.PlantNormalSpawnRules

class PlantSnowSpawnRules(): PlantNormalSpawnRules() {

    override fun getPlantableRule(context: SpawnContext): Boolean {
        return super.getPlantableRule(context) || SpawnRules.IS_PLANTABLE_SNOW.testRule(context)
    }

}