package duskdn.plantz_ex.mixin;

import duskdn.plantz_ex.util.PlantHeadAttachment;
import duskdn.plantz_ex.entity.plant.init.PazPlant;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.level.storage.ValueInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

/**
 * @author Josh
 */
@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin {

    @Shadow
    public abstract ServerLevel level();

    @Inject(method = "loadAndSpawnParentVehicle", at = @At(value = "HEAD"))
    public void respawnAttachedPlant(ValueInput playerInput, CallbackInfo ci) {
        if ( !((PlantHeadAttachment) this).plantz$getPlantData().isEmpty() ) {
            Optional<ValueInput> rootTag = playerInput.child("plantz_ex:AttachedPlant");
            if (rootTag.isPresent()) {
                ServerLevel serverLevel = this.level();
                Entity entity = EntityType.loadEntityRecursive(
                        rootTag.get(), serverLevel, EntitySpawnReason.LOAD, e -> !serverLevel.addWithUUID(e) ? null : e
                );
                if (entity instanceof PazPlant plantAttachment) {
                    plantAttachment.attachToEntity((ServerPlayer) (Object) this);
                }
            }
        }
    }

    //TODO probably should move to a EntityMixin instead
    @Inject(method = "teleport(Lnet/minecraft/world/level/portal/TeleportTransition;)Lnet/minecraft/server/level/ServerPlayer;", at = @At(value = "RETURN"))
    public void teleportAttachedPlant(TeleportTransition transition, CallbackInfoReturnable<ServerPlayer> cir) {
        ServerPlayer targetPlayer = cir.getReturnValue() != null ? cir.getReturnValue() : (ServerPlayer) (Object) this;
        if ( ((PlantHeadAttachment) this).plantz$getPlant() instanceof PazPlant plantAttachment ) {
                plantAttachment.detachFromEntity();
                plantAttachment.teleport(
                    new TeleportTransition(
                            transition.newLevel(),
                            transition.position(),
                            transition.deltaMovement(),
                            transition.yRot(),
                            transition.xRot(),
                            TeleportTransition.DO_NOTHING.then( entity -> {
                                if (entity instanceof PazPlant plantEntity) {
                                    plantEntity.setYRot(plantAttachment.yRotO);
                                    plantEntity.setXRot(plantAttachment.xRotO);
                                    plantEntity.attachToEntity(targetPlayer);
                                }
                            })
                    )
            );
        }
    }
}
