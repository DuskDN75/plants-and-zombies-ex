import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public abstract class PazOwnableEntity extends LivingEntity implements net.minecraft.world.entity.OwnableEntity {
    public PazOwnableEntity(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }
}
