package duskdn.plantz_ex.entity.plant.all.mushrooms

import duskdn.plantz_ex.init.PazEntities
import duskdn.plantz_ex.ai.goal.ProjectileAttackGoal
import duskdn.plantz_ex.entity.plant.init.AttackingPlant
import duskdn.plantz_ex.entity.plant.init.PazPlant
import duskdn.plantz_ex.entity.plant.interfaces.IAquaticPlant
import duskdn.plantz_ex.entity.plant.utils.mushroomSurvivalCheck
import duskdn.plantz_ex.entity.plant.utils.waterSurvivalCheck
import duskdn.plantz_ex.entity.projectile.WaterSpore
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.monster.zombie.Zombie
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class SeaShroom(level: Level) : AttackingPlant(PazEntities.SEA_SHROOM, level), IAquaticPlant {

    override fun isPushedByFluid(): Boolean {
        return false
    }

    override fun registerGoals() {
        super.registerGoals()

        this.goalSelector.addGoal(
            2, ProjectileAttackGoal(
                usingEntity = this,
                projectileFactory = { WaterSpore(level(), this) },
                cooldownTime = 20
            )
        )
    }

    override fun aiStep() {
        applyBuoyancy()

        super.aiStep()
    }

    override fun canSurviveOn(block: BlockState): Boolean {
        return super.canSurviveOn(block) || mushroomSurvivalCheck(block) || waterSurvivalCheck(block)
    }
}