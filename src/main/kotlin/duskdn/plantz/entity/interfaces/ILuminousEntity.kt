package duskdn.plantz.entity.interfaces

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.ai.navigation.PathNavigation
import net.minecraft.world.phys.Vec3

interface ILuminousEntity: IEntity {

    var lastLightBlockPos: BlockPos?

    fun removeLightBlock() {

        val level = entity.level()

        val currentPos = entity.blockPosition()

        level.getBlockState(currentPos)

        if (lastLightBlockPos == null || lastLightBlockPos != currentPos)

    }

    fun updateLightBlock() {

    }

}