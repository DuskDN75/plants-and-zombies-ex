package duskdn.plantz_ex.entity.plant.all

import duskdn.plantz_ex.ai.goal.EffectApplyGoal
import duskdn.plantz_ex.init.PazEntities
import duskdn.plantz_ex.entity.plant.init.PazPlant
import duskdn.plantz_ex.entity.plant.utils.enemyCheck
import duskdn.plantz_ex.init.PazEffects
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level

class Plantern(
    type: EntityType<out PazPlant>,
    level: Level,
) : PazPlant(PazEntities.PLANTERN, level) {
    override fun attackGoals() {}

    override fun getLightLevel(): Int {
        return if (isAsleep) 8 else 15
    }

    override fun registerGoals() {
        super.registerGoals()

        val effectApplyGoal = EffectApplyGoal(
            usingEntity = this,
            effectFactory = { target ->
                if (enemyCheck(target) || target is PazPlant) {
                    target.addEffect(MobEffectInstance(PazEffects.ENLIGHTENED, 25, 0))
                }
            }
        )

        goalSelector.addGoal(2, effectApplyGoal)
    }

}