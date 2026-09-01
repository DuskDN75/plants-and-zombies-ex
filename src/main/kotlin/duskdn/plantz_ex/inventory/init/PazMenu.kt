package duskdn.plantz_ex.inventory.init

import duskdn.plantz_ex.init.MailboxData
import duskdn.plantz_ex.init.PazMenus
import duskdn.plantz_ex.init.PazTags
import duskdn.plantz_ex.block.entity.MailboxManager
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerLevelAccess
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import java.util.*

abstract class PazMenu(
    menuType: MenuType<*>,
    containerId: Int
) : AbstractContainerMenu(menuType, containerId) {

    fun addStandardInventorySlots(container: Container, left: Int, top: Int) {
        addInventoryExtendedSlots(container, left, top)
        val hotbarSeparator = 4
        val topToHotbar = 58
        addInventoryHotbarSlots(container, left, top + 58)
    }

    fun addInventoryHotbarSlots(container: Container, left: Int, top: Int) {
        for (x in 0..9) {
            this.addSlot(Slot(container, x, left + x * 18, top));
        }
    }

    fun addInventoryExtendedSlots(inventory: Container, left: Int, top: Int) {
        for (y in 0..2) {
            for (x in 0..8) {
                this.addSlot(Slot(inventory, x + (y + 1) * 9, left + x * 18, top + y * 18))
            }
        }
    }

}