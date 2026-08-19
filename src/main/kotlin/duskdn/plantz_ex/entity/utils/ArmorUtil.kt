package duskdn.plantz_ex.entity.utils

import duskdn.plantz_ex.init.PazBlocks
import duskdn.plantz_ex.init.PazComponents
import duskdn.plantz_ex.init.PazItems
import duskdn.plantz_ex.init.PazSounds
import duskdn.plantz_ex.item.component.BlocksProjectileDamage
import duskdn.plantz_ex.mixin.EntityAccessor
import duskdn.plantz_ex.mixin.LivingEntityAccessor
import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair
import net.minecraft.advancements.triggers.CriteriaTriggers
import net.minecraft.core.component.DataComponents
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.stats.Stats
import net.minecraft.tags.DamageTypeTags
import net.minecraft.tags.EntityTypeTags
import net.minecraft.util.RandomSource
import net.minecraft.util.random.WeightedList
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import kotlin.math.ceil

enum class NullVariant(val item: Item?, val itemName: String?, val equipmentSlot: EquipmentSlot = EquipmentSlot.HEAD) {
    NONE(null, "basic"),
}

enum class HatVariant(val item: Item?, val itemName: String?, val equipmentSlot: EquipmentSlot = EquipmentSlot.HEAD) {
    CONE(PazBlocks.CONE.asItem(), "cone"),
    BUCKET(Items.BUCKET, "bucket"),
}

enum class ShieldVariant(val item: Item?, val itemName: String?, val equipmentSlot: EquipmentSlot = EquipmentSlot.OFFHAND) {
    SCREEN_DOOR(PazBlocks.SCREEN_DOOR.asItem(), "screen_door"),
}

enum class FlagVariant(val item: Item?, val itemName: String?, val equipmentSlot: EquipmentSlot = EquipmentSlot.MAINHAND) {
    FLAG(PazBlocks.PLANTZ_FLAG.asItem(), "flag")
}

object MobHatWeights {

    @JvmStatic
    var randomizer: WeightedList<Any>

    init {

        val builder = WeightedList.builder<Any>().apply {
            add(NullVariant.NONE, 400)
            add(HatVariant.CONE, 200)
            add(HatVariant.BUCKET, 100)
        }

        randomizer = builder.build()

    }

}

object MobShieldWeights {

    @JvmStatic
    var randomizer: WeightedList<Any>

    init {

        val builder = WeightedList.builder<Any>().apply {
            add(NullVariant.NONE, 500)
            add(ShieldVariant.SCREEN_DOOR, 100)
        }

        randomizer = builder.build()

    }

}

object MobFlagWeights {

    @JvmStatic
    var randomizer: WeightedList<Any>

    init {

        val builder = WeightedList.builder<Any>().apply {
            add(NullVariant.NONE, 100)
            add(FlagVariant.FLAG, 5)
        }

        randomizer = builder.build()

    }

}

object ArmorUtil {

    @JvmStatic
    fun getArmor(mob: Mob, random: RandomSource): MutableList<Pair<EquipmentSlot, ItemStack>> {

        val equipment: MutableList<Pair<EquipmentSlot, ItemStack>> = mutableListOf()

        val mobHat = MobHatWeights.randomizer.getRandom(random)

        val mobShield = MobShieldWeights.randomizer.getRandom(random)

        val mobFlag = MobFlagWeights.randomizer.getRandom(random)

        val randomCull: Float = 1.0f

        if (mobFlag.)

    }

