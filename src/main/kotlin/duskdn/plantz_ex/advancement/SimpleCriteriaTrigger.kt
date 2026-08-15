package duskdn.plantz_ex.advancement

import com.mojang.serialization.Codec
import net.minecraft.advancements.predicates.ContextAwarePredicate
import net.minecraft.server.level.ServerPlayer
import net.minecraft.advancements.triggers.SimpleCriterionTrigger
import java.util.Optional

class SimpleCriterionTrigger<T, C : SimpleCriterionCondition<T>>(
    val codec: Codec<C>,
) : SimpleCriterionTrigger<C>() {
    override fun codec() = codec

    fun trigger(player: ServerPlayer, context: T) {
        return this.trigger(player) {
            it.matches(player, context)
        }
    }
}

abstract class SimpleCriterionCondition<T>(
    val playerCtx: Optional<ContextAwarePredicate>
) : SimpleCriterionTrigger.SimpleInstance {
    override fun player() = playerCtx

    abstract fun matches(player: ServerPlayer, context: T): Boolean
}