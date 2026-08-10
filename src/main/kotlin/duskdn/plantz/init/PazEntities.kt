package duskdn.plantz.init

import duskdn.plantz.ai.goal.DestroyFlagGoal
import duskdn.plantz.ai.goal.PathfindToFlagGoal
import duskdn.plantz.entity.Balloon
import duskdn.plantz.entity.PlantPotMinecart
import duskdn.plantz.entity.Sun
import duskdn.plantz.entity.gnome.Gnome
import duskdn.plantz.entity.plant.all.BonkChoy
import duskdn.plantz.entity.plant.all.CabbagePult
import duskdn.plantz.entity.plant.all.Cactus
import duskdn.plantz.entity.plant.all.CherryBomb
import duskdn.plantz.entity.plant.all.Chomper
import duskdn.plantz.entity.plant.all.CoffeeBean
import duskdn.plantz.entity.plant.all.mushrooms.DoomShroom
import duskdn.plantz.entity.plant.all.ElectricPeaShooter
import duskdn.plantz.entity.plant.all.ExplodeONut
import duskdn.plantz.entity.plant.all.FirePeaShooter
import duskdn.plantz.entity.plant.all.FlowerPot
import duskdn.plantz.entity.plant.all.GraveBuster
import duskdn.plantz.entity.plant.all.mushrooms.FumeShroom
import duskdn.plantz.entity.plant.all.mushrooms.HypnoShroom
import duskdn.plantz.entity.plant.all.IcePeaShooter
import duskdn.plantz.entity.plant.all.KernelPult
import duskdn.plantz.entity.plant.all.igenous.LavaLily
import duskdn.plantz.entity.plant.all.LightningReed
import duskdn.plantz.entity.plant.all.aquatic.LilyPad
import duskdn.plantz.entity.plant.all.MelonPult
import duskdn.plantz.entity.plant.all.PeaShooter
import duskdn.plantz.entity.plant.all.PotatoMine
import duskdn.plantz.entity.plant.all.mushrooms.PuffShroom
import duskdn.plantz.entity.plant.all.Repeater
import duskdn.plantz.entity.plant.all.mushrooms.ScaredyShroom
import duskdn.plantz.entity.plant.all.mushrooms.SeaShroom
import duskdn.plantz.entity.plant.all.mushrooms.SunShroom
import duskdn.plantz.entity.plant.all.Sunflower
import duskdn.plantz.entity.plant.all.aquatic.TangleKelp
import duskdn.plantz.entity.plant.all.WallNut
import duskdn.plantz.entity.plant.all.aquatic.WaterPeaShooter
import duskdn.plantz.entity.plant.all.WaterPot
import duskdn.plantz.entity.plant.all.aerial.SkyPeaShooter
import duskdn.plantz.entity.plant.all.mushrooms.IceShroom
import duskdn.plantz.entity.plant.init.PazPlant
import duskdn.plantz.entity.projectile.*
import duskdn.plantz.entity.projectile.peas.Pea
import duskdn.plantz.entity.projectile.peas.PeaElectric
import duskdn.plantz.entity.projectile.peas.PeaFire
import duskdn.plantz.entity.projectile.peas.PeaIce
import duskdn.plantz.entity.projectile.peas.PeaWater
import duskdn.plantz.entity.turret.Turret
import duskdn.plantz.entity.zombie.*
import duskdn.plantz.mixin.MobAccessor
import duskdn.plantz.util.pazResource
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.entity.*
import net.minecraft.world.entity.Mob.createMobAttributes
import net.minecraft.world.entity.ai.attributes.AttributeInstance
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.monster.zombie.Zombie
import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin
import net.minecraft.world.entity.projectile.Projectile

object PazEntities {

    val followMultiplier = 1.0

    val followModifierID: Identifier = Identifier.fromNamespaceAndPath("plantz","night_follow_multiplier")

    var isNight = false

    private fun applyNightFollowModifier(instance: AttributeInstance) {
        if (instance.getModifier(followModifierID) == null) {

            val newMod = AttributeModifier(
                followModifierID,
                5.0,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            )

            instance.addPermanentModifier(newMod)
        }
    }

    private fun removeNightFollowModifier(instance: AttributeInstance) {
        if (instance.getModifier(followModifierID) != null) {

            instance.removeModifier(followModifierID)

        }
    }

