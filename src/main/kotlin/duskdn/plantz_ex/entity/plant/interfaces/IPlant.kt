package duskdn.plantz_ex.entity.plant.interfaces

import duskdn.plantz_ex.entity.interfaces.IEntity
import duskdn.plantz_ex.entity.plant.init.PazPlant

interface IPlant: IEntity {

    override val entity: PazPlant
        get() = this as PazPlant

}