package duskdn.plantz.entity.plant.interfaces

import duskdn.plantz.entity.plant.init.InstantUsePlant
import duskdn.plantz.entity.plant.init.PazPlant
import duskdn.plantz.entity.plant.init.PlantAbilities
import duskdn.plantz.entity.plant.interfaces.IExplosivePlant.Companion.SWELL
import duskdn.plantz.init.PazDataSerializers.DATA_ACTIVE
import duskdn.plantz.init.PazDataSerializers.DATA_SWELL
import duskdn.plantz.init.PazDataSerializers.DATA_SWELL_OLD
import net.minecraft.core.BlockPos
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.sounds.SoundEvent
import net.minecraft.util.Mth
import net.minecraft.util.Unit
import net.minecraft.world.level.Level

interface IInstantPlant: IPlant {

    companion object {
        val ACTIVE: EntityDataAccessor<Boolean> = SynchedEntityData.defineId<Boolean>(PazPlant::class.java, DATA_ACTIVE)
    }

    fun getMaxActiveTime() : Int = 30

    fun getActiveSound(): SoundEvent?

    fun discardOnActivate(): Boolean = true

    var active: Boolean
        get() = entity.entityData.get(ACTIVE)
        set(value) {
            entity.entityData.set(ACTIVE, value)
        }

}