package joshxviii.plantz.entity.plant.init

sealed class PlantAbilities {

    sealed class FluidInteractions {

        enum class FluidInteractionState {
            FLOATS,
            SINKS,
            STILL
        }

        sealed class FluidInteraction(
            val canSurvive: Boolean,
            val canBreathe: Boolean,
            val fluidInteraction: FluidInteractionState,
        ) : PlantAbilities()

        data class SURVIVES_ON_WATER(
            val survives: Boolean = false,
            val breaths: Boolean = false,
            val interaction: FluidInteractionState = FluidInteractionState.FLOATS
        ) : FluidInteraction(survives, breaths, interaction)

        data class SURVIVES_ON_LAVA(
            val survives: Boolean = false,
            val breaths: Boolean = false,
            val interaction: FluidInteractionState = FluidInteractionState.STILL
        ) : FluidInteraction(survives, breaths, interaction)

        data class SURVIVES_ON_AIR(
            val survives: Boolean = true,
            val breaths: Boolean = true,
            val interaction: FluidInteractionState = FluidInteractionState.SINKS
        ) : FluidInteraction(survives, breaths, interaction)
    }

}