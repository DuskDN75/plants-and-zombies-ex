package duskdn.plantz.entity.plant.traits

data class PlantHabitatTraits(
    val traits: List<HabitatTrait>
)

data class PlantProjectileTraits(
    val traits: List<ProjectileTrait>
)

data class PlantDamageTypes(
    val traits: List<DamageTrait>
)

sealed class HabitatTrait(val isActive: Boolean) {
    class Terrestrial(isActive: Boolean) : HabitatTrait(isActive)
    class Aquatic(isActive: Boolean) : HabitatTrait(isActive)
    class Aerial(isActive: Boolean) : HabitatTrait(isActive)
    class Subterranean(isActive: Boolean) : HabitatTrait(isActive)
    class Infernal(isActive: Boolean) : HabitatTrait(isActive)
}

sealed class ProjectileTrait(val isActive: Boolean) {
    class Flammable(isActive: Boolean) : HabitatTrait(isActive)
    class Permeable(isActive: Boolean) : HabitatTrait(isActive)
}

sealed class DamageTrait(val isActive: Boolean) {
    class Fire(isActive: Boolean) : HabitatTrait(isActive)
    class Ice(isActive: Boolean) : HabitatTrait(isActive)
    class Water(isActive: Boolean) : HabitatTrait(isActive)
    class Electric(isActive: Boolean) : HabitatTrait(isActive)
    class Poison(isActive: Boolean) : HabitatTrait(isActive)
    class Toxic(isActive: Boolean) : HabitatTrait(isActive)
    class Earth(isActive: Boolean) : HabitatTrait(isActive)
    class Butter(isActive: Boolean) : HabitatTrait(isActive)
    class Sharp(isActive: Boolean) : HabitatTrait(isActive)
}