package duskdn.plantz_ex.worldgen.spawns.plant.rules

import duskdn.plantz_ex.worldgen.spawns.SpawnRules
import duskdn.plantz_ex.worldgen.spawns.init.SpawnContext
import duskdn.plantz_ex.worldgen.spawns.plant.init.PlantSpawnRules
import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.ServerLevelAccessor

open class PlantNormalSpawnRules(): PlantSpawnRules() {

    open var isMushroom: Boolean = false
    open var allowedOnCarriers: Boolean = false
    open var strict: Boolean = true

    override fun getValidRule(context: SpawnContext): Boolean {
        context.setData("seaLevelOffset", -8)

        var otherRule = if (isMushroom) {
            SpawnRules.IS_DARK.testRule(context) && context.pos.y < 0
        } else {
            SpawnRules.ABOVE_SEALEVEL.testRule(context) && SpawnRules.IS_LIGHT.testRule(context)
        }

        if (!strict) otherRule = true

        return super.getValidRule(context) && otherRule
    }

    override fun spawnCheck(
        type: EntityType<out LivingEntity>,
        level: ServerLevelAccessor,
        spawnReason: EntitySpawnReason,
        pos: BlockPos,
        random: RandomSource
    ): Boolean {
        return super.spawnCheck(type, level, spawnReason, pos, random)
    }

//    override fun addRules() {
//        super.addRules()
//
//
//    }

}