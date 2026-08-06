package duskdn.plantz.worldgen.zombie.init

import duskdn.plantz.worldgen.SpawnRules.HAS_NO_ADJACENT
import duskdn.plantz.worldgen.SpawnRules.IS_PLANTABLE_DEFAULT
import duskdn.plantz.worldgen.SpawnRules.IS_VALID_SPAWN
import duskdn.plantz.worldgen.init.BaseSpawnRules
import duskdn.plantz.worldgen.init.SpawnContext

open class ZombieSpawnRules: BaseSpawnRules() {

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