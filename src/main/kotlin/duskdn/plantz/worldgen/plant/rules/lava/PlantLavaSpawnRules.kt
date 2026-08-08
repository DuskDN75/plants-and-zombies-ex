package duskdn.plantz.worldgen.plant.rules.lava

import duskdn.plantz.worldgen.SpawnRules
import duskdn.plantz.worldgen.init.SpawnContext
import duskdn.plantz.worldgen.plant.rules.ground.PlantFireSpawnRules

open class PlantLavaSpawnRules(): PlantFireSpawnRules() {

    var lavaSpawnChance: Float = 0.25f

    override var strict = false

    override fun getValidRule(context: SpawnContext): Boolean {
        context.setData("lavaSpawnChance", lavaSpawnChance)
        return super.getValidRule(context) && SpawnRules.LAVA_SPAWN.testRule(context)
    }

    override fun getPlantableRule(context: SpawnContext): Boolean {
        return super.getPlantableRule(context) || SpawnRules.IS_PLANTABLE_LAVA.testRule(context)
    }

}