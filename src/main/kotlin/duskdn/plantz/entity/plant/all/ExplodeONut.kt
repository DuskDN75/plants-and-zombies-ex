package duskdn.plantz.entity.plant.all

import duskdn.plantz.ai.goal.ExplodeGoal
import duskdn.plantz.entity.plant.init.ExplosivePlant
import duskdn.plantz.init.NukeBlastParticleOptions
import duskdn.plantz.init.NukeSmokeParticleOptions
import duskdn.plantz.init.NukeWaveParticleOptions
import duskdn.plantz.init.PazEntities
import duskdn.plantz.init.PazTags.EntityTypes.WALLNUT_DEFLECTABLE
import net.minecraft.core.Holder
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.monster.zombie.Zombie
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class ExplodeONut(type: EntityType<out ExplosivePlant>, level: Level) : ExplosivePlant(PazEntities.EXPLODE_O_NUT, level) {

    override fun attackGoals() {}

    override fun allowPlayerCollision(): Boolean {
        return true
    }

    override fun hurtServer(level: ServerLevel, source: DamageSource, damage: Float): Boolean {
        source.directEntity?.let {
            if (it.`is`(WALLNUT_DEFLECTABLE)) return false
        }
        return super.hurtServer(level, source, damage)
    }

    override fun registerGoals() {
        this.goalSelector.addGoal(1, ExplodeGoal(
            usingEntity = this,
            attackRadius = 7f,
            destroyBlocks = true,
            actionPredicate = {
                (lastDamageSource?.directEntity != null && deathTime == 0)
            },
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

    override fun actuallyHurt(level: ServerLevel, source: DamageSource, damage: Float) {
        val reducedDamage = if (source.entity is Zombie) damage*0.25f else damage
        super.actuallyHurt(level, source, reducedDamage)
    }

    override fun tickDeath() {
        if (lastDamageSource?.directEntity != null && deathTime == 0) explode()
    }

    override fun explode(
        radius: Float,
        sound: Holder.Reference<SoundEvent>,
        damageType: ResourceKey<DamageType>,
        destroyBlocks: Boolean,
        discardOnExplode: Boolean
    ) {
        super.explode(3f, sound, damageType, destroyBlocks, discardOnExplode)
        val level = level() as? ServerLevel ?: return
        level.sendParticles(NukeWaveParticleOptions(color = 0xD0370D, scale = 2f),
            x, y, z, 1, 0.0, 0.0, 0.0, 0.0
        )
        level.sendParticles(NukeBlastParticleOptions(color = 0xFFE88D, scale = 1.5f),
            x, y, z, 1, 0.0, 0.0, 0.0, 0.0
        )
        level.sendParticles(NukeSmokeParticleOptions(color = 0xB87878, scale = 0.6f),
            x, y+1, z, 15, 0.0, 0.5, 0.0, 0.0
        )
    }

    override fun canSurviveOn(block: BlockState): Boolean {
        return super.canSurviveOn(block) || !block.getCollisionShape(level(), blockPosition().below()).isEmpty
    }
}