    fun initialize() {

        ServerEntityEvents.ENTITY_LOAD.register { entity, level ->

            if (entity is Zombie) (entity as MobAccessor).targetSelector.addGoal(4, NearestAttackableTargetGoal(entity, Gnome::class.java, 5, true, false, null))

            if (entity is Mob && entity.`is`(PazTags.EntityTypes.ATTACKS_PLANTS)) {
                (entity as MobAccessor).targetSelector.addGoal(0, NearestAttackableTargetGoal(entity, WallNut::class.java, 2, false, true) { target, level -> ((target as? WallNut
                    ?: target as? ExplodeONut)?.let { it.distanceToSqr(entity) < 16 } ?: false)})
                (entity as MobAccessor).targetSelector.addGoal(1, NearestAttackableTargetGoal(entity, PazPlant::class.java, 6, false, true) { target, level ->
                    target !is WallNut && target.passengers.isEmpty() && !target.`is`(PazTags.EntityTypes.IGNORED_BY_PLANT_ATTACKERS)
                })
            }

            if (entity is PathfinderMob && entity.`is`(PazTags.EntityTypes.ZOMBIE_RAIDERS)) {
                (entity as MobAccessor).goalSelector.addGoal(3, DestroyFlagGoal(entity))
                (entity as MobAccessor).goalSelector.addGoal(2, PathfindToFlagGoal(entity))
            }
        }

        ServerTickEvents.END_LEVEL_TICK.register { server ->
            val level = server.level

            if (level.isDarkOutside != isNight) {

                isNight = level.isDarkOutside

                for (entity in level.allEntities) {
                    if (entity is PathfinderMob && entity.`is`(PazTags.EntityTypes.ATTACKS_PLANTS)) {

                        val attribute = entity.getAttribute(Attributes.FOLLOW_RANGE) ?: continue

                        if (isNight) {
                            applyNightFollowModifier(attribute)
                        } else {
                            removeNightFollowModifier(attribute)
                        }

                    }
                }

            }
        }

    }

