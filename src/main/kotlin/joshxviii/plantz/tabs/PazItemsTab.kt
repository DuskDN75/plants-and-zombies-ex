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

object PazItemsTab : PazCreativeTab() {

    override var tab_key: String = "plantz_item_tab"

    override fun initialize() {

         paz_tab = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
             tab_key,
            CreativeModeTab.builder(CreativeModeTab.Row.BOTTOM, 0)
                .title(Component.translatable("itemGroup.plantz.$tab_key"))
                .icon { ItemStack(PazItems.SUN) }

                .displayItems { parameters, output ->

                    //music
                    output.accept(PazItems.MUSIC_DISC_GRASSY_GROOVE)

                    // items
                    if (parameters.hasPermissions()) output.accept(PazBlocks.TIME_MACHINE)
                    output.accept(PazItems.BRAINZ_ALLOY)
                    output.accept(PazItems.SUN_BATTERY)
                    output.accept(PazItems.SUN)
                    output.accept(PazItems.SUN_BOTTLE)
                    output.accept(PazItems.WATERING_CAN)
                    output.accept(PazBlocks.PLANT_POT)
                    output.accept(PazBlocks.WATER_POT)
                    output.accept(PazBlocks.ZEN_PLANT_POT)
                    output.accept(PazItems.PLANT_POT_MINECART)
                    output.accept(PazItems.PLANT_POT_HELMET)
                    output.accept(PazItems.DUCKY_TUBE)
                    output.accept(PazBlocks.CONE)
                    output.accept(PazItems.NEWSPAPER)
                    output.accept(PazItems.FOOTBALL_HELMET)
                    output.accept(PazItems.DYE_BLASTER)
                    output.accept(PazBlocks.BRAINZ_FLAG)
                    output.accept(PazBlocks.PLANTZ_FLAG)

                    // balloons
                    PazItems.balloonByColor.forEach { output.accept(it.value) }

                    // mailboxes
                    PazBlocks.mailboxByColor.forEach { output.accept(it.value) }

                    // other
                    output.accept(PazBlocks.GRAVESTONE)
                }
                .build()
        )

    }
}