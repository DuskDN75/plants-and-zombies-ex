package duskdn.plantz.worldgen.plant.init

import duskdn.plantz.worldgen.SpawnRules.ABOVE_SEALEVEL
import duskdn.plantz.worldgen.SpawnRules.DEFAULT_PLANT_RULE
import duskdn.plantz.worldgen.SpawnRules.HAS_NO_ADJACENT
import duskdn.plantz.worldgen.SpawnRules.IS_PLANTABLE_DEFAULT
import duskdn.plantz.worldgen.SpawnRules.IS_VALID_SPAWN
import duskdn.plantz.worldgen.init.BaseSpawnRules
import duskdn.plantz.worldgen.init.SpawnContext
import duskdn.plantz.worldgen.init.SpawnRule

open class PlantSpawnRules: BaseSpawnRules() {

    open fun getValidRule(context: SpawnContext): Boolean {
        return IS_VALID_SPAWN.testRule(context)
    }

    open fun getAdjacentRule(context: SpawnContext): Boolean {
        return HAS_NO_ADJACENT.testRule(context)
    }

    open fun getPlantableRule(context: SpawnContext): Boolean {
        return IS_PLANTABLE_DEFAULT.testRule(context)
    }

    override fun addRules() {
        addRule { context ->
            getValidRule(context)
        }

        addRule { context ->
            getAdjacentRule(context)
        }

        addRule { context ->
            getPlantableRule(context)
        }
    }

}