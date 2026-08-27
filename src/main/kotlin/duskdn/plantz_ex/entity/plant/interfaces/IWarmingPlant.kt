package duskdn.plantz_ex.entity.plant.interfaces

import duskdn.plantz_ex.util.Utils
import net.minecraft.core.BlockPos
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.AABB

interface IWarmingPlant: IPlant {

    fun getMeltRate() : Int = 20

    fun getMeltAmount() : Int = 1

    fun getMeltChance() : Double = 0.5

    fun getMeltRadius() : Double = 5.0

    fun meltSnowAround() {

        val rate = getMeltRate()

        if (entity.tickCount % rate != 0) return

        val radius = getMeltRadius()

        val radSquared = radius*radius

        val chance = getMeltChance()

        val level = entity.level()

        val random = entity.random

        val snowNearby = BlockPos.betweenClosed(AABB(entity.blockPosition()).inflate(radius, radius, radius)).map { it.immutable() }
            .filter { pos ->

            val blockState = level.getBlockState(pos)

            entity.blockPosition().distSqr(pos) <= radSquared && (blockState.`is`(BlockTags.SNOW) || blockState.`is`(BlockTags.ICE) )
        }

        println("NEARBY SNOW IS: $snowNearby")

        snowNearby.forEach {

            val blockState = level.getBlockState(it)

            val roll = random.nextDouble()

            println("ROLL: $roll CHANCE: $chance")

            if (roll <= chance) {

                if (blockState.`is`(Blocks.ICE)) {
                    level.setBlockAndUpdate(it, Blocks.WATER.defaultBlockState())
                } else {
                    Utils.subtractSnowLayerOrDelete(level, it, blockState)
                }

            }
        }

    }

}