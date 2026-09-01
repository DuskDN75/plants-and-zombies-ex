package duskdn.plantz_ex.item

import duskdn.plantz_ex.item.interfaces.IBlocksProjectileDamage
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Equipable
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.UseAnim
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block

class ScreenDoorItem(block: Block, properties: Properties) : BlockItem(block, properties), Equipable, IBlocksProjectileDamage {

    override fun use(level: Level, player: Player, hand: InteractionHand): InteractionResultHolder<ItemStack?> {
        player.startUsingItem(hand)
        player.playSound(SoundEvents.WOODEN_DOOR_CLOSE, 0.8f, 1.2f)
        return InteractionResultHolder(InteractionResult.CONSUME, player.getItemInHand(hand))
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

    override fun getUseDuration(itemStack: ItemStack, user: LivingEntity): Int {
        return 72000
    }

    override fun getUseAnimation(itemStack: ItemStack): UseAnim {
        return UseAnim.SPYGLASS
    }

    override fun getEquipmentSlot(): EquipmentSlot {
        return EquipmentSlot.OFFHAND
    }

    override fun getEquipSound(): Holder<SoundEvent?>? {
        return BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.SHIELD_BLOCK)
    }

    override fun getBreakingSound(): SoundEvent? {
        return SoundEvents.SHIELD_BREAK
    }
}