package duskdn.plantz.entity.plant.all

import duskdn.plantz.init.NukeBlastParticleOptions
import duskdn.plantz.init.NukeSmokeParticleOptions
import duskdn.plantz.init.NukeWaveParticleOptions
import duskdn.plantz.init.PazEntities
import duskdn.plantz.ai.goal.ExplodeGoal
import duskdn.plantz.entity.plant.init.Explosive
import duskdn.plantz.entity.plant.init.PazPlant
import duskdn.plantz.entity.plant.utils.mushroomSurvivalCheck
import duskdn.plantz.entity.plant.utils.sandSurvivalCheck
import duskdn.plantz.entity.plant.utils.stoneSurvivalCheck
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.monster.zombie.Zombie
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class CherryBomb(type: EntityType<out Explosive>, level: Level) : Explosive(PazEntities.CHERRY_BOMB, level) {

    override fun registerGoals() {
        super.registerGoals()
        this.goalSelector.addGoal(1, ExplodeGoal(
            explosiveEntity = this,
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
            }
        ))
        this.targetSelector.addGoal(4, NearestAttackableTargetGoal(this, LivingEntity::class.java, 5, true, false) { target, level ->
            target !is PazPlant
                    && (target is Zombie
                    || (target is Enemy && isTame)
                    || (target is Player && !isTame))
        })
    }

    override fun canSurviveOn(block: BlockState): Boolean {
        return super.canSurviveOn(block) || mushroomSurvivalCheck(block) || stoneSurvivalCheck(block) || sandSurvivalCheck(block)
    }
}