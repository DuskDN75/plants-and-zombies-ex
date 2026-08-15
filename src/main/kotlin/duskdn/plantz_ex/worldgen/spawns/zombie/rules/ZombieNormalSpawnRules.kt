package duskdn.plantz_ex.worldgen.spawns.zombie.rules

import duskdn.plantz_ex.worldgen.spawns.SpawnRules
import duskdn.plantz_ex.worldgen.spawns.init.SpawnContext
import duskdn.plantz_ex.worldgen.spawns.plant.init.PlantSpawnRules

open class ZombieNormalSpawnRules(): PlantSpawnRules() {

    open var isMushroom: Boolean = false
    open var strict: Boolean = true

    override fun getValidRule(context: SpawnContext): Boolean {
        context.setData("seaLevelOffset", -8)

        var otherRule = if (isMushroom) {
            SpawnRules.IS_DARK.testRule(context)
        } else {
            SpawnRules.ABOVE_SEALEVEL.testRule(context) && !SpawnRules.IS_DARK.testRule(context)
        }

        if (!strict) otherRule = true

        return super.getValidRule(context) && otherRule
    }

}