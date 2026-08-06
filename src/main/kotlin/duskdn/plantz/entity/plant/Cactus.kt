package duskdn.plantz.entity.plant

import duskdn.plantz.ai.goal.ProjectileAttackGoal
import duskdn.plantz.entity.Balloon
import duskdn.plantz.entity.plant.init.AttackingPlant
import duskdn.plantz.entity.plant.init.PazPlant
import duskdn.plantz.entity.projectile.Needle
import duskdn.plantz.entity.zombie.BalloonZombie
import duskdn.plantz.init.PazEntities
import duskdn.plantz.init.PazTags
import duskdn.plantz.init.PazTags.BlockTags.PLANTABLE
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.tags.BlockTags
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.monster.zombie.Zombie
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState

class Cactus(type: EntityType<out AttackingPlant>, level: Level) : AttackingPlant(PazEntities.CACTUS, level) {

    lateinit var attackGoal: CactusAttackGoal

    override fun registerAttackGoal() {

        this.targetSelector.addGoal(4, NearestAttackableTargetGoal(this, LivingEntity::class.java, 5, mustSeeTarget(), false) { target, level ->

            (enemyCheck(target) && !(target is BalloonZombie && target.balloons.isNotEmpty()))

        })
    }

    override fun registerGoals() {
        super.registerGoals()

        attackGoal = CactusAttackGoal(this)

        this.goalSelector.addGoal(2, attackGoal)
    }

    override fun canSurviveOn(block: BlockState): Boolean {
        return super.canSurviveOn(block) || block.`is`(BlockTags.SAND) || block.`is`(Blocks.SOUL_SAND) || block.`is`(Blocks.CACTUS)
    }

    class CactusAttackGoal(
        val cactusEntity: Cactus,
    ) : ProjectileAttackGoal(
        usingEntity = cactusEntity,
        projectileFactory = { Needle(cactusEntity.level(), cactusEntity) },
        velocity = 1.5,
        cooldownTime = 60,
        actionDelay = 6,
        attackRadius = 45.0f
    ) {

        override fun canDoAction(): Boolean {// check distance and line of sight
            val target = usingEntity.target ?: return false
            if (!target.isAlive) return false

            if (target.`is`(PazTags.EntityTypes.FLYING_ENEMY)) {

                attackRadius = 80.0f

                return true

            }

            attackRadius = 30.0f

            return super.canDoAction()
        }

        fun tryTargetBalloon(target: BalloonZombie) {

            if (!target.isAlive || target.balloons.isEmpty()) return

            val balloon: Balloon = target.balloons.first()

            if (!balloon.isAlive) return

            usingEntity.target = balloon

        }

        override fun doAction(): Boolean {

            if (usingEntity.target is BalloonZombie) {

                tryTargetBalloon(usingEntity.target as BalloonZombie)

            }

            return super.doAction()
        }

    }
}