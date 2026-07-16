package joshxviii.plantz.networking

import joshxviii.plantz.util.pazResource
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

data class ServerConfigResponsePayload(
    val json: String
) : CustomPacketPayload {
    companion object {
        val ID: CustomPacketPayload.Type<ServerConfigResponsePayload> = CustomPacketPayload.Type(pazResource("server_config_response"))
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, ServerConfigResponsePayload> =
            StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8,
                ServerConfigResponsePayload::json,
                ::ServerConfigResponsePayload,
            )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = ID
}