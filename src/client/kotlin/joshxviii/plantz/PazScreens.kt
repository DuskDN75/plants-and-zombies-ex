package joshxviii.plantz

import joshxviii.plantz.gui.MailboxScreen
import joshxviii.plantz.gui.TimeMachineScreen
import joshxviii.plantz.init.PazMenus
import net.minecraft.client.gui.screens.MenuScreens

object PazScreens {

    fun registerAll() {
        MenuScreens.register(PazMenus.MAILBOX_MENU, ::MailboxScreen)
        MenuScreens.register(PazMenus.TIME_MACHINE_MENU, ::TimeMachineScreen)
    }

}