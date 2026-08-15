package duskdn.plantz_ex

import duskdn.plantz_ex.gui.MailboxScreen
import duskdn.plantz_ex.gui.TimeMachineScreen
import duskdn.plantz_ex.init.PazMenus
import net.minecraft.client.gui.screens.MenuScreens

object PazScreens {

    fun registerAll() {
        MenuScreens.register(PazMenus.MAILBOX_MENU, ::MailboxScreen)
        MenuScreens.register(PazMenus.TIME_MACHINE_MENU, ::TimeMachineScreen)
    }

}