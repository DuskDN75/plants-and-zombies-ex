package duskdn.plantz.entity.utils

import duskdn.plantz.init.PazComponents
import duskdn.plantz.init.PazItems
import duskdn.plantz.init.PazSounds
import duskdn.plantz.item.component.BlocksProjectileDamage
import duskdn.plantz.mixin.LivingEntityAccessor
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import kotlin.math.ceil

object ArmorUtil {

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

        if (!component.tanksDamage) return damage

        val curArmorHealth = armor.maxDamage - armor.damageValue

        armor.hurtAndBreak(ceil(damage).toInt(), entity, slot)

        if (entity.level() is ServerLevel && (curArmorHealth-damage) <= 0) {
            armor.shrink(1)
            entity.playSound(SoundEvents.ITEM_BREAK.value())
        } else {

            val hitSound: SoundEvent

            if (armor.`is`(PazItems.NEWSPAPER)) hitSound = PazSounds.PROJECTILE_HIT_PAPER
            else if (armor.`is`(Items.BUCKET)) hitSound = PazSounds.PROJECTILE_HIT_BUCKET
            else hitSound = PazSounds.PROJECTILE_HIT_CONE

            entity.playSound(hitSound)
        }

        return damage-curArmorHealth

    }

    @JvmStatic
    fun goThroughArmorsAndDamage(level: ServerLevel, source: DamageSource, entity: LivingEntity, damage: Float, armors: MutableList<Pair<EquipmentSlot, Pair<ItemStack, BlocksProjectileDamage>>>, damageEntity: Boolean = true) {

        var damageMult = 1.0f

        if (source.directEntity !is Projectile) {
            damageMult = 3.0f
        }

        var leftoverDamage: Float = damage * damageMult

        val accessor = (entity as LivingEntityAccessor)

        for (armor in armors) {
            if (leftoverDamage <= 0) {
                break
            }

            leftoverDamage = ArmorUtil.damageArmor(
                entity,
                armor.first,
                armor.second,
                leftoverDamage.toDouble()
            ).toFloat()

            accessor.invokeActuallyHurt(level, source, 0.001f)
        }

        if (damageEntity) {
            accessor.invokeResolveMobResponsibleForDamage(source)
            accessor.invokeResolvePlayerResponsibleForDamage(source)
            (entity as LivingEntityAccessor).invokeActuallyHurt(level, source, leftoverDamage / damageMult)
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