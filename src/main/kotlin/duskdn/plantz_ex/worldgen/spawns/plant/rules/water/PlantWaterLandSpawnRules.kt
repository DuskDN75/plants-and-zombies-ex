package duskdn.plantz_ex.worldgen.spawns.plant.rules.water

import duskdn.plantz_ex.worldgen.spawns.init.SpawnContext
import duskdn.plantz_ex.worldgen.spawns.plant.rules.PlantNormalSpawnRules
import duskdn.plantz_ex.worldgen.spawns.SpawnRules

open class PlantWaterLandSpawnRules(): PlantNormalSpawnRules() {

    var waterSpawnChance: Float = 0.25f

    override fun getValidRule(context: SpawnContext): Boolean {
        context.setData("waterSpawnChance", waterSpawnChance)
        return (super.getValidRule(context) || SpawnRules.IS_VALID_SPAWN_WATER.testRule(context)) && SpawnRules.SPECIAL_WATER_SPAWN.testRule(context)
    }

    override fun getPlantableRule(context: SpawnContext): Boolean {
        return super.getPlantableRule(context) || SpawnRules.IS_PLANTABLE_WATER.testRule(context) || SpawnRules.IS_PLANTABLE_SAND.testRule(context)
    }

}