package duskdn.plantz.entity.plant

import duskdn.plantz.init.PazEntities
import duskdn.plantz.init.PazTags.EntityTypes.WALLNUT_DEFLECTABLE
import duskdn.plantz.entity.Sun
import duskdn.plantz.entity.plant.init.PazPlant
import duskdn.plantz.entity.plant.utils.PlantSpawnUtils
import duskdn.plantz.entity.plant.utils.PlantUtils
import duskdn.plantz.init.PazBlocks
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.monster.zombie.Zombie
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluids

class WallNut(type: EntityType<out PazPlant>, level: Level) : PazPlant(PazEntities.WALL_NUT, level) {

    override fun attackGoals() {}

    override fun getZenGrownSeedType(): EntityType<*> = if (random.nextFloat() < 0.05f) PazEntities.EXPLODE_O_NUT else super.getZenGrownSeedType()

    override fun allowPlayerCollision(): Boolean {
        return true
    }

    override fun hurtServer(level: ServerLevel, source: DamageSource, damage: Float): Boolean {
        source.directEntity?.let {
            if (it.`is`(WALLNUT_DEFLECTABLE)) return false
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