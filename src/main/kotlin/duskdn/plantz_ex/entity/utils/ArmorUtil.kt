package duskdn.plantz_ex.entity.utils

import duskdn.plantz_ex.init.PazBlocks
import duskdn.plantz_ex.init.PazComponents
import duskdn.plantz_ex.init.PazItems
import duskdn.plantz_ex.init.PazSounds
import duskdn.plantz_ex.item.component.BlocksProjectileDamage
import duskdn.plantz_ex.mixin.EntityAccessor
import duskdn.plantz_ex.mixin.LivingEntityAccessor
import duskdn.plantz_ex.util.blueDustParticle
import duskdn.plantz_ex.util.defaultDustParticle
import duskdn.plantz_ex.util.orangeDustParticle
import duskdn.plantz_ex.util.redDustParticle
import duskdn.plantz_ex.util.trackVector
import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair
import net.minecraft.advancements.triggers.CriteriaTriggers
import net.minecraft.core.Direction
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
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.phys.Vec3
import kotlin.math.absoluteValue
import kotlin.math.ceil

enum class ArmorVariant(val item: Item?, val itemName: String?, val equipmentSlot: EquipmentSlot? = null) {
    NONE(null, "basic", null),
    CONE(PazBlocks.CONE.asItem(), "cone", EquipmentSlot.HEAD),
    BUCKET(Items.BUCKET, "bucket", EquipmentSlot.HEAD),
    FOOTBALL_HELEMT(PazItems.FOOTBALL_HELMET, "football_helmet", EquipmentSlot.HEAD),
    SCREEN_DOOR(PazBlocks.SCREEN_DOOR.asItem(), "screen_door", EquipmentSlot.OFFHAND),
    FLAG(PazBlocks.BRAINZ_FLAG.asItem(), "flag", EquipmentSlot.MAINHAND);

    companion object {
        val ITEM_KEY: Map<Item, ArmorVariant> = entries
            .filter { it.item != null }
            .associateBy { it.item!! }

        val defaultArmorVariants: List<ArmorVariant> = listOf(
            CONE,
            BUCKET,
            SCREEN_DOOR,
            FLAG
        )

        val defaultArmorVariantsWithNone: List<ArmorVariant> = defaultArmorVariants.plus(NONE)

        fun getByItem(item: Item?): ArmorVariant = ITEM_KEY[item] ?: NONE
    }
}

data object HatVariants {
    val variants: List<ArmorVariant> = listOf(
        ArmorVariant.CONE,
        ArmorVariant.BUCKET
    )
}

data object ShieldVariants {
    val variants: List<ArmorVariant> = listOf(
        ArmorVariant.SCREEN_DOOR,
    )
}

data object FlagVariants {
    val variants: List<ArmorVariant> = listOf(
        ArmorVariant.FLAG,
    )
}

object MobHatWeights {

    @JvmStatic
    var randomizer: WeightedList<ArmorVariant>

    init {

        val builder = WeightedList.builder<ArmorVariant>().apply {
            add(ArmorVariant.NONE, 400)
            add(ArmorVariant.CONE, 200)
            add(ArmorVariant.BUCKET, 100)
        }

        randomizer = builder.build()

    }

}

object MobShieldWeights {

    @JvmStatic
    var randomizer: WeightedList<ArmorVariant>

    init {

        val builder = WeightedList.builder<ArmorVariant>().apply {
            add(ArmorVariant.NONE, 500)
            add(ArmorVariant.SCREEN_DOOR, 100)
        }

        randomizer = builder.build()

    }

}

object MobFlagWeights {

    @JvmStatic
    var randomizer: WeightedList<ArmorVariant>

    init {

        val builder = WeightedList.builder<ArmorVariant>().apply {
            add(ArmorVariant.NONE, 200)
            add(ArmorVariant.FLAG, 1)
        }

        randomizer = builder.build()

    }

}

object ArmorUtil {

    @JvmStatic
    fun getArmor(random: RandomSource, spawnReason: EntitySpawnReason = EntitySpawnReason.MOB_SUMMONED): MutableList<Pair<EquipmentSlot, ItemStack>> {

        val equipment: MutableList<Pair<EquipmentSlot, ItemStack>> = mutableListOf()

        val mobHat = MobHatWeights.randomizer.getRandom(random).get()

        val mobShield = MobShieldWeights.randomizer.getRandom(random).get()

        val mobFlag = MobFlagWeights.randomizer.getRandom(random).get()

        var randomCull: Float = 1.0f

        if (mobFlag.item != null && mobFlag.equipmentSlot != null && random.nextFloat() < randomCull && spawnReason != EntitySpawnReason.REINFORCEMENT) {

            equipment.add(
                mobFlag.equipmentSlot to mobFlag.item.defaultInstance
            )

            randomCull *= 0.1f

        }

        if (mobShield.item != null && mobShield.equipmentSlot != null && random.nextFloat() < randomCull) {

            equipment.add(
                mobShield.equipmentSlot to mobShield.item.defaultInstance
            )

            randomCull *= 0.1f

        }

        if (mobHat.item != null && mobHat.equipmentSlot != null && random.nextFloat() < randomCull) {

            equipment.add(
                mobHat.equipmentSlot to mobHat.item.defaultInstance
            )

        }

        return equipment

    }

