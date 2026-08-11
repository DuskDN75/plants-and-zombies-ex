package duskdn.plantz.entity.interfaces

import duskdn.plantz.entity.plant.init.PazPlant
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.ai.navigation.PathNavigation
import net.minecraft.world.phys.Vec3

interface IEntity {

    val entity: Entity
        get() = this as Entity

}