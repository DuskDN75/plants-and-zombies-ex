package duskdn.plantz_ex.ai.goal

import duskdn.plantz_ex.init.PazSounds
import duskdn.plantz_ex.entity.plant.init.PazPlant
import duskdn.plantz_ex.init.PazDamageTypes
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceKey
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.attributes.Attributes
import java.util.function.Predicate

class EffectApplyGoal(
    usingEntity: PathfinderMob,
    cooldownTime: Int = 20,
    actionDelay: Int = 0,
    actionStartEffect: (ActionData?) -> Unit = {},
    actionSuccessEffect: (ActionData?) -> Unit = {},
    actionEndEffect: (ActionData?) -> Unit = {},
    actionPredicate: Predicate<PathfinderMob> = Predicate { true },
    var effectFactory: (LivingEntity) -> Unit = {},
    var attackRadius: Float = usingEntity.attributes.getValue(Attributes.FOLLOW_RANGE).toFloat(),
    val soundEvent: Holder.Reference<SoundEvent> = PazSounds.PLANT_EXPLODE,
    val damageType: ResourceKey<DamageType> = PazDamageTypes.PLANT_AOE,
) : ActionGoal(usingEntity, cooldownTime, actionDelay, actionStartEffect, actionSuccessEffect, actionEndEffect, actionPredicate) {

    var updateRate: Int = 10

    var radiusTargets: MutableList<LivingEntity> = mutableListOf()

    override fun canDoAction(): Boolean {
        return radiusTargets.isNotEmpty()
    }

    override fun doAction(): Boolean {

        for (target in radiusTargets) {
            effectFactory(target)
        }

        return true
    }

    override fun canUse(): Boolean = (
    usingEntity.tickCount>cooldownTime
            && !(usingEntity is PazPlant && ((usingEntity as PazPlant).isAsleep || (usingEntity as PazPlant).isGrowingSeeds))
    )

    fun getTargets(): MutableList<LivingEntity> {
        return usingEntity.level().getEntitiesOfClass(
            LivingEntity::class.java,
            usingEntity.boundingBox.inflate(attackRadius.toDouble())
        ).filter {
            (it != usingEntity) && it.isAlive
        }.toMutableList()
    }

    override fun tick() {
        super.tick()

        if (usingEntity.tickCount % updateRate == 0) {
            radiusTargets = getTargets()
        }
    }

}