package duskdn.plantz.entity.plant.all.mushrooms

import duskdn.plantz.init.NukeBlastParticleOptions
import duskdn.plantz.init.NukeSmokeParticleOptions
import duskdn.plantz.init.NukeWaveParticleOptions
import duskdn.plantz.init.PazEntities
import duskdn.plantz.ai.goal.ExplodeGoal
import duskdn.plantz.entity.plant.init.ExplosivePlant
import duskdn.plantz.entity.plant.init.PazPlant
import duskdn.plantz.entity.plant.interfaces.IExplosivePlant
import duskdn.plantz.entity.plant.utils.mushroomSurvivalCheck
import duskdn.plantz.entity.plant.utils.stoneSurvivalCheck
import net.minecraft.core.Holder
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class DoomShroom(type: EntityType<out PazPlant>, level: Level) : ExplosivePlant(PazEntities.DOOM_SHROOM, level) {

    override fun registerGoals() {
        super.registerGoals()
        this.goalSelector.addGoal(1, ExplodeGoal(
            usingEntity = this,
            attackRadius = 7f,
            destroyBlocks = true,
            actionEndEffect = {
                playSound(SoundEvents.DRAGON_FIREBALL_EXPLODE, 2f, 0.0f)
                playSound(SoundEvents.ENDER_DRAGON_SHOOT, 2f, 0.0f)
                addParticlesAroundSelf(
                    particle = ParticleTypes.LARGE_SMOKE,
                    amount = 58..60,
                    speed = 0.15,
                )
                val level = level() as? ServerLevel ?: return@ExplodeGoal
                level.sendParticles(NukeWaveParticleOptions(color = 0xCAACF6, scale = 4f),
                    x, y, z, 1, 0.0, 0.0, 0.0, 0.0
                )
                level.sendParticles(NukeBlastParticleOptions(color = 0xC093FF, scale = 2.5f),
                    x, y, z, 1, 0.0, 0.0, 0.0, 0.0
                )
                level.sendParticles(NukeSmokeParticleOptions(color = 0x7425A3, scale = 0.85f),
                    x, y+2.5, z, 17, 0.0, 1.0, 0.0, 0.0
                )
            }
        ))
    }

    override fun explode(
        radius: Float,
        sound: Holder.Reference<SoundEvent>,
        damageType: ResourceKey<DamageType>,
        destroyBlocks: Boolean,
        causeFire: Boolean
    ) {
        super.explode(radius, sound, damageType, destroyBlocks, causeFire)
        if (vehicle != null) {
            vehicle?.kill(level() as ServerLevel)
        }
    }

    override fun canSurviveOn(block: BlockState): Boolean {
        return super.canSurviveOn(block) || mushroomSurvivalCheck(block) || stoneSurvivalCheck(block)
    }
}