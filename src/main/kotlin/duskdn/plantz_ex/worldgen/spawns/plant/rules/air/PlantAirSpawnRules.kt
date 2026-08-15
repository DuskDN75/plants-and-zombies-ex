package duskdn.plantz_ex.worldgen.spawns.plant.rules.air

import duskdn.plantz_ex.worldgen.spawns.init.SpawnContext
import duskdn.plantz_ex.worldgen.spawns.plant.rules.PlantNormalSpawnRules
import duskdn.plantz_ex.worldgen.spawns.SpawnRules

open class PlantAirSpawnRules(): PlantNormalSpawnRules() {

    var airSpawnChance: Float = 0.25f

    override fun getValidRule(context: SpawnContext): Boolean {
        context.setData("airSpawnChance", airSpawnChance)
        return super.getValidRule(context) && SpawnRules.AIR_SPAWN.testRule(context)
    }

    override fun getPlantableRule(context: SpawnContext): Boolean {
        return super.getPlantableRule(context) || SpawnRules.IS_PLANTABLE_AIR.testRule(context)
    }

}