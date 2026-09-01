package duskdn.plantz_ex.item

import duskdn.plantz_ex.entity.plant.init.CarrierPlant
import duskdn.plantz_ex.entity.plant.init.PazPlant
import duskdn.plantz_ex.entity.plant.utils.PlantSpawnUtils
import duskdn.plantz_ex.init.PazComponents
import duskdn.plantz_ex.init.PazConfig
import duskdn.plantz_ex.init.PazEntities
import duskdn.plantz_ex.init.PazItems
import duskdn.plantz_ex.init.PazTags
import duskdn.plantz_ex.util.Utils
import duskdn.plantz_ex.util.debugPrint
import duskdn.plantz_ex.util.getTotalSun
import duskdn.plantz_ex.util.removeSunFromStorageAndInventory
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.stats.Stats
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.CustomData
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.phys.HitResult
import java.util.*

class SeedPacketItem(properties: Properties) : Item(properties) {

    fun plant(
        level: Level,
        player: Player?,
        itemStack: ItemStack,
        pos: BlockPos,
        face: Direction? = null,
        horizontalDir: Direction? = null,
        checkFluid: Boolean = false,
        carrier: PazPlant? = null
    ) {

    }

    override fun getName(itemStack: ItemStack): Component {
        val component = itemStack.get(DataComponents.ENTITY_DATA) ?: return super.getName(itemStack)

        val tag = component.copyTag()

        val id = tag.getString("id")

        if (id.isEmpty()) return super.getName(itemStack)

        val entityId = BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(id))

        val entityName = Component.translatable(entityId.descriptionId)
        return Component.translatable("item.plantz_ex.seed_packet.entity", entityName)
    }

    override fun interactLivingEntity(
        itemStack: ItemStack,
        player: Player,
        target: LivingEntity,
        type: InteractionHand
    ): InteractionResult {

        if (player.cooldowns.isOnCooldown(itemStack.item)) return InteractionResult.PASS
        if (target is PazPlant) {

            if (target is CarrierPlant) {
                val result = PlantSpawnUtils.tryPlant(level = player.level(), player = player, itemStack = itemStack, pos = target.blockPosition(), carrier = target )

                debugPrint("RESULT IS: $result")

                return result
            }

            val result = processSeedPacketInteraction(player, target, itemStack)
            if (result == PacketInteractionResult.SUCCESS) {
                itemStack.consume(1, player)
                applyCooldown(itemStack, player)
                return InteractionResult.SUCCESS
            }
            if (result == PacketInteractionResult.FAIL) return InteractionResult.CONSUME
        }
        return super.interactLivingEntity(itemStack, player, target, type)
    }

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack?>? {
        val itemStack = player.getItemInHand(hand)

        val entityType = typeFromStack(itemStack)
        val waterPlaceable = entityType!=null && entityType.`is`(PazTags.EntityTypes.PLANTABLE_ON_WATER)
        val lavaPlaceable = entityType!=null && entityType.`is`(PazTags.EntityTypes.PLANTABLE_ON_LAVA)
        val airPlaceable = entityType!=null && entityType.`is`(PazTags.EntityTypes.PLANTABLE_ON_AIR)

        if (!waterPlaceable && !lavaPlaceable && !airPlaceable) return InteractionResultHolder(InteractionResult.PASS, player.getItemInHand(hand))

        val hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.ANY)

        if (level is ServerLevel) {

            var direction: Direction

            val pos: BlockPos

            if (hitResult.type == HitResult.Type.MISS && airPlaceable) {
                pos = BlockPos.containing(hitResult.location)
                direction = Direction.UP
            } else {
                pos = hitResult.blockPos
                direction = hitResult.direction
            }

            val block = level.getBlockState(pos).block

            debugPrint("BLOCK IS: $block")
            if (block !is LiquidBlock && !airPlaceable) return InteractionResultHolder(InteractionResult.PASS, player.getItemInHand(hand))
            else if (level.mayInteract(player, pos) && player.mayUseItemAt(pos, direction, itemStack)) {
                val result = PlantSpawnUtils.tryPlant(level, player, itemStack, pos, UseOnContext(player, hand, hitResult).clickedFace, player.direction, checkFluid = true)
                if (result === InteractionResult.SUCCESS) {
                    player.awardStat(Stats.ITEM_USED.get(this))
                }

                return InteractionResultHolder(result, player.getItemInHand(hand))
            } else return InteractionResultHolder(InteractionResult.FAIL, player.getItemInHand(hand))
        }
        return InteractionResultHolder(InteractionResult.SUCCESS, player.getItemInHand(hand))
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

            val didPlant = PlantSpawnUtils.tryPlant(level, context.player, itemStack, spawnPos, clickedFace, context.horizontalDirection)

