package duskdn.plantz_ex.worldgen.spawns.zombie.rules.lava

import duskdn.plantz_ex.worldgen.spawns.SpawnRules
import duskdn.plantz_ex.worldgen.spawns.init.SpawnContext
import duskdn.plantz_ex.worldgen.spawns.plant.rules.ground.PlantFireSpawnRules

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