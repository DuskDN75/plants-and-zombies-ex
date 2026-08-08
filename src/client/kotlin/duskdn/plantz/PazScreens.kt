package duskdn.plantz

import duskdn.plantz.gui.MailboxScreen
import duskdn.plantz.gui.TimeMachineScreen
import duskdn.plantz.init.PazMenus
import net.minecraft.client.gui.screens.MenuScreens

object PazScreens {

    fun registerAll() {
        MenuScreens.register(PazMenus.MAILBOX_MENU, ::MailboxScreen)
        MenuScreens.register(PazMenus.TIME_MACHINE_MENU, ::TimeMachineScreen)
    }

}