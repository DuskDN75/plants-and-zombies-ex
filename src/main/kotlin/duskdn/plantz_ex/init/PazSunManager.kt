package duskdn.plantz_ex.init

import duskdn.plantz_ex.entity.Sun
import duskdn.plantz_ex.raid.getZombieRaids
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.phys.Vec3

object PazSunManager {

    fun initialize() {

        ServerTickEvents.END_SERVER_TICK.register {

            checkSpawnSun(it)
        }

    }

    fun checkSpawnSun(server: MinecraftServer) {

        val tickCount = server.tickCount

        for (player in server.playerList.players) {

            if (player.isCreative || player.isSpectator) continue

            val level = player.level()

            if (!level.isBrightOutside || level.isThundering || !level.canSeeSky(player.blockPosition())) continue

            val sunTime = if (level.isRaining) 600 else 200

            if (tickCount % sunTime == 0) spawnSunAroundPlayer(level, player, 1)

        }

    }

    fun spawnSunAroundPlayer(server: ServerLevel, player: ServerPlayer, amount: Int) {

        val spawnPosition = Vec3(
            player.getRandomX(10.0),
            player.y + 60,
            player.getRandomZ(10.0)
        )

        Sun.award(server, spawnPosition, amount )
    }

}