    // region Plants
    @JvmField val PLACEHOLDER: EntityType<Sunflower> = registerPlant(
        "placeholder",
        EntityType.Builder.of(::Sunflower, MobCategory.CREATURE),
        height = 1.1f,
    )
    @JvmField val SUNFLOWER: EntityType<Sunflower> = registerPlant(
        "sunflower",
        EntityType.Builder.of(::Sunflower, MobCategory.CREATURE),
        height = 1.1f,
    )
    @JvmField val PEA_SHOOTER: EntityType<PeaShooter> = registerPlant(
        "peashooter",
        EntityType.Builder.of(::PeaShooter, MobCategory.CREATURE)
    )
    @JvmField val WALL_NUT: EntityType<WallNut> = registerPlant(
        "wallnut",
        EntityType.Builder.of(::WallNut, MobCategory.CREATURE),
        width = 1.0f,
        height = 1.15f,
        attributes = PazPlant.Companion.PlantAttributes(
            maxHealth = 60.0,
        )
    )
    @JvmField val EXPLODE_O_NUT: EntityType<ExplodeONut> = registerPlant(
        "explode_o_nut",
        EntityType.Builder.of(::ExplodeONut, MobCategory.CREATURE),
        width = 1.0f,
        height = 1.15f,
        attributes = PazPlant.Companion.PlantAttributes(
            maxHealth = 60.0,
        )
    )
    @JvmField val CHOMPER: EntityType<Chomper> = registerPlant(
        "chomper",
        EntityType.Builder.of(::Chomper, MobCategory.CREATURE),
        height = 1.5f,
        attributes = PazPlant.Companion.PlantAttributes(
            maxHealth = 25.0,
            attackDamage = 10.0,
            attackKnockback = 0.15,
            attackRange = 3.0,
            followRange = 4.75,
        )
    )
    @JvmField val CHERRY_BOMB: EntityType<CherryBomb> = registerPlant(
        "cherrybomb",
        EntityType.Builder.of(::CherryBomb, MobCategory.CREATURE),
        width = 0.625f,
        height = 0.75f,
        attributes = PazPlant.Companion.PlantAttributes(
            followRange = 3.75,
        )
    )
    @JvmField val POTATO_MINE: EntityType<PotatoMine> = registerPlant(
        "potatomine",
        EntityType.Builder.of(::PotatoMine, MobCategory.CREATURE),
        width = 0.65f,
        height = 0.35f,
        attributes = PazPlant.Companion.PlantAttributes(
            followRange = 3.75,
        )
    )
    @JvmField val REPEATER: EntityType<Repeater> = registerPlant(
        "repeater",
        EntityType.Builder.of(::Repeater, MobCategory.CREATURE)
    )
    @JvmField val ICE_PEA_SHOOTER: EntityType<IcePeaShooter> = registerPlant(
        "ice_peashooter",
        EntityType.Builder.of(::IcePeaShooter, MobCategory.CREATURE)
    )
    @JvmField val FIRE_PEA_SHOOTER: EntityType<FirePeaShooter> = registerPlant(
        "fire_peashooter",
        EntityType.Builder.of(::FirePeaShooter, MobCategory.CREATURE).fireImmune(),
        attributes = PazPlant.Companion.PlantAttributes(
            attackDamage = PazPlant.PEA_DAMAGE*2,
        )
    )
    @JvmField val ELECTRIC_PEA_SHOOTER: EntityType<ElectricPeaShooter> = registerPlant(
        "electric_peashooter",
        EntityType.Builder.of(::ElectricPeaShooter, MobCategory.CREATURE),
        attributes = PazPlant.Companion.PlantAttributes(
            attackDamage = PazPlant.PEA_DAMAGE*2,
        )
    )
    @JvmField val CACTUS: EntityType<Cactus> = registerPlant(
        "cactus",
        EntityType.Builder.of(::Cactus, MobCategory.CREATURE),
        width = 0.8f,
        height = 1.25f,
        eyeHeight = 0.85f,
        attributes = PazPlant.Companion.PlantAttributes(
            followRange = 80.0,
            attackDamage = PazPlant.PEA_DAMAGE*2
        )
    )
    @JvmField val LIGHTNING_REED: EntityType<LightningReed> = registerPlant(
        "lightning_reed",
        EntityType.Builder.of(::LightningReed, MobCategory.CREATURE),
        width = 0.4f,
        height = 1.0f,
        eyeHeight = 0.7f,
        attributes = PazPlant.Companion.PlantAttributes(
            attackDamage = PazPlant.PEA_DAMAGE*0.5,
            attackRange = 7.25,
            followRange = 6.5,
        )
    )
    @JvmField val CABBAGE_PULT: EntityType<CabbagePult> = registerPlant(
        "cabbagepult",
        EntityType.Builder.of(::CabbagePult, MobCategory.CREATURE),
        width = 0.9f,
        height = 0.8f,
        attributes = PazPlant.Companion.PlantAttributes(
            attackDamage = PazPlant.PEA_DAMAGE*2,
            attackKnockback = 0.5,
            followRange = 40.0,
        )
    )
    @JvmField val KERNEL_PULT: EntityType<KernelPult> = registerPlant(
        "kernelpult",
        EntityType.Builder.of(::KernelPult, MobCategory.CREATURE),
        width = 0.9f,
        height = 0.8f,
        attributes = PazPlant.Companion.PlantAttributes(
            attackDamage = PazPlant.PEA_DAMAGE,
            attackKnockback = 0.5,
            followRange = 40.0,
        )
    )
    @JvmField val MELON_PULT: EntityType<MelonPult> = registerPlant(
        "melonpult",
        EntityType.Builder.of(::MelonPult, MobCategory.CREATURE),
        width = 0.9f,
        height = 0.8f,
        attributes = PazPlant.Companion.PlantAttributes(
            maxHealth = 35.0,
            attackDamage = PazPlant.PEA_DAMAGE*3,
            followRange = 40.0,
        )
    )
    @JvmField val BONK_CHOY: EntityType<BonkChoy> = registerPlant(
        "bonkchoy",
        EntityType.Builder.of(::BonkChoy, MobCategory.CREATURE),
        height = 0.8f,
        eyeHeight = 0.5f,
        attributes = PazPlant.Companion.PlantAttributes(
            attackDamage = PazPlant.PEA_DAMAGE*2,
            attackKnockback = 0.45,
            attackRange = 3.0,
            followRange = 4.0,
        )
    )
    @JvmField val TANGLE_KELP: EntityType<TangleKelp> = registerPlant(
        "tanglekelp", EntityType.Builder.of(::TangleKelp, MobCategory.CREATURE),
        width = 1.0f,
        height = 0.4f,
        eyeHeight = 0.5f,
        attributes = PazPlant.Companion.PlantAttributes(
            maxHealth = 14.0,
            attackDamage = PazPlant.PEA_DAMAGE*8,
            followRange = 1.0,
            scale = 1.25
        )
    )
    @JvmField val PUFF_SHROOM: EntityType<PuffShroom> = registerPlant(
        "puffshroom", EntityType.Builder.of(::PuffShroom, MobCategory.CREATURE),
        width = 0.5f,
        height = 0.65f,
        eyeHeight = 0.3f,
        attributes = PazPlant.Companion.PlantAttributes(
            maxHealth = 12.0,
            attackDamage = PazPlant.PEA_DAMAGE,
            followRange = 10.0,
        )
    )
    @JvmField val SCAREDY_SHROOM: EntityType<ScaredyShroom> = registerPlant(
        "scaredyshroom", EntityType.Builder.of(::ScaredyShroom, MobCategory.CREATURE),
        width = 0.5f,
        height = 0.9f,
        eyeHeight = 0.5f,
        attributes = PazPlant.Companion.PlantAttributes(
            maxHealth = 18.0,
            followRange = 22.0,
            attackDamage = PazPlant.PEA_DAMAGE,
        )
    )
    @JvmField val FUME_SHROOM: EntityType<FumeShroom> = registerPlant(
        "fumeshroom",
        EntityType.Builder.of(::FumeShroom, MobCategory.CREATURE),
        width = 0.8f,
        height = 0.8f,
        attributes = PazPlant.Companion.PlantAttributes(
            attackDamage = PazPlant.PEA_DAMAGE*2,
        )
    )
    @JvmField val SUN_SHROOM: EntityType<SunShroom> = registerPlant(
        "sunshroom",
        EntityType.Builder.of(::SunShroom, MobCategory.CREATURE),
        height = 0.85f
    )
    @JvmField val HYPNOSHROOM: EntityType<HypnoShroom> = registerPlant(
        "hypnoshroom", EntityType.Builder.of(::HypnoShroom, MobCategory.CREATURE),
        width = 0.6f,
        height = 1.3f,
        eyeHeight = 0.6f,
        attributes = PazPlant.Companion.PlantAttributes(
            maxHealth = 4.0,
            followRange = 20.0
        )
    )
    @JvmField val DOOM_SHROOM: EntityType<DoomShroom> = registerPlant(
        "doomshroom", EntityType.Builder.of(::DoomShroom, MobCategory.CREATURE),
        eyeHeight = 0.6f,
        height = 0.8f,
        attributes = PazPlant.Companion.PlantAttributes(
            maxHealth = 28.0,
            followRange = 5.0
        )
    )
    @JvmField val ICE_SHROOM: EntityType<IceShroom> = registerPlant(
        "iceshroom", EntityType.Builder.of(::IceShroom, MobCategory.CREATURE),
        eyeHeight = 0.43f,
        height = 0.7f,
        attributes = PazPlant.Companion.PlantAttributes(
            maxHealth = 28.0,
            followRange = 5.0
        )
    )
    @JvmField val LILYPAD: EntityType<LilyPad> = registerPlant(
        "lilypad", EntityType.Builder.of(::LilyPad, MobCategory.CREATURE),
        width = 0.875f,
        height = 0.125f,
        eyeHeight = 0.125f,
        attributes = PazPlant.Companion.PlantAttributes(
            maxHealth = 12.0,
        )
    )
    @JvmField val SEA_SHROOM: EntityType<SeaShroom> = registerPlant(
        "seashroom", EntityType.Builder.of(::SeaShroom, MobCategory.CREATURE),
        width = 0.5f,
        height = 0.5f,
        attributes = PazPlant.Companion.PlantAttributes(
            maxHealth = 12.0,
            attackDamage = PazPlant.PEA_DAMAGE,
            followRange = 10.0,
        )
    )
    @JvmField val WATER_PEA_SHOOTER: EntityType<WaterPeaShooter> = registerPlant(
        "water_peashooter",
        EntityType.Builder.of(::WaterPeaShooter, MobCategory.CREATURE),
        attributes = PazPlant.Companion.PlantAttributes(
            attackDamage = PazPlant.PEA_DAMAGE,
        )
    )
    @JvmField val COFFEE_BEAN: EntityType<CoffeeBean> = registerPlant(
        "coffeebean", EntityType.Builder.of(::CoffeeBean, MobCategory.CREATURE),
        width = 0.4f,
        height = 0.5f,
        attributes = PazPlant.Companion.PlantAttributes(
            maxHealth = 4.0,
            followRange = 1.0
        )
    )
    @JvmField val FLOWER_POT: EntityType<FlowerPot> = registerPlant(
        "flower_pot", EntityType.Builder.of(::FlowerPot, MobCategory.CREATURE),
        width = 0.8f,
        height = 0.5f,
        attributes = PazPlant.Companion.PlantAttributes(
            maxHealth = 4.0,
            followRange = 1.0
        )
    )
    @JvmField val GRAVE_BUSTER: EntityType<GraveBuster> = registerPlant(
        "grave_buster", EntityType.Builder.of(::GraveBuster, MobCategory.CREATURE),
        width = 1.0f,
        height = 1.0f,
        eyeHeight = 0.6f,
        attributes = PazPlant.Companion.PlantAttributes(
            maxHealth = 6.0,
            followRange = 1.0
        )
    )
    @JvmField val WATER_POT: EntityType<WaterPot> = registerPlant(
        "water_pot", EntityType.Builder.of(::WaterPot, MobCategory.CREATURE),
        width = 1.0f,
        height = 0.75f,
        attributes = PazPlant.Companion.PlantAttributes(
            maxHealth = 4.0,
            followRange = 1.0
        )
    )
    @JvmField val LAVALILY: EntityType<LavaLily> = registerPlant(
        "lavalily", EntityType.Builder.of(::LavaLily, MobCategory.CREATURE).fireImmune(),
        width = 0.875f,
        height = 0.125f,
        eyeHeight = 0.125f,
        attributes = PazPlant.Companion.PlantAttributes(
            maxHealth = 12.0,
        )
    )
    @JvmField val SKY_PEA_SHOOTER: EntityType<SkyPeaShooter> = registerPlant(
        "sky_peashooter", EntityType.Builder.of(::SkyPeaShooter, MobCategory.CREATURE).fireImmune(),
        attributes = PazPlant.Companion.PlantAttributes(
            flyingSpeed = 0.2
        )
    )
    // endregion

