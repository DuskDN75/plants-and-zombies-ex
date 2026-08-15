// fuck my life if this doesnt work

package duskdn.plantz_ex.mixin;

import duskdn.plantz_ex.entity.Balloon;
import net.minecraft.world.entity.Leashable;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Balloon.class)
public abstract class BalloonMixin implements Leashable {

    @Unique
    private Leashable.LeashData plantz$leashData;

    @Override
    @Nullable
    public Leashable.LeashData getLeashData() {
        return this.plantz$leashData;
    }

    @Override
    public void setLeashData(@Nullable Leashable.LeashData leashData) {
        this.plantz$leashData = leashData;
    }
}

