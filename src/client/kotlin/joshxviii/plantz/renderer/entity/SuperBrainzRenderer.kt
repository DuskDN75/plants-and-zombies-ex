package joshxviii.plantz.renderer.entity

import joshxviii.plantz.entity.zombie.PazZombie
import joshxviii.plantz.entity.zombie.SuperBrainz
import joshxviii.plantz.model.zombies.SuperBrainzModel
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.state.ZombieRenderState
import net.minecraft.util.Mth

class SuperBrainzRenderer(
    context: EntityRendererProvider.Context,
    private val model: SuperBrainzModel = SuperBrainzModel(context.bakeLayer(SuperBrainzModel.LAYER_LOCATION)),
): PazZombieRenderer(context, model, model) {

    override fun createRenderState(): PazZombieRenderState {
        return SuperBrainzRenderState()
    }

    override fun extractRenderState(entity: PazZombie, state: ZombieRenderState, partialTicks: Float) {
        super.extractRenderState(entity, state, partialTicks)
        (state as SuperBrainzRenderState)
        extractCapeState(entity, state, partialTicks)
    }

    private fun extractCapeState(entity: PazZombie, state: SuperBrainzRenderState, partialTicks: Float) {
        if (entity !is SuperBrainz) return
        val deltaX = Mth.lerp(partialTicks, entity.xCapeO, entity.xCape) -
                Mth.lerp(partialTicks, entity.xo.toFloat(), entity.x.toFloat())
        val deltaY = Mth.lerp(partialTicks, entity.yCapeO, entity.yCape) -
                Mth.lerp(partialTicks, entity.yo.toFloat(), entity.y.toFloat())
        val deltaZ = Mth.lerp(partialTicks, entity.zCapeO, entity.zCape) -
                Mth.lerp(partialTicks, entity.zo.toFloat(), entity.z.toFloat())

        val yBodyRot = Mth.rotLerp(partialTicks, entity.yBodyRotO, entity.yBodyRot)

        val forwardX = Mth.sin(yBodyRot * (Math.PI / 180f))
        val forwardZ = -Mth.cos(yBodyRot * (Math.PI / 180f))

        var capeFlap = (deltaY * 20.0f)
        capeFlap = Mth.clamp(capeFlap, -6f, 32f)

        var capeLean = (deltaX * forwardX + deltaZ * forwardZ) * 200.0f
        capeLean = Mth.clamp(capeLean, 0f, 150f)

        var capeLean2 = (deltaX * forwardZ - deltaZ * forwardX) * 200.0f
        capeLean2 = Mth.clamp(capeLean2, -20f, 20f)

        val walkDistance = Mth.lerp(partialTicks, entity.walkDistO, entity.walkDist)
        capeFlap += Mth.sin(walkDistance * 2.0) * 32f * 0.3f

        state.capeFlap = capeFlap
        state.capeLean = capeLean
        state.capeLean2 = capeLean2
    }
}

class SuperBrainzRenderState: PazZombieRenderState() {
    var capeFlap: Float = 0f
    var capeLean: Float = 0f
    var capeLean2: Float = 0f
}