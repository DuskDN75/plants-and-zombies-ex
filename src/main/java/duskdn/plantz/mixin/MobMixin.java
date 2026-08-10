package duskdn.plantz.mixin;

import duskdn.plantz.entity.projectile.init.PazProjectile;
import duskdn.plantz.init.PazBlocks;
import duskdn.plantz.init.PazItems;
import duskdn.plantz.init.PazSounds;
import kotlin.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Josh
 */
@Mixin(Mob.class)
public class MobMixin {

    @Unique
    private boolean spawnedWave = false;

    @Unique
    private int waveDelayTicks = 0;

    @Unique
    private void spawnReinforcement(final Mob entity, final ServerLevel level, final LivingEntity target) {
        if (target != null
                && level.isSpawningMonsters()) {
            int x = Mth.floor(entity.getX());
            int z = Mth.floor(entity.getZ());
            EntityType<? extends Mob> type = (EntityType<? extends Mob>) entity.getType();
            Mob reinforcement = type.create(level, EntitySpawnReason.REINFORCEMENT);
            if (reinforcement == null) {
                return;
            }

            for (int i = 0; i < 50; i++) {
                int xt = x + Mth.nextInt(entity.getRandom(), 6, 10) * Mth.nextInt(entity.getRandom(), -1, 1);
                int zt = z + Mth.nextInt(entity.getRandom(), 6, 10) * Mth.nextInt(entity.getRandom(), -1, 1);
                int yt = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, xt, zt);
                BlockPos spawnPos = new BlockPos(xt, yt, zt);

                boolean spawnRulesAllow = SpawnPlacements.checkSpawnRules(type, level, EntitySpawnReason.REINFORCEMENT, spawnPos, level.getRandom());

                IO.println("SPAWN RULES ALLOW?: "+spawnRulesAllow+" SPAWN POS IS: "+spawnPos);

                reinforcement.setPos(xt, yt, zt);

                boolean extraChecks = !level.hasNearbyAlivePlayer(xt, yt, zt, 7.0)
                        && level.isUnobstructed(reinforcement)
                        && level.noCollision(reinforcement)
                        && ((reinforcement instanceof ZombieAccessor zombie) && zombie.invokeCanSpawnInLiquids()) || !level.containsAnyLiquid(reinforcement.getBoundingBox());

                if (extraChecks) {
                    reinforcement.setTarget(target);
                    reinforcement.finalizeSpawn(level, level.getCurrentDifficultyAt(reinforcement.blockPosition()), EntitySpawnReason.REINFORCEMENT, null);
                    level.addFreshEntityWithPassengers(reinforcement);

                    var item = entity.getItemBySlot(EquipmentSlot.LEGS);

                    var headItem = entity.getItemBySlot(EquipmentSlot.HEAD);

                    var offhandItem = entity.getItemBySlot(EquipmentSlot.OFFHAND);

                    var mainhandItem = entity.getItemBySlot(EquipmentSlot.MAINHAND);

                    if (item.is(PazItems.DUCKY_TUBE)) reinforcement.setItemSlot(EquipmentSlot.LEGS, item);

                    if (headItem.is(PazBlocks.CONE.asItem()) || headItem.is(Items.BUCKET)) reinforcement.setItemSlot(EquipmentSlot.HEAD, item);

                    if (offhandItem.is(PazBlocks.BRAINZ_FLAG.asItem()) && mainhandItem.is(PazBlocks.BRAINZ_FLAG.asItem())) reinforcement.setItemSlot(EquipmentSlot.MAINHAND, offhandItem);

                    break;
                }

//                if (spawnRulesAllow) {
//                    reinforcement.setPos(xt, yt, zt);
//
//                    boolean extraChecks = !level.hasNearbyAlivePlayer(xt, yt, zt, 7.0)
//                            && level.isUnobstructed(reinforcement)
//                            && level.noCollision(reinforcement)
//                            && (((ZombieAccessor) reinforcement).invokeCanSpawnInLiquids() || !level.containsAnyLiquid(reinforcement.getBoundingBox()));
//
//                    if (extraChecks) {
//                        reinforcement.setTarget(target);
//                        reinforcement.finalizeSpawn(level, level.getCurrentDifficultyAt(reinforcement.blockPosition()), EntitySpawnReason.REINFORCEMENT, null);
//                        level.addFreshEntityWithPassengers(reinforcement);
//                        break;
//                    }
//                }
            }
        }

    }

    @Unique
    private void spawnWave(Mob entity) {

        if (!(entity.level() instanceof ServerLevel level)) return;

        LivingEntity target = entity.getTarget();

        Difficulty difficulty = level.getDifficulty();

        int minimumCount;
        int maximumCount;

        switch (difficulty) {
            case EASY -> {
                minimumCount = 2;
                maximumCount = 6;
            }
            case NORMAL -> {
                minimumCount = 3;
                maximumCount = 10;
            }
            case HARD -> {
                minimumCount = 6;
                maximumCount = 15;
            }
            default -> {
                minimumCount = 0;
                maximumCount = 1;
            }
        }

        int count = entity.getRandom().nextInt(minimumCount,maximumCount);

        IO.println("Count is: "+count);

        for (int i = 0; i < count; i++) {

            IO.println("SPAWNING REINFORCEMENT: "+i);

            spawnReinforcement(entity, level, target);

        }

    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void waitForWave(CallbackInfo ci) {
        Mob entity = (Mob) (Object) this;

        if (spawnedWave && waveDelayTicks > 0) {

            waveDelayTicks--;

            if (waveDelayTicks <= 0 && entity.getTarget() != null) {

                spawnWave(entity);

                Level level = entity.level();

                if (level.isClientSide()) {

                    level.playLocalSound(entity.blockPosition(), PazSounds.WAVE_START, SoundSource.HOSTILE, 1.0f, 1.0f, false);

                }

            }

        }

    }

    @Inject(method = "setTarget", at = @At("HEAD"))
    public void spawnWave(LivingEntity target, CallbackInfo ci) {

        Mob entity = (Mob) (Object) this;

        if (!spawnedWave) {

            if (entity.getItemBySlot(EquipmentSlot.MAINHAND).is(PazBlocks.BRAINZ_FLAG.asItem()) ) {

                IO.println("WAVE TRIGGERED!");

                waveDelayTicks = 80;

                spawnedWave = true;

                Level level = entity.level();

                if (level.isClientSide()) {

                    level.playLocalSound(entity.blockPosition(), PazSounds.WAVE_INCOMING, SoundSource.HOSTILE, 1.0f, 1.0f, false);

                }

            }

        }

    }

    @Inject(method = "finalizeSpawn", at = @At("HEAD"), cancellable = true)
    public void addArmor(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData, CallbackInfoReturnable<SpawnGroupData> cir) {

        LivingEntity self = (LivingEntity)(Object)this;

        if (self instanceof ZombifiedPiglin zombie) {

            if (zombie.getItemBySlot(EquipmentSlot.HEAD).isEmpty() && spawnReason != EntitySpawnReason.COMMAND && spawnReason != EntitySpawnReason.SPAWN_ITEM_USE){
                if (zombie.getRandom().nextFloat() < 0.25) {
                    zombie.setItemSlot(EquipmentSlot.HEAD, PazBlocks.CONE.asItem().getDefaultInstance());
                    zombie.setDropChance(EquipmentSlot.HEAD, 0.2f);
                }
                else if (zombie.getRandom().nextFloat() < 0.1 && zombie.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
                    zombie.setItemSlot(EquipmentSlot.HEAD, Items.BUCKET.getDefaultInstance());
                }
                else if (zombie.getRandom().nextFloat() < 0.1 && zombie.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
                    zombie.setItemSlot(EquipmentSlot.MAINHAND, PazBlocks.BRAINZ_FLAG.asItem().getDefaultInstance());
                }
            }

        }

    }
}
