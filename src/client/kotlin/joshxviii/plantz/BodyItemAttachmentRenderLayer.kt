package joshxviii.plantz

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import joshxviii.plantz.PazModels.PAINT_COLORS_KEY
import joshxviii.plantz.model.zombies.PazZombieModel
import net.minecraft.client.Minecraft
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.RenderLayer
import net.minecraft.client.renderer.entity.state.HumanoidRenderState
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import net.minecraft.client.renderer.item.ItemStackRenderState
import net.minecraft.client.renderer.rendertype.LayeringTransform
import net.minecraft.client.renderer.rendertype.RenderSetup
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.client.renderer.rendertype.TextureTransform
import net.minecraft.client.renderer.rendertype.TextureTransform.OffsetTextureTransform
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack

class PaintLayer<S : LivingEntityRenderState, M : EntityModel<in S>>(renderer: RenderLayerParent<S, M>) : RenderLayer<S, M>(
    renderer
) {
    companion object {
        val PAINT_TEXTURE_1 = pazResource("textures/entity/paint_overlay/paint_1.png")
        val PAINT_TEXTURE_2 = pazResource("textures/entity/paint_overlay/paint_2.png")
        val PAINT_TEXTURE_3 = pazResource("textures/entity/paint_overlay/paint_3.png")
        val PAINT_TEXTURE_4 = pazResource("textures/entity/paint_overlay/paint_4.png")

        private fun getTextureFromAmplifier(amplifier: Int): Identifier {
            return when {
                amplifier < 10 * 0.25 -> PAINT_TEXTURE_1
                amplifier < 10 * 0.5 -> PAINT_TEXTURE_2
                amplifier < 10 * 0.75 -> PAINT_TEXTURE_3
                else -> PAINT_TEXTURE_4
            }
        }

        fun paintOverlay(amplifier: Int = 0, uOffset: Float = 0.0f, vOffset: Float = 0.0f): RenderType {
            return RenderType.create(
                "paint_overlay",
                RenderSetup.builder(RenderPipelines.ENTITY_CUTOUT)
                    .withTexture("Sampler0", getTextureFromAmplifier(amplifier))
                    .setTextureTransform(OffsetTextureTransform(uOffset, vOffset))
                    .setLayeringTransform(LayeringTransform.VIEW_OFFSET_Z_LAYERING)
                    .useOverlay()
                    .sortOnUpload()
                    .createRenderSetup()
            )
        }
    }

    override fun submit(
        poseStack: PoseStack,
        collector: SubmitNodeCollector,
        lightCoords: Int,
        state: S,
        yRot: Float,
        xRot: Float
    ) {
        poseStack.pushPose()
        //poseStack.scale(1.5f, 1.5f, 1.5f)
        val colors = state.getDataOrDefault(PAINT_COLORS_KEY, mapOf())
        for (i in colors.size - 1 downTo 0) {
            val color = colors.keys.elementAtOrNull(i) ?: continue
            val amplifier = colors.values.elementAtOrNull(i) ?: continue
            if (color != -1) collector.order(i).submitModel(
                parentModel,
                state,
                poseStack,
                paintOverlay(amplifier),
                lightCoords,
                OverlayTexture.NO_OVERLAY,
                color,
                null,
                state.outlineColor,
                null
            )
        }
        poseStack.popPose()
    }

}

class DuckyTubeRenderLayer<S : LivingEntityRenderState, M : EntityModel<in S>>(
    parent: RenderLayerParent<S, M>
) : BodyItemAttachmentRenderLayer<S, M>(
    parent = parent,
    expectedItem = PazItems.DUCKY_TUBE,
    stackSelector = { it.legsEquipment },
)

class DyeVatRenderLayer<S : LivingEntityRenderState, M : EntityModel<in S>>(
    parent: RenderLayerParent<S, M>
) : BodyItemAttachmentRenderLayer<S, M>(
    parent = parent,
    expectedItem = PazItems.DYE_BLASTER,
    stackSelector = { it.mainHandItemStack },
)

abstract class BodyItemAttachmentRenderLayer<S : LivingEntityRenderState, M : EntityModel<in S>>(
    parent: RenderLayerParent<S, M>,
    private val expectedItem: Item,
    private val stackSelector: (HumanoidRenderState) -> ItemStack,
) : RenderLayer<S, M>(parent) {

    private val itemRenderState = ItemStackRenderState()

    override fun submit(
        poseStack: PoseStack,
        collector: SubmitNodeCollector,
        lightCoords: Int,
        state: S,
        yRot: Float,
        xRot: Float
    ) {
        val humanoidModel = parentModel as? HumanoidModel<*> ?: return
        val humanState = state as? HumanoidRenderState ?: return
        val itemStack = stackSelector(humanState)

        if (!itemStack.`is`(expectedItem)) return

        poseStack.pushPose()

        if (state.isBaby) {
            poseStack.translate(0.0, 0.8, 0.0)
            poseStack.scale(0.55f, 0.55f, 0.55f)
        }

        if (humanoidModel is PazZombieModel) humanoidModel.body
        else humanoidModel.body.translateAndRotate(poseStack)

        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0f))

        val minecraft = Minecraft.getInstance()
        itemRenderState.clear()
        minecraft.itemModelResolver.updateForTopItem(
            itemRenderState,
            itemStack,
            ItemDisplayContext.HEAD,
            minecraft.level,
            null,
            0
        )
        itemRenderState.submit(
            poseStack,
            collector,
            lightCoords,
            OverlayTexture.NO_OVERLAY,
            0
        )

        poseStack.popPose()
    }
}