package duskdn.plantz.worldgen

import duskdn.plantz.entity.plant.init.PazPlant
import duskdn.plantz.entity.plant.utils.PlantSpawnUtils.hasAdjacentPlant
import duskdn.plantz.init.PazTags
import duskdn.plantz.worldgen.init.SpawnRule
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.tags.BlockTags
import net.minecraft.tags.FluidTags
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.level.Level
import net.minecraft.world.level.LightLayer
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.AABB

object SpawnRules {

    val IS_VALID_SPAWN = SpawnRule { context ->

        val level = context.level
        val pos = context.pos
        val spawnReason = context.spawnReason

        val blockAtPos = level.getBlockState(pos)

        if (EntitySpawnReason.isSpawner(spawnReason)) return@SpawnRule true

        return@SpawnRule (level.getEntitiesOfClass(PazPlant::class.java, AABB(pos).inflate(38.0, 5.0, 38.0)) { it.tickCount > 0 }.isEmpty()
                && blockAtPos.getCollisionShape(level, pos.above()).isEmpty)
    }

    val IS_PLANTABLE_DEFAULT = SpawnRule { context ->
        val blockBelow = context.level.getBlockState(context.pos.below())

        return@SpawnRule blockBelow.`is`(PazTags.BlockTags.PLANTABLE)
    }

    val IS_PLANTABLE_SAND = SpawnRule { context ->
        val blockBelow = context.level.getBlockState(context.pos.below())

        return@SpawnRule blockBelow.`is`(BlockTags.SAND) || blockBelow.`is`(Blocks.SOUL_SAND) || blockBelow.`is`(Blocks.GRAVEL)
    }

    val IS_PLANTABLE_FIRE = SpawnRule { context ->
        val blockBelow = context.level.getBlockState(context.pos.below())

        return@SpawnRule blockBelow.`is`(Blocks.NETHERRACK) || blockBelow.`is`(Blocks.BASALT) || blockBelow.`is`(Blocks.GRAVEL)
    }

    val IS_PLANTABLE_FREE = SpawnRule { context ->
        val blockBelow = context.level.getBlockState(context.pos.below())

        return@SpawnRule !blockBelow.`is`(BlockTags.AIR)
    }

    val IS_PLANTABLE_SNOW = SpawnRule { context ->
        val blockBelow = context.level.getBlockState(context.pos.below())

        return@SpawnRule blockBelow.`is`(BlockTags.SNOW)
    }

    val IS_PLANTABLE_WATER = SpawnRule { context ->
        val isWater = context.level.getFluidState(context.pos).`is`(FluidTags.WATER)

        val waterAllowed = BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(context.type).`is`(PazTags.EntityTypes.PLANTABLE_ON_WATER)

        return@SpawnRule isWater && waterAllowed
    }

    val IS_PLANTABLE_LAVA = SpawnRule { context ->
        val isWater = context.level.getFluidState(context.pos).`is`(FluidTags.LAVA)

        val waterAllowed = BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(context.type).`is`(PazTags.EntityTypes.PLANTABLE_ON_LAVA)

        return@SpawnRule isWater && waterAllowed
    }

    val IS_DARK = SpawnRule { context ->
        return@SpawnRule context.level.getBrightness(LightLayer.BLOCK, context.pos) < 4
    }

    val IS_THUNDERING = SpawnRule { context ->
        return@SpawnRule context.level.level.isThundering
    }

    val SPECIAL_WATER_SPAWN = SpawnRule { context ->
        val level = context.level
        val pos = context.pos
        val spawnReason = context.spawnReason
        val random = context.randomSource

        val chance = context.getData<Float>("waterSpawnChance") ?: 0.25f

        val isRaining = level.level.isRaining
        val inWater = level.getFluidState(pos).`is`(FluidTags.WATER)
        val rainBonus = if (isRaining) 2.25f else 1f

        val biome = level.getBiome(pos)

        val swampBonus = if (biome.`is`(ConventionalBiomeTags.IS_SWAMP)) 2.25f else 1f

        return@SpawnRule inWater && random.nextFloat() < (chance * rainBonus * swampBonus)
    }

    val LAVA_SPAWN = SpawnRule { context ->
        val level = context.level
        val pos = context.pos
        val random = context.randomSource

        val chance = context.getData<Float>("lavaSpawnChance") ?: 0.25f

        val inLava = level.getFluidState(pos).`is`(FluidTags.LAVA)

        val biome = level.getBiome(pos)

        val hotBonus = if (biome.`is`(ConventionalBiomeTags.IS_HOT)) 2.25f else 1f

        return@SpawnRule inLava && random.nextFloat() < (chance * hotBonus)
    }

    val HAS_NO_ADJACENT = SpawnRule { context ->
        return@SpawnRule !hasAdjacentPlant(
            context.level as Level, context.pos)
    }

    val ABOVE_SEALEVEL = SpawnRule { context ->

        val offset = context.getData<Int>("seaLevelOffset") ?: 0

        return@SpawnRule context.pos.y > context.level.seaLevel + offset
    }

    val DEFAULT_PLANT_RULE = SpawnRule { context ->
        return@SpawnRule IS_VALID_SPAWN.testRule(context) && IS_PLANTABLE_DEFAULT.testRule(context) && HAS_NO_ADJACENT.testRule(context)
    }

}