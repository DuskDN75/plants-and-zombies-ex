package joshxviii.plantz.item

import joshxviii.plantz.*
import joshxviii.plantz.entity.plant.init.Plant
import joshxviii.plantz.init.PazComponents
import joshxviii.plantz.init.PazConfig
import joshxviii.plantz.init.PazEntities
import joshxviii.plantz.init.PazItems
import joshxviii.plantz.init.PazTags
import joshxviii.plantz.util.getTotalSun
import joshxviii.plantz.util.removeSunFromStorageAndInventory
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.TamableAnimal
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.TypedEntityData
import net.minecraft.world.item.component.UseCooldown
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.HitResult
import java.util.*

class SeedPacketItem(properties: Properties) : Item(properties) {

    override fun getName(itemStack: ItemStack): Component {
        val component = itemStack.get(DataComponents.ENTITY_DATA) ?: return super.getName(itemStack)
        val entityId = BuiltInRegistries.ENTITY_TYPE.getKey(component.type())

        val entityName = Component.translatable("entity.${entityId.namespace}.${entityId.path}")
        return Component.translatable("item.plantz.seed_packet.entity", entityName)
    }

    override fun interactLivingEntity(
        itemStack: ItemStack,
        player: Player,
        target: LivingEntity,
        type: InteractionHand
    ): InteractionResult {
        if (player.cooldowns.isOnCooldown(itemStack)) return InteractionResult.PASS
        if (target is Plant) {
            val result = processSeedPacketInteraction(player, target, itemStack)
            if (result == PacketInteractionResult.SUCCESS) {
                itemStack.consume(1, player)
                applyCooldown(itemStack, player)
                return InteractionResult.SUCCESS_SERVER
            }
            if (result == PacketInteractionResult.FAIL) return InteractionResult.CONSUME
        }
        return super.interactLivingEntity(itemStack, player, target, type)
    }

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
        val itemStack = player.getItemInHand(hand)

        val component = itemStack.get(DataComponents.ENTITY_DATA)
        val entityType = component?.type()
        val waterPlaceable = entityType!=null && BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(entityType).`is`(PazTags.EntityTypes.PLANTABLE_ON_WATER)

