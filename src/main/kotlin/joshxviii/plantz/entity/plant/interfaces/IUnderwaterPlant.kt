package joshxviii.plantz.entity.plant.interfaces

import joshxviii.plantz.entity.plant.init.PlantAbilities

interface IUnderwaterPlant: IAquaticPlant {

    companion object : IPlantProps  {
        override val abilities: List<PlantAbilities> = listOf(
            PlantAbilities.FluidInteractions.SURVIVES_ON_WATER(
                survives = true,
                breaths = true,
                interaction = PlantAbilities.FluidInteractions.FluidInteractionState.STILL
            )
        )
    }

}