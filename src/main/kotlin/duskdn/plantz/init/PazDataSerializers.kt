package duskdn.plantz.init

import duskdn.plantz.ai.PlantState
import duskdn.plantz.ai.ZombieState
import duskdn.plantz.entity.gnome.GnomeSoundVariant
import duskdn.plantz.entity.gnome.GnomeVariant
import duskdn.plantz.entity.zombie.BrownCoatVariant
import duskdn.plantz.entity.zombie.ImpVariant
import duskdn.plantz.entity.zombie.SuperBrainzVariant
import duskdn.plantz.util.pazResource
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityDataRegistry
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.syncher.EntityDataSerializer
import net.minecraft.world.item.DyeColor

object PazDataSerializers {
    @JvmField val DATA_PAINT_COLORS = EntityDataSerializer.forValueType<Map<Int, Int>>(ByteBufCodecs.map(::HashMap, ByteBufCodecs.INT, ByteBufCodecs.INT))
    @JvmField val DATA_DYE_COLOR = EntityDataSerializer.forValueType<DyeColor>(DyeColor.STREAM_CODEC)
    @JvmField val DATA_PLANT_STATE = EntityDataSerializer.forValueType<PlantState>(PlantState.STREAM_CODEC)
    @JvmField val DATA_ZOMBIE_STATE = EntityDataSerializer.forValueType<ZombieState>(ZombieState.STREAM_CODEC)
    @JvmField val DATA_COOLDOWN = EntityDataSerializer.forValueType<Int>(ByteBufCodecs.VAR_INT)
    @JvmField val DATA_RECEIVED_SUN = EntityDataSerializer.forValueType<Int>(ByteBufCodecs.VAR_INT)
    @JvmField val DATA_RECEIVED_WATER = EntityDataSerializer.forValueType<Int>(ByteBufCodecs.VAR_INT)
    @JvmField val DATA_SWELL_OLD = EntityDataSerializer.forValueType<Int>(ByteBufCodecs.VAR_INT)
    @JvmField val DATA_SWELL = EntityDataSerializer.forValueType<Int>(ByteBufCodecs.VAR_INT)
    @JvmField val DATA_SEED_GROW_COOLDOWN = EntityDataSerializer.forValueType<Int>(ByteBufCodecs.VAR_INT)
    @JvmField val DATA_COFFEE_BUFF = EntityDataSerializer.forValueType<Int>(ByteBufCodecs.VAR_INT)
    @JvmField val DATA_SLEEPING = EntityDataSerializer.forValueType<Boolean>(ByteBufCodecs.BOOL)
    @JvmField val DATA_POWERED_UP = EntityDataSerializer.forValueType<Boolean>(ByteBufCodecs.BOOL)
    @JvmField val DATA_ACTIVE = EntityDataSerializer.forValueType<Boolean>(ByteBufCodecs.BOOL)
    @JvmField val BROWN_COAT_VARIANT = EntityDataSerializer.forValueType<BrownCoatVariant>(BrownCoatVariant.STREAM_CODEC)
    @JvmField val IMP_VARIANT = EntityDataSerializer.forValueType<ImpVariant>(ImpVariant.STREAM_CODEC)
    @JvmField val SUPER_BRAINZ_VARIANT = EntityDataSerializer.forValueType<SuperBrainzVariant>(SuperBrainzVariant.STREAM_CODEC)
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
        FabricEntityDataRegistry.register(pazResource("swell_old"), DATA_SWELL_OLD)
        FabricEntityDataRegistry.register(pazResource("swell"), DATA_SWELL)
        FabricEntityDataRegistry.register(pazResource("active"), DATA_ACTIVE)
        FabricEntityDataRegistry.register(pazResource("seed_grow_cooldown"), DATA_SEED_GROW_COOLDOWN)
        FabricEntityDataRegistry.register(pazResource("coffe_buff"), DATA_COFFEE_BUFF)
        FabricEntityDataRegistry.register(pazResource("sleeping"), DATA_SLEEPING)
        FabricEntityDataRegistry.register(pazResource("powered_up"), DATA_POWERED_UP)
        FabricEntityDataRegistry.register(pazResource("brown_coat_variant"), BROWN_COAT_VARIANT)
        FabricEntityDataRegistry.register(pazResource("imp_variant"), IMP_VARIANT)
        FabricEntityDataRegistry.register(pazResource("super_brainz_variant"), SUPER_BRAINZ_VARIANT)
        FabricEntityDataRegistry.register(pazResource("gnome_variant"), GNOME_VARIANT)
        FabricEntityDataRegistry.register(pazResource("gnome_sound_variant"), GNOME_SOUND_VARIANT)
    }
}