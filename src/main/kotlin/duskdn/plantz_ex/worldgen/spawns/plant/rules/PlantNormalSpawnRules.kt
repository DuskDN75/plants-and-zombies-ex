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
    open var seaLevelOffset: Int = -8

    override fun getExtraRule(context: SpawnContext): Boolean {

        context.setData("seaLevelOffset", seaLevelOffset)

        if (!strict) return super.getExtraRule(context)

        if (isMushroom) {
            val isDark = SpawnRules.IS_DARK.testRule(context)
            println("IsDark: $isDark")
            return isDark
        } else {
            return SpawnRules.ABOVE_SEALEVEL.testRule(context) && SpawnRules.IS_LIGHT.testRule(context)
        }
    }

    override fun getValidRule(context: SpawnContext): Boolean {
        val result = super.getValidRule(context)

        println("VALID RULE RESULT IS: $result")

        return result
    }

    override fun getAdjacentRule(context: SpawnContext): Boolean {

        val result = super.getAdjacentRule(context)

        println("ADJACENT RULE RESULT IS: $result")

        return result
    }

    override fun getPlantableRule(context: SpawnContext): Boolean {
        val result = super.getPlantableRule(context)

        println("PLANTABLE RULE RESULT IS: $result")

        return result
    }

    override fun spawnCheck(
        type: EntityType<out LivingEntity>,
        level: ServerLevelAccessor,
        spawnReason: EntitySpawnReason,
        pos: BlockPos,
        random: RandomSource
    ): Boolean {

        println("-----> BEGINNING CHECK FOR TYPE: $type ----->")

        val check = super.spawnCheck(type, level, spawnReason, pos, random)

        println("-----> CHECK VALUE IS: $check FOR TYPE: $type AT $pos -----|")

        return check
    }

//    override fun addRules() {
//        super.addRules()
//
//
//    }

}