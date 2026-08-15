package duskdn.plantz_ex.item

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.stats.Stats
import net.minecraft.world.Difficulty
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.SpawnEggItem
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.Spawner
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.HitResult

class PazSpawnEgg(
    properties: Properties,
    val worker: (LivingEntity) -> Unit = {}
): SpawnEggItem(properties) {

    fun spawnMob(
        user: LivingEntity?,
        itemStack: ItemStack,
        level: ServerLevel,
        spawnPos: BlockPos,
        tryMoveDown: Boolean,
        movedUp: Boolean
    ): InteractionResult {
        val type = getType(itemStack)
        if (type == null) {
            return InteractionResult.FAIL
        } else if (!type.isAllowedInPeaceful && level.difficulty == Difficulty.PEACEFUL) {
            return InteractionResult.FAIL
        } else {

            val entity = type.spawn(
                level,
                itemStack,
                user,
                spawnPos,
                EntitySpawnReason.SPAWN_ITEM_USE,
                tryMoveDown,
                movedUp
            )

            if (entity != null) {
                itemStack.consume(1, user)
                level.gameEvent(user, GameEvent.ENTITY_PLACE, spawnPos)
                worker(entity as LivingEntity)
            }

            return InteractionResult.SUCCESS
        }
    }

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResult {
        val itemStack = player.getItemInHand(hand)
        val hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY)
        if (hitResult.type != HitResult.Type.BLOCK) {
            return InteractionResult.PASS
        } else if (level is ServerLevel) {
            val pos = hitResult.blockPos
            if (level.getBlockState(pos).block !is LiquidBlock) {
                return InteractionResult.PASS
            } else if (level.mayInteract(player, pos) && player.mayUseItemAt(
                    pos,
                    hitResult.direction,
                    itemStack
                )
            ) {
                val result = spawnMob(player, itemStack, level, pos, false, false)
                if (result === InteractionResult.SUCCESS) {
                    player.awardStat(Stats.ITEM_USED.get(this))
                }

                return result
            } else {
                return InteractionResult.FAIL
            }
        } else {
            return InteractionResult.SUCCESS
        }
    }

    override fun useOn(context: UseOnContext): InteractionResult {
        val level = context.getLevel()
        if (level !is ServerLevel) {
            return InteractionResult.SUCCESS
        } else {
            val itemStack = context.getItemInHand()
            val pos = context.getClickedPos()
            val clickedFace = context.getClickedFace()
            val blockState = level.getBlockState(pos)
            if (level.getBlockEntity(pos) is Spawner) {

                val spawner: Spawner = level.getBlockEntity(pos) as Spawner

                val type = getType(itemStack)
                if (type == null) {
                    return InteractionResult.FAIL
                } else if (!level.isSpawnerBlockEnabled) {
                    if (context.player is ServerPlayer) {
                        context.player?.sendSystemMessage(Component.translatable("advMode.notEnabled.spawner"))
                    }

                    return InteractionResult.FAIL
                } else {
                    spawner.setEntityId(type, level.getRandom())
                    level.sendBlockUpdated(pos, blockState, blockState, 3)
                    level.gameEvent(context.player, GameEvent.BLOCK_CHANGE, pos)
                    itemStack.shrink(1)
                    return InteractionResult.SUCCESS
                }
            } else {
                val spawnPos: BlockPos?
                if (blockState.getCollisionShape(level, pos).isEmpty()) {
                    spawnPos = pos
                } else {
                    spawnPos = pos.relative(clickedFace)
                }

                return spawnMob(
                    context.player,
                    itemStack,
                    level,
                    spawnPos,
                    true,
                    pos != spawnPos && clickedFace == Direction.UP
                )
            }
        }
    }

}