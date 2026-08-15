package duskdn.plantz_ex.entity.interfaces

import duskdn.plantz_ex.init.PazBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.LightBlock

interface ILuminousEntity: IEntity {

    var lastLightBlockPos: BlockPos?

    fun getLightLevel(): Int {
        return -1
    }

//    var lastLightBlock: BlockState?
//
//    var currentLightBlock: BlockState?

    fun removeLightBlock(pos: BlockPos?) {

        if (pos == null) return

        if (getLightLevel() < 0) return

        val level = entity.level()

        val currentBlockState = level.getBlockState(pos)

        if (currentBlockState.`is`(PazBlocks.ENTITY_LIGHT)) {
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState())
        }

    }

    fun updateLightBlock() {

        val lightLevel = getLightLevel()

        if (lightLevel < 0) return

        val level = entity.level()

        val currentPos = BlockPos.containing(entity.position().relative(Direction.UP, (entity.bbHeight/2).toDouble()))

        val currentBlockState = level.getBlockState(currentPos)

        if (lastLightBlockPos == null || lastLightBlockPos != currentPos) {

            if (lastLightBlockPos != null) {
                removeLightBlock(lastLightBlockPos)
            } else {
                removeLightBlock(currentPos)
            }

            if (currentBlockState.`is`(Blocks.AIR)) {

                val light = PazBlocks.ENTITY_LIGHT.defaultBlockState().setValue(LightBlock.LEVEL, lightLevel)

                level.setBlockAndUpdate(currentPos, light)

//                currentLightBlock = currentBlockState

                lastLightBlockPos = currentPos

//            } else {
//                lastLightBlockPos = null
            }

        }

    }

}