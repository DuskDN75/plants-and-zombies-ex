package duskdn.plantz_ex.entity.plant.all

import duskdn.plantz_ex.init.NukeBlastParticleOptions
import duskdn.plantz_ex.init.NukeSmokeParticleOptions
import duskdn.plantz_ex.init.NukeWaveParticleOptions
import duskdn.plantz_ex.init.PazEntities
import duskdn.plantz_ex.ai.goal.ExplodeGoal
import duskdn.plantz_ex.entity.plant.init.ExplosivePlant
import duskdn.plantz_ex.entity.plant.interfaces.IWarmingPlant
import duskdn.plantz_ex.entity.plant.utils.fireSurvivalCheck
import duskdn.plantz_ex.entity.plant.utils.mushroomSurvivalCheck
import duskdn.plantz_ex.entity.plant.utils.sandSurvivalCheck
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class CherryBomb(level: Level) : ExplosivePlant(PazEntities.CHERRY_BOMB, level), IWarmingPlant {

    override fun registerGoals() {
        super.registerGoals()
        this.goalSelector.addGoal(1, ExplodeGoal(
            usingEntity = this,
            actionEndEffect = {
                addParticlesAroundSelf(
                    particle = ParticleTypes.LARGE_SMOKE,
                    amount = 20..24,
                    speed = 0.02,
                )
                val level = level() as? ServerLevel ?: return@ExplodeGoal
                level.sendParticles(NukeWaveParticleOptions(color = 0xD0370D, scale = 2f),
                    x, y, z, 1, 0.0, 0.0, 0.0, 0.0
                )
                level.sendParticles(NukeBlastParticleOptions(color = 0xFFE88D, scale = 1f),
                    x, y, z, 1, 0.0, 0.0, 0.0, 0.0
                )
                level.sendParticles(NukeSmokeParticleOptions(color = 0xB87878, scale = 0.7f),
                    x, y+1, z, 16, 0.0, 0.5, 0.0, 0.0
                )
                meltSnowAround()
            }
        ))
    }

    override fun canSurviveOn(block: BlockState): Boolean {
        return super.canSurviveOn(block) || mushroomSurvivalCheck(block) || fireSurvivalCheck(block) || sandSurvivalCheck(block)
    }
}