package duskdn.plantz_ex.renderer.entity

import duskdn.plantz_ex.entity.zombie.PazZombie
import duskdn.plantz_ex.entity.zombie.RoboZombie
import duskdn.plantz_ex.model.zombies.RoboZombieModel
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.entity.AnimationState

class RoboZombieRenderer(
    context: EntityRendererProvider.Context,
    private val model: RoboZombieModel = RoboZombieModel(context.bakeLayer(RoboZombieModel.LAYER_LOCATION)),
): PazZombieRenderer(context, model, model) {

    override fun createRenderState(): PazZombieRenderState {
        return RoboZombieRenderState()
    }

    override fun extractRenderState(entity: PazZombie, state: PazZombieRenderState, partialTicks: Float) {
        super.extractRenderState(entity, state, partialTicks)
        (state as RoboZombieRenderState)
        (entity as RoboZombie)
        state.isTankTransformation = entity.isTransformed
        state.idleAnimationState.copyFrom(entity.idleAnimation)
    }

}

class RoboZombieRenderState: PazZombieRenderState() {
    var isTankTransformation = false
    val idleAnimationState: AnimationState = AnimationState()
}
