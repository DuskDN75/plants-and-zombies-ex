package duskdn.plantz.worldgen.plant.rules.ground

import duskdn.plantz.worldgen.SpawnRules
import duskdn.plantz.worldgen.init.SpawnContext
import duskdn.plantz.worldgen.plant.rules.PlantNormalSpawnRules

class PlantLightningSpawnRules(): PlantNormalSpawnRules() {

    override fun getValidRule(context: SpawnContext): Boolean {
        return super.getValidRule(context) && SpawnRules.IS_THUNDERING.testRule(context)
    }

}