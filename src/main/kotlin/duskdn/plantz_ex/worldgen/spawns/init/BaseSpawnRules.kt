package duskdn.plantz_ex.worldgen.spawns.init

import duskdn.plantz_ex.util.debugPrint
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MobSpawnType
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
        spawnReason: MobSpawnType,
        pos: BlockPos,
        random: RandomSource
    ): Boolean {
        if (MobSpawnType.isSpawner(spawnReason)) return true

        if (level !is ServerLevel) {
            return false
        }

        val context = SpawnContext(type, level, spawnReason, pos, random)

        for (rule in spawnRules) {

            debugPrint(rule)

            val ruleResult = rule.testRule(context)

            debugPrint("RULE RESULT IS: $ruleResult")

            if (!ruleResult) return false
        }

        return true
    }

}