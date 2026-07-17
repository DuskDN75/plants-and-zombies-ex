package joshxviii.plantz.init

import joshxviii.plantz.item.SeedPacketItem
import joshxviii.plantz.util.pazResource
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack

abstract class PazCreativeTab {
    // Define the key for the custom tab
    companion object {
        val paz_plant_tab_key: ResourceKey<CreativeModeTab> =
            ResourceKey.create(Registries.CREATIVE_MODE_TAB, pazResource("plantz_plant_tab"))
        val paz_zombie_tab_key: ResourceKey<CreativeModeTab> =
            ResourceKey.create(Registries.CREATIVE_MODE_TAB, pazResource("plantz_zombie_tab"))
        val paz_item_tab_key: ResourceKey<CreativeModeTab> =
            ResourceKey.create(Registries.CREATIVE_MODE_TAB, pazResource("plantz_item_tab"))
    }

    fun getTabKey(): ResourceKey<CreativeModeTab> {
        return ResourceKey.create(Registries.CREATIVE_MODE_TAB, pazResource(tab_key))
    }

    abstract var tab_key: String

    lateinit var paz_tab: CreativeModeTab

    open fun initialize() {}
}