package joshxviii.plantz.entity.plant.interfaces

import joshxviii.plantz.entity.plant.init.PlantAbilities
import joshxviii.plantz.init.PazBlocks
import net.fabricmc.fabric.api.networking.v1.PlayerLookup.level
import net.minecraft.tags.FluidTags
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties

interface IAquaticPlant: IPlant {

    companion object : IPlantProps  {
        override val abilities: List<PlantAbilities> = listOf(
            PlantAbilities.FluidInteractions.SURVIVES_ON_WATER(
                survives = true,
                breaths = true,
                interaction = PlantAbilities.FluidInteractions.FluidInteractionState.FLOATS
            )
        )
    }

    fun waterSurvivalCheck(block: BlockState): Boolean {
        if (block.`is`(PazBlocks.ZEN_PLANT_POT)) {
            return true
        }

        if (block.`is`(PazBlocks.WATER_POT)) {
            return true
        }

        if (block.`is`(Blocks.WATER_CAULDRON)) {
            return block.getValue(BlockStateProperties.LEVEL_CAULDRON) > 0
        }

        val fluidState = level().getFluidState(blockPosition())

        return fluidState.`is`(FluidTags.WATER)
    }

    fun level()

    fun getFluidState(blockPosition: Any) {}

    fun blockPosition()

}