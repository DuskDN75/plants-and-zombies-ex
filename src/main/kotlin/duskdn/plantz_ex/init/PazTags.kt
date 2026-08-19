package duskdn.plantz_ex.init

import duskdn.plantz_ex.util.pazResource
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
        @JvmField val PLANTABLE_ON_AIR = tag("plantable_on_air")
        @JvmField val CARRIER_ALLOW_WATER = tag("carrier_allow_water")
        @JvmField val PLANT_PROJECTILE = tag("plant_projectile")
        @JvmField val IMMUNE_TO_ELECTRICITY = tag("immune_to_electricity")
        @JvmField val CANNOT_CHOMP = tag("cannot_be_chomped")
        @JvmField val CANNOT_CHILL = tag("cannot_be_chilled")
        @JvmField val CANNOT_DRENCH = tag("cannot_be_drenched")
        @JvmField val CANNOT_HYPNOTIZE = tag("cannot_be_hypnotized")
        @JvmField val ZOMBIE_RAIDERS = tag("zombie_raider")
        @JvmField val GETS_DUCKY_TUBE = tag("gets_ducky_tube")
        @JvmField val GETS_ARMOR = tag("gets_armor")
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

        val HAS_SUNFLOWER = plantTag("sunflower")
        val HAS_SUNFLOWER_ALT = plantTag("sunflower", "alt")

        val HAS_PEASHOOTER = plantTag("peashooter")

        val HAS_WALLNUT = plantTag("wallnut")
        val HAS_CACTUS = plantTag("cactus")
        val HAS_LIGHTNING_REED = plantTag("lightning_reed")
        val HAS_CHERRYBOMB = plantTag("cherrybomb")
        val HAS_CHOMPER = plantTag("chomper")
        val HAS_COFFEE_BEAN = plantTag("coffeebean")
        val HAS_FIRE_PEASHOOTER = plantTag("fire_peashooter")
        val HAS_REPEATER = plantTag("repeater")
        val HAS_ICE_PEASHOOTER = plantTag("ice_peashooter")
        val HAS_CABBAGEPULT = plantTag("cabbagepult")
        val HAS_KERNELPULT = plantTag("kernelpult")
        val HAS_MELONPULT = plantTag("melonpult")
        val HAS_POTATOMINE = plantTag("potatomine")
        val HAS_BONK_CHOY = plantTag("bonkchoy")
        val HAS_TANGLE_KELP = plantTag("tanglekelp")

        val HAS_PUFFSHROOM = plantTag("puffshroom")
        val HAS_PUFFSHROOM_CAVES = plantTag("puffshroom", "caves")

        val HAS_SUNSHROOM = plantTag("sunshroom")
        val HAS_SUNSHROOM_ALT = plantTag("sunshroom", "alt")

        val HAS_FUMESHROOM = plantTag("fumeshroom")
        val HAS_HYPNOSHROOM = plantTag("hypnoshroom")
        val HAS_DOOM_SHROOM = plantTag("doomshroom")
        val HAS_ICE_SHROOM = plantTag("doomshroom")

        val HAS_WATER_PEASHOOTER_GROUND = plantTag("water_peashooter", "ground")
        val HAS_WATER_PEASHOOTER_WATER = plantTag("water_peashooter", "water")

        val HAS_LILYPAD = plantTag("lilypad")

        val HAS_SEA_SHROOM = plantTag("seashroom")
        val HAS_SEA_SHROOM_CAVES = plantTag("seashroom", "caves")

        val HAS_SCAREDYSHROOM = plantTag("scaredyshroom")
        val HAS_LAVALILY = plantTag("lavalily")
        val HAS_SKY_PEASHOOTER = plantTag("sky_peashooter")
        val HAS_GRAVE_BUSTER = plantTag("grave_buster")
        val HAS_PLANTERN = plantTag("plantern")





        val COMMON_SPAWNS = tag("zombie/common_spawns")
        val WATER_SPAWNS = tag("zombie/water_spawns")
        val DAY_SPAWNS = tag("zombie/day_spawns")
        val HAS_BROWNCOAT = tag("zombie/has_browncoat")
        val HAS_BROWNCOAT_SNOW = tag("zombie/has_browncoat_snow")
        val HAS_BROWNCOAT_DESERT = tag("zombie/has_browncoat_desert")
        val HAS_BROWNCOAT_BEACH = tag("zombie/has_browncoat_beach")
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

        private fun plantTag(plant: String, type: String = "default"): TagKey<Biome> = tag("plant/$plant/$type")
    }

    fun initialize() {}
}