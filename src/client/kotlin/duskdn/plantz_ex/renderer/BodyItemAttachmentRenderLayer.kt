package duskdn.plantz_ex.renderer

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import duskdn.plantz_ex.PazModels.IS_BUTTERED_KEY
import duskdn.plantz_ex.PazModels.PAINT_COLORS_KEY
import duskdn.plantz_ex.init.PazEntities
import duskdn.plantz_ex.init.PazItems
import duskdn.plantz_ex.model.projectiles.ButterModel
import duskdn.plantz_ex.model.zombies.init.PazZombieModel
import duskdn.plantz_ex.renderer.entity.ProjectileRenderState
import duskdn.plantz_ex.renderer.entity.ProjectileRenderer
import duskdn.plantz_ex.util.pazResource
import net.minecraft.client.Minecraft
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.model.monster.piglin.ZombifiedPiglinModel
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.entity.LivingEntityRenderer
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.RenderLayer
import net.minecraft.client.renderer.entity.state.HumanoidRenderState
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import net.minecraft.client.renderer.item.ItemStackRenderState
import net.minecraft.client.renderer.rendertype.LayeringTransform
import net.minecraft.client.renderer.rendertype.RenderSetup
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.client.renderer.rendertype.TextureTransform.OffsetTextureTransform
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack

class SpecialEffectsLayer<S : LivingEntityRenderState, M : EntityModel<in S>>(private val renderer: RenderLayerParent<S, M>) : RenderLayer<S, M>(
    renderer
) {
    companion object {

        val BUTTER_MODEL = ButterModel(Minecraft.getInstance().entityModels.bakeLayer(ButterModel.LAYER_LOCATION))

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
        submitPaintLayer(poseStack, collector, lightCoords, state)
        submitButterLayer(poseStack, collector, lightCoords, state)
    }

    fun submitButterLayer(
        poseStack: PoseStack,
        collector: SubmitNodeCollector,
        lightCoords: Int,
        state: S
    ) {
        val hasButterEffect = state.getDataOrDefault(IS_BUTTERED_KEY, false)
        if (!hasButterEffect) return

        val humanoidModel = parentModel as? HumanoidModel<*> ?: return

        val butterState = ProjectileRenderState().apply { entityType = PazEntities.BUTTER }
        val texture = ProjectileRenderer.getTextureLocation(butterState) ?: return

        poseStack.pushPose()
        humanoidModel.root().translateAndRotate(poseStack)
        humanoidModel.head.translateAndRotate(poseStack)
        poseStack.mulPose(Axis.YP.rotationDegrees(90f))
        poseStack.mulPose(Axis.XP.rotationDegrees(25f))
        poseStack.mulPose(Axis.ZP.rotationDegrees(90f))
        poseStack.translate(-0.6, -1.4, 0.2)

        collector.submitModel(
            BUTTER_MODEL,
            butterState,
            poseStack,
            RenderTypes.entityCutout(texture),
            lightCoords,
            OverlayTexture.NO_OVERLAY,
            0,
            null
        )
        poseStack.popPose()
    }

    fun submitPaintLayer(
        poseStack: PoseStack,
        collector: SubmitNodeCollector,
        lightCoords: Int,
        state: S
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

//    private fun entityTexture(state: S): Identifier? {
//        val living = renderer as? LivingEntityRenderer<*, S, M> ?: return null
//        return living.getTextureLocation(state)
//    }
//
//    fun alphaFromAmplifier(rgb: Int, amplifier: Int): Int {
//        val strength = amplifier.coerceIn(0, 20) / 20f
//        val a = (strength * 0xFF).toInt().coerceIn(50, 0xFF)
//        return (a shl 24) or (rgb and 0x00FFFFFF)
//    }

}

class DuckyTubeRenderLayer<S : LivingEntityRenderState, M : EntityModel<in S>>(
    parent: RenderLayerParent<S, M>
) : BodyItemAttachmentRenderLayer<S, M>(
    parent = parent,
    expectedItem = PazItems.DUCKY_TUBE,
    stackSelector = { it.legsEquipment },
)

class ObsidianDuckyTubeRenderLayer<S : LivingEntityRenderState, M : EntityModel<in S>>(
    parent: RenderLayerParent<S, M>
) : BodyItemAttachmentRenderLayer<S, M>(
    parent = parent,
    expectedItem = PazItems.OBSIDIAN_DUCKY_TUBE,
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

        if (humanoidModel is PazZombieModel || humanoidModel is ZombifiedPiglinModel) humanoidModel.body
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