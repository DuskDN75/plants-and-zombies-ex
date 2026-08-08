package duskdn.plantz.entity.plant.interfaces

import duskdn.plantz.entity.plant.init.PlantAbilities
import net.minecraft.core.BlockPos
import net.minecraft.util.Unit
import net.minecraft.world.level.Level

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

    fun level(): Level

    fun getFluidState(blockPosition: Any): Unit

    fun blockPosition(): BlockPos

}