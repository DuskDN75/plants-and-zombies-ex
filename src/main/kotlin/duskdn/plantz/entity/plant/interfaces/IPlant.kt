package duskdn.plantz.entity.plant.interfaces

import duskdn.plantz.entity.plant.init.PazPlant

interface IPlant {

    val entity: PazPlant
        get() = this as PazPlant

}