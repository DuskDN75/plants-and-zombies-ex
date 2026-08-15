package duskdn.plantz_ex.mixin.client;

import duskdn.plantz_ex.init.PazItems;
import duskdn.plantz_ex.item.SeedPacketItem;
import kotlin.Triple;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


/**
 * @author Josh
 */
@Mixin(GuiGraphicsExtractor.class)
public abstract class GuiGraphicsExtractorMixin {

    @Inject(
            method = "itemCount",
            at = @At("HEAD"),
            cancellable = true
    )
    private void multiplyVisualCount(Font font, ItemStack itemStack, int x, int y, String countText, CallbackInfo ci) {

        if (!itemStack.isEmpty() && itemStack.is(PazItems.SUN)) {

            LocalPlayer player = Minecraft.getInstance().player;

            GuiGraphicsExtractor self = (GuiGraphicsExtractor) (Object) this;

            int count = itemStack.getCount() * 25;

            String textToDraw = String.valueOf(count);

            int halfWidth = (int) Math.round((double) font.width(textToDraw)/2.0);

            int color = -1;

            if (player != null && player.getMainHandItem().getItem() instanceof SeedPacketItem seedpacket) {

                ItemStack stack = player.getMainHandItem();

                Triple<Boolean, Integer, Integer> checks = seedpacket.checkCanAfford((Player) player, stack);

                if (!checks.component1()) {
                    color = 0xFFFF0000;
                }

            }

            self.text(font, textToDraw, x + 8 - halfWidth, y + 9, color, true);

            ci.cancel();
        }

    }
}
