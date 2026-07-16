package joshxviii.plantz.block

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.RandomSource
import net.minecraft.util.Util
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.ScheduledTickAccess
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.SimpleWaterloggedBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.level.pathfinder.PathComputationType
import net.minecraft.world.phys.shapes.BooleanOp
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class WaterPotBlock(properties: Properties) : BasePotBlock(properties), SimpleWaterloggedBlock {
    companion object {
        val CODEC: MapCodec<WaterPotBlock> = simpleCodec(::WaterPotBlock)

        val SHAPE_INSIDE = column(14.0, 0.0, 12.0)
        val SHAPE: VoxelShape = Util.make {

            val solid = Shapes.or(
                column(12.0, 0.0, 12.0),
                column(14.0, 1.0, 8.0),
                column(16.0, 2.0, 7.0),
                column(14.0, 11.0, 12.0),
            )

            val cut = Shapes.or(
                column(10.0, 9.0, 12.0),
            )

            Shapes.join(solid, cut, BooleanOp.ONLY_FIRST)

        }
    }

    init {
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false))
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return SHAPE
    }

    override fun getInteractionShape(state: BlockState, level: BlockGetter, pos: BlockPos): VoxelShape {
        return SHAPE_INSIDE
    }

    override fun getFluidState(state: BlockState): FluidState {
        return if (state.getValue(WATERLOGGED)) Fluids.WATER.getSource(false) else super.getFluidState(state)
    }

    override fun codec(): MapCodec<out WaterPotBlock> { return CODEC }
}