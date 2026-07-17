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

object PazZombiesTab : PazCreativeTab() {

    override var tab_key: String = "plantz_zombie_tab"

    override fun initialize() {

         paz_tab = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
             tab_key,
            CreativeModeTab.builder(CreativeModeTab.Row.BOTTOM, 0)
                .title(Component.translatable("itemGroup.plantz.$tab_key"))
                .icon { ItemStack(PazItems.SUN) }

                .displayItems { parameters, output ->

                    // zombie spawn eggs
                    output.accept(PazItems.BROWN_COAT_SPAWN_EGG)
                    output.accept(PazItems.NEWSPAPER_ZOMBIE_SPAWN_EGG)
                    output.accept(PazItems.DIGGER_ZOMBIE_SPAWN_EGG)
                    output.accept(PazItems.ENGINEER_ZOMBIE_SPAWN_EGG)
                    output.accept(PazItems.ZOMBIE_YETI_SPAWN_EGG)
                    output.accept(PazItems.DISCO_ZOMBIE_SPAWN_EGG)
                    output.accept(PazItems.BACKUP_DANCER_SPAWN_EGG)
                    output.accept(PazItems.ALL_STAR_SPAWN_EGG)
                    output.accept(PazItems.SOLDIER_ZOMBIE_SPAWN_EGG)
                    output.accept(PazItems.ROBO_ZOMBIE_SPAWN_EGG)
                    output.accept(PazItems.SUPER_BRAINZ_SPAWN_EGG)
                    output.accept(PazItems.IMP_SPAWN_EGG)
                    output.accept(PazItems.GARGANTUAR_SPAWN_EGG)

                    // gnome
                    if (parameters.hasPermissions()) output.accept(PazItems.GNOME_SPAWN_EGG)
                }
                .build()
        )

    }
}