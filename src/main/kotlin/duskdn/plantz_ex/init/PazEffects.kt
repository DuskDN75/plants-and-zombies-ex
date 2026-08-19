package duskdn.plantz_ex.init

import duskdn.plantz_ex.effect.ButteredMobEffect
import duskdn.plantz_ex.effect.ChilledMobEffect
import duskdn.plantz_ex.effect.DrenchedMobEffect
import duskdn.plantz_ex.effect.ElectrifyMobEffect
import duskdn.plantz_ex.effect.EnlightenedMobEffect
import duskdn.plantz_ex.effect.HypnotizedMobEffect
import duskdn.plantz_ex.effect.PaintedMobEffect
import duskdn.plantz_ex.effect.TangledMobEffect
import duskdn.plantz_ex.effect.ToxicMobEffect
import duskdn.plantz_ex.effect.ZombieOmenMobEffect
import duskdn.plantz_ex.util.pazResource
import net.fabricmc.fabric.api.attachment.v1.AttachmentType
import net.fabricmc.fabric.api.registry.FabricPotionBrewingBuilder
import net.fabricmc.fabric.impl.attachment.AttachmentRegistryImpl
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.component.DataComponents
import net.minecraft.core.particles.BlockParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potion
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.level.block.Blocks

object PazEffects {

