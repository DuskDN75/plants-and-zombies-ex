package duskdn.plantz_ex.tabs

import duskdn.plantz_ex.init.PazBlocks
import duskdn.plantz_ex.init.PazCreativeTab
import duskdn.plantz_ex.init.PazItems
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack

object PazZombiesTab : PazCreativeTab() {

    override var tab_key: String = "plantz_zombie_tab"

    override fun initialize() {

         paz_tab = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
             tab_key,
            CreativeModeTab.builder(CreativeModeTab.Row.BOTTOM, 0)
                .title(Component.translatable("itemGroup.plantz_ex.$tab_key"))
                .icon { ItemStack(PazBlocks.BRAINZ_FLAG) }

                .displayItems { parameters, output ->

                    // zombie spawn eggs

                    PazItems.BROWN_COAT_SPAWN_EGGS.forEach { output.accept(it) }

                    output.accept(PazItems.NEWSPAPER_ZOMBIE_SPAWN_EGG)
                    output.accept(PazItems.DISCO_ZOMBIE_SPAWN_EGG)
                    output.accept(PazItems.BACKUP_DANCER_SPAWN_EGG)
                    output.accept(PazItems.ALL_STAR_SPAWN_EGG)

                    output.accept(PazItems.DIGGER_ZOMBIE_SPAWN_EGG)
                    output.accept(PazItems.ENGINEER_ZOMBIE_SPAWN_EGG)
                    output.accept(PazItems.ZOMBIE_YETI_SPAWN_EGG)

                    output.accept(PazItems.SOLDIER_ZOMBIE_SPAWN_EGG)
                    output.accept(PazItems.ROBO_ZOMBIE_SPAWN_EGG)

                    PazItems.BALLOON_ZOMBIE_SPAWN_EGGS.forEach { output.accept(it) }

                    output.accept(PazItems.IMP_SPAWN_EGG)
                    output.accept(PazItems.GARGANTUAR_SPAWN_EGG)

                    output.accept(PazItems.SUPER_BRAINZ_SPAWN_EGG)
                    output.accept(PazItems.PIRATE_CAPTAIN_SPAWN_EGG)

                    // gnome
                    if (parameters.hasPermissions()) {

                        output.accept(PazItems.GNOME_SPAWN_EGG)

                        PazItems.BROWN_COAT_SCREEN_DOOR_VARIANTS_SPAWN_EGGS.forEach { output.accept(it) }

                        PazItems.BROWN_COAT_FLAG_VARIANTS_SPAWN_EGGS.forEach { output.accept(it) }

                        PazItems.BROWN_COAT_FLAG_SCREEN_DOOR_VARIANTS_SPAWN_EGGS.forEach { output.accept(it) }

                    }
                }
                .build()
        )

    }
}