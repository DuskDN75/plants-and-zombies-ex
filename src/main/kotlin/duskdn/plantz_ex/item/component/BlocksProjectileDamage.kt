package duskdn.plantz_ex.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import duskdn.plantz_ex.entity.plant.init.PazPlant
import io.netty.buffer.ByteBuf
import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.item.Item.TooltipContext
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipProvider
import java.util.function.Consumer

class BlocksProjectileDamage(
    val slot: EquipmentSlotGroup = EquipmentSlotGroup.HEAD,
    val tanksDamage: Boolean = true,
    val reflectsDamage: Boolean = false,
    val reflectDistance: Double = 0.0,
    val reflectDistanceY: Double = 0.0,
    val mustBeUsing: Boolean = false,
    val maxHealth: Int = 0,
    val health: Int = 0
) : TooltipProvider {

    override fun addToTooltip(context: TooltipContext, consumer: Consumer<Component>, flag: TooltipFlag) {
//        val maxHealth = (components.get(DataComponents.MAX_DAMAGE) ?: 0) / PazPlant.PEA_DAMAGE
//
//        val damage = (components.get(DataComponents.DAMAGE) ?: 0) / PazPlant.PEA_DAMAGE
//
//        val health = maxHealth-damage

        consumer.accept(Component.translatable("component.blocks_damage.desc").withStyle(ChatFormatting.GRAY))
//        consumer.accept(Component.translatable("component.blocks_damage", health, maxHealth).withStyle(ChatFormatting.BLUE))
    }

    companion object {

        val CODEC: Codec<BlocksProjectileDamage> = RecordCodecBuilder.create { inst ->
            inst.group(
                EquipmentSlotGroup.CODEC.fieldOf("slot").forGetter { it.slot },
                Codec.BOOL.fieldOf("tanksDamage").forGetter { it.tanksDamage },
                Codec.BOOL.fieldOf("reflectsDamage").forGetter { it.reflectsDamage },
                Codec.DOUBLE.fieldOf("reflectDistance").forGetter { it.reflectDistance },
                Codec.DOUBLE.fieldOf("reflectDistanceY").forGetter { it.reflectDistanceY },
                Codec.BOOL.optionalFieldOf("must_be_using", false).forGetter { it.mustBeUsing },
            Codec.INT.optionalFieldOf("maxHealth", 0).forGetter { it.maxHealth },
            Codec.INT.optionalFieldOf("health", 0).forGetter { it.health }
            ).apply(inst, ::BlocksProjectileDamage)
        }

        val STREAM_CODEC: StreamCodec<ByteBuf, BlocksProjectileDamage> = StreamCodec.of(
            {buf, value ->
                EquipmentSlotGroup.STREAM_CODEC.encode(buf, value.slot)
                ByteBufCodecs.BOOL.encode(buf, value.tanksDamage)
                ByteBufCodecs.BOOL.encode(buf, value.reflectsDamage)
                ByteBufCodecs.DOUBLE.encode(buf, value.reflectDistance)
                ByteBufCodecs.DOUBLE.encode(buf, value.reflectDistanceY)
                ByteBufCodecs.BOOL.encode(buf, value.mustBeUsing)
                ByteBufCodecs.INT.encode(buf, value.maxHealth)
                ByteBufCodecs.INT.encode(buf, value.health)
//                BlocksProjectileDamage::slot,
//                ByteBufCodecs.BOOL,
//                BlocksProjectileDamage::tanksDamage,
//                ByteBufCodecs.BOOL,
//                BlocksProjectileDamage::reflectsDamage,
//                ByteBufCodecs.DOUBLE,
//                BlocksProjectileDamage::reflectDistance,
//                ByteBufCodecs.DOUBLE,
//                BlocksProjectileDamage::reflectDistanceY,
//                ByteBufCodecs.BOOL,
//                BlocksProjectileDamage::mustBeUsing,
//                ByteBufCodecs.INT,
//                BlocksProjectileDamage::maxHealth,
//                ByteBufCodecs.INT,
//                BlocksProjectileDamage::health,
//                ::BlocksProjectileDamage
            },
            { buf ->
                BlocksProjectileDamage(
                    EquipmentSlotGroup.STREAM_CODEC.decode(buf),
                    ByteBufCodecs.BOOL.decode(buf),
                    ByteBufCodecs.BOOL.decode(buf),
                    ByteBufCodecs.DOUBLE.decode(buf),
                    ByteBufCodecs.DOUBLE.decode(buf),
                    ByteBufCodecs.BOOL.decode(buf),
                    ByteBufCodecs.INT.decode(buf),
                    ByteBufCodecs.INT.decode(buf)
                )
            }
        )

    }
}