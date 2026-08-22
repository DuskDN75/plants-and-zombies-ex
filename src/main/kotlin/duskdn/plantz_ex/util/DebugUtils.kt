package duskdn.plantz_ex.util

import duskdn.plantz_ex.init.PazConfig
import net.fabricmc.loader.impl.lib.sat4j.core.Vec
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.DustParticleOptions
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.ARGB
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import java.lang.reflect.Field
import java.util.WeakHashMap

val defaultDustParticle = DustParticleOptions(ARGB.color(Vec3(1.0,1.0,1.0)), 2.0f)

val blueDustParticle = DustParticleOptions(ARGB.color(Vec3(0.2,0.5,1.0)), 2.0f)

val redDustParticle = DustParticleOptions(ARGB.color(Vec3(1.0,0.1,0.2)), 2.0f)

val yellowDustParticle = DustParticleOptions(ARGB.color(Vec3(1.0,0.8,0.2)), 2.0f)

val orangeDustParticle = DustParticleOptions(ARGB.color(Vec3(0.8,0.5,0.2)), 2.0f)

fun trackVector(level: Level, dustParticle: DustParticleOptions = defaultDustParticle, targetPos: Vec3, offsetPos: Vec3 = Vec3.ZERO) {

    if (!PazConfig.SHOW_DEBUG_TRACKERS) return

    if (level.isClientSide) return

    (level as ServerLevel).sendParticles(
        dustParticle,
        targetPos.x+offsetPos.x, targetPos.y+offsetPos.y, targetPos.z+offsetPos.z,
        20, 0.0, 0.0, 0.0, 0.0
    )
}

class DebugTracker(
    val parent: Any,
    val variable: String,
    colorVec: Vec3 = Vec3(1.0, 1.0, 1.0),
    val field: Field = parent.javaClass.getDeclaredField(variable)
) {

    val color = ARGB.color(colorVec)

    val dustParticle = DustParticleOptions(color, 2.0f)

    init {
        field.isAccessible = true
    }

    fun update(level: Level) {

        if (level.isClientSide) {

            debugPrint("VARIABLE IS: $variable, LEVEL IS CLIENT SIDE!")

            return

        }

        try {
            val rawVariable = field.get(parent)

            val targetPos: Vec3 = when (rawVariable) {
                is Vec3 -> rawVariable
                is BlockPos -> Vec3.atCenterOf(rawVariable)
                else -> return
            }

            debugPrint("VARIABLE IS: $variable, TARGET POS IS: $targetPos")

            trackVector(level, dustParticle, targetPos)

        } catch (e: Exception) {
            debugPrint("ERROR!!! COULD NOT TRACK VARIABLE $variable WITH EXCEPTION: $e")
        }

    }

}

private val entityTrackerCache = WeakHashMap<Any, MutableList<DebugTracker>>()

var Any.trackers: MutableList<DebugTracker>
    get() = entityTrackerCache.computeIfAbsent(this) { mutableListOf() }
    set(value) { entityTrackerCache[this] = value }

fun Any.updateTrackers(level: Level) {

    if (!PazConfig.SHOW_DEBUG_TRACKERS) return

    val activeTrackers = entityTrackerCache[this] ?: return
    for (tracker in activeTrackers) {
        tracker.update(level)
    }

}

fun Any.trackVariable(variable: String, color: Vec3 = Vec3(1.0, 1.0, 1.0), thing: Any = this) {

    if (!PazConfig.SHOW_DEBUG_TRACKERS) return

    try {
        val tracker = DebugTracker(parent = thing, variable = variable, colorVec = color)

        this.trackers.add(tracker)
    } catch (e: Exception) {
        debugPrint("COULD NOT SETUP TRACKER FOR $variable WITH ERROR: $e")
    }

}

fun debugPrint(message: Any?) {
    if (!PazConfig.SHOW_DEBUG_PRINTS) return

    println(message)
}