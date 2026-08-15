package duskdn.plantz_ex.worldgen.spawns.zombie.rules.ground

import duskdn.plantz_ex.worldgen.spawns.init.SpawnContext
import duskdn.plantz_ex.worldgen.spawns.plant.rules.PlantNormalSpawnRules
import duskdn.plantz_ex.worldgen.spawns.SpawnRules

class PlantFreeSpawnRules(): PlantNormalSpawnRules() {

    override var strict = false

    override fun getPlantableRule(context: SpawnContext): Boolean {
        return SpawnRules.IS_PLANTABLE_FREE.testRule(context)
    }

}