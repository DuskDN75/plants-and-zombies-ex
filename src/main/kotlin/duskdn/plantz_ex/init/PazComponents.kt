package duskdn.plantz_ex.init

import com.mojang.serialization.Codec
import duskdn.plantz_ex.item.component.BlocksProjectileDamage
import duskdn.plantz_ex.item.component.StoredSun
import duskdn.plantz_ex.item.component.StoredWater
import duskdn.plantz_ex.item.component.SunCost
import duskdn.plantz_ex.util.pazResource
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import java.util.function.UnaryOperator

object PazComponents {

    @JvmField
    val PLACEHOLDER_INDEX: DataComponentType<Int> = register(
        "placeholder_index",
    ) { b: DataComponentType.Builder<Int> ->
        DataComponentType.builder<Int>()
            .persistent(Codec.INT)
            .cacheEncoding()
    }

    @JvmField
    val SUN_COST: DataComponentType<SunCost> = register(
        "sun_cost"
    ) { b: DataComponentType.Builder<SunCost> ->
        DataComponentType.builder<SunCost>()
            .persistent(SunCost.CODEC)
            .networkSynchronized(SunCost.STREAM_CODEC)
            .cacheEncoding()
    }

    @JvmField
    val STORED_WATER: DataComponentType<StoredWater> = register(
        "stored_water"
    ) { b: DataComponentType.Builder<StoredWater> ->
        DataComponentType.builder<StoredWater>()
            .persistent(StoredWater.CODEC)
            .networkSynchronized(StoredWater.STREAM_CODEC)
            .cacheEncoding()
    }

    @JvmField
    val STORED_SUN: DataComponentType<StoredSun> = register(
        "stored_sun"
    ) { b: DataComponentType.Builder<StoredSun> ->
        DataComponentType.builder<StoredSun>()
            .persistent(StoredSun.CODEC)
            .networkSynchronized(StoredSun.STREAM_CODEC)
            .cacheEncoding()
    }

    @JvmField
    val BLOCKS_PROJECTILE_DAMAGE: DataComponentType<BlocksProjectileDamage> = register(
        "blocks_projectile_damage"
    ) { b: DataComponentType.Builder<BlocksProjectileDamage> ->
        DataComponentType.builder<BlocksProjectileDamage>()
            .persistent(BlocksProjectileDamage.CODEC)
            .networkSynchronized(BlocksProjectileDamage.STREAM_CODEC)
            .cacheEncoding()
    }

    private fun <T : Any> register(name: String, builder: UnaryOperator<DataComponentType.Builder<T>>): DataComponentType<T> {
        val key = ResourceKey.create(Registries.DATA_COMPONENT_TYPE, pazResource(name))
        return Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            key,
            builder.apply(DataComponentType.builder()).build()
        )
    }

    fun initialize() {}
}