    // region Zombies
    @JvmField val BROWN_COAT: EntityType<BrownCoat> =  registerZombie(
        "browncoat",
        EntityType.Builder.of(::BrownCoat, MobCategory.MONSTER)
            .sized(0.6f, 1.95f)
            .eyeHeight(1.74f)
            .clientTrackingRange(8),
        attributes = PazZombie.Companion.ZombieAttributes(
            spawnReinforcementsChance = 10.0
        )
    )
    @JvmField val NEWSPAPER_ZOMBIE: EntityType<NewspaperZombie> =  registerZombie(
        "newspaper_zombie",
        EntityType.Builder.of(::NewspaperZombie, MobCategory.MONSTER)
            .sized(0.6f, 1.95f)
            .eyeHeight(1.74f)
            .clientTrackingRange(8),
        attributes = PazZombie.Companion.ZombieAttributes(
            maxHealth = PazPlant.PEA_DAMAGE*8,
        )
    )
    @JvmField val DIGGER_ZOMBIE: EntityType<DiggerZombie> =  registerZombie(
        "digger_zombie",
        EntityType.Builder.of(::DiggerZombie, MobCategory.MONSTER)
            .sized(0.63f, 1.95f)
            .eyeHeight(1.74f)
            .clientTrackingRange(8),
        attributes = PazZombie.Companion.ZombieAttributes(
            maxHealth = PazPlant.PEA_DAMAGE*14,
        )
    )
    @JvmField val ENGINEER_ZOMBIE: EntityType<EngineerZombie> =  registerZombie(
        "engineer_zombie",
        EntityType.Builder.of(::EngineerZombie, MobCategory.MONSTER)
            .sized(0.63f, 1.95f)
            .eyeHeight(1.74f)
            .clientTrackingRange(8),
        attributes = PazZombie.Companion.ZombieAttributes(
            maxHealth = PazPlant.PEA_DAMAGE*12,
        )
    )
    @JvmField val ZOMBIE_YETI: EntityType<ZombieYeti> =  registerZombie(
        "zombie_yeti",
        EntityType.Builder.of(::ZombieYeti, MobCategory.MONSTER)
            .sized(1.25f, 2.6f)
            .clientTrackingRange(8),
        attributes = PazZombie.Companion.ZombieAttributes(
            attackDamage = PazPlant.PEA_DAMAGE*5,
            maxHealth = PazPlant.PEA_DAMAGE*48,
            knockbackResistance = 0.5,
            scale = 1.25,
            stepHeight = 1.0,
            interactionRange = 2.5
        )
    )
    @JvmField val DISCO_ZOMBIE: EntityType<DiscoZombie> =  registerZombie(
        "disco_zombie",
        EntityType.Builder.of(::DiscoZombie, MobCategory.MONSTER)
            .sized(0.64f, 2.2f)
            .eyeHeight(1.74f)
            .clientTrackingRange(8),
        attributes = PazZombie.Companion.ZombieAttributes(
            attackDamage = PazPlant.PEA_DAMAGE*2,
            maxHealth = PazPlant.PEA_DAMAGE*24,
            spawnReinforcementsChance = 0.1
        )
    )
    @JvmField val BACKUP_DANCER: EntityType<BackupDancer> =  registerZombie(
        "backup_dancer",
        EntityType.Builder.of(::BackupDancer, MobCategory.MONSTER)
            .sized(0.64f, 1.96f)
            .eyeHeight(1.74f)
            .clientTrackingRange(8),
        attributes = PazZombie.Companion.ZombieAttributes(
            attackDamage = PazPlant.PEA_DAMAGE,
            maxHealth = PazPlant.PEA_DAMAGE*6
        )
    )
    @JvmField val ALL_STAR: EntityType<AllStar> =  registerZombie(
        "all_star",
        EntityType.Builder.of(::AllStar, MobCategory.MONSTER)
            .sized(0.6f, 1.95f)
            .eyeHeight(1.74f)
            .clientTrackingRange(8),
        attributes = PazZombie.Companion.ZombieAttributes(
            attackDamage = PazPlant.PEA_DAMAGE*3,
            maxHealth = PazPlant.PEA_DAMAGE*6,
            stepHeight = 1.0,
            spawnReinforcementsChance = 1.5
        )
    )
    @JvmField val SOLDIER_ZOMBIE: EntityType<SoldierZombie> = registerZombie(
        "soldier_zombie",
        EntityType.Builder.of(::SoldierZombie, MobCategory.MONSTER)
            .sized(0.6f, 1.95f)
            .eyeHeight(1.74f)
            .clientTrackingRange(8),
        attributes = PazZombie.Companion.ZombieAttributes(
            attackDamage = PazPlant.PEA_DAMAGE*2,
            maxHealth = PazPlant.PEA_DAMAGE*16,
            movementSpeed = 0.237,
            spawnReinforcementsChance = 2.0
        )
    )
    @JvmField val PIRATE_CAPTAIN: EntityType<PirateCaptain> =  registerZombie(
        "pirate_captain",
        EntityType.Builder.of(::PirateCaptain, MobCategory.MONSTER)
            .sized(0.6f, 1.95f)
            .eyeHeight(1.74f)
            .clientTrackingRange(8),
        attributes = PazZombie.Companion.ZombieAttributes(
            armor = 4.0,
            attackDamage = 10.0,
            maxHealth = 120.0,
            movementSpeed = 0.21,
            spawnReinforcementsChance = 0.0,
        )
    )
    @JvmField val PIRATE_CAPTAIN_GHOST: EntityType<PirateCaptainGhost> =  registerZombie(
        "pirate_captain_ghost",
        EntityType.Builder.of(::PirateCaptainGhost, MobCategory.MONSTER)
            .sized(0.6f, 1.95f)
            .eyeHeight(1.74f)
            .clientTrackingRange(8),
        attributes = PazZombie.Companion.ZombieAttributes(
            attackDamage = 9.0,
            maxHealth = 80.0,
            movementSpeed = 0.25,
            followRange = 126.0,
            spawnReinforcementsChance = 0.0,
        )
    )
    @JvmField val ROBO_ZOMBIE: EntityType<RoboZombie> =  registerZombie(
        "robo_zombie",
        EntityType.Builder.of(::RoboZombie, MobCategory.MONSTER)
            .sized(1.3f, 1.95f)
            .eyeHeight(1.74f)
            .clientTrackingRange(8),
        attributes = PazZombie.Companion.ZombieAttributes(
            armor = 16.0,
            attackDamage = PazPlant.PEA_DAMAGE*3,
            maxHealth = PazPlant.PEA_DAMAGE*40,
            stepHeight = 1.0,
            movementSpeed = 0.23,
            knockbackResistance = 1.0,
            spawnReinforcementsChance = 0.0,
        )
    )
    @JvmField val SUPER_BRAINZ: EntityType<SuperBrainz> =  registerZombie(
        "super_brainz",
        EntityType.Builder.of(::SuperBrainz, MobCategory.MONSTER)
            .sized(1.0f, 2.2f)
            .eyeHeight(2.0f)
            .clientTrackingRange(8),
        attributes = PazZombie.Companion.ZombieAttributes(
            armor = 8.0,
            attackDamage = PazPlant.PEA_DAMAGE*3,
            maxHealth = PazPlant.PEA_DAMAGE*40,
            stepHeight = 1.0,
            movementSpeed = 0.25,
            knockbackResistance = 0.6,
            spawnReinforcementsChance = 0.0
        )
    )
    @JvmField val IMP: EntityType<Imp> =  registerZombie(
        "imp",
        EntityType.Builder.of(::Imp, MobCategory.MONSTER)
            .sized(0.45f, 0.95f)
            .passengerAttachments(2.075f)
            .ridingOffset(-0.7f)
            .clientTrackingRange(8),
        attributes = PazZombie.Companion.ZombieAttributes(
            attackDamage = PazPlant.PEA_DAMAGE*3,
            maxHealth = PazPlant.PEA_DAMAGE*4,
            movementSpeed = PazZombie.ZOMBIE_SPEED*1.5,
            spawnReinforcementsChance = 0.3
        )
    )
    @JvmField val GARGANTUAR: EntityType<Gargantuar> =  registerZombie(
        "gargantuar",
        EntityType.Builder.of(::Gargantuar, MobCategory.MONSTER)
            .sized(1.7f, 3.2f)
            .passengerAttachments(2.0f)
            .clientTrackingRange(8),
        attributes = PazZombie.Companion.ZombieAttributes(
            attackDamage = PazPlant.PEA_DAMAGE*8,
            maxHealth = PazPlant.PEA_DAMAGE*150,
            movementSpeed = PazZombie.ZOMBIE_SPEED*0.8,
            spawnReinforcementsChance = 0.0,
            knockbackResistance = 1.4,
            explosionKnockbackResistance = 0.7,
            scale = 1.33,
            stepHeight = 1.0,
            interactionRange = 3.5
        )
    )
    @JvmField val BALLOON_ZOMBIE: EntityType<BalloonZombie> = registerZombie(
        "balloon_zombie",
        EntityType.Builder.of(::BalloonZombie, MobCategory.MONSTER)
            .sized(0.6f, 1.95f)
            .eyeHeight(1.74f)
            .clientTrackingRange(8),
        attributes = PazZombie.Companion.ZombieAttributes(
            spawnReinforcementsChance = 10.0,
            flyingSpeed = PazZombie.ZOMBIE_SPEED,
            maxHealth = PazPlant.PEA_DAMAGE*8,
        )
    )
    // endregion

