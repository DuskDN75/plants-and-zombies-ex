package duskdn.plantz.worldgen

import duskdn.plantz.worldgen.plant.rules.ground.PlantFireSpawnRules
import duskdn.plantz.worldgen.plant.rules.lava.PlantLavaSpawnRules
import duskdn.plantz.worldgen.plant.rules.ground.PlantLightningSpawnRules
import duskdn.plantz.worldgen.plant.rules.PlantNormalSpawnRules
import duskdn.plantz.worldgen.plant.rules.ground.PlantSandSpawnRules
import duskdn.plantz.worldgen.plant.rules.ground.PlantSnowSpawnRules
import duskdn.plantz.worldgen.plant.rules.water.PlantWaterSpawnRules
import duskdn.plantz.worldgen.plant.init.PlantSpawnRules

object SpawnRuleSets {

    val PLANT_DEFAULT_SPAWN_RULES = PlantSpawnRules()

    val PLANT_NORMAL_SPAWN_RULES = PlantNormalSpawnRules()

    val PLANT_FREE_SPAWN_RULES = PlantSpawnRules()

    val PLANT_MUSHROOM_SPAWN_RULES = PlantNormalSpawnRules().apply {
        isMushroom = true
    }

    val PLANT_SAND_SPAWN_RULES = PlantSandSpawnRules()

    val PLANT_SNOW_SPAWN_RULES = PlantSnowSpawnRules()

    val PLANT_WATER_SPAWN_RULES = PlantWaterSpawnRules().apply {
        waterSpawnChance = 0.5f
    }

    val PLANT_WATER_MUSHROOM_SPAWN_RULES = PlantWaterSpawnRules().apply {
        waterSpawnChance = 0.25f
        isMushroom = true
    }

    val PLANT_WATER_RARE_SPAWN_RULES = PlantWaterSpawnRules().apply {
        waterSpawnChance = 0.1f
    }

    val PLANT_FIRE_SPAWN_RULES = PlantFireSpawnRules()

    val PLANT_LAVA_SPAWN_RULES = PlantLavaSpawnRules()

    val PLANT_LIGHTNING_SPAWN_RULES = PlantLightningSpawnRules()

}