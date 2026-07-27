package duskdn.plantz.init

import duskdn.plantz.util.pazResource
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.CreativeModeTab

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