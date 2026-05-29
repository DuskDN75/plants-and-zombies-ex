import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public abstract class OwnableEntity extends LivingEntity implements net.minecraft.world.entity.OwnableEntity {
    public OwnableEntity(EntityType<LivingEntity> type, Level level) {
        super(type, level);
    }
}
