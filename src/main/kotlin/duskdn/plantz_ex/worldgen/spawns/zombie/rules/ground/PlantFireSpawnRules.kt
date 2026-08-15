package duskdn.plantz_ex.worldgen.spawns.zombie.rules.ground

import duskdn.plantz_ex.worldgen.spawns.SpawnRules
import duskdn.plantz_ex.worldgen.spawns.init.SpawnContext
import duskdn.plantz_ex.worldgen.spawns.plant.rules.PlantNormalSpawnRules

open class PlantFireSpawnRules(): PlantNormalSpawnRules() {

    override var strict = false

    override fun getPlantableRule(context: SpawnContext): Boolean {
        return super.getPlantableRule(context) && SpawnRules.IS_PLANTABLE_FIRE.testRule(context)
    }

}