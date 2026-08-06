package duskdn.plantz.tabs

import com.mojang.serialization.Codec
import duskdn.plantz.init.PazBlocks
import duskdn.plantz.init.PazComponents
import duskdn.plantz.init.PazComponents.PLACEHOLDER_INDEX
import duskdn.plantz.init.PazCreativeTab
import duskdn.plantz.init.PazEntities
import duskdn.plantz.item.SeedPacketItem
import duskdn.plantz.util.pazResource
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack

object PazPlantsTab : PazCreativeTab() {

    override var tab_key: String = "plantz_plant_tab"

    var placeholderCount = 0

    override fun initialize() {

         paz_tab = Registry.register(
            BuiltInRegistries.CREATIVE_MODE_TAB,
             tab_key,
            CreativeModeTab.builder(CreativeModeTab.Row.BOTTOM, 0)
                .title(Component.translatable("itemGroup.plantz.$tab_key"))
                .icon { ItemStack(PazBlocks.PLANTZ_FLAG) }

                .displayItems { parameters, output ->

                    fun addPlant(type: EntityType<*>) {

                        val stack = SeedPacketItem.stackFor(type)

                        if (type == PazEntities.PLACEHOLDER) {
                            stack.set(PLACEHOLDER_INDEX, placeholderCount++)
                        }

                        output.accept(stack)
                    }

                    // region BASE GAME
//
//                    addPlant(PazEntities.SUNFLOWER)
//                    addPlant(PazEntities.PEA_SHOOTER)
//                    addPlant(PazEntities.CHERRY_BOMB)
//                    addPlant(PazEntities.WALL_NUT)
//                    addPlant(PazEntities.POTATO_MINE)
//                    addPlant(PazEntities.ICE_PEA_SHOOTER)
//                    addPlant(PazEntities.CHOMPER)
//                    addPlant(PazEntities.REPEATER)
//
//                    addPlant(PazEntities.PUFF_SHROOM)
//                    addPlant(PazEntities.SUN_SHROOM)
//                    addPlant(PazEntities.FUME_SHROOM)
//                    addPlant(PazEntities.PLACEHOLDER)
//                    addPlant(PazEntities.HYPNOSHROOM)
//                    addPlant(PazEntities.SCAREDY_SHROOM)
//                    addPlant(PazEntities.PLACEHOLDER)
//                    addPlant(PazEntities.DOOM_SHROOM)
//
//                    addPlant(PazEntities.LILYPAD)
//                    addPlant(PazEntities.PLACEHOLDER)
//                    addPlant(PazEntities.PLACEHOLDER)
//                    addPlant(PazEntities.TANGLE_KELP)
//                    addPlant(PazEntities.PLACEHOLDER)
//                    addPlant(PazEntities.PLACEHOLDER)
//                    addPlant(PazEntities.PLACEHOLDER)
//                    addPlant(PazEntities.PLACEHOLDER)
//
//
//                    addPlant(PazEntities.SEA_SHROOM)
//                    addPlant(PazEntities.PLACEHOLDER)
//                    addPlant(PazEntities.CACTUS)
//                    addPlant(PazEntities.PLACEHOLDER)
//                    addPlant(PazEntities.PLACEHOLDER)
//                    addPlant(PazEntities.PLACEHOLDER)
//                    addPlant(PazEntities.PLACEHOLDER)
//                    addPlant(PazEntities.PLACEHOLDER)
//
//                    addPlant(PazEntities.CABBAGE_PULT)
//                    addPlant(PazEntities.FLOWER_POT)
//                    addPlant(PazEntities.KERNEL_PULT)
//                    addPlant(PazEntities.COFFEE_BEAN)
//                    addPlant(PazEntities.PLACEHOLDER)
//                    addPlant(PazEntities.PLACEHOLDER)
//                    addPlant(PazEntities.PLACEHOLDER)
//                    addPlant(PazEntities.MELON_PULT)
//                    addPlant(PazEntities.LIGHTNING_REED)
//
//                    addPlant(PazEntities.EXPLODE_O_NUT)
//                    addPlant(PazEntities.FIRE_PEA_SHOOTER)
//                    addPlant(PazEntities.WATER_PEA_SHOOTER)
//                    addPlant(PazEntities.ELECTRIC_PEA_SHOOTER)
//                    addPlant(PazEntities.WATER_POT)
//                    addPlant(PazEntities.LAVALILY)
//
                    //endregion

                    // region MORE ORGANIZED

                    // region SUNFLOWER PLANTS
                    addPlant(PazEntities.SUNFLOWER)
                    // endregion

                    // region INSTANT PLANTS
                    addPlant(PazEntities.CHERRY_BOMB)
                    addPlant(PazEntities.POTATO_MINE)
                    addPlant(PazEntities.CHOMPER)
                    // endregion

                    // region DEFENSIVE PLANTS
                    addPlant(PazEntities.WALL_NUT)
                    // endregion

                    // region BASIC PLANTS
                    addPlant(PazEntities.PEA_SHOOTER)
                    addPlant(PazEntities.REPEATER)
                    // endregion

                    // region ICE PLANTS
                    addPlant(PazEntities.ICE_PEA_SHOOTER)
                    addPlant(PazEntities.PLACEHOLDER) // Iceberg Lettuce
                    // endregion

                    // region ELECTRIC PLANTS
                    addPlant(PazEntities.ELECTRIC_PEA_SHOOTER)
                    addPlant(PazEntities.LIGHTNING_REED)
                    // endregion

                    // region AIR PLANTS
                    addPlant(PazEntities.PLACEHOLDER) // Sky Shooter
                    addPlant(PazEntities.PLACEHOLDER) // Floater Pot
                    addPlant(PazEntities.PLACEHOLDER) // Blover
                    // endregion

                    // region SHARP PLANTS
                    addPlant(PazEntities.CACTUS)
                    addPlant(PazEntities.PLACEHOLDER) // Spikeweed
                    addPlant(PazEntities.PLACEHOLDER) // Starfruit
                    // endregion

                    // region MUSHROOMS
                    addPlant(PazEntities.PUFF_SHROOM)
                    addPlant(PazEntities.SUN_SHROOM)
                    addPlant(PazEntities.FUME_SHROOM)
                    addPlant(PazEntities.HYPNOSHROOM)
                    addPlant(PazEntities.SCAREDY_SHROOM)
                    addPlant(PazEntities.PLACEHOLDER) // Ice Shroom
                    addPlant(PazEntities.DOOM_SHROOM)
                    addPlant(PazEntities.PLACEHOLDER) // Fire Shroom
                    addPlant(PazEntities.PLACEHOLDER) // Crimson Shroom
                    addPlant(PazEntities.PLACEHOLDER) // Warped Shroom
                    // endregion

                    // region AQUATIC PLANTS
                    addPlant(PazEntities.LILYPAD)
                    addPlant(PazEntities.TANGLE_KELP)
                    addPlant(PazEntities.SEA_SHROOM)
                    addPlant(PazEntities.WATER_PEA_SHOOTER)
                    addPlant(PazEntities.WATER_POT)
                    // endregion

                    // region FIRE PLANTS
                    addPlant(PazEntities.FIRE_PEA_SHOOTER)
                    // endregion

                    // region LAVA PLANTS
                    addPlant(PazEntities.LAVALILY)
                    // endregion

                    // region PULT PLANTS
                    addPlant(PazEntities.FLOWER_POT)
                    addPlant(PazEntities.CABBAGE_PULT)
                    addPlant(PazEntities.KERNEL_PULT)
                    addPlant(PazEntities.MELON_PULT)
                    // endregion

                    // region MISC PLANTS
                    addPlant(PazEntities.BONK_CHOY)
                    addPlant(PazEntities.EXPLODE_O_NUT)
                    addPlant(PazEntities.COFFEE_BEAN)
                    addPlant(PazEntities.PLACEHOLDER) // Warped Shroom
                    // endregion

                    //endregion

                }
                .build()
        )

    }
}