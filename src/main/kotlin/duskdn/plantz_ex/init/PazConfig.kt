package duskdn.plantz_ex.init

import com.google.gson.GsonBuilder
import duskdn.plantz_ex.PazMain
import duskdn.plantz_ex.util.debugPrint
import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.EntityType
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.reader
import kotlin.io.path.writer
import kotlin.math.pow

data class PlantDataConfig(
    var sunCost: Int = 0,
    var cooldownTime: Double = 5.0
)

object CooldownTime {
    const val VSHORT = 2.0
    const val SHORT = 5.0
    const val MEDIUM = 10.0
    const val MLONG = 15.0
    const val LONG = 30.0
    const val VLONG = 45.0
    const val VVLONG = 60.0
}

data class ServerConfig(
    var coopPlanting: Boolean = true,
    var playerCreditForPlantKills: Boolean = false,
    var seedGrowTime: Int = 12000,
    var extraGrowTimePerSun: Int = 2400,
    var zenPotTimeReduction: Double = 0.25,
    var hydrationSunReduction: Double = 0.5,
    var plantPotDamageReduction: Double = 0.5,
    var coffeeBuffDuration: Int = 48_000,
    var sunCostTamingThreshold: Int = 30,
    var plantCooldownEnabled: Boolean = true,
    var solarBatteryMax: Int = 800,
    var plantData: MutableMap<String, PlantDataConfig> = mutableMapOf(
        "plantz_ex:sunflower"              to PlantDataConfig(
            50,
            CooldownTime.SHORT
        ),
        "plantz_ex:peashooter"             to PlantDataConfig(
            75,
            CooldownTime.SHORT
        ),
        "plantz_ex:wallnut"                to PlantDataConfig(
            50,
            CooldownTime.LONG
        ),
        "plantz_ex:explode_o_nut"          to PlantDataConfig(
            150,
            CooldownTime.VLONG
        ),
        "plantz_ex:chomper"                to PlantDataConfig(
            150,
            CooldownTime.MEDIUM
        ),
        "plantz_ex:cherrybomb"             to PlantDataConfig(
            150,
            CooldownTime.VLONG
        ),
        "plantz_ex:potatomine"             to PlantDataConfig(
            25,
            CooldownTime.LONG
        ),
        "plantz_ex:repeater"               to PlantDataConfig(
            200,
            CooldownTime.MEDIUM
        ),
        "plantz_ex:ice_peashooter"         to PlantDataConfig(
            175,
            CooldownTime.SHORT
        ),
        "plantz_ex:fire_peashooter"        to PlantDataConfig(
            175,
            CooldownTime.MEDIUM
        ),
        "plantz_ex:electric_peashooter"    to PlantDataConfig(
            250,
            CooldownTime.MEDIUM
        ),
        "plantz_ex:cactus"                 to PlantDataConfig(
            175,
            CooldownTime.SHORT
        ),
        "plantz_ex:lightning_reed"         to PlantDataConfig(
            100,
            CooldownTime.SHORT
        ),
        "plantz_ex:cabbagepult"            to PlantDataConfig(
            100,
            CooldownTime.SHORT
        ),
        "plantz_ex:kernelpult"             to PlantDataConfig(
            100,
            CooldownTime.SHORT
        ),
        "plantz_ex:melonpult"              to PlantDataConfig(
            300,
            CooldownTime.MEDIUM
        ),
        "plantz_ex:bonkchoy"               to PlantDataConfig(
            150,
            CooldownTime.SHORT
        ),
        "plantz_ex:tanglekelp"             to PlantDataConfig(
            25,
            CooldownTime.VLONG
        ),
        "plantz_ex:puffshroom"             to PlantDataConfig(
            0,
            CooldownTime.VSHORT
        ),
        "plantz_ex:scaredyshroom"          to PlantDataConfig(
            50,
            CooldownTime.SHORT
        ),
        "plantz_ex:fumeshroom"             to PlantDataConfig(
            125,
            CooldownTime.SHORT
        ),
        "plantz_ex:sunshroom"              to PlantDataConfig(
            25,
            CooldownTime.SHORT
        ),
        "plantz_ex:hypnoshroom"            to PlantDataConfig(
            75,
            CooldownTime.VLONG
        ),
        "plantz_ex:doomshroom"             to PlantDataConfig(
            250,
            CooldownTime.VVLONG
        ),
        "plantz_ex:lilypad"                to PlantDataConfig(
            25,
            CooldownTime.SHORT
        ),
        "plantz_ex:water_peashooter"       to PlantDataConfig(
            175,
            CooldownTime.SHORT
        ),
        "plantz_ex:seashroom"              to PlantDataConfig(
            0,
            CooldownTime.VSHORT
        ),
        "plantz_ex:coffeebean"             to PlantDataConfig(
            75,
            CooldownTime.SHORT
        ),
        "plantz_ex:flower_pot"             to PlantDataConfig(
            50, 
            5.0
        ),
        "plantz_ex:water_pot"              to PlantDataConfig(
            75,
            CooldownTime.SHORT
        ),
        "plantz_ex:lavalily"               to PlantDataConfig(
            75,
            CooldownTime.SHORT
        ),
        "plantz_ex:plantern"               to PlantDataConfig(
            75,
            CooldownTime.MEDIUM
        ),
        "plantz_ex:sky_peashooter"               to PlantDataConfig(
            75,
            CooldownTime.MEDIUM
        ),
        "plantz_ex:grave_buster"               to PlantDataConfig(
            75,
            CooldownTime.MLONG
        ),
    ),
)
data class ClientConfig(
    var showDebugInfo: Boolean = false,
)

