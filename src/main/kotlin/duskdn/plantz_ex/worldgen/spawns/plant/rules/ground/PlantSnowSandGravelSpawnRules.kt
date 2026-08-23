package duskdn.plantz_ex.worldgen.spawns.plant.rules.ground

import duskdn.plantz_ex.worldgen.spawns.init.SpawnContext
import duskdn.plantz_ex.worldgen.spawns.plant.rules.PlantNormalSpawnRules
import duskdn.plantz_ex.worldgen.spawns.SpawnRules

open class PlantSnowSandGravelSpawnRules(): PlantSandGravelSpawnRules() {

    override fun getPlantableRule(context: SpawnContext): Boolean {
        return super.getPlantableRule(context) || SpawnRules.IS_PLANTABLE_SNOW.testRule(context)
    }

}