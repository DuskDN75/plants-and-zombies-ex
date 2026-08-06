package duskdn.plantz.init

import duskdn.plantz.util.pazResource
import net.minecraft.core.registries.Registries
import net.minecraft.tags.TagKey
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.EntityType
import net.minecraft.world.item.Item
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.block.Block

object PazTags {
    object BlockTags {
        @JvmField val MAILBOX = tag("mailbox")
        @JvmField val PLANTABLE = tag("plantable")
        @JvmField val FARMABLE = tag("farmable")
        @JvmField val PLANT_POT = tag("gives_plant_pot_protection")
        @JvmField val YETI_SPAWNABLE_ON = tag("yeti_spawnable_on")
        @JvmField val DIGGER_BREAKABLE = tag("digger_breakable")

        //TODO add "can survive on" block tags for all plants.
        @JvmField val SURVIVES_ON_POTATOMINE = tag("can_survive_on/potatomine")
        private fun tag(name: String): TagKey<Block> = TagKey.create(Registries.BLOCK, pazResource(name))
    }

    object ItemTags {
        @JvmField val GNOME_PREFERRED_WEAPONS = tag("gnome_preferred_weapons")
        @JvmField val DIGGER_PREFERRED_WEAPONS = tag("digger_preferred_weapons")
        @JvmField val NEWSPAPER_ZOMBIE_PREFERRED_WEAPONS = tag("newspaper_zombie_preferred_weapons")
        private fun tag(name: String): TagKey<Item> = TagKey.create(Registries.ITEM, pazResource(name))
    }

    object EntityTypes {
        @JvmField val PLANT = tag("plant")
        @JvmField val MUSHROOM = tag("mushroom")
        @JvmField val CARRIER = tag("carrier")
        @JvmField val AMPHIBIOUS = tag("amphibious")
        @JvmField val PLANTABLE_ON_WATER = tag("plantable_on_water")
        @JvmField val PLANTABLE_ON_LAVA = tag("plantable_on_lava")
        @JvmField val CARRIER_ALLOW_WATER = tag("carrier_allow_water")
        @JvmField val PLANT_PROJECTILE = tag("plant_projectile")
        @JvmField val IMMUNE_TO_ELECTRICITY = tag("immune_to_electricity")
        @JvmField val CANNOT_CHOMP = tag("cannot_be_chomped")
        @JvmField val CANNOT_CHILL = tag("cannot_be_chilled")
        @JvmField val CANNOT_DRENCH = tag("cannot_be_drenched")
        @JvmField val CANNOT_HYPNOTIZE = tag("cannot_be_hypnotized")
        @JvmField val ZOMBIE_RAIDERS = tag("zombie_raider")
        @JvmField val GETS_DUCKY_TUBE = tag("gets_ducky_tube")
        @JvmField val ATTACKS_PLANTS = tag("attacks_plants")
        @JvmField val IGNORED_BY_PLANT_ATTACKERS = tag("ignored_by_plant_attackers")
        @JvmField val FLYING_ENEMY = tag("flying_enemy")
        @JvmField val GNOME_RIDEABLE = tag("gnome_rideable")
        @JvmField val WALLNUT_DEFLECTABLE = tag("wallnut_deflectable")
        private fun tag(name: String): TagKey<EntityType<*>> = TagKey.create(Registries.ENTITY_TYPE, pazResource(name))
    }

    object DamageTypes {
        @JvmField val PLANT_PROJECTILE = tag("plant_projectile")
        @JvmField val IS_ELECTRIC = tag("is_electric")
        private fun tag(name: String): TagKey<DamageType> = TagKey.create(Registries.DAMAGE_TYPE, pazResource(name))
    }

    object Biomes {
        val GRAVESTONE_IGNORE_BRIGHTNESS = tag("gravestone_ignore_brightness")

        val HAS_SUNFLOWER = tag("plant/has_sunflower")
        val HAS_SUNFLOWER_ALT = tag("plant/has_sunflower_alt")
        val HAS_PEASHOOTER = tag("plant/has_peashooter")
        val HAS_WALLNUT = tag("plant/has_wallnut")
        val HAS_CACTUS = tag("plant/has_cactus")
        val HAS_LIGHTNING_REED = tag("plant/has_lightning_reed")
        val HAS_CHERRYBOMB = tag("plant/has_cherrybomb")
        val HAS_CHOMPER = tag("plant/has_chomper")
        val HAS_COFFEE_BEAN = tag("plant/has_coffeebean")
        val HAS_FIRE_PEASHOOTER = tag("plant/has_fire_peashooter")
        val HAS_REPEATER = tag("plant/has_repeater")
        val HAS_ICE_PEASHOOTER = tag("plant/has_ice_peashooter")
        val HAS_CABBAGEPULT = tag("plant/has_cabbagepult")
        val HAS_KERNELPULT = tag("plant/has_kernelpult")
        val HAS_MELONPULT = tag("plant/has_melonpult")
        val HAS_POTATOMINE = tag("plant/has_potatomine")
        val HAS_BONK_CHOY = tag("plant/has_bonkchoy")
        val HAS_TANGLE_KELP = tag("plant/has_tanglekelp")
        val HAS_PUFFSHROOM = tag("plant/has_puffshroom")
        val HAS_SUNSHROOM = tag("plant/has_sunshroom")
        val HAS_SUNSHROOM_ALT = tag("plant/has_sunshroom_alt")
        val HAS_FUMESHROOM = tag("plant/has_fumeshroom")
        val HAS_HYPNOSHROOM = tag("plant/has_hypnoshroom")
        val HAS_DOOM_SHROOM = tag("plant/has_doomshroom")
        val HAS_WATER_PEASHOOTER = tag("plant/has_water_peashooter")
        val HAS_LILYPAD = tag("plant/has_lilypad")
        val HAS_SEA_SHROOM = tag("plant/has_seashroom")
        val HAS_SCAREDYSHROOM = tag("plant/has_scaredyshroom")
        val HAS_LAVALILY = tag("plant/has_lavalily")

        val COMMON_SPAWNS = tag("zombie/common_spawns")
        val WATER_SPAWNS = tag("zombie/water_spawns")
        val DAY_SPAWNS = tag("zombie/day_spawns")
        val HAS_BROWNCOAT = tag("zombie/has_browncoat")
        val HAS_NEWSPAPER_ZOMBIE = tag("zombie/has_newspaper_zombie")
        val HAS_DIGGER = tag("zombie/has_digger")
        val HAS_DIGGER_ALT = tag("zombie/has_digger_alt")
        val HAS_ZOMBIE_YETI = tag("zombie/has_zombie_yeti")
        val HAS_ZOMBIE_YETI_ALT = tag("zombie/has_zombie_yeti_alt")
        val HAS_DISCO_ZOMBIE = tag("zombie/has_disco_zombie")
        val HAS_ALL_STAR = tag("zombie/has_all_star")
        val HAS_IMP = tag("zombie/has_imp")
        val HAS_BALLOON_ZOMBIE = tag("zombie/has_balloon_zombie")
        val HAS_BALLOON_ZOMBIE_ALT = tag("zombie/has_balloon_zombie_alt")

        private fun tag(name: String): TagKey<Biome>  = TagKey.create(Registries.BIOME, pazResource(name))
    }

    fun initialize() {}
}