    @JvmField val ZOMBIE_TURRET: EntityType<Turret> = registerTurret(
        "zombie_turret",
        attributes = createMobAttributes()
            .add(Attributes.MAX_HEALTH, 20.0)
            .add(Attributes.ATTACK_DAMAGE, 2.0)
    )
    @JvmField val ELECTRO_TURRET: EntityType<Turret> = registerTurret(
        "electro_turret",
        attributes = createMobAttributes()
            .add(Attributes.MAX_HEALTH, 20.0)
            .add(Attributes.ATTACK_DAMAGE, 2.0)
    )

    @JvmField val GNOME: EntityType<Gnome> =  registerGnome(
        "gnome",
        EntityType.Builder.of(::Gnome, MobCategory.MONSTER)
            .sized(0.4f, 0.78f)
            .ridingOffset(-0.15f),
        attributes = createMobAttributes()
            .add(Attributes.STEP_HEIGHT, 1.0)
            .add(Attributes.MAX_HEALTH, 18.0)
            .add(Attributes.MOVEMENT_SPEED, 0.5)
            .add(Attributes.JUMP_STRENGTH, 0.4)
            .add(Attributes.KNOCKBACK_RESISTANCE, 0.3)
            .add(Attributes.ATTACK_DAMAGE, 2.5)
    )

