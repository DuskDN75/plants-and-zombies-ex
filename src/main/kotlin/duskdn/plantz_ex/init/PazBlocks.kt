package duskdn.plantz_ex.init

import duskdn.plantz_ex.block.ConeBlock
import duskdn.plantz_ex.block.FlagBlock
import duskdn.plantz_ex.block.GardenGnomeBlock
import duskdn.plantz_ex.block.GravestoneBlock
import duskdn.plantz_ex.block.MailboxBlock
import duskdn.plantz_ex.block.ScreenDoorBlock
import duskdn.plantz_ex.block.SunBatteryBlock
import duskdn.plantz_ex.block.TimeMachineBlock
import duskdn.plantz_ex.block.WateringCanBlock
import duskdn.plantz_ex.block.ZenPotBlock
import duskdn.plantz_ex.block.entity.FlagBlockEntity
import duskdn.plantz_ex.block.entity.GravestoneBlockEntity
import duskdn.plantz_ex.block.entity.MailboxBlockEntity
import duskdn.plantz_ex.block.entity.SunBatteryBlockEntity
import duskdn.plantz_ex.block.entity.TimeMachineBlockEntity
import duskdn.plantz_ex.entity.plant.init.PazPlant
import duskdn.plantz_ex.item.ScreenDoorItem
import duskdn.plantz_ex.item.component.BlocksProjectileDamage
import duskdn.plantz_ex.util.pazResource
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.fabricmc.fabric.api.`object`.builder.v1.world.poi.PoiHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.component.ItemAttributeModifiers
import net.minecraft.world.item.equipment.Equippable
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.LightBlock
import net.minecraft.world.level.block.SlabBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.StairBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockSetType
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.material.PushReaction

object PazBlocks {
    @JvmField val HAS_WATER = BooleanProperty.create("has_water");
    @JvmField val STORED_WATER = IntegerProperty.create("stored_water", 0, 9999);

//    @JvmField val PLANT_POT: Block = registerBlock(
//        "plant_pot",
//        BlockBehaviour.Properties.of()
//            .sound(SoundType.STONE)
//            .strength(0.2F)
//            .noOcclusion()
//            .pushReaction(PushReaction.NORMAL),
//        ::PlantPotBlock
//    )
//    @JvmField val WATER_POT: Block = registerBlock(
//        "water_pot",
//        BlockBehaviour.Properties.of()
//            .sound(SoundType.STONE)
//            .strength(0.2F)
//            .noOcclusion()
//            .pushReaction(PushReaction.NORMAL),
//        ::WaterPotBlock
//    )
    @JvmField val ZEN_POT: Block = registerBlock(
        "zen_pot",
        BlockBehaviour.Properties.of()
            .sound(SoundType.WOOD)
            .strength(0.2F)
            .noOcclusion()
            .pushReaction(PushReaction.NORMAL),
        ::ZenPotBlock
    )
    @JvmField val ENTITY_LIGHT: Block = registerBlock(
        "entity_light",
        BlockBehaviour.Properties.of()
            .lightLevel { state -> state.getValue(LightBlock.LEVEL) },
        ::LightBlock
    )
    @JvmField val WATERING_CAN_BLOCK: Block = registerBlock(
        "watering_can",
        BlockBehaviour.Properties.of()
            .sound(SoundType.LANTERN)
            .strength(0.18F)
            .noOcclusion()
            .pushReaction(PushReaction.DESTROY),
        ::WateringCanBlock,
        null
    )
    @JvmField val SUN_BATTERY_BLOCK: Block = registerBlock(
        "sun_battery",
        BlockBehaviour.Properties.of()
            .sound(SoundType.COPPER_BULB)
            .strength(0.25F)
            .noOcclusion()
            .pushReaction(PushReaction.BLOCK)
            .lightLevel(SunBatteryBlock.LIGHT_EMISSION),
        ::SunBatteryBlock,
        null
    )
    val SUN_BATTERY_BLOCK_ENTITY: BlockEntityType<SunBatteryBlockEntity> = registerBlockEntity(
        "sun_battery",
        ::SunBatteryBlockEntity,
        SUN_BATTERY_BLOCK
    )
    @JvmField val SUN_BATTERY_POI = PoiHelper.register(pazResource("sun_battery"), 8, 16, SUN_BATTERY_BLOCK)
    @JvmField val TIME_MACHINE: Block = registerBlock(
        "time_machine",
        BlockBehaviour.Properties.of()
            .sound(SoundType.COPPER_GRATE)
            .strength(0.7F)
            .noOcclusion()
            .pushReaction(PushReaction.BLOCK)
            .lightLevel(TimeMachineBlock.LIGHT_EMISSION),
        ::TimeMachineBlock
    )
    val TIME_MACHINE_ENTITY: BlockEntityType<TimeMachineBlockEntity> = registerBlockEntity(
        "time_machine",
        ::TimeMachineBlockEntity,
        TIME_MACHINE
    )

