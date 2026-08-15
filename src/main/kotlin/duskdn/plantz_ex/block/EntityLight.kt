package duskdn.plantz_ex.block

import net.minecraft.world.level.block.LightBlock

class EntityLight(properties: Properties): LightBlock(properties) {

//    companion object {
//        val CODEC: MapCodec<EntityLight> = simpleCodec(::EntityLight)
//        val SHAPE: VoxelShape = Util.make {
//            Shapes.or(
//                column(12.0, 0.0, 2.0),
//                column(6.0, 2.0, 7.0),
//                column(4.0, 7.0, 12.0),
//            )
//        }
//
//        val ACTIVE: BooleanProperty = BooleanProperty.create("active")
//    }
//
//    init {
//        registerDefaultState(stateDefinition.any().setValue(ACTIVE, false))
//    }
//
//    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
//        super.createBlockStateDefinition(builder)
//
//        builder.add(ACTIVE)
//    }
//
//    private val parentEntity: LivingEntity? = null
//
//    override fun neighborChanged(
//        state: BlockState,
//        level: Level,
//        pos: BlockPos,
//        block: Block,
//        orientation: Orientation?,
//        movedByPiston: Boolean
//    ) {
//        super.neighborChanged(state, level, pos, block, orientation, movedByPiston)
//
//        if (!level.isClientSide && !state.getValue(ACTIVE)) {
//            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState())
//        }
//    }
//
//    override fun codec(): MapCodec<EntityLight> { return EntityLight.CODEC }

}