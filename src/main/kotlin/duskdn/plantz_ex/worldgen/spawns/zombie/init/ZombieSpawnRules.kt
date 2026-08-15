package duskdn.plantz_ex.worldgen.spawns.zombie.init

import duskdn.plantz_ex.worldgen.spawns.init.BaseSpawnRules
import duskdn.plantz_ex.worldgen.spawns.init.SpawnContext
import duskdn.plantz_ex.worldgen.spawns.SpawnRules

open class ZombieSpawnRules: BaseSpawnRules() {

    open fun getValidRule(context: SpawnContext): Boolean {
        return SpawnRules.IS_VALID_SPAWN.testRule(context)
    }

    open fun getAdjacentRule(context: SpawnContext): Boolean {
        return SpawnRules.HAS_NO_ADJACENT.testRule(context)
    }

    open fun getPlantableRule(context: SpawnContext): Boolean {
        return SpawnRules.IS_PLANTABLE_DEFAULT.testRule(context)
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