    val gardenGnomeVariants = listOf<DyeColor>(
        DyeColor.BLUE,
        DyeColor.GREEN,
        DyeColor.RED,
        DyeColor.YELLOW
    )

    @JvmField val GARDEN_GNOMES: Map<DyeColor, Block> = gardenGnomeVariants.associateWith { color ->
        registerBlock("${color.name.lowercase()}_garden_gnome", gardenGnomeProperties(), {GardenGnomeBlock(it)},
            folder = "garden_gnome"
        )
    }

    @JvmField val MAILBOXES: Map<DyeColor, Block> = DyeColor.entries.associateWith { color ->
        registerBlock("${color.name.lowercase()}_mailbox", mailboxProperties(
            color.mapColor
        ), {MailboxBlock(it, color)},
            folder = "mailbox"
        )
    }

    val MAILBOX_ENTITY: BlockEntityType<MailboxBlockEntity> = registerBlockEntity(
        "mailbox",
        ::MailboxBlockEntity,
        *MAILBOXES.values.toTypedArray(),
        folder = "mailbox"
    )

    @JvmField val CONE: Block = registerBlock(
        "cone",
        BlockBehaviour.Properties.of()
            .sound(SoundType.CANDLE)
            .instabreak()
            .noOcclusion()
            .pushReaction(PushReaction.DESTROY),
        ::ConeBlock,
        Item.Properties()
            .component(DataComponents.MAX_DAMAGE, (PazPlant.PEA_DAMAGE*15).toInt())
            .component(DataComponents.MAX_STACK_SIZE, 1)
            .component(DataComponents.DAMAGE, 0)
            .component(PazComponents.BLOCKS_PROJECTILE_DAMAGE, BlocksProjectileDamage())
            .component(
                DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.HEAD)
                    .setEquipSound(SoundEvents.ARMOR_EQUIP_LEATHER)
                    .build()
            ).component(
                DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                    .add(
                        Attributes.ARMOR,
                        AttributeModifier(pazResource("cone_armor"), 0.5, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.HEAD
                    ).add(
                        Attributes.KNOCKBACK_RESISTANCE,
                        AttributeModifier(
                            pazResource("cone_knockback_resistance"),
                            0.1,
                            AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.HEAD
                    ).build()
            ),
        folder = "armor"
    )

    @JvmField val BRAINZ_ALLOY_BLOCK: Block = registerBlock(
        "brainz_alloy_block",
        BlockBehaviour.Properties.of().sound(SoundType.COPPER).requiresCorrectToolForDrops()
            .strength(3.0F, 6.0F),
        folder = "brainz_alloy"
    )
    @JvmField val BRAINZ_ALLOY_STAIRS: Block = registerBlock(
        "brainz_alloy_stairs",
        BlockBehaviour.Properties.of().sound(SoundType.COPPER).requiresCorrectToolForDrops()
            .strength(3.0F, 6.0F),
        { StairBlock(BRAINZ_ALLOY_BLOCK.defaultBlockState(), it) },
        folder = "brainz_alloy"
    )
    @JvmField val BRAINZ_ALLOY_SLAB: Block = registerBlock(
        "brainz_alloy_slab",
        BlockBehaviour.Properties.of().sound(SoundType.COPPER).requiresCorrectToolForDrops()
            .strength(3.25F, 6.0F),
        ::SlabBlock,
        folder = "brainz_alloy"
    )
    @JvmField val SMOOTH_BRAINZ_ALLOY_BLOCK: Block = registerBlock(
        "smooth_brainz_alloy_block",
        BlockBehaviour.Properties.of().sound(SoundType.COPPER).requiresCorrectToolForDrops()
            .strength(3.25F, 6.0F),
        folder = "brainz_alloy"
    )
    @JvmField val TREADED_BRAINZ_ALLOY_BLOCK: Block = registerBlock(
        "treaded_brainz_alloy_block",
        BlockBehaviour.Properties.of().sound(SoundType.COPPER).requiresCorrectToolForDrops()
            .strength(3.25F, 6.0F),
        folder = "brainz_alloy"
    )
    @JvmField val REINFORCED_BRAINZ_ALLOY_BLOCK: Block = registerBlock(
        "reinforced_brainz_alloy_block",
        BlockBehaviour.Properties.of().sound(SoundType.COPPER).requiresCorrectToolForDrops()
            .strength(10.0F, 1200.0F),
        folder = "brainz_alloy"
    )

    @JvmField val BRAINZ_FLAG: Block = registerBlock(
        "brainz_flag",
        BlockBehaviour.Properties.of()
            .sound(SoundType.WOOD)
            .instabreak()
            .noCollision()
            .pushReaction(PushReaction.DESTROY),
        ::FlagBlock,
        Item.Properties()
            .stacksTo(16)
            .rarity(Rarity.RARE)
            .equippableUnswappable(EquipmentSlot.OFFHAND)
            .component(
                DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                    .add(
                        Attributes.SPAWN_REINFORCEMENTS_CHANCE,
                        AttributeModifier(
                            pazResource("zombie_leader_flag"),
                            1.0, AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                        ),
                        EquipmentSlotGroup.HAND
                    ).add(
                        Attributes.FOLLOW_RANGE,
                        AttributeModifier(
                            pazResource("zombie_leader_flag"),
                            1.0, AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                        ),
                        EquipmentSlotGroup.HAND
                    ).build()
            ),
        folder = "flags"
    )
    @JvmField val PLANTZ_FLAG: Block = registerBlock(
        "plantz_flag",
        BlockBehaviour.Properties.of()
            .sound(SoundType.WOOD)
            .instabreak()
            .noCollision()
            .pushReaction(PushReaction.DESTROY),
        ::FlagBlock,
        Item.Properties()
            .stacksTo(16)
            .rarity(Rarity.RARE)
            .equippableUnswappable(EquipmentSlot.OFFHAND)
            .component(
                DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                    .add(
                        Attributes.MOVEMENT_SPEED,
                        AttributeModifier(
                            pazResource("plantz_flag"),
                            0.2,
                            AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                        ),
                        EquipmentSlotGroup.HAND
                    ).add(
                        Attributes.ATTACK_SPEED,
                        AttributeModifier(
                            pazResource("plantz_flag"),
                            0.2,
                            AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                        ),
                        EquipmentSlotGroup.HAND
                    ).build()
            ),
        folder = "flags"
    )
    val FLAG_BLOCK_ENTITY: BlockEntityType<FlagBlockEntity> = registerBlockEntity(
        "flag_block",
        ::FlagBlockEntity,
        PLANTZ_FLAG, BRAINZ_FLAG,
        folder = "flags"
    )
    @JvmField val PLANTZ_FLAG_POI = PoiHelper.register(pazResource("plantz_flag"), 8, 32, PLANTZ_FLAG)

    @JvmField val GRAVESTONE: Block = registerBlock(
        "gravestone",
        BlockBehaviour.Properties.of()
            .sound(SoundType.TUFF_BRICKS)
            .strength(20.0F)
            .pushReaction(PushReaction.BLOCK)
            .requiresCorrectToolForDrops(),
        ::GravestoneBlock,
    )
    val GRAVESTONE_BLOCK_ENTITY: BlockEntityType<GravestoneBlockEntity> = registerBlockEntity(
        "gravestone",
        ::GravestoneBlockEntity,
        GRAVESTONE
    )

    @JvmField val SCREEN_DOOR: Block = registerBlock(
        "screen_door",
        BlockBehaviour.Properties.of().sound(SoundType.WOOD)
            .requiresCorrectToolForDrops()
            .noOcclusion(),
        { properties -> ScreenDoorBlock(BlockSetType.OAK, properties) },
        Item.Properties()
            .component(DataComponents.MAX_DAMAGE, (PazPlant.PEA_DAMAGE*42).toInt())
            .component(DataComponents.MAX_STACK_SIZE, 1)
            .component(DataComponents.DAMAGE, 0)
            .component(PazComponents.BLOCKS_PROJECTILE_DAMAGE, BlocksProjectileDamage(
                slot = EquipmentSlotGroup.OFFHAND,
                reflectsDamage = true,
                reflectDistance = -0.5,
                tanksDamage = false,
                mustBeUsing = true
            ))
            .component(DataComponents.BREAK_SOUND, SoundEvents.SHIELD_BREAK)
            .component(
                DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.MAINHAND)
                    .setEquipSound(SoundEvents.SHIELD_BLOCK)
                    .build())
            .component(
                DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.OFFHAND)
                    .setEquipSound(SoundEvents.SHIELD_BLOCK)
                    .build())
            .component(
                DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                    .add(
                        Attributes.ARMOR,
                        AttributeModifier(pazResource("screen_door_armor"), 0.5, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.HAND
                    ).add(
                        Attributes.KNOCKBACK_RESISTANCE,
                        AttributeModifier(
                            pazResource("screen_door_knockback_resistance"),
                            0.1,
                            AttributeModifier.Operation.ADD_VALUE
                        ),
                        EquipmentSlotGroup.HAND
                    ).build()
            ),
        ::ScreenDoorItem,
        folder = "screen_door",
        itemFolder = "armor"
    )


