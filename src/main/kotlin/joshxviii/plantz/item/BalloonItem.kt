package joshxviii.plantz.item

import net.minecraft.world.InteractionResult
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext

class BalloonItem(
    properties: Properties,
    val color: DyeColor = DyeColor.WHITE
) : Item(properties) {
    override fun useOn(context: UseOnContext): InteractionResult {
        return super.useOn(context)
    }
}