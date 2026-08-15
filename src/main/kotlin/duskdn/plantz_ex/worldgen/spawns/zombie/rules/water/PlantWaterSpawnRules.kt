package duskdn.plantz_ex.worldgen.spawns.zombie.rules.water

import duskdn.plantz_ex.worldgen.spawns.init.SpawnContext
import duskdn.plantz_ex.worldgen.spawns.plant.rules.PlantNormalSpawnRules
import duskdn.plantz_ex.worldgen.spawns.SpawnRules

open class PlantWaterSpawnRules(): PlantNormalSpawnRules() {

    var waterSpawnChance: Float = 0.25f

    override fun getValidRule(context: SpawnContext): Boolean {
        context.setData("waterSpawnChance", waterSpawnChance)
        return super.getValidRule(context) && SpawnRules.SPECIAL_WATER_SPAWN.testRule(context)
    }

    override fun getPlantableRule(context: SpawnContext): Boolean {
        return super.getPlantableRule(context) || SpawnRules.IS_PLANTABLE_WATER.testRule(context)
    }

}