    private fun registerBlock(
        name: String,
        properties: BlockBehaviour.Properties = BlockBehaviour.Properties.of(),
        blockFactory: (BlockBehaviour.Properties) -> Block = ::Block,
        itemProperties: Item.Properties? = Item.Properties(),
        itemFactory: ((Block, Item.Properties) -> Item)? = { block, props ->
            BlockItem(block, props)
        },
        folder: String? = null,
        itemFolder: String? = null,
    ): Block {

        val blockPath = if (folder != null) "$folder/$name" else name

        val itemPath = if (itemFolder != null) "$itemFolder/$name" else blockPath

        val key = ResourceKey.create(Registries.BLOCK, pazResource(blockPath))
        val block = blockFactory(properties.setId(key))
        Registry.register(BuiltInRegistries.BLOCK, key, block)

        if (itemFactory!=null && itemProperties!=null) {
            val itemKey = ResourceKey.create(Registries.ITEM, pazResource(itemPath))
            val blockItem = itemFactory(block, itemProperties.setId(itemKey))
            Registry.register(BuiltInRegistries.ITEM, itemKey, blockItem)
        }

        return block
    }


    private fun <T : BlockEntity> registerBlockEntity(
        name: String,
        factory: (BlockPos, BlockState) -> T,
        vararg validBlocks: Block,
        folder: String? = null,
    ): BlockEntityType<T> {

        val blockPath = if (folder != null) "$folder/$name" else name

        val key = ResourceKey.create(Registries.BLOCK_ENTITY_TYPE, pazResource(blockPath))
        val builder = FabricBlockEntityTypeBuilder.create(
            factory,
            *validBlocks
        )
        val blockEntity = builder.build()
        Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, key, blockEntity)
        return blockEntity
    }

    private fun gardenGnomeProperties(mapColor: MapColor = MapColor.COLOR_BLUE): BlockBehaviour.Properties {
        return BlockBehaviour.Properties.of()
            .mapColor(mapColor)
            .sound(SoundType.DECORATED_POT)
            .strength(1.8F)
            .noOcclusion()
            .pushReaction(PushReaction.NORMAL)
    }

    private fun mailboxProperties(mapColor: MapColor = MapColor.SNOW): BlockBehaviour.Properties {
        return BlockBehaviour.Properties.of()
            .mapColor(mapColor)
            .sound(SoundType.LANTERN)
            .strength(1.3F)
            .noOcclusion()
            .pushReaction(PushReaction.BLOCK)
    }

    fun initialize() {
    }
}