    //region Projectiles
    @JvmField val THROWN_SUN_BOTTLE: EntityType<ThrownSunBottle> = registerProjectile("thrown_sun_bottle", EntityType.Builder.of({ _, l->ThrownSunBottle(l)}, MobCategory.MISC))
    @JvmField val PEA: EntityType<Pea> = registerProjectile("pea", EntityType.Builder.of({ _, l->
        Pea(
            l
        )
    }, MobCategory.MISC).sized(2.0f, 2.0f))
    @JvmField val PEA_ICE: EntityType<PeaIce> = registerProjectile("pea_ice", EntityType.Builder.of({ _, l-> PeaIce(l) }, MobCategory.MISC).sized(2.0f, 2.0f))
    @JvmField val PEA_FIRE: EntityType<PeaFire> = registerProjectile("pea_fire", EntityType.Builder.of({ _, l->
        PeaFire(
            l
        )
    }, MobCategory.MISC).sized(2.0f, 2.0f))
    @JvmField val PEA_WATER: EntityType<PeaWater> = registerProjectile("pea_water", EntityType.Builder.of({ _, l->
        PeaWater(
            l
        )
    }, MobCategory.MISC).sized(2.0f, 2.0f))
    @JvmField val PEA_ELECTRIC: EntityType<PeaElectric> = registerProjectile("pea_electric", EntityType.Builder.of({ _, l->
        PeaElectric(
            l
        )
    }, MobCategory.MISC).sized(2.0f, 2.0f))
    @JvmField val NEEDLE: EntityType<Needle> = registerProjectile("needle", EntityType.Builder.of({_,l->Needle(l)}, MobCategory.MISC).sized(2.0f, 2.0f), width = 0.42f, height = 0.42f)
    @JvmField val SPORE: EntityType<Spore> = registerProjectile("spore", EntityType.Builder.of({_,l->Spore(l)}, MobCategory.MISC).sized(2.0f, 2.0f))
    @JvmField val WATER_SPORE: EntityType<WaterSpore> = registerProjectile("water_spore", EntityType.Builder.of({_,l-> WaterSpore(l)}, MobCategory.MISC).sized(2.0f, 2.0f))
    @JvmField val CABBAGE: EntityType<Cabbage> = registerProjectile("cabbage", EntityType.Builder.of({_,l->Cabbage(l)}, MobCategory.MISC).sized(3.0f, 3.0f), width = 1.0f, height = 1.0f)
    @JvmField val KERNEL: EntityType<Kernel> = registerProjectile("kernel", EntityType.Builder.of({_,l->Kernel(l)}, MobCategory.MISC).sized(3.0f, 3.0f), width = 1.0f, height = 1.0f)
    @JvmField val BUTTER: EntityType<Butter> = registerProjectile("butter", EntityType.Builder.of({_,l->Butter(l)}, MobCategory.MISC).sized(3.0f, 3.0f), width = 1.5f, height = 1.0f)
    @JvmField val MELON: EntityType<Melon> = registerProjectile("melon", EntityType.Builder.of({_,l->Melon(l)}, MobCategory.MISC).sized(2.0f, 2.0f), width = 1.0f, height = 0.8f)
    @JvmField val PAINT_BALL: EntityType<PaintBall> = registerProjectile("paint_ball", EntityType.Builder.of({ _, l->PaintBall(l)}, MobCategory.MISC), width = 0.42f, height = 0.42f)
    @JvmField val MISSILE: EntityType<Missile> = registerProjectile("missile", EntityType.Builder.of({ _, l->Missile(l)}, MobCategory.MISC), width = 0.42f, height = 0.42f)
    // endregion

