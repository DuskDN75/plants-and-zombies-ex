package duskdn.plantz_ex.init

import duskdn.plantz_ex.networking.MailboxListResponsePayload
import duskdn.plantz_ex.networking.SendMailRequestPayload
import duskdn.plantz_ex.networking.SendMailRequestPayload.Companion.handleSendMailPacket
import duskdn.plantz_ex.networking.SendMailResponsePayload
import duskdn.plantz_ex.networking.ServerConfigResponsePayload
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking

object PazNetwork {

    fun initialize() {
        // Register payloads
        PayloadTypeRegistry.serverboundPlay().register(SendMailRequestPayload.ID, SendMailRequestPayload.STREAM_CODEC)

        PayloadTypeRegistry.clientboundPlay().register(SendMailRequestPayload.ID, SendMailRequestPayload.STREAM_CODEC)
        PayloadTypeRegistry.clientboundPlay().register(SendMailResponsePayload.ID, SendMailResponsePayload.STREAM_CODEC)
        PayloadTypeRegistry.clientboundPlay().register(MailboxListResponsePayload.ID, MailboxListResponsePayload.STREAM_CODEC)
        PayloadTypeRegistry.clientboundPlay().register(ServerConfigResponsePayload.ID, ServerConfigResponsePayload.STREAM_CODEC)

        // Register server receiver
        ServerPlayNetworking.registerGlobalReceiver(SendMailRequestPayload.ID, ::handleSendMailPacket)
    }
}