    @JvmField val TOXIC: Holder<MobEffect> = register("toxic",
        ToxicMobEffect(MobEffectCategory.HARMFUL, 10762143, PazServerParticles.SPORE_HIT))
    @JvmField val HYPNOTIZE: Holder<MobEffect> = register("hypnotize",
        HypnotizedMobEffect(MobEffectCategory.NEUTRAL, 15841255, PazServerParticles.HYPNO_SPORE))
    @JvmField val ZOMBIE_OMEN : Holder<MobEffect> = register("zombie_omen",
        ZombieOmenMobEffect(MobEffectCategory.NEUTRAL, 1297708, PazServerParticles.ZOMBIE_OMEN)
            .withSoundOnAdded(PazSounds.APPLY_ZOMBIE_OMEN))
    @JvmField val ELECTRIFIED : Holder<MobEffect> = register("electrified",
        ElectrifyMobEffect(MobEffectCategory.HARMFUL, 0x87FFFB, PazServerParticles.ELECTRIFIED))
    @JvmField val PAINTED : Map<DyeColor, Holder<MobEffect>> = (
            DyeColor.entries.associateWith { color -> register("painted/${color}",
                PaintedMobEffect(MobEffectCategory.HARMFUL, color.fireworkColor))
                //.withSoundOnAdded(PazSounds.APPLY_ZOMBIE_OMEN))
            })
    @JvmField val BUTTERED: Holder<MobEffect> = register("buttered",
        ButteredMobEffect(MobEffectCategory.HARMFUL, 13416767, PazServerParticles.BUTTER_DRIP)
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, pazResource("effect.buttered"), -999.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(Attributes.JUMP_STRENGTH, pazResource("effect.buttered"), -999.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(Attributes.ATTACK_DAMAGE, pazResource("effect.buttered"), -999.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(Attributes.ENTITY_INTERACTION_RANGE,
                pazResource("effect.buttered"), -999.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(Attributes.BLOCK_INTERACTION_RANGE,
                pazResource("effect.buttered"), -999.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    )

    @JvmField val CHILLED: Holder<MobEffect> = register("chilled",
        ChilledMobEffect(MobEffectCategory.HARMFUL, 0x8BC1FF)
    )

    @JvmField val DRENCHED: Holder<MobEffect> = register("drenched",
        DrenchedMobEffect(MobEffectCategory.HARMFUL, 0x3F76E4)
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, pazResource("effect.drenched"), -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(Attributes.FLYING_SPEED, pazResource("effect.drenched"), -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(Attributes.ARMOR_TOUGHNESS, pazResource("effect.drenched"), -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(Attributes.JUMP_STRENGTH, pazResource("effect.drenched"), -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(Attributes.ATTACK_SPEED, pazResource("effect.drenched"), -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(Attributes.BLOCK_BREAK_SPEED, pazResource("effect.drenched"), -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    )
    @JvmField val TANGLED: Holder<MobEffect> = register("tangled",
        TangledMobEffect(
            MobEffectCategory.HARMFUL,
            0x354023,
            BlockParticleOption(ParticleTypes.BLOCK, Blocks.SWEET_BERRY_BUSH.defaultBlockState())
        )
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, pazResource("effect.tangled"), -999.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(Attributes.WATER_MOVEMENT_EFFICIENCY, pazResource("effect.tangled"), -999.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(Attributes.JUMP_STRENGTH, pazResource("effect.tangled"), -999.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(Attributes.ATTACK_DAMAGE, pazResource("effect.tangled"), -999.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(Attributes.FOLLOW_RANGE, pazResource("effect.tangled"), -999.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(Attributes.ENTITY_INTERACTION_RANGE, pazResource("effect.tangled"), -999.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(Attributes.BLOCK_INTERACTION_RANGE, pazResource("effect.tangled"), -999.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    )

    @JvmField val ENLIGHTENED: Holder<MobEffect> = register("enlightened",
        EnlightenedMobEffect(MobEffectCategory.NEUTRAL, 0xFFFDD2))

    fun register(name: String, mobEffect: MobEffect): Holder<MobEffect> {
        return Registry.registerForHolder(
            BuiltInRegistries.MOB_EFFECT,
            pazResource(name),
            mobEffect
        )
    }

    @JvmField val HYPNOTIZE_POTION: Holder<Potion> = registerPotion("hypnotize", MobEffectInstance(HYPNOTIZE, 3600))
    @JvmField val BUTTERED_POTION: Holder<Potion> = registerPotion("buttered", MobEffectInstance(BUTTERED, 100))
    @JvmField val ELECTRIFIED_POTION: Holder<Potion> = registerPotion("electrified", MobEffectInstance(ELECTRIFIED, 200))
    @JvmField val CHILLED_POTION: Holder<Potion> = registerPotion("chilled", MobEffectInstance(CHILLED, 100))
    @JvmField val DRENCHED_POTION: Holder<Potion> = registerPotion("drenched", MobEffectInstance(DRENCHED, 100))
    @JvmField val ENLIGHTENED_POTION: Holder<Potion> = registerPotion("enlightened", MobEffectInstance(ENLIGHTENED, 100))
    @JvmField val CURSED_POTION: Holder<Potion> = registerPotion(
        "cursed",
        MobEffectInstance(MobEffects.UNLUCK, 1200),
        MobEffectInstance(MobEffects.SLOWNESS, 1200),
        MobEffectInstance(MobEffects.WEAKNESS, 1200)
    )
    @JvmField val AWAKENING_POTION: Holder<Potion> = registerPotion(
        "awakening",
        MobEffectInstance(MobEffects.SPEED, 1200),
        MobEffectInstance(MobEffects.INFESTED, 1200),
        MobEffectInstance(MobEffects.HUNGER, 1200),
        MobEffectInstance(MobEffects.NAUSEA, 1200),
        MobEffectInstance(MobEffects.BLINDNESS, 1200),
        MobEffectInstance(MobEffects.HASTE, 1200),
        MobEffectInstance(MobEffects.POISON, 1200),
        MobEffectInstance(MobEffects.MINING_FATIGUE, 1200),
        MobEffectInstance(MobEffects.SLOWNESS, 1200),
        MobEffectInstance(MobEffects.WEAKNESS, 1200),
        MobEffectInstance(MobEffects.STRENGTH, 1200)
    )
    @JvmField val SPROUTING_POTION: Holder<Potion> = registerPotion(
        "sprouting",
        MobEffectInstance(MobEffects.SPEED, 600),
        MobEffectInstance(MobEffects.STRENGTH, 600),
        MobEffectInstance(MobEffects.HEALTH_BOOST, 1200),
        MobEffectInstance(MobEffects.ABSORPTION, 1200),
        MobEffectInstance(MobEffects.LUCK, 200)
    )

    fun registerBasicPotion(name: String): Holder<Potion> {
        val potion = Potion(name)
        return Registry.registerForHolder(
            BuiltInRegistries.POTION,
            pazResource(name),
            potion
        )
    }

    fun registerPotion(name: String, vararg effects: MobEffectInstance): Holder<Potion> {
        val potion = Potion(name, *effects)
        return Registry.registerForHolder(
            BuiltInRegistries.POTION,
            pazResource(name),
            potion
        )
    }

    val HYPNOTIZED_GOAL_ATTACHMENT: AttachmentType<Goal> =
        AttachmentRegistryImpl.builder<Goal>().buildAndRegister(pazResource("hypnotized_goal"))

    fun initialize() {

        FabricPotionBrewingBuilder.BUILD.register { builder ->
            builder.registerPotionRecipe(
                Potions.WEAKNESS,
                Ingredient.of(Items.MILK_BUCKET),
                BUTTERED_POTION
            )
        }

        FabricPotionBrewingBuilder.BUILD.register { builder ->
            builder.registerPotionRecipe(
                Potions.MUNDANE,
                Ingredient.of(Blocks.SOUL_SAND.asItem()),
                CURSED_POTION
            )
        }

        FabricPotionBrewingBuilder.BUILD.register { builder ->
            builder.registerPotionRecipe(
                CURSED_POTION,
                Ingredient.of(Blocks.CARVED_PUMPKIN.asItem()),
                AWAKENING_POTION
            )
        }

        FabricPotionBrewingBuilder.BUILD.register { builder ->
            builder.registerPotionRecipe(
                Potions.REGENERATION,
                Ingredient.of(PazItems.SUN),
                ENLIGHTENED_POTION
            )
        }

        FabricPotionBrewingBuilder.BUILD.register { builder ->
            builder.registerPotionRecipe(
                Potions.POISON,
                Ingredient.of(Items.RED_MUSHROOM),
                HYPNOTIZE_POTION
            )
        }

        FabricPotionBrewingBuilder.BUILD.register { builder ->
            builder.registerPotionRecipe(
                Potions.AWKWARD,
                Ingredient.of(Items.WET_SPONGE),
                DRENCHED_POTION
            )
        }

        FabricPotionBrewingBuilder.BUILD.register { builder ->
            builder.registerPotionRecipe(
                Potions.AWKWARD,
                Ingredient.of(Blocks.ICE.asItem()),
                CHILLED_POTION
            )
        }

//        FabricPotionBrewingBuilder.BUILD.register { builder ->
//            builder.registerPotionRecipe(
//                AWAKENING_POTION,
//                Ingredient.of(ItemStack(Items.POTION).apply { set(DataComponents.POTION_CONTENTS, PotionContents(ENLIGHTENED_POTION)) }.item),
//                SPROUTING_POTION
//            )
//        }

    }
}