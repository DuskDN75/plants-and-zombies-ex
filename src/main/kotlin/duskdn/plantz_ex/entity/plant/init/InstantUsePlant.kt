package duskdn.plantz_ex.entity.plant.init

import duskdn.plantz_ex.entity.plant.interfaces.IInstantPlant
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level

abstract class InstantUsePlant(type: EntityType<out InstantUsePlant>, level: Level) : PazPlant(type, level), IInstantPlant {
//    companion object {
//        val ACTIVE_DIRECTION: EntityDataAccessor<Int> = SynchedEntityData.defineId<Int>(InstantUsePlant::class.java,
//            PazDataSerializers.ACTIVE_DIRECTION
//        )
//    }
//
//    override fun defineSynchedData(entityData: SynchedEntityData.Builder) {
//        super.defineSynchedData(entityData)
//        entityData.define(ACTIVE_DIRECTION, 0)
//    }
}