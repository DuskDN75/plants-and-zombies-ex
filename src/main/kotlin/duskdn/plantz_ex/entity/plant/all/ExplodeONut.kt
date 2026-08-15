package duskdn.plantz_ex.entity.plant.all

import duskdn.plantz_ex.ai.goal.ExplodeGoal
import duskdn.plantz_ex.entity.plant.interfaces.AbstractWallNut
import duskdn.plantz_ex.entity.plant.interfaces.IExplosivePlant
import duskdn.plantz_ex.init.NukeBlastParticleOptions
import duskdn.plantz_ex.init.NukeSmokeParticleOptions
import duskdn.plantz_ex.init.NukeWaveParticleOptions
import duskdn.plantz_ex.init.PazEntities
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

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