package duskdn.plantz.entity.plant.interfaces

import duskdn.plantz.entity.plant.init.PazPlant
import duskdn.plantz.entity.plant.utils.PlantSpawnUtils
import duskdn.plantz.init.PazTags
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.monster.zombie.Zombie
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

abstract class AbstractWallNut(type: EntityType<out AbstractWallNut>, level: Level) : PazPlant(type, level) {

    override fun attackGoals() {}

    override fun allowPlayerCollision(): Boolean {
        return true
    }

    override fun allowEntityCollision(): Boolean {
        return true
    }

    override fun hurtServer(level: ServerLevel, source: DamageSource, damage: Float): Boolean {
        source.directEntity?.let {
            if (it.`is`(PazTags.EntityTypes.WALLNUT_DEFLECTABLE)) return false
        }
        return super.hurtServer(level, source, damage)
    }

    override fun actuallyHurt(level: ServerLevel, source: DamageSource, damage: Float) {
        val reducedDamage = if (source.entity is Zombie) damage*0.25f else damage
        super.actuallyHurt(level, source, reducedDamage)
    }

    override fun canSurviveOn(block: BlockState): Boolean {
        return super.canSurviveOn(block) || PlantSpawnUtils.solidFloorCheck(level(), blockPosition().below(), block)
    }

}