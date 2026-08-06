package joshxviii.plantz.renderer.entity

import joshxviii.plantz.PazEntities
import joshxviii.plantz.entity.zombie.PazZombie
import joshxviii.plantz.entity.zombie.PirateCaptain
import joshxviii.plantz.entity.zombie.PirateCaptainGhost
import joshxviii.plantz.entity.zombie.RoboZombie
import joshxviii.plantz.model.zombies.PazZombieModel
import joshxviii.plantz.model.zombies.PirateCaptainModel
import joshxviii.plantz.model.zombies.RoboZombieModel
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.entity.AnimationState

class PirateCaptainRenderer(
    context: EntityRendererProvider.Context,
    private val model: PazZombieModel = PirateCaptainModel(context.bakeLayer(PirateCaptainModel.LAYER_LOCATION)),
): PazZombieRenderer(context, model, model) {

    override fun createRenderState(): PazZombieRenderState {
        return PirateCaptainRenderState()
    }

    override fun extractRenderState(entity: PazZombie, state: PazZombieRenderState, partialTicks: Float) {
        super.extractRenderState(entity, state, partialTicks)
        (state as PirateCaptainRenderState)
        when (entity) {
            is PirateCaptain -> {

            }
            is PirateCaptainGhost -> {

            }
        }
    }

}

class PirateCaptainRenderState: PazZombieRenderState() {
    val idleAnimationState: AnimationState = AnimationState()
    val walkAnimationState: AnimationState = AnimationState()
}
