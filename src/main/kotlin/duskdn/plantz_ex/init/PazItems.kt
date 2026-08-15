package duskdn.plantz_ex.init

import duskdn.plantz_ex.entity.plant.init.PazPlant
import duskdn.plantz_ex.entity.zombie.HatVariant
import duskdn.plantz_ex.item.*
import duskdn.plantz_ex.item.component.BlocksProjectileDamage
import duskdn.plantz_ex.item.component.StoredSun
import duskdn.plantz_ex.item.component.StoredWater
import duskdn.plantz_ex.item.component.SunCost
import duskdn.plantz_ex.util.pazResource
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents
import net.fabricmc.fabric.api.registry.FuelValueEvents
import net.fabricmc.fabric.impl.item.ItemComponentTooltipProviderRegistryImpl
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponents
import net.minecraft.core.dispenser.BlockSource
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior
import net.minecraft.core.dispenser.MinecartDispenseItemBehavior
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.*
import net.minecraft.world.item.Items.GLASS_BOTTLE
import net.minecraft.world.item.component.ItemAttributeModifiers
import net.minecraft.world.item.component.UseCooldown
import net.minecraft.world.item.equipment.ArmorMaterials
import net.minecraft.world.item.equipment.ArmorType
import net.minecraft.world.item.equipment.EquipmentAssets
import net.minecraft.world.item.equipment.Equippable
import net.minecraft.world.level.block.ComposterBlock
import net.minecraft.world.level.block.DispenserBlock
import java.util.function.Function

object PazItems {
    @JvmField
    val SUN: Item = registerItem(
        "sun", ::SunItem,
        properties = Item.Properties()
            .stacksTo(80)
    )
    @JvmField
    val SUN_BOTTLE: Item = registerItem(
        "sun_bottle", ::SunBottleItem,
        properties = Item.Properties().craftRemainder(GLASS_BOTTLE)
    )
    @JvmField
    val SUN_BATTERY: Item = registerItem(
        "sun_battery", ::SunBatteryItem,
        properties = Item.Properties()
            .stacksTo(1)
            .rarity(Rarity.EPIC)
            .component(PazComponents.STORED_SUN, StoredSun())
    )
    @JvmField
    val WATERING_CAN: Item = registerItem(
        "watering_can", ::WateringCanItem,
        properties = Item.Properties()
            .stacksTo(1)
            .component(PazComponents.STORED_WATER, StoredWater())
    )
    @JvmField
    val BRAINZ_ALLOY: Item = registerItem(
        "brainz_alloy",
        properties = Item.Properties(),
        folder = "brainz_alloy"
    )
    @JvmField
    val BOT_BLUEPRINT: Item = registerItem(
        "bot_blueprint",
        properties = Item.Properties()
    )
    @JvmField
    val NEWSPAPER: Item = registerItem(
        "newspaper", ::NewspaperItem,
        properties = Item.Properties()
            .durability(8*PazPlant.PEA_ARMOR_DAMAGE)
            .component(PazComponents.BLOCKS_PROJECTILE_DAMAGE, BlocksProjectileDamage(
                slot = EquipmentSlotGroup.HAND,
                reflectsDamage = true,
                tanksDamage = false,
                mustBeUsing = true
            )
            ).component(DataComponents.BREAK_SOUND, SoundEvents.SHIELD_BREAK),
        folder = "armor"
    )
    const val DUCKY_TUBE_DAMAGE_INTERVAL = 45
    val DUCKY_EQUIP_ASSET = ResourceKey.create(EquipmentAssets.ROOT_ID, pazResource("armor/ducky_tube"))

    val OBSIDIAN_DUCKY_EQUIP_ASSET = ResourceKey.create(EquipmentAssets.ROOT_ID, pazResource("armor/obsidian_ducky_tube"))