object PazConfig {

    const val PLANT_SPAWNING_DISABLED = false

    const val ZOMBIE_SPAWNING_DISABLED = false

    const val SERVER_CONFIG_PATH = "plants-and-zombies-ex/paz-server.json"
    const val CLIENT_CONFIG_PATH = "plants-and-zombies-ex/paz-client.json"

    val GSON = GsonBuilder()
        .setPrettyPrinting()
        .disableHtmlEscaping()
        .create()

    private val serverConfigPath: Path = FabricLoader.getInstance().configDir.resolve(SERVER_CONFIG_PATH)
    private val clientConfigPath: Path = FabricLoader.getInstance().configDir.resolve(CLIENT_CONFIG_PATH)

    private val defaultServerConfig = ServerConfig()
    var server = defaultServerConfig

    private val defaultClientConfig = ClientConfig()
    var client = defaultClientConfig

    fun load() {
        if (FabricLoader.getInstance().environmentType == EnvType.CLIENT) {
            client = loadConfig(clientConfigPath, ClientConfig())
        }
        server = loadConfig(serverConfigPath, ServerConfig())

        defaultServerConfig.plantData.forEach { string, config ->
            server.plantData.putIfAbsent(string, config.copy())
        }

        saveConfig(serverConfigPath, server)
    }

    private inline fun <reified T> loadConfig(path: Path, default: T): T {
        return if (path.exists()) {
            try {
                path.reader().use { GSON.fromJson(it, T::class.java) } ?: default
            } catch (e: Exception) {
                PazMain.LOGGER.error("Failed to load config", e)
                default
            }
        } else default.also { saveConfig(path, it) }
    }

    private inline fun <reified T> saveConfig(path: Path, config: T) {
        try {
            Files.createDirectories(path.parent)
            path.writer().use { GSON.toJson(config, it) }
        } catch (exception: Exception) {
            PazMain.LOGGER.error("Failed to save plantz config.", exception)
        }
    }

    val SHOW_DEBUG_INFO: Boolean
        get() = client.showDebugInfo

    val COFFEE_BUFF_DURATION: Int
        get() = server.coffeeBuffDuration.coerceAtLeast(0)

    val SUN_BATTERY_MAX: Int
        get() = server.solarBatteryMax.coerceAtLeast(0)

    val COOP_PLANTING: Boolean
        get() = server.coopPlanting

    val PLAYER_CREDIT_FOR_PLANT_KILLS: Boolean
        get() = server.playerCreditForPlantKills

    val HYDRATION_SUN_REDUCTION: Double
        get() = 1f - server.hydrationSunReduction.coerceIn(0.0, 1.0)

    val PLANT_POT_DAMAGE_REDUCTION: Double
        get() = 1f - server.plantPotDamageReduction.coerceIn(0.0, 1.0)

    val PLANT_COOLDOWN_ENABLED: Boolean
        get() = server.plantCooldownEnabled

    fun getGrowTime(sunCost: Int, cooldownTime: Double, zenBuff: Boolean): Int {
        val time = server.seedGrowTime.coerceAtLeast(0) + ((sunCost*sunCost) * cooldownTime).toInt()
        return if (zenBuff) (time * (1f - server.zenPotTimeReduction.coerceIn(0.0, 1.0))).toInt() else time
    }

    fun getCooldownTime(type: EntityType<*>?): Double {
        val id = type?.let { BuiltInRegistries.ENTITY_TYPE.getKey(it) }
        return getCooldownTime(id)
    }

    fun getCooldownTime(entityId: Identifier?): Double {
        if (entityId == null) return 0.0
        val key = entityId.toString()
        val value = (server.plantData[key]?.cooldownTime ?:// config
        defaultServerConfig.plantData[key]?.cooldownTime?.let {// default
            putDefaults(entityId, cooldown = it)
            it
        }
        ?: -1) as Double // not in id list
        return value.coerceAtLeast(0.0)
    }

    fun getSunCost(type: EntityType<*>?): Int {
        return getTrueSunCost(type)/25
    }

    fun getTrueSunCost(type: EntityType<*>?): Int {
        val id = type?.let { BuiltInRegistries.ENTITY_TYPE.getKey(it) }
        return getTrueSunCost(id)
    }

    fun getTrueSunCost(entityId: Identifier?): Int {
        if (entityId == null) return 0
        val key = entityId.toString()
        debugPrint(key)
        debugPrint(server.plantData)
        val value = (server.plantData[key]?.sunCost ?:// config
        defaultServerConfig.plantData[key]?.sunCost?.let {// default
            putDefaults(entityId, sunCost = it)
            it
        }
        ?: 0) // not in id list
        return value.coerceAtLeast(0)
    }

    fun getTameChance(type: EntityType<*>?): Double {
        val a = server.sunCostTamingThreshold
        val sunCost = getSunCost(type).coerceAtMost(a)
        val weight = 3 // higher weight = harder to tame
        val chance = ((a - sunCost).toDouble() / (a + sunCost).toDouble()).pow(weight).coerceIn(0.02, 1.0)
        return chance
    }

    fun putDefaults(entityId: Identifier, sunCost: Int = 0, cooldown: Double = 0.0) {
        server.plantData.putIfAbsent(entityId.toString(), PlantDataConfig(sunCost, cooldown))
        saveConfig(serverConfigPath, server)
    }
}