    @JvmStatic
    fun addArmor(mob: Mob, spawnReason: EntitySpawnReason = EntitySpawnReason.MOB_SUMMONED) {

        val equipment: MutableList<Pair<EquipmentSlot, ItemStack>> = getArmor(mob.random, spawnReason)

        for (item in equipment) {

            mob.setItemSlot(item.first, item.second)

        }

    }

    @JvmStatic
    fun checkForArmor(entity: LivingEntity): MutableList<Pair<EquipmentSlot, Pair<ItemStack, BlocksProjectileDamage>>> {

        val armors = mutableListOf<Pair<EquipmentSlot, Pair<ItemStack, BlocksProjectileDamage>>>()

        val slots: Set<EquipmentSlot> = setOf(
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

            println("mustBeUsing: ${component.mustBeUsing} EntityUsing: ${entity.isUsingItem}")

            if (component.mustBeUsing && !entity.isUsingItem) continue

            val validSlot = component.slot
            val matchesSlot = validSlot.test(slot)
            if (!matchesSlot) continue

            val armorVariant: ArmorVariant = ArmorVariant.getByItem(item.item)

            println("Variant: $armorVariant VariantSlot: ${armorVariant.equipmentSlot} Slot: $slot")

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
    @JvmOverloads
    fun goThroughArmorsAndDamage(level: ServerLevel, source: DamageSource, entity: LivingEntity, damage: Float, armors: MutableList<Pair<EquipmentSlot, Pair<ItemStack, BlocksProjectileDamage>>>, damageEntity: Boolean = true, hitPosition: Vec3 = entity.lookAngle): Boolean {

        var damageMult = 1.0f

        if (source.directEntity !is Projectile && source.directEntity is LivingEntity) {
            damageMult = (source.directEntity as LivingEntity).attributes.getValue(Attributes.ATTACK_DAMAGE).toFloat()
        }

        var leftoverDamage: Float = damage * damageMult

        val accessor = (entity as LivingEntityAccessor)

        val attackPos = hitPosition

        val lookVector = entity.lookAngle

        val centerPos = entity.position().relative(Direction.UP, entity.bbHeight/2.0)

        var attackDirection = attackPos.vectorTo(centerPos).normalize()

        val xzDirection = Vec3(attackDirection.x, 0.0, attackDirection.z).normalize()

        val xzLookDirection = Vec3(lookVector.x, 0.0, lookVector.z).normalize()

        val xzDot = xzDirection.dot(xzLookDirection)

        val yDot = attackDirection.y

        trackVector(level, orangeDustParticle, attackPos)

        trackVector(level, defaultDustParticle, lookVector)

        trackVector(level, blueDustParticle, entity.eyePosition, attackDirection.scale(3.0))

        var reflectsDamage = false

        for (armor in armors) {
            if (leftoverDamage <= 0) {
                break
            }

            if (armor.second.second.reflectsDamage && source.directEntity is Projectile && !reflectsDamage && !damageEntity) {

                val reflectDistance = armor.second.second.reflectDistance

                reflectsDamage = ((xzDot < reflectDistance) && (yDot.absoluteValue < 0.8))

                println("TESTING: ${xzDot < reflectDistance}, ${yDot.absoluteValue < 0.8}")

                println("REFLECTS DAMAGE: $reflectsDamage, REFLECT DISTANCE IS: $reflectDistance, xzDot: $xzDot, yDot: $yDot, xzDirection $xzDirection, xzLookDirection: $xzLookDirection")

                if (!reflectsDamage) return false

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

        return true
    }

    @JvmStatic
    fun doArmorDamage(level: ServerLevel, source: DamageSource, entity: LivingEntity, damage: Float): Boolean {
        val armors: MutableList<Pair<EquipmentSlot, Pair<ItemStack, BlocksProjectileDamage>>> = checkForArmor(entity)

        armors.removeIf { armor: Pair<EquipmentSlot?, Pair<ItemStack?, BlocksProjectileDamage?>?>? -> armor!!.second!!.second!!.reflectsDamage }

        if (!armors.isEmpty()) {
            return goThroughArmorsAndDamage(level, source, entity, damage, armors)
        }

        return false

    }

}