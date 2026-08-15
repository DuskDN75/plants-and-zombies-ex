package duskdn.plantz_ex.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.vertex.PoseStack;
import duskdn.plantz_ex.renderer.DuckyTubeRenderLayer;
import duskdn.plantz_ex.renderer.DyeVatRenderLayer;
import duskdn.plantz_ex.renderer.ObsidianDuckyTubeRenderLayer;
import duskdn.plantz_ex.renderer.PaintLayer;
import duskdn.plantz_ex.util.PazEntityData;
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
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

import static duskdn.plantz_ex.PazModels.*;

@Debug(export = true)
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> implements RenderLayerParent<S, M> {

    @Shadow
    protected abstract boolean addLayer(RenderLayer<S, M> layer);

    @Inject(method = "<init>", at = @At("TAIL"))
    private void plantz$addDuckyTubeLayer(EntityRendererProvider.Context context, M model, float shadow, CallbackInfo ci) {
        this.addLayer(new DuckyTubeRenderLayer<>(this));
        this.addLayer(new ObsidianDuckyTubeRenderLayer<>(this));
        this.addLayer(new DyeVatRenderLayer<>(this));
        this.addLayer(new PaintLayer<>(this));
    }

    @Inject(method = "extractRenderState*", at = @At("TAIL"))
    private void checkForEffects(T entity, S state, float partialTicks, CallbackInfo ci) {
        boolean hasHypno = ((PazEntityData) entity).plantz$getHypnoId();
        state.setData(IS_HYPNOTIZED_KEY, hasHypno);

        boolean hasChilled = ((PazEntityData) entity).plantz$getChilledId();
        state.setData(IS_CHILLED_KEY, hasChilled);

        boolean hasDrenched = ((PazEntityData) entity).plantz$getDrenchedId();
        state.setData(IS_DRENCHED_KEY, hasDrenched);

        boolean hasFrozen = ((PazEntityData) entity).plantz$getFrozenId();
        state.setData(IS_FROZEN_KEY, hasFrozen);

        boolean hasEnlightened = ((PazEntityData) entity).plantz$getEnlightenedId();
        state.setData(IS_ENLIGHTENED_KEY, hasEnlightened);

        Map<Integer, Integer> paintColors = ((PazEntityData) entity).plantz$getPaintedColors();
        state.setData(PAINT_COLORS_KEY, paintColors);
    }

    @Unique
    private static final int PLANTZ_HYPNO_TINT = 0xFFD036FF;

    @Unique
    private static final int PLANTZ_CHILLED_TINT = 0xFF8BC1FF;

    @Unique
    private static final int PLANTZ_FROZEN_TINT = 0xFF0C0293;

    @Unique
    private static final int PLANTZ_DRENCHED_TINT = 0xFF3F76E4;

    @Unique
    private static final int PLANTZ_ENLIGHTENED_TINT = 0xFFFFFDD2;

//    @ModifyVariable(
//            method = "submit*",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IIILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V")
//    )
//    private int plantz$applyTint(int tintedColor, S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
//
//        if (state.getDataOrDefault(IS_HYPNOTIZED_KEY, false)) {
//            tintedColor = (ARGB.multiply(tintedColor, PLANTZ_HYPNO_TINT));
//        }
//
//        if (state.getDataOrDefault(IS_CHILLED_KEY, false)) {
//            tintedColor = (ARGB.multiply(tintedColor, PLANTZ_CHILLED_TINT));
//        }
//
//        if (state.getDataOrDefault(IS_DRENCHED_KEY, false)) {
//            tintedColor = (ARGB.srgbLerp(0.8f, tintedColor, PLANTZ_DRENCHED_TINT));
//        }
//
//        if (state.getDataOrDefault(IS_FROZEN_KEY, false)) {
//            tintedColor = (ARGB.srgbLerp(0.8f, tintedColor, PLANTZ_FROZEN_TINT));
//        }
//
//        if (state.getDataOrDefault(IS_ENLIGHTENED_KEY, false)) {
//            tintedColor = (ARGB.linearLerp(1.0f, tintedColor, PLANTZ_ENLIGHTENED_TINT));
//        }
//
//        return tintedColor;
//    }

    @ModifyExpressionValue(
            method = "submit*",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ARGB;multiply(II)I")
    )
    private int plantz$applyTint(int tintedColor, S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {

        if (state.getDataOrDefault(IS_HYPNOTIZED_KEY, false)) {
            tintedColor = ARGB.multiply(tintedColor, PLANTZ_HYPNO_TINT);
        }

        if (state.getDataOrDefault(IS_DRENCHED_KEY, false)) {
            tintedColor = ARGB.multiply(tintedColor, PLANTZ_DRENCHED_TINT);
        }

        if (state.getDataOrDefault(IS_CHILLED_KEY, false)) {
            tintedColor = ARGB.multiply(tintedColor, PLANTZ_CHILLED_TINT);
        }

        if (state.getDataOrDefault(IS_FROZEN_KEY, false)) {
            tintedColor = ARGB.multiply(tintedColor, PLANTZ_FROZEN_TINT);
        }

        if (state.getDataOrDefault(IS_ENLIGHTENED_KEY, false)) {
            tintedColor = ARGB.addRgb(tintedColor, PLANTZ_ENLIGHTENED_TINT);
        }

        return tintedColor;
    }
}