//            if (didPlant == InteractionResult.SUCCESS && context.player != null) {
////                applyCooldown(itemStack, context.player as Player)
//            }

            return didPlant
        }
    }

    @JvmOverloads
    fun checkCanAfford(player: Player, itemStack: ItemStack, type: EntityType<*>? = typeFromStack(itemStack)): Triple<Boolean,Int,Int> {
        val availableSun = player.getTotalSun()
        val sunCost = itemStack.get(PazComponents.SUN_COST)?.getSunCost(type)?: 0
        return Triple(sunCost <= availableSun || player.hasInfiniteMaterials(), availableSun, sunCost)
    }

    // seed packet interaction with plants
    fun processSeedPacketInteraction(player: Player, plant: PazPlant, itemStack: ItemStack): PacketInteractionResult {
        val type = typeFromStack(itemStack)
        val (canAfford, availableSun, sunCost) = checkCanAfford(player, itemStack, type)

        val result = when (type) {
            PazEntities.COFFEE_BEAN -> {
                when {
                    plant.isGrowingSeeds -> {
                        player.displayClientMessage(Component.translatable("message.plantz_ex.growing", plant.name.copy().withStyle(
                            ChatFormatting.RED)).withStyle(ChatFormatting.DARK_RED), true)
                        PacketInteractionResult.FAIL
                    }
                    !canAfford -> PacketInteractionResult.CANT_AFFORD
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
        if (result == PacketInteractionResult.CANT_AFFORD) player.displayClientMessage(Component.translatable("message.plantz_ex.not_enough_sun", availableSun, sunCost).withStyle(ChatFormatting.RED), true)
        // remove used sun
        if (result == PacketInteractionResult.SUCCESS && !player.hasInfiniteMaterials()) {
            player.removeSunFromStorageAndInventory(sunCost)
//            applyCooldown(itemStack, player)
        }

        return result
    }
    enum class PacketInteractionResult {
        SUCCESS,
        FAIL,
        CANT_AFFORD,
        NO_INTERACTION
    }

//    fun setCooldownGroup(itemStack: ItemStack, player: Player) {
//        val entityType: EntityType<*> = Utils.getEntityType(itemStack) ?: return
//        val group = BuiltInRegistries.ENTITY_TYPE.getKey(entityType)
//        val cooldownTime = PazConfig.getCooldownTime(entityType).toFloat()
//        itemStack.set(DataComponents.USE_COOLDOWN, UseCooldown(cooldownTime, Optional.of(group)))
//    }

    fun applyCooldown(itemStack: ItemStack, player: Player) {
        val entityType = typeFromStack(itemStack) ?: return
        val group = BuiltInRegistries.ENTITY_TYPE.getKey(entityType)
        if (PazConfig.PLANT_COOLDOWN_ENABLED && !player.isCreative) {
            val cooldownTime = PazConfig.getCooldownTime(entityType).toFloat()
//            itemStack.set(DataComponents.USE_COOLDOWN, UseCooldown(cooldownTime, Optional.of(group)))
            player.cooldowns.addCooldown(itemStack.item, (cooldownTime*20).toInt())
        } else {
            player.cooldowns.removeCooldown(itemStack.item)
//            itemStack.set(DataComponents.USE_COOLDOWN, UseCooldown(0f))
        }
    }

    companion object {
        fun stackFor(type: EntityType<*>): ItemStack {
            val stack = ItemStack(PazItems.SEED_PACKET)

            val tag = CompoundTag()

            tag.putString(
                "id",
                BuiltInRegistries.ENTITY_TYPE.getKey(type).toString()
            )

            stack.set(DataComponents.ENTITY_DATA, CustomData.of(tag))

            return stack
        }

        fun typeFromStack(itemStack: ItemStack): EntityType<*>? {

            val component = itemStack.get(DataComponents.ENTITY_DATA)
            val entityType = component?.copyTag()?.getString("id")
                ?.let { BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(it)) }

            return entityType
        }
    }
}
