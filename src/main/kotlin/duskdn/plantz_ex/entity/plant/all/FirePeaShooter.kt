package duskdn.plantz_ex.entity.plant.all

import duskdn.plantz_ex.init.PazEntities
import duskdn.plantz_ex.init.PazServerParticles
import duskdn.plantz_ex.ai.goal.ProjectileAttackGoal
import duskdn.plantz_ex.entity.plant.init.AttackingPlant
import duskdn.plantz_ex.entity.plant.init.PazPlant
import duskdn.plantz_ex.entity.plant.interfaces.IWarmingPlant
import duskdn.plantz_ex.entity.plant.utils.fireSurvivalCheck
import duskdn.plantz_ex.entity.projectile.peas.PeaFire
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.monster.zombie.Zombie
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class FirePeaShooter(level: Level) : AttackingPlant(PazEntities.FIRE_PEA_SHOOTER, level), IWarmingPlant {
    override fun registerGoals() {
        super.registerGoals()

        this.goalSelector.addGoal(
            2, ProjectileAttackGoal(
                usingEntity = this,
                projectileFactory = { PeaFire(level(), this) },
                cooldownTime = 20,
                actionDelay = 3
            )
        )
    }

    override fun getLightLevel(): Int {
        return if (isAsleep) 8 else 15
    }

    override fun getMeltRadius(): Double = 3.0

    override fun getMeltChance(): Double = 0.2

    override fun tick() {
        super.tick()

        if (tickCount % 3 == 0 && tickCount > 18 && isAlive) {

            val direction = calculateUpVector(this.xRot - 50, this.yHeadRot).scale(0.3)
            this.level().addParticle(
                PazServerParticles.EMBER,
                direction.x.toFloat() + this.getRandomX(0.2),
                direction.y.toFloat() + this.y + eyeHeight.toDouble() - 0.1,
                direction.z.toFloat() + this.getRandomZ(0.2),
                0.0, 0.0, 0.0,
            )

        }

        if (!level().isClientSide) {
            meltSnowAround()
        }
    }

    override fun canSurviveOn(block: BlockState): Boolean {
        return super.canSurviveOn(block) || fireSurvivalCheck(block)
    }

}
