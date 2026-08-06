package duskdn.plantz.entity.plant.all

import duskdn.plantz.init.PazEntities
import duskdn.plantz.init.PazTags.BlockTags.PLANTABLE
import duskdn.plantz.ai.goal.ProjectileAttackGoal
import duskdn.plantz.entity.plant.init.AttackingPlant
import duskdn.plantz.entity.plant.utils.snowSurvivalCheck
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

    override fun registerGoals() {
        super.registerGoals()

        this.goalSelector.addGoal(2, ProjectileAttackGoal(
            usingEntity = this,
            projectileFactory =  { PeaIce(level(), this) },
            cooldownTime = 20,
            actionDelay = 3))
    }

    override fun canSurviveOn(block: BlockState): Boolean {
        return super.canSurviveOn(block) || snowSurvivalCheck(block)
    }
}
