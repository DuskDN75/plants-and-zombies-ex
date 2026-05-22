package joshxviii.plantz

import io.netty.buffer.ByteBuf
import joshxviii.plantz.ai.PlantState
import joshxviii.plantz.ai.ZombieState
import joshxviii.plantz.entity.gnome.GnomeSoundVariant
import joshxviii.plantz.entity.gnome.GnomeVariant
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityDataRegistry
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.syncher.EntityDataSerializer
import net.minecraft.world.item.DyeColor
import org.apache.logging.log4j.core.util.Integers

object PazDataSerializers {
    @JvmField val DATA_PAINT_COLORS = EntityDataSerializer.forValueType<Map<Int, Int>>(ByteBufCodecs.map(::HashMap, ByteBufCodecs.INT, ByteBufCodecs.INT))
    @JvmField val DATA_DYE_COLOR = EntityDataSerializer.forValueType<DyeColor>(DyeColor.STREAM_CODEC)
    @JvmField val DATA_PLANT_STATE = EntityDataSerializer.forValueType<PlantState>(PlantState.STREAM_CODEC)
    @JvmField val DATA_ZOMBIE_STATE = EntityDataSerializer.forValueType<ZombieState>(ZombieState.STREAM_CODEC)
    @JvmField val DATA_COOLDOWN = EntityDataSerializer.forValueType<Int>(ByteBufCodecs.VAR_INT)
    @JvmField val DATA_RECEIVED_SUN = EntityDataSerializer.forValueType<Int>(ByteBufCodecs.VAR_INT)
    @JvmField val DATA_RECEIVED_WATER = EntityDataSerializer.forValueType<Int>(ByteBufCodecs.VAR_INT)
    @JvmField val DATA_SWELL_DIR = EntityDataSerializer.forValueType<Int>(ByteBufCodecs.VAR_INT)
    @JvmField val DATA_SEED_GROW_COOLDOWN = EntityDataSerializer.forValueType<Int>(ByteBufCodecs.VAR_INT)
    @JvmField val DATA_COFFEE_BUFF = EntityDataSerializer.forValueType<Int>(ByteBufCodecs.VAR_INT)
    @JvmField val DATA_SLEEPING = EntityDataSerializer.forValueType<Boolean>(ByteBufCodecs.BOOL)
    @JvmField val GNOME_VARIANT = EntityDataSerializer.forValueType<GnomeVariant>(GnomeVariant.STREAM_CODEC)
    @JvmField val GNOME_SOUND_VARIANT = EntityDataSerializer.forValueType<GnomeSoundVariant>(GnomeSoundVariant.STREAM_CODEC)

    fun initialize() {
        FabricEntityDataRegistry.register(pazResource("paint_colors"), DATA_PAINT_COLORS)
        FabricEntityDataRegistry.register(pazResource("dye_color"), DATA_DYE_COLOR)
        FabricEntityDataRegistry.register(pazResource("plant_state"), DATA_PLANT_STATE)
        FabricEntityDataRegistry.register(pazResource("zombie_state"), DATA_ZOMBIE_STATE)
        FabricEntityDataRegistry.register(pazResource("cooldown"), DATA_COOLDOWN)
        FabricEntityDataRegistry.register(pazResource("received_sun"), DATA_RECEIVED_SUN)
        FabricEntityDataRegistry.register(pazResource("received_water"), DATA_RECEIVED_WATER)
        FabricEntityDataRegistry.register(pazResource("swell_dir"), DATA_SWELL_DIR)
        FabricEntityDataRegistry.register(pazResource("seed_grow_cooldown"), DATA_SEED_GROW_COOLDOWN)
        FabricEntityDataRegistry.register(pazResource("coffe_buff"), DATA_COFFEE_BUFF)
        FabricEntityDataRegistry.register(pazResource("sleeping"), DATA_SLEEPING)
        FabricEntityDataRegistry.register(pazResource("gnome_variant"), GNOME_VARIANT)
        FabricEntityDataRegistry.register(pazResource("gnome_sound_variant"), GNOME_SOUND_VARIANT)
    }
}