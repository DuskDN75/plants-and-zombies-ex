package duskdn.plantz_ex.mixin;

import duskdn.plantz_ex.util.PlantHeadAttachment;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author Josh
 */
@Mixin(PlayerList.class)
public class PlayerListMixin {
    @Inject(method = "remove", at = @At(value = "TAIL"))
    public void removePlantAttachment(ServerPlayer player, CallbackInfo ci) {
        if (((PlantHeadAttachment) player).plantzex$hasPlantOnHead()) {
            var plant = ((PlantHeadAttachment) player).plantzex$getPlant();
            if (plant!=null) {
                plant.setRemoved(Entity.RemovalReason.UNLOADED_WITH_PLAYER);
            }
        }
    }
}