    @JvmStatic
    fun checkForArmor(entity: LivingEntity): MutableList<Pair<EquipmentSlot, Pair<ItemStack, BlocksProjectileDamage>>> {

        val armors = mutableListOf<Pair<EquipmentSlot, Pair<ItemStack, BlocksProjectileDamage>>>()

        val slots: Set<EquipmentSlot> = setOf<EquipmentSlot>(
            EquipmentSlot.HEAD,
            EquipmentSlot.CHEST,
            EquipmentSlot.LEGS,
            EquipmentSlot.FEET,
            EquipmentSlot.MAINHAND,
            EquipmentSlot.OFFHAND
        )

        for (slot in slots) {
            val item: ItemStack = entity.getItemBySlot(slot)
            val component = item.components.get(PazComponents.BLOCKS_PROJECTILE_DAMAGE) ?: continue
            if (component.mustBeUsing && !entity.isUsingItem) continue

            val validSlot = component.slot
            val matchesSlot = validSlot.test(slot)
            if (!matchesSlot) continue

            armors.add(slot to (item to component))
        }

        return armors
    }

    @JvmStatic
    fun damageArmor(entity: LivingEntity, slot: EquipmentSlot, item: Pair<ItemStack, BlocksProjectileDamage>, damage: Double): Double {

        val armor = item.first

        val component = item.second

//        if (!component.tanksDamage) return damage

        val curArmorHealth = armor.maxDamage - armor.damageValue

        armor.hurtAndBreak(ceil(damage).toInt(), entity, slot)

        if (entity.level() is ServerLevel && (curArmorHealth-damage) <= 0) {
            armor.shrink(1)
            entity.playSound(SoundEvents.ITEM_BREAK.value())
        } else {

            val hitSound: SoundEvent

            if (armor.`is`(PazItems.NEWSPAPER)) hitSound = PazSounds.PROJECTILE_HIT_PAPER
            else if (armor.`is`(Items.BUCKET) || armor.`is`(PazBlocks.SCREEN_DOOR.asItem())) hitSound = PazSounds.PROJECTILE_HIT_BUCKET
            else hitSound = PazSounds.PROJECTILE_HIT_CONE

            entity.playSound(hitSound)
        }

        return damage-curArmorHealth

    }

    fun entityHurtServer(entity: LivingEntity, source: DamageSource, dmg: Float): Boolean {

        var damage = dmg

        val accessor = (entity as LivingEntityAccessor)

        val level = entity.level() as ServerLevel

        if (entity.isInvulnerableTo(level, source)) {
            return false
        } else if (entity.isDeadOrDying) {
            return false
        } else if (source.`is`(DamageTypeTags.IS_FIRE) && entity.hasEffect(MobEffects.FIRE_RESISTANCE)) {
            return false
        } else {
            if (entity.isSleeping) {
                entity.stopSleeping()
            }

            entity.noActionTime = 0

            val itemInUse: ItemStack = entity.getUseItem()
            val damageBlocked: Float = entity.applyItemBlocking(entity.level() as ServerLevel, source, damage)
            damage -= damageBlocked
            val blocked = damageBlocked > 0.0f
            if (source.`is`(DamageTypeTags.IS_FREEZING) && entity.`is`(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)) {
                damage *= 5.0f
            }

            if (java.lang.Float.isNaN(damage) || java.lang.Float.isInfinite(damage)) {
                damage = Float.MAX_VALUE
            }

            accessor.invokeSetLastHurt(damage)
            entity.invulnerableTime = 20

            accessor.invokeActuallyHurt(entity.level() as ServerLevel?, source, damage)

            entity.hurtDuration = 10
            entity.hurtTime = entity.hurtDuration

            accessor.invokeResolvePlayerResponsibleForDamage(source)
            accessor.invokeResolveMobResponsibleForDamage(source)

            val blocksAttacks = itemInUse.get(DataComponents.BLOCKS_ATTACKS)
            if (blocked && blocksAttacks != null) {
                blocksAttacks.onBlocked(level, entity)
            } else {
                level.broadcastDamageEvent(entity, source)
            }

            if (!source.`is`(DamageTypeTags.NO_IMPACT) && (!blocked)) {
                (entity as EntityAccessor).invokeMarkHurt()
            }

            if (!source.`is`(DamageTypeTags.NO_KNOCKBACK)) {
                var xd = 0.0
                var zd = 0.0
                if (source.directEntity is Projectile) {

                    val projectile = source.directEntity as Projectile

                    val knockbackDirection: DoubleDoubleImmutablePair =
                        projectile.calculateHorizontalHurtKnockbackDirection(entity, source)
                    xd = -knockbackDirection.leftDouble()
                    zd = -knockbackDirection.rightDouble()
                } else if (source.sourcePosition != null) {
                    xd = source.sourcePosition!!.x() - entity.x
                    zd = source.sourcePosition!!.z() - entity.z
                }

                entity.knockback(0.4, xd, zd, source, damage)
                if (!blocked) {
                    entity.indicateDamage(xd, zd)
                }
            }

            if (entity.isDeadOrDying) {
                if (!accessor.invokeCheckTotemDeathProtection(source)) {

                    entity.makeSound(accessor.invokeGetDeathSound())
                    accessor.invokePlaySecondaryHurtSound(source)

                    entity.die(source)
                }
            } else if (damage > 0.0) {
                accessor.invokePlayHurtSound(source)
                accessor.invokePlaySecondaryHurtSound(source)
            }

            for (effect in entity.activeEffects) {
                effect.onMobHurt(level, entity, source, damage)
            }

            if (entity is ServerPlayer) {

                val serverPlayer = entity as ServerPlayer

                CriteriaTriggers.ENTITY_HURT_PLAYER.trigger(serverPlayer, source, damage, damage, blocked)
                if (damageBlocked > 0.0f && damageBlocked < 3.4028235E37f) {
                    serverPlayer.awardStat(Stats.DAMAGE_BLOCKED_BY_SHIELD, Math.round(damageBlocked * 10.0f))
                }
            }

            if (source.getEntity() is ServerPlayer) {

                val sourcePlayer = source.getEntity() as ServerPlayer

                CriteriaTriggers.PLAYER_HURT_ENTITY.trigger(sourcePlayer, entity, source, damage, damage, blocked)
            }

            return true
        }
    }

