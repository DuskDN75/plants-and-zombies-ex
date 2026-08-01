package duskdn.plantz.entity.plant.init

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.monster.zombie.Zombie
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluids

/**
 * Base class for all plant entities that attack.
 * Provides basic behavior for all attacking plants.
 */
abstract class CarrierPlant(type: EntityType<out CarrierPlant>, level: Level) : PazPlant(type, level) {
    companion object {
        fun carrierCollision(carrier: PazPlant, other: Entity?): Boolean {
            return carrier.isAlive && other != carrier.attachedEntity
        }
    }

    open fun setRider(plant: PazPlant) {

        println("SETTING RIDER")

        val pos = this.position()

        plant.startRiding(this, true, true)
        plant.snapTo(pos)
        val yaw = this.yRot
        plant.yHeadRot = yaw
        plant.yBodyRot = yaw
        plant.yRot = yaw
    }

    override fun attackGoals() {}

    override fun canBeCollidedWith(other: Entity?): Boolean = carrierCollision(this, other)

    override fun actuallyHurt(level: ServerLevel, source: DamageSource, damage: Float) {
        val reducedDamage = if (source.entity is Zombie) damage*0.25f else damage
        super.actuallyHurt(level, source, reducedDamage)
    }

    override fun canSurviveOn(block: BlockState): Boolean {

        val isWater = block.`is`(Blocks.WATER) || block.fluidState.`is`(Fluids.WATER)

        val isLava = block.`is`(Blocks.LAVA) || block.fluidState.`is`(Fluids.LAVA)

        if (isWater || isLava) return false

        val solidFloor = !block.getCollisionShape(level(), blockPosition().below()).isEmpty

        return super.canSurviveOn(block) || solidFloor
    }
}