    //region Other
    @JvmField val PLANT_POT_MINECART: EntityType<PlantPotMinecart> = register(
        "plant_pot_minecart",
        EntityType.Builder.of(::PlantPotMinecart, MobCategory.MISC)
            .noLootTable()
            .sized(0.98F, 0.7F)
            .passengerAttachments(0.75F)
            .clientTrackingRange(8)
    )
    @JvmField val BALLOON: EntityType<Balloon> = registerBalloon(
        "balloon",
        EntityType.Builder.of(::Balloon, MobCategory.MISC)
            .noLootTable()
            .sized(0.6f, 0.7f)
            .clientTrackingRange(8),
        attributes = createMobAttributes()
            .add(Attributes.MAX_HEALTH, PazPlant.PEA_DAMAGE*3)
    )
    @JvmField val SUN: EntityType<Sun> = register(
        "sun",
        EntityType.Builder.of(::Sun, MobCategory.MISC)
            .fireImmune()
            .noLootTable()
            .sized(0.3F, 0.3F)
            .clientTrackingRange(6)
            .updateInterval(20)
    )
    // endregion

    private fun <T : LivingEntity> registerPlant(
        name : String,
        builder: EntityType.Builder<T> = EntityType.Builder.createNothing(MobCategory.CREATURE),
        width: Float = 0.6f,
        height: Float = 1.0f,
        eyeHeight: Float = height * 0.85f,
        attributes: PazPlant.Companion.PlantAttributes = PazPlant.Companion.PlantAttributes()
    ): EntityType<T> {
        builder.sized(width, height).eyeHeight(eyeHeight)
        val type = register(name, builder)
        FabricDefaultAttributeRegistry.register(type, attributes.apply(createMobAttributes()))
        return type
    }

