package duskdn.plantz_ex.mixin;

import duskdn.plantz_ex.init.PazBlocks;
import duskdn.plantz_ex.init.PazItems;
import duskdn.plantz_ex.init.PazSounds;
import duskdn.plantz_ex.init.PazTags;
import duskdn.plantz_ex.util.MobHatWeights;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static duskdn.plantz_ex.util.DebugUtilsKt.debugPrint;
import static java.lang.Math.max;

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
                int yt = (level.canSeeSky(entity.blockPosition())) ? level.getHeight(Heightmap.Types.WORLD_SURFACE, xt, zt) : level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, xt, zt);
                BlockPos spawnPos = new BlockPos(xt, yt, zt);

                boolean spawnRulesAllow = SpawnPlacements.checkSpawnRules(type, level, EntitySpawnReason.REINFORCEMENT, spawnPos, level.getRandom());

                debugPrint("SPAWN RULES ALLOW?: "+spawnRulesAllow+" SPAWN POS IS: "+spawnPos);

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

        debugPrint("Count is: "+count);

        for (int i = 0; i < count; i++) {

            debugPrint("SPAWNING REINFORCEMENT: "+i);

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

                debugPrint("WAVE TRIGGERED!");

                waveDelayTicks = 80;

                spawnedWave = true;

                Level level = entity.level();

                if (level.isClientSide()) {

                    level.playLocalSound(entity.blockPosition(), PazSounds.WAVE_INCOMING, SoundSource.HOSTILE, 1.0f, 1.0f, false);

                }

            }

        }

    }

    @Inject(method = "finalizeSpawn", at = @At("TAIL"))
    public void plantzex$addArmor(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData, CallbackInfoReturnable<SpawnGroupData> cir) {

        if (spawnReason == EntitySpawnReason.COMMAND || spawnReason == EntitySpawnReason.SPAWN_ITEM_USE || spawnReason == EntitySpawnReason.MOB_SUMMONED) return;

        Mob self = (Mob) (Object)this;

        if (!(self.level() instanceof ServerLevel serverLevel)) return;

        boolean getsArmor = self.is(PazTags.EntityTypes.GETS_ARMOR);

        if (getsArmor) {

            var headSlot = self.getItemBySlot(EquipmentSlot.HEAD);

            var mainHandSlot = self.getItemBySlot(EquipmentSlot.MAINHAND);

            var shouldGiveFlag = false;

            double screenDoorChance = 0.05;

            MobHatWeights.getRandomizer().getRandom(self.getRandom());

            if (self.getRandom().nextFloat() < 0.25 && headSlot.isEmpty()) {
                self.setItemSlot(EquipmentSlot.HEAD, PazBlocks.CONE.asItem().getDefaultInstance());
                self.setDropChance(EquipmentSlot.HEAD, 0.2f);
            }
            else if (self.getRandom().nextFloat() < 0.1 && headSlot.isEmpty()) {
                self.setItemSlot(EquipmentSlot.HEAD, Items.BUCKET.getDefaultInstance());
                self.setDropChance(EquipmentSlot.HEAD, 0.1f);
            }
            else if (self.getRandom().nextFloat() < 0.1 && self.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
                self.setItemSlot(EquipmentSlot.MAINHAND, PazBlocks.BRAINZ_FLAG.asItem().getDefaultInstance());
                self.setDropChance(EquipmentSlot.MAINHAND, 0.01f);
            }

            if (headSlot.isEmpty()){
                if (self.getRandom().nextFloat() < 0.25) {
                    self.setItemSlot(EquipmentSlot.HEAD, PazBlocks.CONE.asItem().getDefaultInstance());
                    self.setDropChance(EquipmentSlot.HEAD, 0.2f);
                }
                else if (self.getRandom().nextFloat() < 0.1 && self.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
                    self.setItemSlot(EquipmentSlot.HEAD, Items.BUCKET.getDefaultInstance());
                    self.setDropChance(EquipmentSlot.HEAD, 0.1f);
                }
                else if (self.getRandom().nextFloat() < 0.1 && self.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
                    self.setItemSlot(EquipmentSlot.MAINHAND, PazBlocks.BRAINZ_FLAG.asItem().getDefaultInstance());
                    self.setDropChance(EquipmentSlot.MAINHAND, 0.01f);
                }
            }

            if (mainHandSlot.isEmpty()) {

                if (self.getRandom().nextFloat() < screenDoorChance) {
                    setItemSlot(EquipmentSlot.MAINHAND, PazBlocks.SCREEN_DOOR.asItem().defaultInstance)
                    setDropChance(EquipmentSlot.MAINHAND, 0.1f)
                }
            }

        }

    }

    @Unique
    int waterTime = -1;

    @Inject(method = "tick", at = @At(value = "TAIL"))
    private void plantzex$addDuckyTube(CallbackInfo ci) {
        var self = (Mob) (Object) this;

        if (!(self.level() instanceof ServerLevel level)) return;

        boolean inWater = self.isEyeInFluid(FluidTags.WATER);
        boolean inLava = self.isEyeInFluid(FluidTags.LAVA);

        boolean getsDuckyTube = self.is(PazTags.EntityTypes.GETS_DUCKY_TUBE);

        if ((inWater || inLava) && getsDuckyTube) {
            waterTime++;
            if (waterTime>=250) {

                debugPrint("WATER TIME IS "+waterTime);

                var currentLegs = self.getItemBySlot(EquipmentSlot.LEGS);
//                if (!currentLegs.isEmpty() && ((double) max(self.getRandom().nextFloat() - 0.1f, 0.0f)) < dropChance) {
//                    if (level!=null) self.spawnAtLocation(level, currentLegs);
//                }

                if (currentLegs.isEmpty()) {
                    self.setItemSlot(EquipmentSlot.LEGS, (inWater ? PazItems.DUCKY_TUBE : PazItems.OBSIDIAN_DUCKY_TUBE).getDefaultInstance());
                    self.setDropChance(EquipmentSlot.LEGS, 0.2f);
                }

            }
        } else waterTime = -1;
    }

}
