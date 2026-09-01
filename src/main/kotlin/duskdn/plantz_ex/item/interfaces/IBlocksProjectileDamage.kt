package duskdn.plantz_ex.item.interfaces

import duskdn.plantz_ex.entity.plant.init.PazPlant
import net.minecraft.ChatFormatting
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.Item.TooltipContext
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import kotlin.collections.get

interface IBlocksProjectileDamage {

    fun addBlocksProjectileText(
        item: Item,
        itemStack: ItemStack,
        tooltipContext: TooltipContext,
        list: MutableList<Component?>,
        tooltipFlag: TooltipFlag
    ) {
        val maxHealth = (itemStack.get(DataComponents.MAX_DAMAGE) ?: 0) / PazPlant.PEA_DAMAGE

        val damage = (itemStack.get(DataComponents.DAMAGE) ?: 0) / PazPlant.PEA_DAMAGE

        val health = maxHealth-damage

        list.add(
            Component.translatable("component.blocks_damage", health, maxHealth).withStyle(ChatFormatting.BLUE)
        )

    }

}