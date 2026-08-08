package duskdn.plantz.init

import duskdn.plantz.effect.ButteredMobEffect
import duskdn.plantz.effect.ChilledMobEffect
import duskdn.plantz.effect.DrenchedMobEffect
import duskdn.plantz.effect.ElectrifyMobEffect
import duskdn.plantz.effect.HypnotizedMobEffect
import duskdn.plantz.effect.PaintedMobEffect
import duskdn.plantz.effect.TangledMobEffect
import duskdn.plantz.effect.ToxicMobEffect
import duskdn.plantz.effect.ZombieOmenMobEffect
import duskdn.plantz.util.pazResource
import net.fabricmc.fabric.api.attachment.v1.AttachmentType
import net.fabricmc.fabric.impl.attachment.AttachmentRegistryImpl
import net.minecraft.core.Holder
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.alchemy.Potion

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
            .addAttributeModifier(Attributes.MOVEMENT_SPEED, pazResource("effect.chilled"), -0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(Attributes.FLYING_SPEED, pazResource("effect.chilled"), -0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(Attributes.ATTACK_SPEED, pazResource("effect.chilled"), -0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
            .addAttributeModifier(Attributes.BLOCK_BREAK_SPEED, pazResource("effect.chilled"), -0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
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
    fun registerPotion(name: String, effects: MobEffectInstance): Holder<Potion> {
        val potion = Potion(name, effects)
        return Registry.registerForHolder(
            BuiltInRegistries.POTION,
            pazResource(name),
            potion
        )
    }

    val HYPNOTIZED_GOAL_ATTACHMENT: AttachmentType<Goal> =
        AttachmentRegistryImpl.builder<Goal>().buildAndRegister(pazResource("hypnotized_goal"))

    fun initialize() {

    }
}