    @JvmField
    val DUCKY_TUBE: Item = registerItem(
        "ducky_tube", ::DuckyTubeItem,
        properties = Item.Properties()
            .durability(9*PazPlant.PEA_ARMOR_DAMAGE)
            .attributes(
                ItemAttributeModifiers.builder()
                    .add(
                        Attributes.WATER_MOVEMENT_EFFICIENCY,
                        AttributeModifier(pazResource("ducky_tube"), 1.5, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.LEGS
                    ).build())
            .component(DataComponents.EQUIPPABLE,
                Equippable.builder(EquipmentSlot.LEGS)
                .setAsset(DUCKY_EQUIP_ASSET)
                .build()),
        folder = "armor"
    )

    @JvmField
    val OBSIDIAN_DUCKY_TUBE: Item = registerItem(
        "obsidian_ducky_tube", ::DuckyTubeItem,
        properties = Item.Properties()
            .durability(12*PazPlant.PEA_ARMOR_DAMAGE)
            .attributes(
                ItemAttributeModifiers.builder()
                    .add(
                        Attributes.WATER_MOVEMENT_EFFICIENCY,
                        AttributeModifier(pazResource("obsidian_ducky_tube"), -0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                        EquipmentSlotGroup.LEGS
                    ).build())
            .component(DataComponents.EQUIPPABLE,
                Equippable.builder(EquipmentSlot.LEGS)
                    .setAsset(OBSIDIAN_DUCKY_EQUIP_ASSET)
                    .build()),
        folder = "armor"
    )

    @JvmField val BALLOONS: Map<DyeColor, Item> = DyeColor.entries.associateWith { color ->
        registerItem(
            "${color.name.lowercase()}_balloon",
            { BalloonItem(it, DyeColor.WHITE) },
            properties = Item.Properties(),
            folder = "balloon"
        )
    }

    @JvmField
    val DYE_BLASTER: Item = registerItem(
        "dye_blaster", ::DyeBlasterItem,
        properties = Item.Properties()
            .durability(435)
            .repairable(BRAINZ_ALLOY)
            .stacksTo(1)
    )
    @JvmField
    val FOOTBALL_HELMET: Item = registerItem(
        "football_helmet", ::FootballHelmetItem,
        properties = Item.Properties()
            .durability((PazPlant.PEA_DAMAGE*70).toInt())
            .humanoidArmor(ArmorMaterials.CHAINMAIL, ArmorType.HELMET)
            .component(PazComponents.BLOCKS_PROJECTILE_DAMAGE, BlocksProjectileDamage()
            )
            .component(
                DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.HEAD)
                    .setEquipSound(SoundEvents.ARMOR_EQUIP_IRON)
                    .build()
            )
            .component(
                DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.builder()
                    .add(
                        Attributes.ARMOR,
                        AttributeModifier(pazResource("football_armor"), 4.0, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.HEAD
                    ).add(
                        Attributes.KNOCKBACK_RESISTANCE,
                        AttributeModifier(pazResource("football_knockback_resistance"), 0.1, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.HEAD
                    ).build()
            ),
        folder = "armor"
    )
    @JvmField
    val SEED_PACKET: Item = registerItem(
        "seed_packet", ::SeedPacketItem,
        properties = Item.Properties()
            .component(PazComponents.SUN_COST, SunCost())
            .component(DataComponents.USE_COOLDOWN, UseCooldown(0f))
    )
    @JvmField
    val PLANT_POT_MINECART: Item = registerItem(
        "plant_pot_minecart", { p: Item.Properties -> MinecartItem(PazEntities.PLANT_POT_MINECART ,p) },
        properties = Item.Properties().stacksTo(1)
    )
    @JvmField
    val PLANT_POT_HELMET: Item = registerItem(
        "plant_pot_helmet", ::PlantPotHelmetItem,
        properties = Item.Properties()
            .durability(185)
            .rarity(Rarity.UNCOMMON)
            .repairable(Items.BRICK)
            .component(
                DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.HEAD)
                    .setEquipSound(SoundEvents.ARMOR_EQUIP_GENERIC)
                    .build()),
        folder = "armor"
    )
    @JvmField
    val MUSIC_DISC_GRASSY_GROOVE: Item = registerItem(
        "music_disc_grassy_groove",
        properties = Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).jukeboxPlayable(PazJukeboxSongs.GRASSY_GROOVE)
    )

    @JvmField val BROWN_COAT_SPAWN_EGGS: MutableList<Item> = registerPazZombieWithVariants(
        PazEntities.BROWN_COAT,
        "browncoat"
    )

    @JvmField val NEWSPAPER_ZOMBIE_SPAWN_EGG: Item = registerPazZombieSpawnEgg(PazEntities.NEWSPAPER_ZOMBIE)
    @JvmField val DIGGER_ZOMBIE_SPAWN_EGG: Item = registerPazZombieSpawnEgg(PazEntities.DIGGER_ZOMBIE)
    @JvmField val ENGINEER_ZOMBIE_SPAWN_EGG: Item = registerPazZombieSpawnEgg(PazEntities.ENGINEER_ZOMBIE)
    @JvmField val ZOMBIE_YETI_SPAWN_EGG: Item = registerPazZombieSpawnEgg(PazEntities.ZOMBIE_YETI)
    @JvmField val DISCO_ZOMBIE_SPAWN_EGG: Item = registerPazZombieSpawnEgg(PazEntities.DISCO_ZOMBIE)
    @JvmField val BACKUP_DANCER_SPAWN_EGG: Item = registerPazZombieSpawnEgg(PazEntities.BACKUP_DANCER)
    @JvmField val ALL_STAR_SPAWN_EGG: Item = registerPazZombieSpawnEgg(PazEntities.ALL_STAR)
    @JvmField val SOLDIER_ZOMBIE_SPAWN_EGG: Item = registerPazZombieSpawnEgg(PazEntities.SOLDIER_ZOMBIE)
    @JvmField val ROBO_ZOMBIE_SPAWN_EGG: Item = registerPazZombieSpawnEgg(PazEntities.ROBO_ZOMBIE)
    @JvmField val SUPER_BRAINZ_SPAWN_EGG: Item = registerPazZombieSpawnEgg(PazEntities.SUPER_BRAINZ)
    @JvmField val IMP_SPAWN_EGG: Item = registerPazZombieSpawnEgg(PazEntities.IMP)
    @JvmField val GARGANTUAR_SPAWN_EGG: Item = registerPazZombieSpawnEgg(PazEntities.GARGANTUAR)

    @JvmField val BALLOON_ZOMBIE_SPAWN_EGGS: MutableList<Item> = registerPazZombieWithVariants(
        PazEntities.BALLOON_ZOMBIE,
        "balloon"
    )

    @JvmField val PIRATE_CAPTAIN_SPAWN_EGG: Item = registerPazZombieSpawnEgg(PazEntities.PIRATE_CAPTAIN)

    @JvmField val GNOME_SPAWN_EGG: Item = registerPazSpawnEgg(
        PazEntities.GNOME,
        folder="gnome"
    )

    private fun registerItem(
        name: String,
        itemFactory: Function<Item.Properties, Item> = { p: Item.Properties -> Item(p) },
        properties: Item.Properties = Item.Properties(),
        folder: String? = null,
    ) : Item {

        val itemPath = if (folder != null) "$folder/$name" else name

        val key = ResourceKey.create(Registries.ITEM, pazResource(itemPath))
        val item = itemFactory.apply(properties.setId(key))
        Registry.register(BuiltInRegistries.ITEM, key, item)

        return item
    }

    private fun registerPazZombieSpawnEgg(
        type: EntityType<*>,
        worker: (LivingEntity) -> Unit = {},
        properties: Item.Properties = Item.Properties(),
        customId: String? = null,
        folder: String? = null
    ): Item {

        val folderPath = if (folder != null) "zombies/$folder" else "zombies"

        val entityId = if (customId != null) customId else EntityType.getKey(type).path
        return registerPazSpawnEgg(
            type,
            worker,
            properties,
            customId,
            folder = folderPath
        )
    }

    private fun registerPazSpawnEgg(
        type: EntityType<*>,
        worker: (LivingEntity) -> Unit = {},
        properties: Item.Properties = Item.Properties(),
        customId: String? = null,
        folder: String? = null
    ): Item {

        val folderPath = if (folder != null) "spawn_egg/$folder" else "spawn_egg"

        val entityId = if (customId != null) customId else EntityType.getKey(type).path
        return registerItem(
            entityId,
            { props ->
                PazSpawnEgg(props, worker)
            },
            properties
                .spawnEgg(type),
            folder = folderPath
        )
    }

    private fun registerPazZombieWithVariants(
        type: EntityType<*>,
        folder: String? = null,
        properties: Item.Properties = Item.Properties(),
        extraWorker: (LivingEntity) -> Unit = {},
    ): MutableList<Item> {

        val spawnEggList: MutableList<Item> = mutableListOf()

        val entityId = EntityType.getKey(type).path

        HatVariant.entries.forEach { hatVariant ->
            spawnEggList.add(
                registerPazZombieSpawnEgg(
                    type,
                    worker = {

                        if (hatVariant.hat != null) {
                            it.setItemSlot(
                                EquipmentSlot.HEAD,
                                hatVariant.hat.defaultInstance
                            )
                        }

                        extraWorker(it)

                    },
                    customId = hatVariant.hatName,
                    folder = folder
                )
            )
        }

        spawnEggList.add(
            registerPazZombieSpawnEgg(
                type,
                worker = {
                    it.setItemSlot(
                        EquipmentSlot.MAINHAND,
                        PazBlocks.BRAINZ_FLAG.asItem().defaultInstance
                    )
                    it
                },
                customId = "flag",
                folder = folder
            )
        )

        spawnEggList.add(
            registerPazZombieSpawnEgg(
                type,
                worker = {
                    it.setItemSlot(
                        EquipmentSlot.MAINHAND,
                        PazBlocks.SCREEN_DOOR.asItem().defaultInstance
                    )
                    it
                },
                customId = "screen_door",
                folder = folder
            )
        )

        return spawnEggList
    }

    fun initialize() {

        FuelValueEvents.BUILD.register { builder, context ->
            builder.add(SUN, (context.baseSmeltTime()*0.5).toInt())
            builder.add(NEWSPAPER, context.baseSmeltTime())
        }

        ComposterBlock.COMPOSTABLES.put(SEED_PACKET.asItem(), 1.0f)

        // Modify components
        ItemComponentTooltipProviderRegistryImpl.addLast(PazComponents.STORED_WATER)
        ItemComponentTooltipProviderRegistryImpl.addLast(PazComponents.STORED_SUN)
        ItemComponentTooltipProviderRegistryImpl.addLast(PazComponents.SUN_COST)
        ItemComponentTooltipProviderRegistryImpl.addLast(PazComponents.BLOCKS_PROJECTILE_DAMAGE)

        DefaultItemComponentEvents.MODIFY.register {
            it.modify(Items.BUCKET) { builder ->
                builder.set(PazComponents.BLOCKS_PROJECTILE_DAMAGE, BlocksProjectileDamage())
                builder.set(DataComponents.EQUIPPABLE, Equippable.builder(EquipmentSlot.HEAD)
                    .setEquipSound(SoundEvents.ARMOR_EQUIP_IRON)
                    .build()
                )

                val armorModifier = ItemAttributeModifiers.builder()
                    .add(
                        Attributes.ARMOR,
                        AttributeModifier(pazResource("bucket_armor"), 1.0, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.HEAD
                    ).add(
                        Attributes.KNOCKBACK_RESISTANCE,
                        AttributeModifier(pazResource("bucket_knockback_resistance"), 0.05, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.HEAD
                    ).build()

                builder.set(DataComponents.ATTRIBUTE_MODIFIERS, armorModifier)

                builder.set(DataComponents.MAX_DAMAGE, (PazPlant.PEA_DAMAGE*42).toInt())

                builder.set(DataComponents.MAX_STACK_SIZE, 1)

                builder.set(DataComponents.DAMAGE, 0)
            }
        }

        // Dispenser behavior
        DispenserBlock.registerBehavior(
            SEED_PACKET, object : DefaultDispenseItemBehavior() {
            public override fun execute(source: BlockSource, dispensed: ItemStack): ItemStack {
                return super.execute(source, dispensed)
            }
        })

        DispenserBlock.registerProjectileBehavior(SUN_BOTTLE)

        DispenserBlock.registerBehavior(
            PLANT_POT_MINECART, object : MinecartDispenseItemBehavior(PazEntities.PLANT_POT_MINECART) {}
        )
    }
}