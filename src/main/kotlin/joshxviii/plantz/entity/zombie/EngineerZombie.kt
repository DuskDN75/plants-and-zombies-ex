package joshxviii.plantz.entity.zombie

import joshxviii.plantz.PazSounds
import joshxviii.plantz.PazTags
import joshxviii.plantz.ai.goal.MineBlocksToTargetGoal
import joshxviii.plantz.ai.pathfinding.MinerNavigation
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.util.RandomSource
import net.minecraft.world.Difficulty
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.navigation.PathNavigation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.level.ServerLevelAccessor


class EngineerZombie(type: EntityType<out EngineerZombie>, level: Level) : PazZombie(type, level) {

    override fun registerGoals() {
        super.registerGoals()
    }

    override fun getAmbientSound(): SoundEvent {
        return PazSounds.DIGGER_ZOMBIE_AMBIENT
    }
    override fun getHurtSound(source: DamageSource): SoundEvent {
        return PazSounds.DIGGER_ZOMBIE_HURT
    }
    override fun getDeathSound(): SoundEvent {
        return PazSounds.DIGGER_ZOMBIE_DEATH
    }
    override fun getStepSound(): SoundEvent {
        return SoundEvents.ZOMBIE_STEP
    }

    override fun doHurtTarget(level: ServerLevel, target: Entity): Boolean {
        val result = super.doHurtTarget(level, target)
        return result
    }

    override fun finalizeSpawn(
        level: ServerLevelAccessor,
        difficulty: DifficultyInstance,
        spawnReason: EntitySpawnReason,
        groupData: SpawnGroupData?
    ): SpawnGroupData? {
        val data = super.finalizeSpawn(level, difficulty, spawnReason, ZombieGroupData(false, false))

        setCanBreakDoors(true)

        return data
    }
}