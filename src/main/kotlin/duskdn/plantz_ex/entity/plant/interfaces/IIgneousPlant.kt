package duskdn.plantz_ex.entity.plant.interfaces

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.tags.FluidTags
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.phys.Vec3

interface IIgneousPlant: IPlant {

    fun applyBuoyancy() {

        val height = 1-entity.buoyancyHeight

        val level = entity.level()

        val startPos = BlockPos.containing(entity.position().relative(Direction.UP, entity.bbHeight * height))

        val fluidType = level
            .getBlockState(startPos)
            .fluidState.type

        if (fluidType === Fluids.EMPTY || fluidType === Fluids.FLOWING_WATER || fluidType === Fluids.WATER) return

        var surfacePos = startPos.immutable()

        while (level.getFluidState(surfacePos.above()).type == fluidType) {
            surfacePos = surfacePos.above()
        }

        val topHeight = level.getFluidState(surfacePos).getHeight(level, surfacePos) + surfacePos.y - (entity.bbHeight * height)

        val distance = topHeight - entity.y

        //base
        var upwardForce: Double = 0.1

        // submerged
        if (entity.isEyeInFluid(FluidTags.LAVA)) upwardForce += 0.05

//        if (distance <= 1) upwardForce *= distance

        if (distance <= 0.2) {
            entity.setPosRaw(entity.x, topHeight, entity.z)
        } else {
            entity.addDeltaMovement(Vec3(0.0, upwardForce, 0.0))
        }

        entity.fallDistance = 0.0
    }

}