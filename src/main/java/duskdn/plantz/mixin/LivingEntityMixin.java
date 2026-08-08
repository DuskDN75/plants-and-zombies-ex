package duskdn.plantz.mixin;

import com.mojang.serialization.Codec;
import duskdn.plantz.effect.PaintedMobEffect;
import duskdn.plantz.entity.plant.init.PazPlant;
import duskdn.plantz.entity.projectile.init.PazProjectile;
import duskdn.plantz.init.PazEffects;
import duskdn.plantz.init.PazItems;
import duskdn.plantz.init.PazTags;
import duskdn.plantz.util.PazEntityData;
import duskdn.plantz.util.PlantHeadAttachment;
import kotlin.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static duskdn.plantz.init.PazDataSerializers.DATA_PAINT_COLORS;
import static duskdn.plantz.init.PazItems.DUCKY_TUBE_DAMAGE_INTERVAL;

@Mixin(LivingEntity.class)
abstract public class LivingEntityMixin implements PlantHeadAttachment, PazEntityData {

    @Unique
    private static final EntityDataAccessor<Boolean> DATA_HYPNO_ID = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<Boolean> DATA_CHILLED_ID = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<Boolean> DATA_DRENCHED_ID = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<Boolean> DATA_FROZEN_ID = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);
    @Unique
    private static final EntityDataAccessor<Map<Integer, Integer>> DATA_PAINTED_COLORS = SynchedEntityData.defineId(LivingEntity.class, DATA_PAINT_COLORS);

    @Shadow
    public abstract boolean hasEffect(Holder<MobEffect> effect);

    @Shadow
    public int swingTime;

    @Shadow
    public abstract @org.jspecify.annotations.Nullable MobEffectInstance getEffect(Holder<MobEffect> effect);

    @Unique
    private CompoundTag plantData = new CompoundTag();

    @Unique
    private @Nullable PazPlant plantEntity = null;

    @Override
    public @Nullable PazPlant plantz$getPlant() {
        return plantEntity;
    }

    @Override
    public void plantz$setPlant(@Nullable PazPlant value) {
        plantEntity = value;
    }

    @Override
    public @NotNull CompoundTag plantz$getPlantData() {
        return plantData;
    }

    @Override
    public void plantz$setPlantData(@NotNull CompoundTag value) {
        plantData = value;
    }

    @Override
    public boolean plantz$hasPlantOnHead() {
        return plantEntity != null &&  plantEntity.isAlive() && !plantEntity.isRemoved();
    }

    @Unique
    public boolean plantz$getHypnoId() {
        return ((Entity) (Object) this).getEntityData().get(DATA_HYPNO_ID);
    }
    @Unique
    public boolean plantz$getChilledId() {
        return ((Entity) (Object) this).getEntityData().get(DATA_CHILLED_ID);
    }
    @Unique
    public boolean plantz$getFrozenId() {
        return ((Entity) (Object) this).getEntityData().get(DATA_FROZEN_ID);
    }
    @Unique
    public boolean plantz$getDrenchedId() {
        return ((Entity) (Object) this).getEntityData().get(DATA_DRENCHED_ID);
    }
    @Unique
    public Map<Integer, Integer> plantz$getPaintedColors() {
        return ((Entity) (Object) this).getEntityData().get(DATA_PAINTED_COLORS);
    }

    @Unique
    private boolean prevFloatTag = false;
    @Unique
    private float prevWaterMalus = 0f;

    @Inject(method = "onEquipItem", at = @At("TAIL"))
    private void plantz$checkFloatTag(EquipmentSlot slot, ItemStack oldStack, ItemStack stack, CallbackInfo ci) {
        if ((LivingEntity) (Object) this instanceof PathfinderMob mob) {
            if (stack.is(PazItems.DUCKY_TUBE) && slot == EquipmentSlot.LEGS) {
                prevFloatTag = mob.getNavigation().canFloat();
                prevWaterMalus = mob.getPathfindingMalus(PathType.WATER);
                mob.getNavigation().setCanFloat(true);
            }
            else if (oldStack.is(PazItems.DUCKY_TUBE) && slot == EquipmentSlot.LEGS) {
                mob.getNavigation().setCanFloat(prevFloatTag);
                mob.setPathfindingMalus(PathType.WATER, 0.0F);
            }
        }
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void plantz$applyDuckyTubeBuoyancy(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        var item = entity.getItemBySlot(EquipmentSlot.LEGS);
        if (!item.is(PazItems.DUCKY_TUBE) && !entity.is(PazTags.EntityTypes.PLANTABLE_ON_WATER) && !entity.is(PazTags.EntityTypes.PLANTABLE_ON_LAVA) && !entity.fireImmune()) return;
        if (entity instanceof Player player && player.getAbilities().flying) return;
        var fluidType = entity.level().getBlockState(BlockPos.containing(entity.position().relative(Direction.UP, entity.getBbHeight()*.5))).getFluidState().getType();
        if (fluidType == Fluids.EMPTY ) return;

        //base
        double upwardForce = (fluidType == Fluids.LAVA && item.is(PazItems.DUCKY_TUBE)) ? 0.15 : 0.015;
        // submerged
        if (entity.isEyeInFluid(FluidTags.WATER)) upwardForce += 0.135;
        if (entity.isEyeInFluid(FluidTags.LAVA)) upwardForce += 0.15;
        // sneaking
        if (entity.isShiftKeyDown()) upwardForce *= fluidType == Fluids.LAVA? 0.0 : 0.2;

        entity.addDeltaMovement(new Vec3(0.0, upwardForce, 0.0));

        entity.fallDistance = 0.0F;

        if (!entity.level().isClientSide() && entity.tickCount % DUCKY_TUBE_DAMAGE_INTERVAL==0 && entity.getRandom().nextFloat() > 0.5f)
            item.hurtAndBreak(1, entity, EquipmentSlot.LEGS);
    }

    @Inject(method = "aiStep", at = @At("TAIL"))
    private void plantz$applyDuckyTubeSpeed(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;

        var item = entity.getItemBySlot(EquipmentSlot.LEGS);
        if (!item.is(PazItems.DUCKY_TUBE)) return;

        if (entity.isInLava()) {
            var speedMult = 1.8;

            var dm = entity.getDeltaMovement();

            entity.setDeltaMovement(
                    new Vec3(
                            dm.x*speedMult,
                            dm.y,
                            dm.z*speedMult
                    )
            );
        }
    }

    @Inject(method = "defineSynchedData", at = @At(value = "TAIL"))
    public void defineData(SynchedEntityData.Builder entityData, CallbackInfo ci) {
        entityData.define(DATA_HYPNO_ID, false);
        entityData.define(DATA_DRENCHED_ID, false);
        entityData.define(DATA_CHILLED_ID, false);
        entityData.define(DATA_FROZEN_ID, false);
        entityData.define(DATA_PAINTED_COLORS, new HashMap<>());
    }
    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void saveDataFlag(ValueOutput output, CallbackInfo ci) {
        var self = (LivingEntity) (Object) this;
        output.putBoolean("plantz:IsHypnotized", self.getEntityData().get(DATA_HYPNO_ID));
        output.putBoolean("plantz:IsChilled", self.getEntityData().get(DATA_CHILLED_ID));
        output.putBoolean("plantz:IsDrenched", self.getEntityData().get(DATA_DRENCHED_ID));
        output.putBoolean("plantz:IsFrozen", self.getEntityData().get(DATA_FROZEN_ID));
        output.store("plantz:PaintedColor", Codec.unboundedMap(Codec.INT, Codec.INT), self.getEntityData().get(DATA_PAINTED_COLORS));
        if (!this.plantz$getPlantData().isEmpty()) {
            output.store("plantz:AttachedPlant", CompoundTag.CODEC, this.plantz$getPlantData());
        }
    }
    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void loadDataFlag(ValueInput input, CallbackInfo ci) {
        var self = (LivingEntity) (Object) this;
        self.getEntityData().set(DATA_HYPNO_ID, input.getBooleanOr("plantz:IsHypnotized", false));
        self.getEntityData().set(DATA_CHILLED_ID, input.getBooleanOr("plantz:IsChilled", false));
        self.getEntityData().set(DATA_DRENCHED_ID, input.getBooleanOr("plantz:IsDrenched", false));
        self.getEntityData().set(DATA_FROZEN_ID, input.getBooleanOr("plantz:IsFrozen", false));
        self.getEntityData().set(DATA_PAINTED_COLORS, input.read("plantz:PaintedColor", Codec.unboundedMap(Codec.INT, Codec.INT)).orElseGet(HashMap::new));
        plantz$setPlantData(input.read("plantz:AttachedPlant", CompoundTag.CODEC).orElseGet(CompoundTag::new));
        if (self instanceof PathfinderMob mob) {
            prevFloatTag = mob.getNavigation().canFloat();
            prevWaterMalus = mob.getPathfindingMalus(PathType.WATER);
            if (mob.getItemBySlot(EquipmentSlot.LEGS).is(PazItems.DUCKY_TUBE)) {
                mob.getNavigation().setCanFloat(true);
                mob.setPathfindingMalus(PathType.WATER, 0.0F);
            }
        }
    }

    @Inject(method = "onEffectAdded", at = @At(value = "TAIL"))
    public void onHypnoAdded(MobEffectInstance effect, Entity source, CallbackInfo ci) {
        updateEffects();
    }
    @Inject(method = "onEffectsRemoved", at = @At(value = "TAIL"))
    public void onHypnoRemoved(Collection<MobEffectInstance> effects, CallbackInfo ci) {
        updateEffects();
    }
    @Unique
    public void updateEffects() {
        var self = (LivingEntity) (Object) this;
        self.getEntityData().set(DATA_HYPNO_ID, this.hasEffect(PazEffects.HYPNOTIZE));
        self.getEntityData().set(DATA_CHILLED_ID, this.hasEffect(PazEffects.CHILLED));
        self.getEntityData().set(DATA_DRENCHED_ID, this.hasEffect(PazEffects.DRENCHED));
        self.getEntityData().set(DATA_PAINTED_COLORS, PaintedMobEffect.getPaintColors(self));
    }

    @Inject(method = "canBeAffected", at = @At(value = "RETURN"), cancellable = true)
    public void immuneToHypnosis(MobEffectInstance newEffect, CallbackInfoReturnable<Boolean> cir) {
        if (newEffect.is(PazEffects.HYPNOTIZE)) {
            cir.setReturnValue(!((Entity) (Object) this).is(PazTags.EntityTypes.CANNOT_HYPNOTIZE));
        }
        if (newEffect.is(PazEffects.CHILLED)) {
            cir.setReturnValue(!((Entity) (Object) this).canFreeze());

            LivingEntity entity = (LivingEntity) (Object) this;

            entity.setRemainingFireTicks(0);
            entity.clearFire();
        }
        if (newEffect.is(PazEffects.DRENCHED)) {
            cir.setReturnValue(!((Entity) (Object) this).is(PazTags.EntityTypes.CANNOT_DRENCH));

            LivingEntity entity = (LivingEntity) (Object) this;

            entity.setRemainingFireTicks(0);
            entity.clearFire();
        }
        updateEffects();
    }
    @Inject(method = "canAttack", at = @At(value = "RETURN"), cancellable = true)
    public void stopTargetingFriendlies(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        if (this.hasEffect(PazEffects.HYPNOTIZE) && (target instanceof PazPlant || target instanceof Player || target.hasEffect(PazEffects.HYPNOTIZE))) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "hurtServer", at = @At("HEAD"), cancellable = true)
    public void ownerIgnorePlantAttacks(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        var self = (LivingEntity) (Object) this;
        var sourceEntity = source.getEntity();
        var directEntity = source.getDirectEntity();
        if (source.is(DamageTypes.LIGHTNING_BOLT)) self.addEffect(new MobEffectInstance(PazEffects.ELECTRIFIED, 300, 1));
        if (source.is(PazTags.DamageTypes.IS_ELECTRIC) && self.is(PazTags.EntityTypes.IMMUNE_TO_ELECTRICITY)) {
            cir.setReturnValue(false);
            cir.cancel();
        }
        if (sourceEntity instanceof PazPlant plant) {
            if (plant.hasSameOwner(self)) {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
        else if (directEntity instanceof PazPlant plant) {
            if (plant.hasSameOwner(self)) {
                cir.setReturnValue(false);
                cir.cancel();
            }
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void checkFire(CallbackInfo ci) {
        var self = (LivingEntity) (Object) this;

        if (self.isOnFire()) {
            self.removeEffect(PazEffects.CHILLED);

            if (self.hasEffect(PazEffects.DRENCHED)) {
                self.setRemainingFireTicks(0);
                self.clearFire();
            }
        }
    }

    int waterTime = -1;

    @Inject(method = "tick", at = @At(value = "TAIL"))
    private void plantz$addDuckyTube(CallbackInfo ci) {
        var self = (LivingEntity) (Object) this;

        boolean inFluid = self.isEyeInFluid(FluidTags.WATER) || self.isEyeInFluid(FluidTags.LAVA);

        if (inFluid && self.is(PazTags.EntityTypes.GETS_DUCKY_TUBE)) {
            waterTime++;
            if (waterTime>=250 && self.getItemBySlot(EquipmentSlot.LEGS).isEmpty()) {
                self.setItemSlot(EquipmentSlot.LEGS, PazItems.DUCKY_TUBE.getDefaultInstance());
            }
        } else waterTime = -1;
    }

    @Unique
    float leftoverDamage = 0;

    @Inject(method = "hurtServer", at = @At("HEAD"), cancellable = true)
    public void ignoreIfArmored(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {

        LivingEntity self = (LivingEntity)(Object)this;

        var armors = PazProjectile.Companion.checkForArmor(self);

        float damageMult = 1.0f;

        if (!(source.getDirectEntity() instanceof Projectile)) {
            damageMult = 3.0f;
        }

        if ((source.getDirectEntity() instanceof PazProjectile projectile)) {
            damageMult = projectile.getDamage();
        }

        float leftoverDamage = damage*damageMult;

        if (!armors.isEmpty()) {

            var accessor = ((LivingEntityAccessor)self);

            for (@NotNull Pair<@NotNull EquipmentSlot, @NotNull ItemStack> armor : armors) {

                if (leftoverDamage <= 0) {
                    break;
                }

                leftoverDamage = (float) PazProjectile.Companion.damageArmor(
                        self,
                        armor.getFirst(),
                        armor.getSecond(),
                        leftoverDamage
                );

                IO.println(leftoverDamage);

                accessor.invokeActuallyHurt(level, source, 0.001F);

            }

            accessor.invokeResolveMobResponsibleForDamage(source);
            accessor.invokeResolvePlayerResponsibleForDamage(source);
            ((LivingEntityAccessor)self).invokeActuallyHurt(level, source, leftoverDamage/damageMult);

            cir.setReturnValue(true);
            cir.cancel();
            return;

        }

    }

}