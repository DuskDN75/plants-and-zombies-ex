package duskdn.plantz.entity.turret

import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level

abstract class Turret(type: EntityType<out LivingEntity>, level: Level) : LivingEntity(type, level) {

}