package duskdn.plantz_ex.item

import duskdn.plantz_ex.item.interfaces.IBlocksProjectileDamage
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Equipable
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block

class ConeItem(block: Block, properties: Properties) : BlockItem(block, properties), Equipable, IBlocksProjectileDamage {

    override fun getEquipmentSlot(): EquipmentSlot {
        return EquipmentSlot.HEAD
    }

    override fun appendHoverText(
        itemStack: ItemStack,
        tooltipContext: TooltipContext,
        list: MutableList<Component?>,
        tooltipFlag: TooltipFlag
    ) {
        super.appendHoverText(itemStack, tooltipContext, list, tooltipFlag)

        addBlocksProjectileText(this, itemStack, tooltipContext, list, tooltipFlag)
    }

}