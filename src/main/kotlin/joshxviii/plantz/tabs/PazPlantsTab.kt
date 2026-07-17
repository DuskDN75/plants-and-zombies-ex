package joshxviii.plantz.tabs

import joshxviii.plantz.init.PazBlocks
import joshxviii.plantz.init.PazCreativeTab
import joshxviii.plantz.init.PazEntities
import joshxviii.plantz.init.PazItems
import joshxviii.plantz.item.SeedPacketItem
import joshxviii.plantz.util.pazResource
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack

object PazPlantsTab : PazCreativeTab() {

    override var tab_key: String = "plantz_plant_tab"

    override fun initialize() {

         paz_tab = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
             tab_key,
            CreativeModeTab.builder(CreativeModeTab.Row.BOTTOM, 0)
                .title(Component.translatable("itemGroup.plantz.$tab_key"))
                .icon { ItemStack(PazItems.SUN) }

                .displayItems { parameters, output ->

                    // seed packets
                    output.accept(SeedPacketItem.stackFor(PazEntities.SUNFLOWER))
                    output.accept(SeedPacketItem.stackFor(PazEntities.PEA_SHOOTER))
                    output.accept(SeedPacketItem.stackFor(PazEntities.WALL_NUT))
                    output.accept(SeedPacketItem.stackFor(PazEntities.EXPLODE_O_NUT))
                    output.accept(SeedPacketItem.stackFor(PazEntities.CHOMPER))
                    output.accept(SeedPacketItem.stackFor(PazEntities.CHERRY_BOMB))
                    output.accept(SeedPacketItem.stackFor(PazEntities.POTATO_MINE))
                    output.accept(SeedPacketItem.stackFor(PazEntities.REPEATER))
                    output.accept(SeedPacketItem.stackFor(PazEntities.ICE_PEA_SHOOTER))
                    output.accept(SeedPacketItem.stackFor(PazEntities.FIRE_PEA_SHOOTER))
                    output.accept(SeedPacketItem.stackFor(PazEntities.ELECTRIC_PEA_SHOOTER))
                    output.accept(SeedPacketItem.stackFor(PazEntities.CACTUS))
                    output.accept(SeedPacketItem.stackFor(PazEntities.LIGHTNING_REED))
                    output.accept(SeedPacketItem.stackFor(PazEntities.CABBAGE_PULT))
                    output.accept(SeedPacketItem.stackFor(PazEntities.KERNEL_PULT))
                    output.accept(SeedPacketItem.stackFor(PazEntities.MELON_PULT))
                    output.accept(SeedPacketItem.stackFor(PazEntities.BONK_CHOY))
                    output.accept(SeedPacketItem.stackFor(PazEntities.TANGLE_KELP))
                    output.accept(SeedPacketItem.stackFor(PazEntities.SUN_SHROOM))
                    output.accept(SeedPacketItem.stackFor(PazEntities.PUFF_SHROOM))
                    output.accept(SeedPacketItem.stackFor(PazEntities.FUME_SHROOM))
                    output.accept(SeedPacketItem.stackFor(PazEntities.SCAREDY_SHROOM))
                    output.accept(SeedPacketItem.stackFor(PazEntities.HYPNOSHROOM))
                    output.accept(SeedPacketItem.stackFor(PazEntities.DOOM_SHROOM))
                    output.accept(SeedPacketItem.stackFor(PazEntities.LILYPAD))
                    output.accept(SeedPacketItem.stackFor(PazEntities.SEA_SHROOM))
                    output.accept(SeedPacketItem.stackFor(PazEntities.COFFEE_BEAN))
                }
                .build()
        )

    }
}