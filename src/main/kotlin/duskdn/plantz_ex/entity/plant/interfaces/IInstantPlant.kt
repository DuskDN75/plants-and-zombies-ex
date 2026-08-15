package duskdn.plantz_ex.entity.plant.interfaces

import duskdn.plantz_ex.entity.plant.init.PazPlant
import duskdn.plantz_ex.entity.plant.init.PazPlant.Companion.ACTIVE
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.sounds.SoundEvent

interface IInstantPlant: IPlant {

    fun getMaxActiveTime() : Int = 30

    fun getActiveSound(): SoundEvent?

    fun discardOnActivate(): Boolean = true

    var active: Boolean
        get() = entity.entityData.get(ACTIVE)
        set(value) {
            entity.entityData.set(ACTIVE, value)
        }

}