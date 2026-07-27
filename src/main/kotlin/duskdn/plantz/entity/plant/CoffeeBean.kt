package duskdn.plantz.entity.plant

import duskdn.plantz.init.PazEntities
import duskdn.plantz.init.PazTags.BlockTags.PLANTABLE
import duskdn.plantz.ai.goal.WakeUpSleepingPlantsGoal
import duskdn.plantz.entity.plant.init.PazPlant
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.DustParticleOptions
import net.minecraft.tags.BlockTags
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.state.BlockState

class CoffeeBean(type: EntityType<out PazPlant>, level: Level) : PazPlant(PazEntities.COFFEE_BEAN, level) {

    companion object {
        fun checkCoffeeBeanSpawnRules(
            type: EntityType<out PazPlant>,
            level: LevelAccessor,
            spawnReason: EntitySpawnReason,
            pos: BlockPos,
            random: RandomSource
        ): Boolean {
            val blockBelow = level.getBlockState(pos.below())
            return checkValidSpawn(level, pos, spawnReason)
                    && (blockBelow.`is`(PLANTABLE) || !blockBelow.`is`(BlockTags.AIR))
        }
    }

    override fun registerGoals() {
        super.registerGoals()
        this.goalSelector.addGoal(1, WakeUpSleepingPlantsGoal(
            this,
            actionDelay = 9,
            actionSuccessEffect = {
                addParticlesAroundSelf(
                    level(),
                    DustParticleOptions(8606770, 1f),
                    amount = 12..14,
                    horizontalSpreadScale = 0.1,
                    verticalSpreadScale = 0.3,
                    height = 0.25f,
                    speed = 0.02
                )
                discard()
            }
        ))
    }
    override fun attackGoals() {}

    override fun canSurviveOn(block: BlockState): Boolean {
        return super.canSurviveOn(block) || !block.getCollisionShape(level(), blockPosition().below()).isEmpty
    }
}