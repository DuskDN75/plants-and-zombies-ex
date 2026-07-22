package joshxviii.plantz.entity.plant.init

import joshxviii.plantz.entity.Sun
import joshxviii.plantz.entity.plant.utils.onValidGround
import joshxviii.plantz.init.PazBlocks
import joshxviii.plantz.init.PazCriteria
import joshxviii.plantz.init.PazEntities
import joshxviii.plantz.init.PazItems
import joshxviii.plantz.init.PazTags
import joshxviii.plantz.init.PazTags.EntityTypes.WALLNUT_DEFLECTABLE
import joshxviii.plantz.item.SeedPacketItem
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.TamableAnimal
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.monster.zombie.Zombie
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.phys.Vec3
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Base class for all plant entities that attack.
 * Provides basic behavior for all attacking plants.
 */
abstract class CarrierPlant(type: EntityType<out CarrierPlant>, level: Level) : Plant(type, level) {
    companion object {
        fun carrierCollision(carrier: Plant, other: Entity?): Boolean {
            return carrier.isAlive && other != carrier.attachedEntity
        }
    }

    fun setRider(plant: Plant) {

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