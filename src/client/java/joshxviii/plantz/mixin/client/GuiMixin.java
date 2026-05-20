package joshxviii.plantz.mixin.client;

import joshxviii.plantz.PazEffects;
import joshxviii.plantz.effect.PaintedMobEffect;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

import static joshxviii.plantz.PazModels.PAINT_OVERLAY_TEXTURE;

/**
 * @author Josh
 */
@Mixin(Gui.class)
public abstract class GuiMixin {

    @Shadow
    protected abstract void extractTextureOverlay(GuiGraphicsExtractor graphics, Identifier texture, float alpha);

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "extractCameraOverlays", at = @At("HEAD"))
    public void extractCameraOverlays(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        LocalPlayer player = this.minecraft.player;
        if (player.hasEffect(PazEffects.PAINTED)) {
            var effect = Objects.requireNonNull(player.getEffect(PazEffects.PAINTED)).getEffect().value();
            if (effect instanceof PaintedMobEffect paintedMobEffect) {
                extractPaintOverlay(graphics, paintedMobEffect.getPaintColor(), 1f);
            }
        }
    }

    private void extractPaintOverlay(final GuiGraphicsExtractor graphics, int color, final float scale) {
        float srcWidth = Math.min(graphics.guiWidth(), graphics.guiHeight());
        float ratio = Math.min(graphics.guiWidth() / srcWidth, graphics.guiHeight() / srcWidth) * scale;
        int width = Mth.floor(srcWidth * ratio);
        int height = Mth.floor(srcWidth * ratio);
        int left = (graphics.guiWidth() - width) / 2;
        int top = (graphics.guiHeight() - height) / 2;
        int right = left + width;
        int bottom = top + height;
        graphics.blit(RenderPipelines.GUI_TEXTURED, PAINT_OVERLAY_TEXTURE, 0, 0, 0.0F, 0.0F, graphics.guiWidth(), graphics.guiHeight(), graphics.guiWidth(), graphics.guiHeight(), ARGB.opaque(color));
        //graphics.fill(RenderPipelines.GUI, 0, bottom, graphics.guiWidth(), graphics.guiHeight(), -16777216);
        //graphics.fill(RenderPipelines.GUI, 0, 0, graphics.guiWidth(), top, -16777216);
        //graphics.fill(RenderPipelines.GUI, 0, top, left, bottom, -16777216);
        //graphics.fill(RenderPipelines.GUI, right, top, graphics.guiWidth(), bottom, -16777216);
    }
}
