package duskdn.plantz_ex.worldgen.spawns.plant.rules.ground

import duskdn.plantz_ex.worldgen.spawns.SpawnRules
import duskdn.plantz_ex.worldgen.spawns.init.SpawnContext
import duskdn.plantz_ex.worldgen.spawns.plant.rules.PlantNormalSpawnRules

class PlantLightningSpawnRules(): PlantNormalSpawnRules() {

    override fun getValidRule(context: SpawnContext): Boolean {
        return super.getValidRule(context) && SpawnRules.IS_THUNDERING.testRule(context)
    }

}