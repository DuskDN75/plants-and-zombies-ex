package duskdn.plantz.entity.plant.all

import duskdn.plantz.entity.plant.interfaces.AbstractWallNut
import duskdn.plantz.init.PazEntities
import duskdn.plantz.init.PazTags.EntityTypes.WALLNUT_DEFLECTABLE
import duskdn.plantz.entity.plant.utils.PlantSpawnUtils
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.monster.zombie.Zombie
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class WallNut(type: EntityType<out AbstractWallNut>, level: Level) : AbstractWallNut(PazEntities.WALL_NUT, level) {
    override fun getZenGrownSeedType(): EntityType<*> = if (random.nextFloat() < 0.05f) PazEntities.EXPLODE_O_NUT else super.getZenGrownSeedType()
}