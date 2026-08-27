package duskdn.plantz_ex.mixin.client;

import duskdn.plantz_ex.init.PazBlocks;
import duskdn.plantz_ex.init.PazItems;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.monster.skeleton.SkeletonModel;
import net.minecraft.client.renderer.entity.state.SkeletonRenderState;
import net.minecraft.client.renderer.entity.state.UndeadRenderState;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


/**
 * @author Josh
 */
@Mixin(SkeletonModel.class)
public abstract class SkeletonModelMixin {

    @Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/SkeletonRenderState;)V", at = @At("TAIL"))
    private void plantzex$skeletonraisearmanim(SkeletonRenderState state, CallbackInfo ci) {

        SkeletonModel model = (SkeletonModel) (Object) this;

        boolean isHoldingFlag = state.rightHandItemStack.is(PazBlocks.BRAINZ_FLAG.asItem()) || state.leftHandItemStack.is(PazBlocks.BRAINZ_FLAG.asItem());

        boolean isHoldingScreenDoor = state.rightHandItemStack.is(PazBlocks.SCREEN_DOOR.asItem()) || state.leftHandItemStack.is(PazBlocks.SCREEN_DOOR.asItem());

        if (isHoldingFlag || isHoldingScreenDoor) {

            var armDrop = -(float)Math.PI / 2.25F;

            float attackYRotModifier = Mth.sin(state.attackTime * (float) Math.PI);
            float attackXRotModifier = Mth.sin((1.0F - (1.0F - state.attackTime) * (1.0F - state.attackTime)) * (float)Math.PI);
            float xRot = armDrop + attackYRotModifier * 1.2F - attackXRotModifier * 0.4F;
            float yRot = 0.1F - attackYRotModifier * 0.6F;
            model.rightArm.xRot = xRot;
            model.rightArm.yRot = -yRot;
            model.rightArm.zRot = 0.0F;
            model.leftArm.xRot = xRot;
            model.leftArm.yRot = yRot;
            model.leftArm.zRot = 0.0F;

            AnimationUtils.bobArms(model.rightArm, model.leftArm, state.ageInTicks);

        }

    }

}
