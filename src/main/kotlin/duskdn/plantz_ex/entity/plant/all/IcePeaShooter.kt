package duskdn.plantz_ex.entity.plant.all

import duskdn.plantz_ex.init.PazEntities
import duskdn.plantz_ex.ai.goal.ProjectileAttackGoal
import duskdn.plantz_ex.entity.plant.init.AttackingPlant
import duskdn.plantz_ex.entity.plant.utils.snowSurvivalCheck
import duskdn.plantz_ex.entity.projectile.peas.PeaIce
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
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
