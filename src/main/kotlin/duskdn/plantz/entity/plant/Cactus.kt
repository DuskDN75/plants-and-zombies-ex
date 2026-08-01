package duskdn.plantz.entity.plant

import duskdn.plantz.ai.goal.ProjectileAttackGoal
import duskdn.plantz.entity.plant.init.AttackingPlant
import duskdn.plantz.entity.projectile.Needle
import duskdn.plantz.entity.zombie.BalloonZombie
import duskdn.plantz.init.PazEntities
import duskdn.plantz.init.PazTags
import duskdn.plantz.init.PazTags.BlockTags.PLANTABLE
import net.minecraft.core.BlockPos
import net.minecraft.tags.BlockTags
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
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

    lateinit var attackGoal: CactusAttackGoal

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

        override fun doAction(): Boolean {

            if (usingEntity.target is BalloonZombie && (usingEntity.target as BalloonZombie).hasBalloon) {

                val target: Entity = ((usingEntity.target?: return false) as BalloonZombie).balloon as Entity

                if (!target.isAlive) return false

                val result = fire(target)

                return result

            }

            return super.doAction()
        }

    }
}