package duskdn.plantz_ex.entity.interfaces

import net.minecraft.world.entity.Entity

interface IEntity {

    val entity: Entity
        get() = this as Entity

}