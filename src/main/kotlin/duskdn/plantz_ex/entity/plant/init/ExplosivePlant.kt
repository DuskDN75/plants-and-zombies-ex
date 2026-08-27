package duskdn.plantz_ex.entity.plant.init

import duskdn.plantz_ex.entity.plant.interfaces.IExplosivePlant
import duskdn.plantz_ex.entity.plant.interfaces.IWarmingPlant
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level

abstract class ExplosivePlant(type: EntityType<out ExplosivePlant>, level: Level) : AttackingPlant(type, level), IExplosivePlant, IWarmingPlant {
    override fun mobInteract(player: Player, hand: InteractionHand): InteractionResult {
        return if (getTriggered(player, hand)) InteractionResult.SUCCESS_SERVER else super.mobInteract(player, hand)
    }

    override fun getMeltChance(): Double = 1.0

    override fun getMeltRate(): Int = 1

    override fun getMeltRadius(): Double {
        return attributes.getValue(Attributes.FOLLOW_RANGE)
    }

    override val checkForAdjacent = isAsleep

    override var swellSpeed: Int = 1
}