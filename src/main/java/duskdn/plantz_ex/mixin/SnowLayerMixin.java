package duskdn.plantz_ex.mixin;

import duskdn.plantz_ex.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Josh
 */
@Mixin(SnowLayerBlock.class)
public class SnowLayerMixin {
    @Inject(method = "randomTick", at = @At(value = "HEAD"), cancellable = true)
    public void removeSnowByLayers(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {

        if (level.getBrightness(LightLayer.BLOCK, pos) > 11) {
            Utils.subtractSnowLayerOrDelete(level, pos, state, 1);
        }

        ci.cancel();

    }
}
