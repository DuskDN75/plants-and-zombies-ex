package duskdn.plantz.entity.plant

import duskdn.plantz.init.PazEntities
import duskdn.plantz.init.PazTags.BlockTags.PLANTABLE
import duskdn.plantz.ai.goal.ProjectileAttackGoal
import duskdn.plantz.entity.plant.init.AttackingPlant
import duskdn.plantz.entity.projectile.peas.PeaIce
import net.minecraft.core.BlockPos
import net.minecraft.tags.BlockTags
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.state.BlockState

class IcePeaShooter(type: EntityType<out AttackingPlant>, level: Level) : AttackingPlant(PazEntities.ICE_PEA_SHOOTER, level) {

    companion object {
        fun checkIcePeaShooterSpawnRules(
            type: EntityType<out AttackingPlant>,
            level: LevelAccessor,
            spawnReason: EntitySpawnReason,
            pos: BlockPos,
            random: RandomSource
        ): Boolean {
            val blockBelow = level.getBlockState(pos.below())
            return checkValidSpawn(level, pos, spawnReason)
                    && (blockBelow.`is`(PLANTABLE) || blockBelow.`is`(BlockTags.SNOW))
        }
    }

    override fun registerGoals() {
        super.registerGoals()

        this.goalSelector.addGoal(2, ProjectileAttackGoal(
            usingEntity = this,
            projectileFactory =  { PeaIce(level(), this) },
            cooldownTime = 20,
            actionDelay = 3))
    }

    override fun canSurviveOn(block: BlockState): Boolean {
        return super.canSurviveOn(block) || block.`is`(BlockTags.SNOW)
    }
}
