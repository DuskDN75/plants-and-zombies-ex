package joshxviii.plantz

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes
import net.minecraft.core.Registry
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.particles.ScalableParticleOptionsBase
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.ARGB
import net.minecraft.util.ExtraCodecs
import org.joml.Vector3f
import java.util.function.Function

object PazServerParticles {
    @JvmField val BUTTER_DRIP:SimpleParticleType = registerSimple("butter_drip")
    @JvmField val PEA_HIT: SimpleParticleType = registerSimple("pea")
    @JvmField val ICE_PEA_HIT: SimpleParticleType = registerSimple("pea_ice")
    @JvmField val FIRE_PEA_HIT: SimpleParticleType = registerSimple("pea_fire")
    @JvmField val HYPNO_SPORE: SimpleParticleType = registerSimple("hypno_spore")
    @JvmField val SPORE: SimpleParticleType = registerSimple("spore")
    @JvmField val SPORE_HIT: SimpleParticleType = registerSimple("spore_hit")
    @JvmField val FUME_BUBBLE: SimpleParticleType = registerSimple("fume_bubble")
    @JvmField val EMBER: SimpleParticleType = registerSimple("ember")
    @JvmField val ENERGIZED: SimpleParticleType = registerSimple("energized")
    @JvmField val SLEEP: SimpleParticleType = registerSimple("sleep")
    @JvmField val NOTIFY: SimpleParticleType = registerSimple("notify")
    @JvmField val NEEDS_WATER: SimpleParticleType = registerSimple("needs_water")
    @JvmField val NEEDS_SUN: SimpleParticleType = registerSimple("needs_sun")
    @JvmField val NEEDS_TIME: SimpleParticleType = registerSimple("needs_time")
    @JvmField val ZOMBIE_OMEN: SimpleParticleType = registerSimple("zombie_omen")
    @JvmField val NUKE_WAVE: ParticleType<NukeWaveParticleOptions> = register("nuke_wave", { NukeWaveParticleOptions.CODEC}, {NukeWaveParticleOptions.STREAM_CODEC})
    @JvmField val NUKE_BLAST: ParticleType<NukeBlastParticleOptions> = register("nuke_blast", {NukeBlastParticleOptions.CODEC}, {NukeBlastParticleOptions.STREAM_CODEC})
    @JvmField val PAINT_BALL: ParticleType<PaintParticleOptions> = register("paint_ball", {PaintParticleOptions.CODEC}, {PaintParticleOptions.STREAM_CODEC})

    fun registerSimple(name: String): SimpleParticleType {
        val particleType = FabricParticleTypes.simple()
        Registry.register(BuiltInRegistries.PARTICLE_TYPE, pazResource(name), particleType)
        return particleType
    }

    private fun <T : ParticleOptions> register(
        name: String,
        codec: Function<ParticleType<T>, MapCodec<T>>,
        streamCodec: Function<ParticleType<T>, StreamCodec<in RegistryFriendlyByteBuf, T>>
    ): ParticleType<T> {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, pazResource(name), object : ParticleType<T>(false) {
            override fun codec(): MapCodec<T> = codec.apply(this)

            override fun streamCodec(): StreamCodec<in RegistryFriendlyByteBuf, T> = streamCodec.apply(this)
        })
    }

    fun initialize() {}
}

class PaintParticleOptions(private val color: Int, scale: Float = 1f) : ScalableParticleOptionsBase(scale) {
    override fun getType(): ParticleType<PaintParticleOptions> {
        return PazServerParticles.PAINT_BALL
    }

    fun getColor(): Vector3f = ARGB.vector3fFromRGB24(this.color)

    companion object {
        val CODEC: MapCodec<PaintParticleOptions> = RecordCodecBuilder.mapCodec { i ->
                i.group(
                    ExtraCodecs.RGB_COLOR_CODEC.fieldOf("color").forGetter { it.color },
                    SCALE.fieldOf("scale").forGetter { it.scale }
                ).apply(i, ::PaintParticleOptions) }
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, PaintParticleOptions> = StreamCodec.composite(
            ByteBufCodecs.INT, { it.color },
            ByteBufCodecs.FLOAT, { it.scale },
            ::PaintParticleOptions)
    }
}

class NukeWaveParticleOptions(scale: Float = 1f) : ScalableParticleOptionsBase(scale) {
    override fun getType(): ParticleType<NukeWaveParticleOptions> = PazServerParticles.NUKE_WAVE

    companion object {
        val CODEC: MapCodec<NukeWaveParticleOptions> = RecordCodecBuilder.mapCodec { i ->
            i.group(SCALE.fieldOf("scale").forGetter { it.scale }).apply(i, ::NukeWaveParticleOptions) }
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, NukeWaveParticleOptions> = StreamCodec.composite(
            ByteBufCodecs.FLOAT, { it.scale }, ::NukeWaveParticleOptions)
    }
}

class NukeBlastParticleOptions(scale: Float = 1f) : ScalableParticleOptionsBase(scale) {
    override fun getType(): ParticleType<NukeBlastParticleOptions> = PazServerParticles.NUKE_BLAST

    companion object {
        val CODEC: MapCodec<NukeBlastParticleOptions> = RecordCodecBuilder.mapCodec { i ->
            i.group(SCALE.fieldOf("scale").forGetter { it.scale }).apply(i, ::NukeBlastParticleOptions) }
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, NukeBlastParticleOptions> = StreamCodec.composite(
            ByteBufCodecs.FLOAT, { it.scale }, ::NukeBlastParticleOptions)
    }
}