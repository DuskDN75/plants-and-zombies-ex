package joshxviii.plantz.inventory

import joshxviii.plantz.init.MailboxData
import joshxviii.plantz.init.PazMenus
import joshxviii.plantz.init.PazTags
import joshxviii.plantz.block.entity.MailboxManager
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import java.util.*

class MailboxMenu(
    containerId: Int,
    val inventory: Inventory,
    val data: MailboxData,
    private val access: ContainerLevelAccess = ContainerLevelAccess.NULL
) : AbstractContainerMenu(PazMenus.MAILBOX_MENU, containerId) {

    var slotUpdateListener = Runnable {}
    var mailboxListUpdateListener = Runnable {}
    var selectedMailboxIndex: Int? = null
    val mailSlot: Slot
    var responseMessage: Component = Component.empty()
    var responseTimeout: Int = 0
    var availableMailboxes: List<MailboxData> = emptyList()
        set(value) {
            field = value
            updateFilteredMailboxes()
            mailboxListUpdateListener.run()
        }
    var filteredMailboxes: List<MailboxData> = emptyList()
    var searchFilter: String = ""
        set(value) {
            field = value
            updateFilteredMailboxes()
            slotUpdateListener.run()
        }

    private val inputContainer: Container = object : SimpleContainer(1) {
        init { Objects.requireNonNull<MailboxMenu>(this@MailboxMenu) }
        override fun setChanged() {
            super.setChanged()
            this@MailboxMenu.slotsChanged(this)
            this@MailboxMenu.slotUpdateListener.run()
        }
    }

    init {
        mailSlot = addSlot(object : Slot(inputContainer, 0, 20, 32) {
            init { Objects.requireNonNull(this@MailboxMenu) }
            override fun mayPlace(itemStack: ItemStack): Boolean = true
        })
        addStandardInventorySlots(inventory, 8, 98)
    }

    fun getMailbox(index: Int?): MailboxData? {
        if (index == null ) return null
        if (index < 0 || index >= filteredMailboxes.size) return null
        return filteredMailboxes[index]
    }

    override fun stillValid(player: Player): Boolean {
        return access.evaluate( { level: Level, pos: BlockPos ->
            if (!isValidBlock(level.getBlockState(pos))) false
            else player.isWithinBlockInteractionRange(pos, 4.0)
        }, true)
    }
    fun isValidBlock(state: BlockState): Boolean = state.`is`(PazTags.BlockTags.MAILBOX)

    fun updateFilteredMailboxes() {
        filteredMailboxes = MailboxManager.searchMailboxes(availableMailboxes, searchFilter)
    }

    override fun slotsChanged(container: Container) {
        val mailStack = this.mailSlot.item
        if (!mailStack.isEmpty) {
            this.broadcastChanges()
        }
    }

    override fun quickMoveStack(player: Player, slotIndex: Int): ItemStack {
        val clicked = ItemStack.EMPTY
        val slot = slots[slotIndex]
        if (slot.hasItem()) {
            val stack = slot.item
            if (slotIndex == 0) {
                if (!this.moveItemStackTo(stack, 1, 37, false)) return ItemStack.EMPTY
            } else {
                if (!this.moveItemStackTo(stack, 0, 1, false)) return ItemStack.EMPTY
            }
        }
        return clicked
    }

    override fun removed(player: Player) {
        super.removed(player)
        if(mailSlot.hasItem()) clearContainer(player, inputContainer)
    }
}