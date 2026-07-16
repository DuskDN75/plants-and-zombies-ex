package joshxviii.plantz.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.world.level.block.LilyPadBlock;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Dusk
 */
@Mixin(LilyPadBlock.class)
public class WaterPotMixin {
    @Inject(method = "mayPlaceOn", at = @At(value = "HEAD"))
    private void waterPotPlacement(BlockState state, BlockGetter level, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {

    }
}
