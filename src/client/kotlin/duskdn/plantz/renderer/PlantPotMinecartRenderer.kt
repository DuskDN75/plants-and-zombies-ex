<<<<<<<< HEAD:src/client/kotlin/joshxviii/plantz/renderer/PlantPotMinecartRenderer.kt
package joshxviii.plantz.renderer
========
package duskdn.plantz
>>>>>>>> 68eac8a988f75e82769978a50f4547f227e4f5a3:src/client/kotlin/duskdn/plantz/PlantPotMinecartRenderer.kt

import com.mojang.blaze3d.vertex.PoseStack
import duskdn.plantz.entity.PlantPotMinecart
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.block.BlockModelRenderState
import net.minecraft.client.renderer.entity.AbstractMinecartRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.state.MinecartRenderState

class PlantPotMinecartRenderer(
    context: EntityRendererProvider.Context,
    model: ModelLayerLocation,
): AbstractMinecartRenderer<PlantPotMinecart, MinecartRenderState>(
    context,
    model
) {
    override fun submitMinecartContents(
        state: MinecartRenderState,
        blockModel: BlockModelRenderState,
        poseStack: PoseStack,
        submitNodeCollector: SubmitNodeCollector,
        lightCoords: Int
    ) {
        poseStack.translate(-0.5 * 1/3, 0.15, -0.5 * 1/3)
        poseStack.scale(4/3f, 4/3f, 4/3f)
        super.submitMinecartContents(state, blockModel, poseStack, submitNodeCollector, lightCoords)
    }

    override fun createRenderState(): MinecartRenderState = MinecartRenderState()
}