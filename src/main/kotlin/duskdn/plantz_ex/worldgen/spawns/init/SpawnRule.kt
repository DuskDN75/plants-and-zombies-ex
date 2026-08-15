package duskdn.plantz_ex.worldgen.spawns.init

import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.ServerLevelAccessor

data class SpawnContext(
    val type: EntityType<out LivingEntity>,
    val level: ServerLevelAccessor,
    val spawnReason: EntitySpawnReason,
    val pos: BlockPos,
    val randomSource: RandomSource
) {

    val extraData = mutableMapOf<String, Any>()

    inline fun <reified T> getData(key:String): T? = extraData[key] as? T

    fun setData(key:String, value: Any?) {
        extraData[key] = value as Any
    }
}

fun interface SpawnRule {
    fun testRule(context: SpawnContext): Boolean

    infix fun and(other: SpawnRule): SpawnRule {
        return SpawnRule { context ->
            this.testRule(context) && other.testRule(context)
        }
    }
}