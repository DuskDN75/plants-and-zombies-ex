package duskdn.plantz_ex.mixin;

import net.minecraft.world.entity.monster.zombie.Zombie;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Zombie.class)
public interface ZombieAccessor {

    @Invoker("canSpawnInLiquids")
    boolean invokeCanSpawnInLiquids();
}