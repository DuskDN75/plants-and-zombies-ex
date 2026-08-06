package duskdn.plantz.ai.goal

import duskdn.plantz.entity.Balloon
import duskdn.plantz.entity.plant.all.aerial.SkyPeaShooter
import duskdn.plantz.entity.projectile.peas.Pea
import duskdn.plantz.entity.zombie.BalloonZombie
import duskdn.plantz.init.PazSounds
import duskdn.plantz.init.PazTags
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.PathfinderMob
import net.minecraft.world.entity.ai.attributes.Attributes
import java.util.function.Predicate

open class BalloonPriorityProjectileAttackGoal(
    usingEntity: PathfinderMob,
    cooldownTime: Int = 20,
    actionDelay: Int = 0,
    actionStartEffect: () -> Unit = {},
    actionSuccessEffect: () -> Unit = {},
    actionEndEffect: () -> Unit = {},
    actionPredicate: Predicate<PathfinderMob> = Predicate { true },
    projectileFactory: () -> Entity,
    inaccuracy: Float = 0.0f,
    attackRadius: Float = usingEntity.attributes.getValue(Attributes.FOLLOW_RANGE).toFloat(),
    velocity : Double = 1.2,
    useHighArc: Boolean = false,
    soundEvent: SoundEvent? = PazSounds.PROJECTILE_FIRE,
) : ProjectileAttackGoal(
    usingEntity = usingEntity,
    projectileFactory = projectileFactory,
    velocity = velocity,
    cooldownTime = cooldownTime,
    actionDelay = actionDelay,
    attackRadius = attackRadius,
    useHighArc = useHighArc,
    soundEvent = soundEvent,
    inaccuracy = inaccuracy,
    actionPredicate = actionPredicate,
    actionStartEffect = actionStartEffect,
    actionSuccessEffect = actionSuccessEffect,
    actionEndEffect = actionEndEffect,
) {
    fun tryTargetBalloon(target: BalloonZombie) {

        if (!target.isAlive || target.balloons.isEmpty()) return

        val balloon: Balloon = target.balloons.first()

        if (!balloon.isAlive) return

        usingEntity.target = balloon

    }

    override fun doAction(): Boolean {

        if (usingEntity.target is BalloonZombie) {

            tryTargetBalloon(usingEntity.target as BalloonZombie)

        }

        return super.doAction()
    }

}