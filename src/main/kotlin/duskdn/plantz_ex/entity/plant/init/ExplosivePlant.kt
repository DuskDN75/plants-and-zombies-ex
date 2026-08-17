package duskdn.plantz_ex.entity.plant.init

import duskdn.plantz_ex.entity.plant.interfaces.IExplosivePlant
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

abstract class ExplosivePlant(type: EntityType<out ExplosivePlant>, level: Level) : AttackingPlant(type, level), IExplosivePlant {
    override fun mobInteract(player: Player, hand: InteractionHand): InteractionResult {
        return if (getTriggered(player, hand)) InteractionResult.SUCCESS_SERVER else super.mobInteract(player, hand)
    }

    override val checkForAdjacent = isAsleep

    override var swellSpeed: Int = 1
}