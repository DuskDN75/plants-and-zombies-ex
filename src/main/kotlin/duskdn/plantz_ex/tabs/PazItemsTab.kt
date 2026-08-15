package duskdn.plantz_ex.tabs

import duskdn.plantz_ex.init.PazBlocks
import duskdn.plantz_ex.init.PazCreativeTab
import duskdn.plantz_ex.init.PazItems
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack

object PazItemsTab : PazCreativeTab() {

    override var tab_key: String = "plantz_item_tab"

    override fun initialize() {

         paz_tab = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
             tab_key,
            CreativeModeTab.builder(CreativeModeTab.Row.BOTTOM, 0)
                .title(Component.translatable("itemGroup.plantz_ex.$tab_key"))
                .icon { ItemStack(PazItems.SUN) }

                .displayItems { parameters, output ->

                    //music
                    output.accept(PazItems.MUSIC_DISC_GRASSY_GROOVE)

                    // items
                    output.accept(PazItems.BRAINZ_ALLOY)
                    output.accept(PazItems.SUN)
                    output.accept(PazItems.SUN_BOTTLE)
                    output.accept(PazItems.WATERING_CAN)
                    output.accept(PazItems.PLANT_POT_MINECART)
                    output.accept(PazItems.PLANT_POT_HELMET)
                    output.accept(PazItems.DUCKY_TUBE)
                    output.accept(PazItems.OBSIDIAN_DUCKY_TUBE)
                    output.accept(PazBlocks.CONE)
                    output.accept(PazItems.NEWSPAPER)
                    output.accept(PazItems.FOOTBALL_HELMET)
                    output.accept(PazItems.DYE_BLASTER)

                    // balloons
                    PazItems.BALLOONS.forEach { output.accept(it.value) }
                }
                .build()
        )

    }
}