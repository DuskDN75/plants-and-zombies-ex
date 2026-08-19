package duskdn.plantz_ex

import com.mojang.blaze3d.PrimitiveTopology
import com.mojang.blaze3d.pipeline.*
import com.mojang.blaze3d.platform.CompareOp
import com.mojang.blaze3d.shaders.UniformType
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import net.minecraft.client.renderer.RenderPipelines


object PazRenderPipelines {

    val BASIC_LAYOUT: BindGroupLayout =
        BindGroupLayout.builder() // Specifies that the shaders have a 'Sampler0' sampler
            .withSampler("Sampler0") // Specifies that the shaders have access to the 'Globals' uniform
            .build()

    @JvmField
    val ELECTRIC_ARC = RenderPipelines.register(
        RenderPipeline.builder(*arrayOf(RenderPipelines.MATRICES_FOG_SNIPPET))
            .withLocation("pipeline/energy_swirl")
            .withVertexShader("core/entity")
            .withFragmentShader("core/entity")
            .withShaderDefine("ALPHA_CUTOUT", 1.0f)
            .withShaderDefine("EMISSIVE")
            .withShaderDefine("NO_OVERLAY")
            .withShaderDefine("NO_CARDINAL_LIGHTING")
            .withShaderDefine("APPLY_TEXTURE_MATRIX")
            .withBindGroupLayout(BASIC_LAYOUT)
            .withColorTargetState(ColorTargetState(BlendFunction.ADDITIVE))
            .withCull(false)
            .withVertexBinding(0, DefaultVertexFormat.ENTITY)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .withDepthStencilState(DepthStencilState.DEFAULT)
            .build())

    @JvmField
    val ADDITIVE_TRANSLUCENT = RenderPipelines.register(
        RenderPipeline.builder(*arrayOf(RenderPipelines.MATRICES_FOG_SNIPPET))
            .withLocation("pipeline/energy_swirl")
            .withVertexShader("core/entity")
            .withFragmentShader("core/entity")
//            .withShaderDefine("ALPHA_CUTOUT", 0.1f)
            .withShaderDefine("EMISSIVE")
            .withShaderDefine("NO_OVERLAY")
            .withShaderDefine("NO_CARDINAL_LIGHTING")
            .withShaderDefine("APPLY_TEXTURE_MATRIX")
            .withShaderDefine("PER_FACE_LIGHTING")
            .withBindGroupLayout(BASIC_LAYOUT)
            .withColorTargetState(ColorTargetState(BlendFunction.ADDITIVE))
            .withCull(false)
            .withVertexBinding(0, DefaultVertexFormat.ENTITY)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .withDepthStencilState(DepthStencilState.DEFAULT)
            .build()
    )

    fun initialize() {}
}