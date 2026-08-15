package duskdn.plantz_ex.worldgen.spawns.zombie.rules.ground

import duskdn.plantz_ex.worldgen.spawns.init.SpawnContext
import duskdn.plantz_ex.worldgen.spawns.plant.rules.PlantNormalSpawnRules
import duskdn.plantz_ex.worldgen.spawns.SpawnRules

open class PlantSandSpawnRules(): PlantNormalSpawnRules() {

    override fun getPlantableRule(context: SpawnContext): Boolean {
        return super.getPlantableRule(context) && SpawnRules.IS_PLANTABLE_SAND.testRule(context)
    }

}