package duskdn.plantz

import duskdn.plantz.init.PazConfig
import duskdn.plantz.init.ServerConfig
import duskdn.plantz.inventory.MailboxMenu
import duskdn.plantz.networking.MailboxListResponsePayload
import duskdn.plantz.networking.SendMailResponsePayload
import duskdn.plantz.networking.ServerConfigResponsePayload
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking

object PazClientNetwork {

    fun initialize() {
        ClientPlayNetworking.registerGlobalReceiver(SendMailResponsePayload.ID) { payload, context ->
            context.client().execute {
                val mc = context.client()
                val player = mc.player ?: return@execute
                val menu = player.containerMenu as? MailboxMenu ?: return@execute
                menu.responseMessage = payload.message
                menu.responseTimeout = 30
            }
        }

        ClientPlayNetworking.registerGlobalReceiver(MailboxListResponsePayload.ID) { payload, context ->
            context.client().execute {
                val player = context.player()
                val menu = player.containerMenu as? MailboxMenu ?: return@execute

                // Rebuild list from positions
                menu.availableMailboxes = payload.mailboxes
                menu.updateFilteredMailboxes()
            }

        }

        ClientPlayNetworking.registerGlobalReceiver(ServerConfigResponsePayload.ID) { payload, context ->
            context.client().execute {
                try {
                    PazConfig.server = PazConfig.GSON.fromJson(payload.json, ServerConfig::class.java)
                    PazMain.LOGGER.info("Server config synced!")
                } catch (e: Exception) {
                    PazMain.LOGGER.error("Failed to parse server config", e)
                }
            }
        }

    }
}