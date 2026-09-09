package duskdn.plantz_ex.common

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import duskdn.plantz_ex.util.teleportAwayFromDirection
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.ExtraCodecs
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.animal.fox.Fox
import net.minecraft.world.item.consume_effects.TeleportRandomlyConsumeEffect
import net.minecraft.world.level.Level
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.Vec3
import java.util.function.Function

@JvmRecord
data class PazTeleportRandomlyAwayEffect(val diameter: Float, val angle: Float) {
    constructor() : this(16.0f, 60.0f)

    fun apply(level: Level, user: LivingEntity, direction: Vec3): Boolean {
        var teleported = false

        for (attempt in 0..15) {
            if (user.isPassenger) {
                user.stopRiding()
            }

            val oldPos = user.position()
            if (user.teleportAwayFromDirection(diameter, angle, direction, true)) {
                level.gameEvent(GameEvent.TELEPORT, oldPos, GameEvent.Context.of(user))
                val soundSource: SoundSource?
                val soundEvent: SoundEvent?
                if (user is Fox) {
                    soundEvent = SoundEvents.FOX_TELEPORT
                    soundSource = SoundSource.NEUTRAL
                } else {
                    soundEvent = SoundEvents.CHORUS_FRUIT_TELEPORT
                    soundSource = SoundSource.PLAYERS
                }

                level.playSound(null as Entity?, user.x, user.y, user.z, soundEvent, soundSource)
                user.resetFallDistance()
                teleported = true
                break
            }
        }

        if (teleported) {
            user.resetCurrentImpulseContext()
        }

        return teleported
    }

    companion object {
        private const val DEFAULT_DIAMETER = 16.0f
//        val CODEC: MapCodec<PazTeleportRandomlyAwayEffect?> =
//            RecordCodecBuilder.mapCodec<PazTeleportRandomlyAwayEffect?>(Function { i: RecordCodecBuilder.Instance<PazTeleportRandomlyAwayEffect?>? ->
//                i!!.group<Float?>(
//                    ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("diameter", 16.0f)
//                        .forGetter<PazTeleportRandomlyAwayEffect?>(PazTeleportRandomlyAwayEffect::diameter)
//                ).apply<PazTeleportRandomlyAwayEffect?>(
//                    i,
//                    Function { diameter: Float? -> PazTeleportRandomlyAwayEffect(diameter!!) })
//            })
//        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf?, PazTeleportRandomlyAwayEffect?>
//
//        init {
//            PazTeleportRandomlyAwayEffect.Companion.STREAM_CODEC =
//                StreamCodec.composite<RegistryFriendlyByteBuf?, PazTeleportRandomlyAwayEffect?, Float?>(
//                    ByteBufCodecs.FLOAT,
//                    PazTeleportRandomlyAwayEffect::diameter,
//                    Function { diameter: Float? -> PazTeleportRandomlyAwayEffect(diameter!!) })
//        }
    }
}