    private fun <T : Zombie> registerZombie(
        name: String,
        builder: EntityType.Builder<T> = EntityType.Builder.createNothing(MobCategory.MONSTER),
        attributes: PazZombie.Companion.ZombieAttributes = PazZombie.Companion.ZombieAttributes()
    ): EntityType<T> {
        val type = register(name, builder
            .ridingOffset(-0.7f)
            .notInPeaceful())
        FabricDefaultAttributeRegistry.register(type, attributes.apply(createMobAttributes()))
        return type
    }

    private fun <T : Gnome> registerGnome(
        name : String,
        builder: EntityType.Builder<T> = EntityType.Builder.createNothing(MobCategory.MONSTER),
        attributes: AttributeSupplier.Builder = createMobAttributes()
    ): EntityType<T> {
        val type = register(name, builder)
        FabricDefaultAttributeRegistry.register(type, attributes)
        return type
    }

    private fun <T : Turret> registerTurret(
        name : String,
        builder: EntityType.Builder<T> = EntityType.Builder.createNothing(MobCategory.MONSTER),
        attributes: AttributeSupplier.Builder = createMobAttributes()
    ): EntityType<T> {
        val type = register(name, builder
            .ridingOffset(-0.7f)
            .notInPeaceful())
        FabricDefaultAttributeRegistry.register(type, attributes)
        return type
    }

    private fun <T : Projectile> registerProjectile(
        name : String,
        builder: EntityType.Builder<T> = EntityType.Builder.createNothing(MobCategory.MISC),
        width: Float = 0.3125f,
        height: Float = 0.3125f
    ): EntityType<T> {
        builder.sized(width, height).eyeHeight(0.0f)
        return register(name, builder)
    }

    private fun <T : Balloon> registerBalloon(
        name : String,
        builder: EntityType.Builder<T> = EntityType.Builder.createNothing(MobCategory.MISC),
        attributes: AttributeSupplier.Builder = createMobAttributes()
    ): EntityType<T> {
        val type = register(name, builder)
        FabricDefaultAttributeRegistry.register(type, attributes)
        return type
    }

    private fun <T : Entity> register(
        name : String,
        builder: EntityType.Builder<T>
    ): EntityType<T> {
        val id = ResourceKey.create(Registries.ENTITY_TYPE, pazResource(name))
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, id, builder.build(id))
    }

    val MAGIC_NAMES = mapOf<EntityType<*>, String>(
        CHOMPER      to "chester",
        DISCO_ZOMBIE to "mj",
        BROWN_COAT   to "tugboat"
    )
}