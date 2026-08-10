package duskdn.plantz.entity.plant.all

import duskdn.plantz.ai.goal.ExplodeGoal
import duskdn.plantz.entity.plant.init.ExplosivePlant
import duskdn.plantz.entity.plant.interfaces.AbstractWallNut
import duskdn.plantz.entity.plant.interfaces.IExplosivePlant
import duskdn.plantz.entity.plant.interfaces.IExplosivePlant.Companion.SWELL
import duskdn.plantz.entity.plant.interfaces.IExplosivePlant.Companion.SWELL_OLD
import duskdn.plantz.entity.plant.interfaces.IInstantPlant.Companion.ACTIVE
import duskdn.plantz.entity.plant.utils.PlantSpawnUtils
import duskdn.plantz.init.NukeBlastParticleOptions
import duskdn.plantz.init.NukeSmokeParticleOptions
import duskdn.plantz.init.NukeWaveParticleOptions
import duskdn.plantz.init.PazEntities
import duskdn.plantz.init.PazTags.EntityTypes.WALLNUT_DEFLECTABLE
import net.minecraft.core.Holder
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.entity.monster.zombie.Zombie
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class ExplodeONut(type: EntityType<out AbstractWallNut>, level: Level) : AbstractWallNut(PazEntities.EXPLODE_O_NUT, level), IExplosivePlant {

    var explodeGoal: ExplodeGoal<ExplodeONut>? = null

    init {
        active = false
    }

    override fun registerGoals() {

        explodeGoal = ExplodeGoal(
            usingEntity = this,
            attackRadius = 3f,
            destroyBlocks = true,
            actionEndEffect = {
                val level = level() as? ServerLevel ?: return@ExplodeGoal
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
        )

        this.goalSelector.addGoal(1, explodeGoal as Goal)
    }

    override fun mobInteract(player: Player, hand: InteractionHand): InteractionResult {
        return if (getTriggered(player, hand)) InteractionResult.SUCCESS_SERVER else super.mobInteract(player, hand)
    }

    override fun tickDeath() {
        if (lastDamageSource?.directEntity != null && deathTime == 0 && explodeGoal != null) explodeGoal!!.runAction()
    }

    override fun discardOnActivate(): Boolean {
        return true
    }

    override var swellSpeed: Int = 0
}