package joshxviii.plantz

import com.mojang.blaze3d.vertex.PoseStack
import joshxviii.plantz.ai.ZombieState
import joshxviii.plantz.entity.zombie.AllStar
import joshxviii.plantz.entity.zombie.DiscoZombie
import joshxviii.plantz.entity.zombie.Gargantuar
import joshxviii.plantz.entity.zombie.NewspaperZombie
import joshxviii.plantz.entity.zombie.PazZombie
import joshxviii.plantz.model.zombies.PazZombieModel
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.ModelLayers
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.monster.zombie.ZombieModel
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.entity.AbstractZombieRenderer
import net.minecraft.client.renderer.entity.ArmorModelSet
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.EyesLayer
import net.minecraft.client.renderer.entity.state.ZombieRenderState
import net.minecraft.client.renderer.rendertype.RenderType
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.AnimationState
import net.minecraft.world.entity.monster.zombie.Zombie
import net.minecraft.world.phys.Vec3

class PazZombieRenderer(
    context: EntityRendererProvider.Context,
    private val defaultModel: PazZombieModel = PazZombieModel(null, context.bakeLayer(PazZombieModel.LAYER_LOCATION)),
    private val babyModel: PazZombieModel = PazZombieModel(null, context.bakeLayer(ModelLayers.ZOMBIE_BABY)),
    armorSet: ArmorModelSet<ModelLayerLocation> = ModelLayers.ZOMBIE_ARMOR,
    babyArmorSet: ArmorModelSet<ModelLayerLocation> = ModelLayers.ZOMBIE_BABY_ARMOR
) : AbstractZombieRenderer<PazZombie, ZombieRenderState, PazZombieModel>(
    context,
    defaultModel,
    babyModel,
    ArmorModelSet.bake<PazZombieModel>(armorSet, context.modelSet) { root: ModelPart -> PazZombieModel(null, root) },
    ArmorModelSet.bake<PazZombieModel>(babyArmorSet, context.modelSet) { root: ModelPart -> PazZombieModel(null, root) }
) {

    init {
        addLayer(EmissiveZombieLayer(this))
    }

    override fun submit(
        state: ZombieRenderState,
        poseStack: PoseStack,
        collector: SubmitNodeCollector,
        camera: CameraRenderState
    ) {
        (state as PazZombieRenderState)

        // debug info text
        if (PazConfig.SHOW_DEBUG_INFO) collector.submitNameTag(
            poseStack, Vec3(0.0,state.eyeHeight.toDouble(),0.0), -20,
            Component.literal("${state.zombieState.name}").withColor(0xFFFFFFF),
            true, -1, 20.0, camera
        )
        if (state.zombieState != ZombieState.EMERGING || state.ageInTicks>1) super.submit(state, poseStack, collector, camera)
    }

    override fun createRenderState(): PazZombieRenderState {
        return PazZombieRenderState()
    }

    override fun getShadowRadius(state: ZombieRenderState): Float {
        return super.getShadowRadius(state)
    }

    override fun extractRenderState(entity: PazZombie, state: ZombieRenderState, partialTicks: Float) {
        super.extractRenderState(entity, state, partialTicks)
        (entity as PazZombie)
        (state as PazZombieRenderState)
        state.zombieState = entity.state
        state.initAnimationState.copyFrom(entity.emergeAnimation)
        if (entity is Gargantuar) {
            state.actionAnimationState.copyFrom(entity.smashAttackAnimation)
            state.specialAnimationState.copyFrom(entity.throwImpAnimation)
        }
        if (entity is DiscoZombie) state.actionAnimationState.copyFrom(entity.summonAnimation)
        if (entity is AllStar) state.actionAnimationState.copyFrom(entity.chargeAnimation)
        if (entity is NewspaperZombie) state.isAngry = entity.isAngry()
        state.customName = entity.customName?.string ?: ""
        state.textureExtra =
            when (entity) {
                is Gargantuar -> if (entity.hasImp) "imp" else ""
                is NewspaperZombie -> if (entity.isAngry()) "angry" else ""
                else -> ""
            }
    }

    override fun getTextureLocation(state: ZombieRenderState): Identifier {
        (state as PazZombieRenderState)
        return state.getTextureLocation(PazZombieRenderState.TEXTURE_PATH, state.getSuffixes())
    }
}

class EmissiveZombieLayer<M : EntityModel<ZombieRenderState>>(
    renderer: RenderLayerParent<ZombieRenderState, M>,
) : EyesLayer<ZombieRenderState, M>(renderer) {

    override fun submit(
        poseStack: PoseStack,
        submitNodeCollector: SubmitNodeCollector,
        lightCoords: Int,
        state: ZombieRenderState,
        yRot: Float,
        xRot: Float
    ) {
        if (state !is PazZombieRenderState) return
        val textureLocation = state.getEmissiveTextureLocation(PazZombieRenderState.TEXTURE_PATH, state.getSuffixes()) ?: return
        val renderType = RenderTypes.eyes(textureLocation)
        submitNodeCollector.order(1).submitModel(this.parentModel, state, poseStack, renderType, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
    }

    override fun renderType(): RenderType = RenderTypes.lines()
}

class PazZombieRenderState : ZombieRenderState() {

    companion object {
        const val TEXTURE_PATH = "textures/entity/zombie"
    }
    var customName: String = ""
    var textureExtra: String = ""
    var actionTime: Int = 0
    var isAngry: Boolean = false
    var zombieState: ZombieState = ZombieState.IDLE
    val initAnimationState: AnimationState = AnimationState()
    val actionAnimationState: AnimationState = AnimationState()
    val specialAnimationState: AnimationState = AnimationState()

    fun getSuffixes(): MutableList<String> {
        val magicName = this.isMagicName(customName)
        val suffixes = mutableListOf<String>().apply {
            if (textureExtra.isNotEmpty())      add(textureExtra)
            if (magicName.isNotEmpty())         add(magicName)
            else if (isBaby)                    add("baby")
        }
        return suffixes
    }
}