package joshxviii.plantz.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {

    @Invoker("plantz$getHypnoId")
    boolean plantz$getHypnoId();

    @Invoker("plantz$getChilledId")
    boolean plantz$getChilledId();

    @Invoker("plantz$getDrenchedId")
    boolean plantz$getDrenchedId();

    @Invoker("plantz$getPaintedColors")
    Map<Integer, Integer> plantz$getPaintedColors();

    @Invoker("actuallyHurt")
    void invokeActuallyHurt(ServerLevel level, DamageSource source, float amount);

    @Invoker("resolveMobResponsibleForDamage")
    void invokeResolveMobResponsibleForDamage(DamageSource source);

    @Invoker("resolvePlayerResponsibleForDamage")
    Player invokeResolvePlayerResponsibleForDamage(DamageSource source);

}
