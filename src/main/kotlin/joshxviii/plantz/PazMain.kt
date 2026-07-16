package joshxviii.plantz

import joshxviii.plantz.block.entity.MailboxBlockEntity
import joshxviii.plantz.block.entity.MailboxManager
import joshxviii.plantz.block.entity.getMailboxMailQueue
import joshxviii.plantz.init.PazAttributes
import joshxviii.plantz.init.PazBlocks
import joshxviii.plantz.init.PazConfig
import joshxviii.plantz.init.PazCriteria
import joshxviii.plantz.init.PazDamageTypes
import joshxviii.plantz.init.PazDataSerializers
import joshxviii.plantz.init.PazEffects
import joshxviii.plantz.init.PazEntities
import joshxviii.plantz.init.PazItems
import joshxviii.plantz.init.PazJukeboxSongs
import joshxviii.plantz.init.PazLootTables
import joshxviii.plantz.init.PazMenus
import joshxviii.plantz.init.PazNetwork
import joshxviii.plantz.init.PazServerParticles
import joshxviii.plantz.init.PazSounds
import joshxviii.plantz.init.PazSpawnPlacements
import joshxviii.plantz.networking.ServerConfigResponsePayload
import joshxviii.plantz.raid.getZombieRaids
import joshxviii.plantz.init.PazCreativeTab
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object PazMain : ModInitializer {
	const val MODID = "plantz"
	@JvmField
	val LOGGER: Logger = LogManager.getLogger()

	override fun onInitialize() {
		PazConfig.load()
		ServerLifecycleEvents.SERVER_STARTING.register { PazConfig.load() }
		ServerPlayerEvents.JOIN.register { player ->
			val json = PazConfig.let { it.GSON.toJson(it.server) }
			val payload = ServerConfigResponsePayload(json)
			ServerPlayNetworking.send(player, payload)

			LOGGER.info("Sent server config to ${player.name.string}")
		}

		ServerTickEvents.END_LEVEL_TICK.register { it.getZombieRaids().tick(it) }

		// mailbox managing
		ServerBlockEntityEvents.BLOCK_ENTITY_LOAD.register { blockEntity, level ->
			(blockEntity as? MailboxBlockEntity)?.let {
				MailboxManager.registerMailbox(level, it)
				level.getMailboxMailQueue().deliverTo(it)
			}
		}
		ServerBlockEntityEvents.BLOCK_ENTITY_UNLOAD.register { blockEntity, level ->
			(blockEntity as? MailboxBlockEntity)?.let {
				//MailboxManager.unregisterMailbox(level, blockEntity.blockPos)
			}
		}

		PazServerParticles.initialize()
		PazBlocks.initialize()
		PazItems.initialize()
		PazLootTables.initialize()
		PazEffects.initialize()
		PazCreativeTab.initialize()
		PazEntities.initialize()
		PazDamageTypes.initialize()
		PazCriteria.initialize()
		PazDataSerializers.initialize()
		PazAttributes.initialize()
		PazSounds.initialize()
		PazSpawnPlacements.initialize()
		PazMenus.initialize()
		PazNetwork.initialize()
		PazJukeboxSongs.initialize()
	}
}