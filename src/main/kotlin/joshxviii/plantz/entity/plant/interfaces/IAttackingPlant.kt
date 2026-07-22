package joshxviii.plantz.entity.plant.interfaces

import joshxviii.plantz.entity.plant.init.PlantAbilities
import joshxviii.plantz.init.PazBlocks
import net.fabricmc.fabric.api.networking.v1.PlayerLookup.level
import net.minecraft.core.BlockPos
import net.minecraft.tags.FluidTags
import net.minecraft.util.Unit
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties

interface IAttackingPlant: IPlant {

    companion object : IPlantProps  {
        override val abilities: List<PlantAbilities> = listOf(
            PlantAbilities.FluidInteractions.SURVIVES_ON_WATER(
                survives = true,
                breaths = true,
                interaction = PlantAbilities.FluidInteractions.FluidInteractionState.FLOATS
            )
        )
    }

    fun level(): Level

    fun getFluidState(blockPosition: Any): Unit

    fun blockPosition(): BlockPos

}