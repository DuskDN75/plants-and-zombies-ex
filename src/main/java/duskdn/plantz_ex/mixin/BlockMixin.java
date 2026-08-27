package duskdn.plantz_ex.mixin;

import duskdn.plantz_ex.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.world.level.block.Block.*;

/**
 * @author Josh
 */
@Mixin(Block.class)
public class BlockMixin {

    @Inject(method = "playerDestroy", at = @At(value = "HEAD"), cancellable = true)
    public void removeSnowByLayers(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack destroyedWith, CallbackInfo ci) {

        if (level instanceof ServerLevel server && state.is(Blocks.SNOW)) {
            getDrops(state, server, pos, blockEntity, player, destroyedWith).forEach((stack) -> popResource(level, pos, stack));

            Utils.subtractSnowLayerOrDelete(level, pos, state, 1);

            ci.cancel();
        }

    }
}
