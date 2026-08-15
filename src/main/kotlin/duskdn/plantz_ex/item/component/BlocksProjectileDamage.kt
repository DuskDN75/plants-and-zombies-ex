package duskdn.plantz_ex.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import duskdn.plantz_ex.entity.plant.init.PazPlant
import io.netty.buffer.ByteBuf
import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponentGetter
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
    val mustBeUsing: Boolean = false
) : TooltipProvider {

    override fun addToTooltip(
        context: TooltipContext,
        consumer: Consumer<Component>,
        flag: TooltipFlag,
        components: DataComponentGetter
    ) {
        val maxHealth = (components.get(DataComponents.MAX_DAMAGE) ?: 0) / PazPlant.PEA_DAMAGE

        val damage = (components.get(DataComponents.DAMAGE) ?: 0) / PazPlant.PEA_DAMAGE

        val health = maxHealth-damage

        consumer.accept(Component.translatable("component.blocks_damage.desc").withStyle(ChatFormatting.GRAY))
        consumer.accept(Component.translatable("component.blocks_damage", health, maxHealth).withStyle(ChatFormatting.BLUE))
    }

    companion object {

        val CODEC: Codec<BlocksProjectileDamage> = RecordCodecBuilder.create { inst ->
            inst.group(
                EquipmentSlotGroup.CODEC.fieldOf("slot").forGetter { it.slot },
                Codec.BOOL.fieldOf("tanksDamage").forGetter { it.tanksDamage },
                Codec.BOOL.fieldOf("reflectsDamage").forGetter { it.reflectsDamage },
                Codec.BOOL.optionalFieldOf("must_be_using", false).forGetter { it.mustBeUsing }
            ).apply(inst, ::BlocksProjectileDamage)
        }

        val STREAM_CODEC: StreamCodec<ByteBuf, BlocksProjectileDamage> = StreamCodec.composite(
            EquipmentSlotGroup.STREAM_CODEC,
            BlocksProjectileDamage::slot,
            ByteBufCodecs.BOOL,
            BlocksProjectileDamage::tanksDamage,
            ByteBufCodecs.BOOL,
            BlocksProjectileDamage::reflectsDamage,
            ByteBufCodecs.BOOL,
            BlocksProjectileDamage::mustBeUsing,
            ::BlocksProjectileDamage
        )

    }
}