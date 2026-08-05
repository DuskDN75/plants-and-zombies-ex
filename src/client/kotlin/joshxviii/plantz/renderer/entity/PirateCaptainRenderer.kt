package joshxviii.plantz.renderer.entity

import joshxviii.plantz.entity.zombie.PazZombie
import joshxviii.plantz.entity.zombie.PirateCaptain
import joshxviii.plantz.entity.zombie.RoboZombie
import joshxviii.plantz.model.zombies.PirateCaptainModel
import joshxviii.plantz.model.zombies.RoboZombieModel
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.state.ZombieRenderState
import net.minecraft.world.entity.AnimationState

class PirateCaptainRenderer(
    context: EntityRendererProvider.Context,
    private val model: PirateCaptainModel = PirateCaptainModel(context.bakeLayer(PirateCaptainModel.LAYER_LOCATION)),
): PazZombieRenderer(context, model, model) {

    override fun createRenderState(): PazZombieRenderState {
        return PirateCaptainRenderState()
    }

    override fun extractRenderState(entity: PazZombie, state: ZombieRenderState, partialTicks: Float) {
        super.extractRenderState(entity, state, partialTicks)
        (state as PirateCaptainRenderState)
        (entity as PirateCaptain)

    }

}

class PirateCaptainRenderState: PazZombieRenderState() {

}