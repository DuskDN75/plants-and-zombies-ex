package duskdn.plantz_ex.worldgen.spawns.init

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.ServerLevelAccessor

abstract class BaseSpawnRules {

    protected val spawnRules = mutableListOf<SpawnRule>()

    fun addRule(spawnRule: SpawnRule) {
        spawnRules.add(spawnRule)
    }

    init {
        addRules()
    }

    abstract fun addRules()

    /**
     * Default plant spawn rules
     */
    open fun spawnCheck(
        type: EntityType<out LivingEntity>,
        level: ServerLevelAccessor,
        spawnReason: EntitySpawnReason,
        pos: BlockPos,
        random: RandomSource
    ): Boolean {
        if (EntitySpawnReason.isSpawner(spawnReason)) return true

        val context = SpawnContext(type, level as ServerLevel, spawnReason, pos, random)

        for (rule in spawnRules) {
            if (!rule.testRule(context)) return false
        }

        return true
    }

}