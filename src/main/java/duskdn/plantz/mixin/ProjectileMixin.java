package duskdn.plantz.mixin;

import duskdn.plantz.entity.projectile.init.PazProjectile;
import duskdn.plantz.entity.utils.ArmorUtil;
import duskdn.plantz.init.PazComponents;
import duskdn.plantz.init.PazDamageTypes;
import duskdn.plantz.item.component.BlocksProjectileDamage;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
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
        var type = hitResult.getType();
        if (type == HitResult.Type.ENTITY) {
            var entity = ((EntityHitResult) hitResult).getEntity();;

            if (!(entity.level() instanceof ServerLevel serverLevel)) return;

            var projectile = (Projectile) (Object) this;
            if (entity instanceof LivingEntity livingEntity) {

                var armors = ArmorUtil.checkForArmor(livingEntity);

                armors.removeIf(
                        armor -> !armor.getSecond().getSecond().getReflectsDamage()
                );

                IO.println("ARMORS IS: "+armors);

                if (armors.isEmpty()) return;

                DamageSource source = projectile.damageSources().source(
                        PazDamageTypes.PLANT,
                        projectile,
                        projectile.getOwner()
                );

                var damage = (float) ((projectile.getOwner() instanceof LivingEntity projOwner) ? projOwner.getAttributes().getValue(Attributes.ATTACK_DAMAGE) : 2.5);

                if (projectile instanceof PazProjectile pazProj) {
                    damage = pazProj.getDamage();
                }

                ArmorUtil.goThroughArmorsAndDamage(serverLevel, source, livingEntity, damage, armors, false);

                var deflection = ProjectileDeflection.REVERSE;
                deflection.deflect(projectile, entity, projectile.getRandom());
                cir.setReturnValue(deflection);
                cir.cancel();
            }
        }
    }
}
