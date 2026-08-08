package duskdn.plantz.mixin;

import duskdn.plantz.init.PazBlocks;
import duskdn.plantz.init.PazSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
    private static final Identifier REINFORCEMENT_CALLER_CHARGE_ID = Identifier.withDefaultNamespace("reinforcement_caller_charge");

    @Unique
    private static final AttributeModifier ZOMBIE_REINFORCEMENT_CALLEE_CHARGE = new AttributeModifier(
            Identifier.withDefaultNamespace("reinforcement_callee_charge"), -0.05F, AttributeModifier.Operation.ADD_VALUE
    );

    @Unique
    private void spawnReinforcement(final Zombie entity, final ServerLevel level, final LivingEntity target) {
        if (target != null
                && level.isSpawningMonsters()) {
            int x = Mth.floor(entity.getX());
            int z = Mth.floor(entity.getZ());
            EntityType<? extends Zombie> type = entity.getType();
            Zombie reinforcement = type.create(level, EntitySpawnReason.REINFORCEMENT);
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
                        && (((ZombieAccessor) reinforcement).invokeCanSpawnInLiquids() || !level.containsAnyLiquid(reinforcement.getBoundingBox()));

                if (extraChecks) {
                    reinforcement.setTarget(target);
                    reinforcement.finalizeSpawn(level, level.getCurrentDifficultyAt(reinforcement.blockPosition()), EntitySpawnReason.REINFORCEMENT, null);
                    level.addFreshEntityWithPassengers(reinforcement);

                    IO.println("SPAWNED ZOMBIE!!!");

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
    private void spawnWave(Zombie zombie) {

        if (!(zombie.level() instanceof ServerLevel level)) return;

        LivingEntity target = zombie.getTarget();

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

        int count = zombie.getRandom().nextInt(minimumCount,maximumCount);

        IO.println("Count is: "+count);

        for (int i = 0; i < count; i++) {

            IO.println("SPAWNING REINFORCEMENT: "+i);

            spawnReinforcement(zombie, level, target);

        }

    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void waitForWave(CallbackInfo ci) {
        Mob entity = (Mob) (Object) this;

        if (entity instanceof Zombie zombie) {

//            IO.println("WAVE DELAY TICKS: "+waveDelayTicks);

            if (spawnedWave && waveDelayTicks > 0) {

                waveDelayTicks--;

                if (waveDelayTicks <= 0 && zombie.getTarget() != null) {

                    spawnWave(zombie);

                    Level level = zombie.level();

                    if (level.isClientSide()) {

                        level.playLocalSound(zombie.blockPosition(), PazSounds.WAVE_START, SoundSource.HOSTILE, 1.0f, 1.0f, false);

                    }

                }

            }

        }

    }

    @Inject(method = "setTarget", at = @At("HEAD"))
    public void spawnWave(LivingEntity target, CallbackInfo ci) {

        Mob entity = (Mob) (Object) this;

        if (!spawnedWave) {

            if (entity.getItemBySlot(EquipmentSlot.MAINHAND).is(PazBlocks.BRAINZ_FLAG.asItem()) && entity instanceof Zombie zombie ) {

                IO.println("WAVE TRIGGERED!");

                waveDelayTicks = 80;

                spawnedWave = true;

                Level level = zombie.level();

                if (level.isClientSide()) {

                    level.playLocalSound(zombie.blockPosition(), PazSounds.WAVE_INCOMING, SoundSource.HOSTILE, 1.0f, 1.0f, false);

                }

            }

        }

    }
}