    @JvmStatic
    fun goThroughArmorsAndDamage(level: ServerLevel, source: DamageSource, entity: LivingEntity, damage: Float, armors: MutableList<Pair<EquipmentSlot, Pair<ItemStack, BlocksProjectileDamage>>>, damageEntity: Boolean = true) {

        var damageMult = 1.0f

        if (source.directEntity !is Projectile && source.directEntity is LivingEntity) {
            damageMult = (source.directEntity as LivingEntity).attributes.getValue(Attributes.ATTACK_DAMAGE).toFloat()
        }

        var leftoverDamage: Float = damage * damageMult

        val accessor = (entity as LivingEntityAccessor)

        for (armor in armors) {
            if (leftoverDamage <= 0) {
                break
            }

            if (armor.second.second.reflectsDamage && source.directEntity is Projectile || armor.second.second.tanksDamage) {

                leftoverDamage = damageArmor(
                    entity,
                    armor.first,
                    armor.second,
                    leftoverDamage.toDouble()
                ).toFloat()

            }

            accessor.invokeActuallyHurt(level, source, 0.001f)
        }

        accessor.invokeResolveMobResponsibleForDamage(source)
        accessor.invokeResolvePlayerResponsibleForDamage(source)

        if (damageEntity) {
            entityHurtServer(entity, source, leftoverDamage.coerceAtLeast(0f) / damageMult)
        }
    }

    @JvmStatic
    fun doArmorDamage(level: ServerLevel, source: DamageSource, entity: LivingEntity, damage: Float): Boolean {
        val armors: MutableList<Pair<EquipmentSlot, Pair<ItemStack, BlocksProjectileDamage>>> = checkForArmor(entity)

        if (!armors.isEmpty()) {
            goThroughArmorsAndDamage(level, source, entity, damage, armors)

            return true
        }

        return false

    }

}