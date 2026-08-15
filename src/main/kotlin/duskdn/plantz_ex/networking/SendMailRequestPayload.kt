package duskdn.plantz_ex.networking

import duskdn.plantz_ex.block.MailboxState
import duskdn.plantz_ex.block.entity.MailboxBlockEntity
import duskdn.plantz_ex.block.entity.MailboxMailQueue
import duskdn.plantz_ex.block.entity.MailboxManager
import duskdn.plantz_ex.block.entity.getMailboxMailQueue
import duskdn.plantz_ex.inventory.MailboxMenu
import duskdn.plantz_ex.util.pazResource
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.core.BlockPos
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.item.ItemStack

data class SendMailRequestPayload(val targetPos: BlockPos) : CustomPacketPayload {

    companion object {
        val ID: CustomPacketPayload.Type<SendMailRequestPayload> = CustomPacketPayload.Type(pazResource("send_mail_request"))

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SendMailRequestPayload> =
            StreamCodec.composite(
                BlockPos.STREAM_CODEC,
                SendMailRequestPayload::targetPos,
                ::SendMailRequestPayload
            )

        fun handleSendMailPacket(payload: SendMailRequestPayload, context: ServerPlayNetworking.Context) {
            val player = context.player()
            val level = player.level()
            val targetPos = payload.targetPos

            val menu = player.containerMenu as? MailboxMenu ?: return

            val senderBE = level.getBlockEntity(menu.data.blockPos) as? MailboxBlockEntity ?: return
            if (menu.availableMailboxes.none { it.blockPos == targetPos }) return

            val stack = menu.mailSlot.item.copy()
            if (stack.isEmpty) return

            val targetBE = level.getBlockEntity(targetPos) as? MailboxBlockEntity
            val success = if (targetBE != null) {
                MailboxMailQueue.tryInsertIntoMailbox(targetBE, stack)
            } else if (level.isLoaded(targetPos)) {
                level.getMailboxMailQueue().discardFor(targetPos)
                MailboxManager.unregisterMailbox(level, targetPos)
                false
            } else {
                level.getMailboxMailQueue().queue(targetPos, stack)
                true
            }

            if (success) {
                menu.mailSlot.set(ItemStack.EMPTY)
                menu.broadcastChanges()
                senderBE.setChanged()
                targetBE?.setChanged()
                targetBE?.updateMailboxState(MailboxState.HAS_MAIL)
                level.playSound(null, menu.data.blockPos, SoundEvents.UI_LOOM_SELECT_PATTERN, SoundSource.BLOCKS, 0.3f, 1.2f)
                ServerPlayNetworking.send(player, SendMailResponsePayload(
                    Component.translatable("container.plantz_ex.mailbox_success").withColor(0x00FF00)
                ))
            }
            else {
                level.playSound(null, menu.data.blockPos, SoundEvents.BARREL_CLOSE, SoundSource.BLOCKS, 0.3f, 1.2f)
                ServerPlayNetworking.send(player, SendMailResponsePayload(
                    Component.translatable("container.plantz_ex.mailbox_full", targetBE?.name ?: Component.translatable("item.plantz_ex.mailbox")).withColor(0xFF0000)
                ))
            }
        }
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = ID
}
