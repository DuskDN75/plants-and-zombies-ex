package duskdn.plantz.worldgen.plant.rules

import duskdn.plantz.worldgen.SpawnRules
import duskdn.plantz.worldgen.init.SpawnContext
import duskdn.plantz.worldgen.plant.init.PlantSpawnRules

open class PlantNormalSpawnRules(): PlantSpawnRules() {

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