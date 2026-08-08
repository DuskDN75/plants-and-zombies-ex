package duskdn.plantz.mixin;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(Projectile.class)
public class ProjectileMixin {

    @Inject(method = "hitTargetOrDeflectSelf", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;deflection(Lnet/minecraft/world/entity/projectile/Projectile;)Lnet/minecraft/world/entity/projectile/ProjectileDeflection;"), cancellable = true)
    public void deflection(HitResult hitResult, CallbackInfoReturnable<ProjectileDeflection> cir) {
//        var type = hitResult.getType();
//        if (type == HitResult.Type.ENTITY) {
//            var entity = ((EntityHitResult) hitResult).getEntity();
//            var projectile = (Projectile) (Object) this;
//            if (entity instanceof LivingEntity livingEntity) {
//                for (EquipmentSlot slot : slots) {
//                    ItemStack item = livingEntity.getItemBySlot(slot);
//                    BlocksProjectileDamage component = item.getComponents().get(PazComponents.BLOCKS_PROJECTILE_DAMAGE);
//                    if (component==null) continue;
//                    if (component.getMustBeUsing() && !livingEntity.isUsingItem()) continue;
//
//                    var damage = 2.5f;
//
//                    if (projectile instanceof PazProjectile pazProj) {
//                        damage = pazProj.getDamage();
//                    }
//
//                    EquipmentSlotGroup validSlot = component.getSlot();
//                    boolean matchesSlot = validSlot.test(slot);
//                    if (!matchesSlot) continue;
//                    float breakChance = component.getBreakChance();
//                    if (entity.level() instanceof ServerLevel && item.getMaxDamage() - item.getDamageValue() <= 0) {
//                        item.shrink(1);
//                        projectile.playSound(SoundEvents.ITEM_BREAK.value());
//                    }
//                    else {
//                        SoundEvent hitSound;
//
//                        if (item.is(PazItems.NEWSPAPER)) hitSound = PazSounds.PROJECTILE_HIT_PAPER;
//                        else if (item.is(Items.BUCKET)) hitSound = PazSounds.PROJECTILE_HIT_BUCKET;
//                        else hitSound = PazSounds.PROJECTILE_HIT_CONE;
//
//                        projectile.playSound(hitSound);
//                        var deflection = ProjectileDeflection.REVERSE;
//                        deflection.deflect(projectile, entity, projectile.getRandom());
//                        cir.setReturnValue(deflection);
//
//                        item.hurtAndBreak((int) Math.ceil(damage), livingEntity, slot);
//                    }
//                }
//            }
//        }
    }

    @Unique
    Set<EquipmentSlot> slots = Set.of(EquipmentSlot.HEAD,EquipmentSlot.CHEST,EquipmentSlot.LEGS, EquipmentSlot.FEET, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
}
