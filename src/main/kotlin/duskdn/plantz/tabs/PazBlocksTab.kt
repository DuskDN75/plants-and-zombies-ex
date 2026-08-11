package duskdn.plantz.tabs

import duskdn.plantz.init.PazBlocks
import duskdn.plantz.init.PazCreativeTab
import duskdn.plantz.init.PazItems
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack

object PazBlocksTab : PazCreativeTab() {

    override var tab_key: String = "plantz_block_tab"

    override fun initialize() {

         paz_tab = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
             tab_key,
            CreativeModeTab.builder(CreativeModeTab.Row.BOTTOM, 0)
                .title(Component.translatable("itemGroup.plantz.$tab_key"))
                .icon { ItemStack(PazBlocks.BRAINZ_ALLOY_BLOCK) }

                .displayItems { parameters, output ->

                    output.accept(PazBlocks.ZEN_POT)
                    output.accept(PazBlocks.TIME_MACHINE)
                    output.accept(PazItems.SUN_BATTERY)

                    output.accept(PazBlocks.BRAINZ_FLAG)
                    output.accept(PazBlocks.PLANTZ_FLAG)

                    output.accept(PazBlocks.BRAINZ_ALLOY_BLOCK)
                    output.accept(PazBlocks.BRAINZ_ALLOY_SLAB)
                    output.accept(PazBlocks.BRAINZ_ALLOY_STAIRS)
                    output.accept(PazBlocks.REINFORCED_BRAINZ_ALLOY_BLOCK)
                    output.accept(PazBlocks.SMOOTH_BRAINZ_ALLOY_BLOCK)
                    output.accept(PazBlocks.TREADED_BRAINZ_ALLOY_BLOCK)

                    output.accept(PazBlocks.SCREEN_DOOR)

                    PazBlocks.GARDEN_GNOMES.forEach { output.accept(it.value) }

                    // mailboxes
                    PazBlocks.MAILBOXES.forEach { output.accept(it.value) }

                    // other
                    output.accept(PazBlocks.GRAVESTONE)
                }
                .build()
        )

    }
}