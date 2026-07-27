package duskdn.plantz.entity.plant

import duskdn.plantz.init.PazEntities
import duskdn.plantz.init.PazTags.BlockTags.PLANTABLE
import duskdn.plantz.ai.goal.ProjectileAttackGoal
import duskdn.plantz.entity.plant.init.AttackingPlant
import duskdn.plantz.entity.projectile.Needle
import net.minecraft.core.BlockPos
import net.minecraft.tags.BlockTags
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState

class Cactus(type: EntityType<out AttackingPlant>, level: Level) : AttackingPlant(PazEntities.CACTUS, level) {

    companion object {
        fun checkCactusSpawnRules(
            type: EntityType<out AttackingPlant>,
            level: LevelAccessor,
            spawnReason: EntitySpawnReason,
            pos: BlockPos,
            random: RandomSource
        ): Boolean {
            val blockBelow = level.getBlockState(pos.below())
            return checkValidSpawn(level, pos, spawnReason)
                    && (blockBelow.`is`(PLANTABLE) || blockBelow.`is`(BlockTags.SAND) || blockBelow.`is`(Blocks.SOUL_SAND))
        }
    }

    override fun registerGoals() {
        super.registerGoals()

        this.goalSelector.addGoal(2, ProjectileAttackGoal(
            usingEntity = this,
            projectileFactory = { Needle(level(), this) },
            velocity = 1.5,
            cooldownTime = 25,
            actionDelay = 6))
    }

    override fun canSurviveOn(block: BlockState): Boolean {
        return super.canSurviveOn(block) || block.`is`(BlockTags.SAND) || block.`is`(Blocks.SOUL_SAND) || block.`is`(Blocks.CACTUS)
    }
}