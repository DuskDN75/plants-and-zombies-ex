package duskdn.plantz.entity.plant.interfaces

import duskdn.plantz.entity.plant.init.InstantUsePlant
import duskdn.plantz.entity.plant.init.InstantUsePlant.Companion.ACTIVE_DIRECTION
import duskdn.plantz.entity.plant.init.PazPlant
import duskdn.plantz.entity.plant.init.PlantAbilities
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvent
import net.minecraft.util.Mth
import net.minecraft.util.Unit
import net.minecraft.world.level.Level

interface IInstantPlant: IPlant {

    var activeDirection: Int
        get() = entity.entityData.get(ACTIVE_DIRECTION)
        set(value) {
            entity.entityData.set(ACTIVE_DIRECTION, value)
        }

    open fun getMaxActiveTime() : Int = 30
    var oldActiveTime: Int
    var activeTime: Int
    fun getActiveTime(a: Float): Float = Mth.lerp(a, oldActiveTime.toFloat(), activeTime.toFloat()) / (getMaxActiveTime() - 2).toFloat()

    fun calculateActiveTime() {
        oldActiveTime = activeTime
        activeTime = (activeTime + activeDirection.coerceIn(-1,1)).coerceIn(0, getMaxActiveTime())
    }

    fun beginActivate(): Boolean

    fun activate(): Boolean

    fun getActiveSound(): SoundEvent

    open fun discardOnActivate(): Boolean = true

}