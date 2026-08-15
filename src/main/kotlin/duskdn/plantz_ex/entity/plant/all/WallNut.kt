package duskdn.plantz_ex.entity.plant.all

import duskdn.plantz_ex.entity.plant.interfaces.AbstractWallNut
import duskdn.plantz_ex.init.PazEntities
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level

class WallNut(type: EntityType<out AbstractWallNut>, level: Level) : AbstractWallNut(PazEntities.WALL_NUT, level) {
    override fun getZenGrownSeedType(): EntityType<*> = if (random.nextFloat() < 0.05f) PazEntities.EXPLODE_O_NUT else super.getZenGrownSeedType()
}