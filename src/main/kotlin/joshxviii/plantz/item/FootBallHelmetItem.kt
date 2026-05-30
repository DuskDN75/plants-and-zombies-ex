package joshxviii.plantz.item

import joshxviii.plantz.entity.zombie.AllStar.Companion.CHARGE_BOOST_ID
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import kotlin.math.sqrt

class FootBallHelmetItem(properties: Properties) : Item(properties) {
    companion object {
        private const val DAMAGE_INTERVAL = 35
        private const val KNOCKBACK_STRENGTH = 0.8
    }

    fun removeModifiers(entity: LivingEntity?) {
        if (entity == null) return
        entity.getAttribute(Attributes.MOVEMENT_SPEED)?.removeModifier(CHARGE_BOOST_ID)
        entity.getAttribute(Attributes.STEP_HEIGHT)?.removeModifier(CHARGE_BOOST_ID)
    }

    override fun inventoryTick(itemStack: ItemStack, level: ServerLevel, owner: Entity, slot: EquipmentSlot?) {
        super.inventoryTick(itemStack, level, owner, slot)
        if (level.isClientSide || owner !is LivingEntity) return
        if (slot == null || !slot.isArmor) {
            removeModifiers(owner)
            return
        }
        if (!owner.isSprinting) {
            removeModifiers(owner)
            return
        }
        else if (owner.getAttribute(Attributes.MOVEMENT_SPEED)?.getModifier(CHARGE_BOOST_ID) == null) {
            owner.getAttribute(Attributes.MOVEMENT_SPEED)?.addTransientModifier(
                AttributeModifier(CHARGE_BOOST_ID, 0.025, AttributeModifier.Operation.ADD_VALUE)
            )
            owner.getAttribute(Attributes.STEP_HEIGHT)?.addTransientModifier(
                AttributeModifier(CHARGE_BOOST_ID, 0.5, AttributeModifier.Operation.ADD_VALUE)
            )
        }
        if (!level.isClientSide && owner.tickCount % DAMAGE_INTERVAL == 0 && owner.getRandom().nextFloat() > 0.5f) {
            itemStack.hurtAndBreak(1, owner, slot)
        }

        val look = owner.lookAngle
        val horizontalLook = Vec3(look.x, 0.0, look.z).normalize()
        if (horizontalLook.lengthSqr() < 0.001) return

        val hitBox: AABB = owner.boundingBox
            .inflate(0.45, 0.15, 0.45)
            .expandTowards(horizontalLook.scale(0.75))

        val targets = level.getEntitiesOfClass(LivingEntity::class.java, hitBox) { target ->
            target !== owner &&
                    target.isAlive &&
                    !target.isSpectator
        }
        if (targets.isEmpty()) return

        for (target in targets) {
            val awayX = target.x - owner.x
            val awayZ = target.z - owner.z
            val length = sqrt(awayX * awayX + awayZ * awayZ).coerceAtLeast(0.001)

            target.knockback(
                KNOCKBACK_STRENGTH,
                -awayX / length,
                -awayZ / length
            )
        }
    }
}