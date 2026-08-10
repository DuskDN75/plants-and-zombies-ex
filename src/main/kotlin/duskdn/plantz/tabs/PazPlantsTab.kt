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

                    fun addPlant(type: EntityType<*>, customName: String = "placeholder") {

                        val stack = SeedPacketItem.stackFor(type)

                        if (type == PazEntities.PLACEHOLDER) {
                            stack.set(PLACEHOLDER_INDEX, placeholderCount++)
                            stack.set(DataComponents.CUSTOM_NAME, Component.translatable("entity.plantz.$customName"))
                        }

                        output.accept(stack)
                    }

                    // region MORE ORGANIZED

                    // region SUNFLOWER PLANTS
                    addPlant(PazEntities.SUNFLOWER)
                    addPlant(PazEntities.PLACEHOLDER,"twin_sunflower") // TWIN SUNFLOWER
                    // endregion

                    // region INSTANT PLANTS
                    addPlant(PazEntities.CHERRY_BOMB)
                    addPlant(PazEntities.POTATO_MINE)
                    addPlant(PazEntities.CHOMPER)
                    addPlant(PazEntities.PLACEHOLDER,"jalapeno") // JALAPENO
                    addPlant(PazEntities.PLACEHOLDER,"cob_cannon") // Cob Cannon
                    // endregion

                    // region DEFENSIVE PLANTS
                    addPlant(PazEntities.WALL_NUT)
                    addPlant(PazEntities.PLACEHOLDER, "tallnut") // TALL NUT
                    addPlant(PazEntities.PLACEHOLDER, "pumpkin") // PUMPKIN
                    addPlant(PazEntities.EXPLODE_O_NUT)
                    // endregion

                    // region BASIC PLANTS
                    addPlant(PazEntities.PEA_SHOOTER)
                    addPlant(PazEntities.REPEATER)
                    addPlant(PazEntities.PLACEHOLDER,"gatling_pea") // GATLING PEA
                    addPlant(PazEntities.PLACEHOLDER, "threepeater") // THREEPEATER
                    addPlant(PazEntities.PLACEHOLDER, "split_pea") // SPLIT PEA
                    addPlant(PazEntities.PLACEHOLDER, "starfruit") // Starfruit
                    // endregion

                    // region ICE PLANTS
                    addPlant(PazEntities.ICE_PEA_SHOOTER)
                    addPlant(PazEntities.PLACEHOLDER, "iceberg_lettuce") // Iceberg Lettuce
                    addPlant(PazEntities.PLACEHOLDER, "hurrikale") // Hurrikale
                    // endregion

                    // region ELECTRIC PLANTS
                    addPlant(PazEntities.ELECTRIC_PEA_SHOOTER)
                    addPlant(PazEntities.LIGHTNING_REED)
                    // endregion

                    // region AIR PLANTS
                    addPlant(PazEntities.SKY_PEA_SHOOTER) // Sky Shooter
                    addPlant(PazEntities.PLACEHOLDER, "floater_pot") // Floater Pot
                    addPlant(PazEntities.PLACEHOLDER, "blover") // Blover
                    // endregion

                    // region SHARP PLANTS
                    addPlant(PazEntities.CACTUS)
                    addPlant(PazEntities.PLACEHOLDER, "spikeweed") // Spikeweed
                    addPlant(PazEntities.PLACEHOLDER, "spikerock") // Spike Rock
                    // endregion

                    // region MUSHROOMS
                    addPlant(PazEntities.PUFF_SHROOM)
                    addPlant(PazEntities.SUN_SHROOM)
                    addPlant(PazEntities.FUME_SHROOM)
                    addPlant(PazEntities.HYPNOSHROOM)
                    addPlant(PazEntities.SCAREDY_SHROOM)
                    addPlant(PazEntities.ICE_SHROOM)
                    addPlant(PazEntities.DOOM_SHROOM)
                    addPlant(PazEntities.PLACEHOLDER, "magnetshroom") // MAGNET SHROOM
                    addPlant(PazEntities.PLACEHOLDER, "fireshroom") // Fire Shroom
                    addPlant(PazEntities.PLACEHOLDER, "crimsonshroom") // Crimson Shroom
                    addPlant(PazEntities.PLACEHOLDER, "warpedshroom") // Warped Shroom
                    addPlant(PazEntities.PLACEHOLDER, "gloomshroom") // Gloom Shroom
                    // endregion

                    // region AQUATIC PLANTS
                    addPlant(PazEntities.LILYPAD)
                    addPlant(PazEntities.TANGLE_KELP)
                    addPlant(PazEntities.SEA_SHROOM)
                    addPlant(PazEntities.WATER_PEA_SHOOTER)
                    addPlant(PazEntities.WATER_POT)
                    addPlant(PazEntities.PLACEHOLDER, "cattail") // CATTAIL
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
                    addPlant(PazEntities.PLACEHOLDER, "winter_melon") // WINTER MELON
                    // endregion

                    // region MISC PLANTS
                    addPlant(PazEntities.COFFEE_BEAN)
                    addPlant(PazEntities.GRAVE_BUSTER)
                    addPlant(PazEntities.PLACEHOLDER, "torchwood") // TORCHWOOD
                    addPlant(PazEntities.PLACEHOLDER, "plantern") // PLANTERN
                    addPlant(PazEntities.PLACEHOLDER, "garlic") // GARLIC
                    addPlant(PazEntities.PLACEHOLDER, "umbrella_leaf") // UMBRELLA LEAF
                    addPlant(PazEntities.PLACEHOLDER, "marigold") // MARIGOLD
                    addPlant(PazEntities.PLACEHOLDER, "gold_magnet") // Gold Magnet
                    // endregion

                    //endregion

                }
                .build()
        )

    }
}