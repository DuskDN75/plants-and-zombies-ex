package duskdn.plantz.worldgen.plant.rules.water

import duskdn.plantz.worldgen.SpawnRules.IS_PLANTABLE_WATER
import duskdn.plantz.worldgen.SpawnRules.SPECIAL_WATER_SPAWN
import duskdn.plantz.worldgen.init.SpawnContext
import duskdn.plantz.worldgen.plant.rules.PlantNormalSpawnRules

open class PlantWaterSpawnRules(): PlantNormalSpawnRules() {

    var waterSpawnChance: Float = 0.25f

    override fun getValidRule(context: SpawnContext): Boolean {
        context.setData("waterSpawnChance", waterSpawnChance)
        return super.getValidRule(context) && SPECIAL_WATER_SPAWN.testRule(context)
    }

    override fun getPlantableRule(context: SpawnContext): Boolean {
        return super.getPlantableRule(context) || IS_PLANTABLE_WATER.testRule(context)
    }

}