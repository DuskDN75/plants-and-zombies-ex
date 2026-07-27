package duskdn.plantz.block

import com.mojang.serialization.MapCodec
import net.minecraft.core.Direction
import net.minecraft.world.level.block.SimpleWaterloggedBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.material.Fluids

class PlantPotBlock(properties: Properties) : BasePotBlock(properties), SimpleWaterloggedBlock {
    companion object {
        val CODEC: MapCodec<PlantPotBlock> = simpleCodec(::PlantPotBlock)
    }

    init {
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false))
    }

    override fun getFluidState(state: BlockState): FluidState {
        return if (state.getValue(WATERLOGGED)) Fluids.WATER.getSource(false) else super.getFluidState(state)
    }

    override fun codec(): MapCodec<out PlantPotBlock> { return CODEC }
}