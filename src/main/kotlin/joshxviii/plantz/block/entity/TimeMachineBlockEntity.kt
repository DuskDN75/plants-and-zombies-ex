package joshxviii.plantz.block.entity

import joshxviii.plantz.PazBlocks
import joshxviii.plantz.PazComponents
import joshxviii.plantz.PazItems
import joshxviii.plantz.TimeMachineData
import joshxviii.plantz.block.MailboxState
import joshxviii.plantz.block.SunBatteryBlock
import joshxviii.plantz.block.TimeMachineBlock.Companion.STATE
import joshxviii.plantz.block.TimeMachineState
import joshxviii.plantz.inventory.TimeMachineMenu
import joshxviii.plantz.item.SunBatteryItem
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.world.ticks.ContainerSingleItem.BlockContainerSingleItem

class TimeMachineBlockEntity(
    worldPosition: BlockPos,
    blockState: BlockState
) : BlockEntity(PazBlocks.TIME_MACHINE_ENTITY, worldPosition, blockState), BlockContainerSingleItem, ExtendedMenuProvider<TimeMachineData> {
    var item: ItemStack = ItemStack.EMPTY
    var tickCount: Int = 0

    companion object {
        fun tick(level: Level, pos: BlockPos, state: BlockState, blockEntity: TimeMachineBlockEntity) {
            blockEntity.tickCount++

            if (level.isClientSide) return
            blockEntity.item.get(PazComponents.STORED_SUN)?.let {
                if (it.hasSun() && level.hasNeighborSignal(pos)) {
                    blockEntity.updateTimeMachineState(TimeMachineState.ACTIVE)
                    if (blockEntity.tickCount % 50 == 0) {
                        val newSun = it.removeSun()
                        blockEntity.item.set(PazComponents.STORED_SUN, newSun)
                    }
                }
                else blockEntity.updateTimeMachineState(TimeMachineState.BATTERY)
            } ?: blockEntity.updateTimeMachineState(TimeMachineState.INACTIVE)
        }
    }

    override fun saveAdditional(output: ValueOutput) {
        super.saveAdditional(output)
        if (!item.isEmpty) output.store("Item", ItemStack.CODEC, this.item)
    }

    override fun loadAdditional(input: ValueInput) {
        super.loadAdditional(input)
        this.item = input.read("Item", ItemStack.CODEC).orElse(ItemStack.EMPTY)?: ItemStack.EMPTY
    }

    override fun getContainerBlockEntity(): BlockEntity = this


    override fun getScreenOpeningData(player: ServerPlayer): TimeMachineData = TimeMachineData(blockPos)
    override fun getDisplayName(): Component  = Component.translatable("block.plantz.time_machine")
    override fun createMenu(containerId: Int, inventory: Inventory, player: Player): AbstractContainerMenu = TimeMachineMenu(containerId, inventory, blockPos, this)


    override fun getTheItem(): ItemStack = item
    override fun setTheItem(itemStack: ItemStack) {
        item = itemStack
    }

    fun updateTimeMachineState(newState: TimeMachineState) {
        level!!.setBlock(blockPos, blockState.setValue(STATE, newState), 3)
    }
}