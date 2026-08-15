package duskdn.plantz_ex.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {

    @Invoker("actuallyHurt")
    void invokeActuallyHurt(ServerLevel level, DamageSource source, float amount);

    @Invoker("playHurtSound")
    void invokePlayHurtSound(DamageSource source);

    @Invoker("playSecondaryHurtSound")
    void invokePlaySecondaryHurtSound(DamageSource source);

    @Invoker("checkTotemDeathProtection")
    boolean invokeCheckTotemDeathProtection(DamageSource source);

    @Invoker("getDeathSound")
    SoundEvent invokeGetDeathSound();

    @Invoker("resolveMobResponsibleForDamage")
    void invokeResolveMobResponsibleForDamage(DamageSource source);

    @Invoker("resolvePlayerResponsibleForDamage")
    Player invokeResolvePlayerResponsibleForDamage(DamageSource source);

    @Accessor("lastHurt")
    float invokeGetLastHurt();

    @Accessor("lastHurt")
    void invokeSetLastHurt(float value);

}
