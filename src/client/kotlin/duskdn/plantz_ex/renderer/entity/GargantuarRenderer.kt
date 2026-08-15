package duskdn.plantz_ex.renderer.entity

import duskdn.plantz_ex.entity.zombie.Gargantuar
import duskdn.plantz_ex.entity.zombie.PazZombie
import duskdn.plantz_ex.model.zombies.GargantuarModel
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.world.entity.AnimationState

class GargantuarRenderer(
    context: EntityRendererProvider.Context,
    private val model: GargantuarModel = GargantuarModel(context.bakeLayer(GargantuarModel.LAYER_LOCATION)),
): PazZombieRenderer(context, model, model) {

    override fun createRenderState(): PazZombieRenderState {
        return GargantuarRenderState()
    }

    override fun extractRenderState(entity: PazZombie, state: PazZombieRenderState, partialTicks: Float) {
        super.extractRenderState(entity, state, partialTicks)
        (state as GargantuarRenderState)
        (entity as Gargantuar)
        state.punchAnimationState.copyFrom(entity.punchAttackAnimation)
        state.smashAnimationState.copyFrom(entity.smashAttackAnimation)
        state.throwImpAnimationState.copyFrom(entity.throwImpAnimation)
    }

}

class GargantuarRenderState: PazZombieRenderState() {
    val punchAnimationState: AnimationState = AnimationState()
    val smashAnimationState: AnimationState = AnimationState()
    val throwImpAnimationState: AnimationState = AnimationState()
}
