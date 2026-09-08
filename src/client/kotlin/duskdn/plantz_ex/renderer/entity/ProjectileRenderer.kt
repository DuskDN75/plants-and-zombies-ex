package duskdn.plantz_ex.renderer.entity

import com.google.common.collect.Lists
import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import duskdn.plantz_ex.entity.projectile.PaintBall
import duskdn.plantz_ex.renderer.getEmissiveTextureLocation
import duskdn.plantz_ex.renderer.getTextureLocation
import duskdn.plantz_ex.renderer.isMagicName
import net.minecraft.client.model.EntityModel
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.EyesLayer
import net.minecraft.client.renderer.entity.layers.RenderLayer
import net.minecraft.client.renderer.rendertype.RenderSetup
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.DyeColor

class ProjectileRenderer(
    val model: EntityModel<ProjectileRenderState>,
    context: EntityRendererProvider.Context,
) : EntityRenderer<Projectile, ProjectileRenderState>(
    context
), RenderLayerParent<ProjectileRenderState, EntityModel<ProjectileRenderState>> {

    val layers: MutableList<RenderLayer<ProjectileRenderState, EntityModel<ProjectileRenderState>>?> = Lists.newArrayList()

    fun addLayer(layer: RenderLayer<ProjectileRenderState, EntityModel<ProjectileRenderState>>?): Boolean {
        return this.layers.add(layer)
    }

    init {
        addLayer(EmissiveProjectileLayer(this))
    }

    override fun submit(
        state: ProjectileRenderState,
        poseStack: PoseStack,
        submitNodeCollector: SubmitNodeCollector,
        camera: CameraRenderState
    ) {
        poseStack.pushPose()
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yRot - 90.0f))
        poseStack.mulPose(Axis.ZP.rotationDegrees(state.xRot))
        poseStack.translate(0.0, -1.5, 0.0)
        val tint = state.color?.fireworkColor?: -1

        val renderType = getRenderType(state, isBodyVisible = true, forceTransparent = true, appearGlowing = state.appearsGlowing()) ?: return

        submitNodeCollector.submitModel(
            this.model,
            state,
            poseStack,
            renderType,
            state.lightCoords,
            OverlayTexture.NO_OVERLAY,
            tint,
            null,
            state.outlineColor,
            null
        )

        if (!this.layers.isEmpty()) {
            this.model.setupAnim(state)

            for (layer in this.layers) {
                layer?.submit(poseStack, submitNodeCollector, state.lightCoords, state, state.yRot, state.xRot)
            }
        }

        poseStack.popPose()
        super.submit(state, poseStack, submitNodeCollector, camera)
    }

    override fun createRenderState(): ProjectileRenderState {
        return ProjectileRenderState()
    }

    override fun extractRenderState(entity: Projectile, state: ProjectileRenderState, partialTick: Float) {
        super.extractRenderState(entity, state, partialTick)
        if (entity is PaintBall) state.color = entity.dyeColor
        state.xRot = entity.getXRot(partialTick)
        state.yRot = entity.getYRot(partialTick)
    }

    fun getTextureLocation(state: ProjectileRenderState): Identifier {
        val texture = state.getTextureLocation(PlantRenderState.TEXTURE_PATH, state.getSuffixes())
        return texture
    }

    fun getRenderType(
        state: ProjectileRenderState,
        isBodyVisible: Boolean,
        forceTransparent: Boolean,
        appearGlowing: Boolean
    ): RenderType? {
        val texture = this.getTextureLocation(state)
        if (forceTransparent) {
            return RenderType.create(
                "plant_projectile",
                RenderSetup.builder(RenderPipelines.ENTITY_CUTOUT)
                    .withTexture("Sampler0", getTextureLocation(state))
                    .useLightmap()
                    .sortOnUpload()
                    .createRenderSetup()
            )
        } else if (isBodyVisible) {
            return this.model.renderType(texture)
        } else {
            return if (appearGlowing) RenderTypes.outline(texture) else null
        }
    }

    override fun getModel(): EntityModel<ProjectileRenderState> {
        return this.model
    }
}

class EmissiveProjectileLayer<M : EntityModel<ProjectileRenderState>>(
    renderer: RenderLayerParent<ProjectileRenderState, M>,
) : EyesLayer<ProjectileRenderState, M>(renderer) {

    override fun submit(
        poseStack: PoseStack,
        submitNodeCollector: SubmitNodeCollector,
        lightCoords: Int,
        state: ProjectileRenderState,
        yRot: Float,
        xRot: Float
    ) {
        val textureLocation = state.getEmissiveTextureLocation(ProjectileRenderState.TEXTURE_PATH, state.getSuffixes()) ?: return

        val renderType = RenderType.create(
            "plant_projectile_emissive",
            RenderSetup.builder(RenderPipelines.ENERGY_SWIRL)
                .withTexture("Sampler0", textureLocation)
                .useLightmap()
                .sortOnUpload()
                .createRenderSetup()
        )

        submitNodeCollector.order(1).submitModel(this.parentModel, state, poseStack, renderType, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
    }

    override fun renderType(): RenderType = RenderTypes.lines()
}

class ProjectileRenderState : net.minecraft.client.renderer.entity.state.EntityRenderState() {

    companion object {
        const val TEXTURE_PATH = "textures/entity/projectile"
    }

    var xRot: Float = 0f
    var yRot: Float = 0f
    var customName: String = ""
    var textureExtra: String = ""
    var color: DyeColor? = null

    fun getSuffixes(): MutableList<String> {

        val magicName = this.isMagicName(customName)
        val suffixes = mutableListOf<String>().apply {
            if (textureExtra.isNotEmpty())      add(textureExtra)
            if (magicName.isNotEmpty())         add(magicName)
        }
        return suffixes
    }
}