        if (!waterPlaceable) return InteractionResult.PASS
        val hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY)
        if (hitResult.type != HitResult.Type.BLOCK) return InteractionResult.PASS
        else if (level is ServerLevel) {
            val pos: BlockPos = hitResult.blockPos
            if (level.getBlockState(pos).block !is LiquidBlock) return InteractionResult.PASS
            else if (level.mayInteract(player, pos) && player.mayUseItemAt(pos, hitResult.direction, itemStack)) {
                val result = tryPlant(level, player, itemStack, pos, UseOnContext(player, hand, hitResult).clickedFace, player.direction, checkWater = true)
                if (result === InteractionResult.SUCCESS) player.awardStat(Stats.ITEM_USED.get(this))

                return result
            } else return InteractionResult.FAIL
        }
        return InteractionResult.SUCCESS
    }

    override fun useOn(context: UseOnContext): InteractionResult {
        val level: Level = context.level
        if (level !is ServerLevel) return InteractionResult.SUCCESS
        else {
            val itemStack = context.itemInHand
            val pos: BlockPos = context.clickedPos
            val clickedFace: Direction = context.clickedFace
            val blockState = level.getBlockState(pos)
            val spawnPos = if (blockState.getCollisionShape(level, pos).isEmpty) pos else pos.relative(clickedFace)

            return tryPlant(level, context.player, itemStack, spawnPos, clickedFace, context.horizontalDirection)
        }
    }

    fun tryPlant(
        level: Level,
        player: Player?,
        itemStack: ItemStack,
        pos: BlockPos,
        face: Direction,
        horizontalDir: Direction,
        checkWater: Boolean = false
    ): InteractionResult {
        if (level !is ServerLevel || player == null) return InteractionResult.PASS

        val component = itemStack.get(DataComponents.ENTITY_DATA)
        val entityType = component?.type()

        val spawnPos = if (level.getBlockState(pos).getCollisionShape(level, pos).isEmpty) pos
        else pos.relative(face)

        val availableSun = player.getTotalSun()
        val sunCost = itemStack.get(PazComponents.SUN_COST)?.getSunCost(entityType) ?: 0
        if (sunCost > availableSun && !player.hasInfiniteMaterials()) {
            player.sendOverlayMessage(
                Component.translatable("message.plantz.not_enough_sun", availableSun, sunCost).withStyle(ChatFormatting.RED)
            )
            return InteractionResult.FAIL
        }

        val entity = entityType?.create(
            level,
            EntityType.createDefaultStackConfig(level, itemStack, player),
            spawnPos,
            EntitySpawnReason.SPAWN_ITEM_USE,
            !checkWater,
            face == Direction.UP
        )?: return InteractionResult.FAIL

        if (entity is Plant) {
            val spawnBlockCollisionShape = level.getBlockState(spawnPos).getCollisionShape(level, spawnPos).let { if (it.isEmpty.not()) it.bounds() else null }
            val entityBox = entity.boundingBox.move(spawnPos.multiply(-1))

            val invalidSpace = !(entity.canSurviveOn(level.getBlockState(spawnPos.below())) || checkWater)
                    || !(spawnBlockCollisionShape==null || !entityBox.intersects(spawnBlockCollisionShape))

            val occupiedSpace = Plant.hasAdjacentPlant(level, spawnPos)

            if (invalidSpace || occupiedSpace) {
                player.sendOverlayMessage(
                    Component.translatable("message.plantz.cannot_survive").withStyle(ChatFormatting.RED)
                )
                return InteractionResult.FAIL
            }
            val yaw = horizontalDir.opposite.toYRot()
            entity.yHeadRot = yaw
            entity.yBodyRot = yaw
            entity.yRot = yaw
        }

        entity.let {
            val existingPlants = level.getEntitiesOfClass(Plant::class.java, AABB(it.blockPosition()))
            if (existingPlants.isNotEmpty()) {
                player.sendOverlayMessage(
                    Component.translatable("message.plantz.already_planted").withStyle(ChatFormatting.RED)
                )
                return InteractionResult.FAIL
            }
        }

        if (!level.addFreshEntity(entity)) {
            entity.discard()
            return InteractionResult.FAIL
        }

        itemStack.consume(1, player)
        if (!player.hasInfiniteMaterials()) {
            player.removeSunFromStorageAndInventory(sunCost)
        }
        entity.playSound(SoundEvents.BIG_DRIPLEAF_PLACE)
        if (entity is TamableAnimal) entity.tame(player)
        level.gameEvent(player, GameEvent.ENTITY_PLACE, spawnPos)

        return InteractionResult.SUCCESS
    }

    // seed packet interaction with plants
    fun processSeedPacketInteraction(player: Player, plant: Plant, itemStack: ItemStack): PacketInteractionResult {
        val type = itemStack.get(DataComponents.ENTITY_DATA)?.type()
        val availableSun = player.getTotalSun()
        val sunCost = itemStack.get(PazComponents.SUN_COST)?.getSunCost(type)?: 0
        val cantAfford = sunCost > availableSun && !player.hasInfiniteMaterials()

        val result = when (type) {
            PazEntities.COFFEE_BEAN -> {
                when {
                    plant.isGrowingSeeds -> {
                        player.sendOverlayMessage(Component.translatable("message.plantz.growing").withStyle(ChatFormatting.RED))
                        PacketInteractionResult.FAIL
                    }
                    cantAfford -> PacketInteractionResult.CANT_AFFORD
                    plant.coffeeBuff>0 -> PacketInteractionResult.FAIL
                    else -> {
                        plant.applyCoffeeBuff()
                        PacketInteractionResult.SUCCESS
                    }
                }
            }
            else -> PacketInteractionResult.NO_INTERACTION
        }
        // show message
        if (result == PacketInteractionResult.CANT_AFFORD) player.sendOverlayMessage(Component.translatable("message.plantz.not_enough_sun", availableSun, sunCost).withStyle(ChatFormatting.RED))
        // remove used sun
        if (result == PacketInteractionResult.SUCCESS && !player.hasInfiniteMaterials()) {
            player.removeSunFromStorageAndInventory(sunCost)
            applyCooldown(itemStack, player)
        }
        return result
    }
    enum class PacketInteractionResult {
        SUCCESS,
        FAIL,
        CANT_AFFORD,
        NO_INTERACTION
    }

    fun applyCooldown(itemStack: ItemStack, player: Player) {
        val entityType = itemStack.get(DataComponents.ENTITY_DATA)?.type()?: return
        val group = BuiltInRegistries.ENTITY_TYPE.getKey(entityType)
        if (PazConfig.PLANT_COOLDOWN_ENABLED) {
            val cooldownTime = PazConfig.getCooldownTime(PazConfig.getSunCost(entityType))
            itemStack.set(DataComponents.USE_COOLDOWN, UseCooldown(cooldownTime, Optional.of(group)))
            player.cooldowns.addCooldown(group, (cooldownTime*20).toInt())
        } else {
            player.cooldowns.removeCooldown(group)
            itemStack.set(DataComponents.USE_COOLDOWN, UseCooldown(0f))
        }
    }

    companion object {
        fun stackFor(type: EntityType<*>): ItemStack {
            val stack = ItemStack(PazItems.SEED_PACKET)
            stack.set(DataComponents.ENTITY_DATA, TypedEntityData.of(type, CompoundTag()))

            return stack
        }

        fun typeFromStack(itemStack: ItemStack): EntityType<*>? {
            val type = itemStack.get(DataComponents.ENTITY_DATA)?.type()?: return null
            return type
        }
    }
}
