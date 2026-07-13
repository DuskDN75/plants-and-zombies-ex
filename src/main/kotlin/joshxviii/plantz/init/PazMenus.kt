package joshxviii.plantz.init

import joshxviii.plantz.inventory.MailboxMenu
import joshxviii.plantz.inventory.TimeMachineMenu
import joshxviii.plantz.pazResource
import net.fabricmc.fabric.api.menu.v1.ExtendedMenuType
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.ComponentSerialization
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec


object PazMenus {

    @JvmField val MAILBOX_MENU: ExtendedMenuType<MailboxMenu, MailboxData> = ExtendedMenuType(
        { containerId, inventory, data -> MailboxMenu(containerId, inventory, data) },
        MailboxData.STREAM_CODEC
    )

    @JvmField val TIME_MACHINE_MENU: ExtendedMenuType<TimeMachineMenu, TimeMachineData> = ExtendedMenuType(
        { containerId, inventory, data -> TimeMachineMenu(containerId, inventory, data.blockPos) },
        TimeMachineData.STREAM_CODEC
    )

    fun initialize() {
        Registry.register(BuiltInRegistries.MENU, pazResource("time_machine"), TIME_MACHINE_MENU)
        Registry.register(BuiltInRegistries.MENU, pazResource("mailbox"), MAILBOX_MENU)
    }
}

@JvmRecord
data class TimeMachineData(val blockPos: BlockPos) {
    companion object {
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, TimeMachineData> =
            StreamCodec.composite(
                BlockPos.STREAM_CODEC,
                TimeMachineData::blockPos
            ) { blockPos: BlockPos -> TimeMachineData(blockPos) }
    }
}

@JvmRecord
data class MailboxData(val blockPos: BlockPos, val color: Int, val name: Component) {
    companion object {
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, MailboxData> =
            StreamCodec.composite(
                BlockPos.STREAM_CODEC,
                MailboxData::blockPos,
                ByteBufCodecs.INT,
                MailboxData::color,
                ComponentSerialization.STREAM_CODEC,
                MailboxData::name,
                ::MailboxData
            )
    }
}