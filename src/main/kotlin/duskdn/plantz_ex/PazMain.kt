package duskdn.plantz_ex

import duskdn.plantz_ex.block.entity.MailboxBlockEntity
import duskdn.plantz_ex.block.entity.MailboxManager
import duskdn.plantz_ex.block.entity.getMailboxMailQueue
import duskdn.plantz_ex.init.PazAttributes
import duskdn.plantz_ex.init.PazBlocks
import duskdn.plantz_ex.init.PazConfig
import duskdn.plantz_ex.init.PazCriteria
import duskdn.plantz_ex.init.PazDamageTypes
import duskdn.plantz_ex.init.PazDataSerializers
import duskdn.plantz_ex.init.PazEffects
import duskdn.plantz_ex.init.PazEntities
import duskdn.plantz_ex.init.PazItems
import duskdn.plantz_ex.init.PazJukeboxSongs
import duskdn.plantz_ex.init.PazLootTables
import duskdn.plantz_ex.init.PazMenus
import duskdn.plantz_ex.init.PazNetwork
import duskdn.plantz_ex.init.PazServerParticles
import duskdn.plantz_ex.init.PazSounds
import duskdn.plantz_ex.init.PazSpawnPlacements
import duskdn.plantz_ex.networking.ServerConfigResponsePayload
import duskdn.plantz_ex.raid.getZombieRaids
import duskdn.plantz_ex.tabs.PazCreativeTabs
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object PazMain : ModInitializer {
	const val MODID = "plantz_ex"
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

		ServerTickEvents.END_LEVEL_TICK.register {
			it.getZombieRaids().tick(it)

			
		}

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
		PazCreativeTabs.initialize()
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