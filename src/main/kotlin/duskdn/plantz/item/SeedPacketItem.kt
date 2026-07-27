package duskdn.plantz.item

import duskdn.plantz.entity.plant.init.CarrierPlant
import duskdn.plantz.entity.plant.init.PazPlant
import duskdn.plantz.entity.plant.utils.PlantSpawnUtils
import duskdn.plantz.init.PazComponents
import duskdn.plantz.init.PazConfig
import duskdn.plantz.init.PazEntities
import duskdn.plantz.init.PazItems
import duskdn.plantz.init.PazTags
import duskdn.plantz.util.getTotalSun
import duskdn.plantz.util.removeSunFromStorageAndInventory
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.TypedEntityData
import net.minecraft.world.item.component.UseCooldown
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.LiquidBlock
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
        if (target is PazPlant) {

            if (target is CarrierPlant) {
                val result = PlantSpawnUtils.tryPlant(level = player.level(), player = player, itemStack = itemStack, pos = target.blockPosition(), carrier = target )

                println("RESULT IS: $result")

                return result
            }

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
        if (level is ServerLevel) {
            val pos: BlockPos = hitResult.blockPos
            if (level.getBlockState(pos).block !is LiquidBlock) return InteractionResult.PASS
            else if (level.mayInteract(player, pos) && player.mayUseItemAt(pos, hitResult.direction, itemStack)) {
                val result = PlantSpawnUtils.tryPlant(level, player, itemStack, pos, UseOnContext(player, hand, hitResult).clickedFace, player.direction, checkWater = true)
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

            return PlantSpawnUtils.tryPlant(level, context.player, itemStack, spawnPos, clickedFace, context.horizontalDirection)
        }
    }

    // seed packet interaction with plants
    fun processSeedPacketInteraction(player: Player, plant: PazPlant, itemStack: ItemStack): PacketInteractionResult {
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
