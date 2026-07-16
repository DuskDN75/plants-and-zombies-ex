package joshxviii.plantz.entity.plant.init

import joshxviii.plantz.entity.Sun
import joshxviii.plantz.init.PazBlocks
import joshxviii.plantz.init.PazCriteria
import joshxviii.plantz.init.PazEntities
import joshxviii.plantz.init.PazItems
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

    override fun interact(player: Player, hand: InteractionHand, location: Vec3): InteractionResult {
        val itemStack = player.getItemInHand(hand)
        val serverLevel = this.level()

        if (passengers.isNotEmpty()) return InteractionResult.PASS

        if (itemStack.`is`(PazItems.SEED_PACKET)) {
            val plantType = SeedPacketItem.typeFromStack(itemStack)

            val entity = if (serverLevel is ServerLevel) plantType?.create(serverLevel, null, BlockPos.containing(this.position()), EntitySpawnReason.SPAWN_ITEM_USE, true, false) else null

            // snap rotation
            if (entity is Plant) {
                entity.startRiding(this, true, true)
                entity.snapTo(this.position())
                val yaw = this.yRot
                entity.yHeadRot = yaw
                entity.yBodyRot = yaw
                entity.yRot = yaw
            }

            if (entity != null && !serverLevel.addFreshEntity(entity)) {
                entity.discard()
                return InteractionResult.FAIL
            }

            itemStack.consume(1, player)
            entity?.playSound(SoundEvents.BIG_DRIPLEAF_PLACE, 1.0f, 1.0f)
            if (entity is TamableAnimal) entity.tame(player)
            serverLevel.gameEvent(player, GameEvent.ENTITY_PLACE, this.position())
            if (player is ServerPlayer) PazCriteria.PLANT_POT_MINECRAFT.trigger(player, entity !=null && hasPassenger(entity))
        }
        return InteractionResult.SUCCESS_SERVER
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