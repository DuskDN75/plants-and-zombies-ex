package joshxviii.plantz.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.vertex.PoseStack;
import joshxviii.plantz.DuckyTubeRenderLayer;
import joshxviii.plantz.DyeVatRenderLayer;
import joshxviii.plantz.mixin.LivingEntityAccessor;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static joshxviii.plantz.PazModels.*;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> implements RenderLayerParent<S, M> {

    @Shadow
    protected abstract boolean addLayer(RenderLayer<S, M> layer);

    @Inject(method = "<init>", at = @At("TAIL"))
    private void plantz$addDuckyTubeLayer(EntityRendererProvider.Context context, M model, float shadow, CallbackInfo ci) {
        this.addLayer(new DuckyTubeRenderLayer<>(this));
        this.addLayer(new DyeVatRenderLayer<>(this));
    }

    @Inject(method = "extractRenderState*", at = @At("TAIL"))
    private void checkForHypnoEffect(T entity, S state, float partialTicks, CallbackInfo ci) {
        boolean hasHypno = ((LivingEntityAccessor) entity).plantz$getHypnoId();
        state.setData(IS_HYPNOTIZED_KEY, hasHypno);
        int paintColor = ((LivingEntityAccessor) entity).plantz$getPaintedColor();
        state.setData(PAINT_COLOR_KEY, paintColor);
    }

    @Unique
    private static final int PLANTZ_HYPNO_TINT = 0xFFD036FF;

    @ModifyExpressionValue(
        method = "submit*",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ARGB;multiply(II)I")
    )
    private int plantz$applyHypnoTint(int tintedColor, S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        int finalColor = tintedColor;

        if (state.getDataOrDefault(IS_HYPNOTIZED_KEY, false)) {
            finalColor = ARGB.multiply(finalColor, PLANTZ_HYPNO_TINT);
        }

        int color = state.getDataOrDefault(PAINT_COLOR_KEY, -1);
        if (color != -1) finalColor = ARGB.multiply(finalColor, color);

        return finalColor;
    }
}
