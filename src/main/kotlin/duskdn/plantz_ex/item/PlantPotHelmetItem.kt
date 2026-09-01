package duskdn.plantz_ex.item

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Equipable
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class PlantPotHelmetItem(properties: Properties) : Item(properties), Equipable {

    override fun inventoryTick(itemStack: ItemStack, level: Level, owner: Entity, i: Int, bl: Boolean) {
        super.inventoryTick(itemStack, level, owner, i, bl)

        if (owner !is LivingEntity) return

        val isWearing = EquipmentSlot.entries
            .filter { it.isArmor }
            .any { owner.getItemBySlot(it) == itemStack }

        if (!isWearing) return

    }

    override fun getEquipmentSlot(): EquipmentSlot {
        return EquipmentSlot.HEAD
    }

}