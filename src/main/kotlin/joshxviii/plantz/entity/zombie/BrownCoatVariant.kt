package joshxviii.plantz.entity.zombie

import com.mojang.serialization.Codec
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.ByIdMap
import net.minecraft.util.RandomSource
import net.minecraft.util.StringRepresentable
import java.util.function.IntFunction

enum class BrownCoatVariant(val suffix: String, val id: Int) : StringRepresentable {
    BROWN("", 0),
    SNOW("snow", 1),
    DESERT("desert", 2);

    override fun getSerializedName(): String = suffix

    companion object {
        fun getDefault(): BrownCoatVariant = BROWN

        fun pickRandomVariant(): BrownCoatVariant = entries.random()

        fun pickForBiome(isSnowy: Boolean, isDesert: Boolean, random: RandomSource): BrownCoatVariant {
            return when {
                isSnowy && isDesert -> if (random.nextBoolean()) SNOW else DESERT
                isSnowy -> if (random.nextFloat() < 0.7f) SNOW else BROWN
                isDesert -> if (random.nextFloat() < 0.7f) DESERT else BROWN
                else -> BROWN
            }
        }

        val CODEC: Codec<BrownCoatVariant> = StringRepresentable.fromEnum(BrownCoatVariant::values)
        private val BY_ID: IntFunction<BrownCoatVariant> = ByIdMap.continuous(BrownCoatVariant::id, entries.toTypedArray(), ByIdMap.OutOfBoundsStrategy.ZERO)
        val STREAM_CODEC: StreamCodec<ByteBuf, BrownCoatVariant> = ByteBufCodecs.idMapper<BrownCoatVariant>(BY_ID, BrownCoatVariant::id)
    }
}
