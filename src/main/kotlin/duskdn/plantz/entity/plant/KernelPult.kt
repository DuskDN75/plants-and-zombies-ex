package duskdn.plantz.entity.plant

import duskdn.plantz.init.PazEntities
import duskdn.plantz.ai.goal.ProjectileAttackGoal
import duskdn.plantz.entity.plant.init.PultPlant
import duskdn.plantz.entity.projectile.Butter
import duskdn.plantz.entity.projectile.Kernel
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec2

class KernelPult(type: EntityType<out PultPlant>, level: Level) : PultPlant(PazEntities.KERNEL_PULT, level) {

    companion object {
        val HAS_BUTTER: EntityDataAccessor<Boolean> = SynchedEntityData.defineId<Boolean>(KernelPult::class.java, EntityDataSerializers.BOOLEAN)
    }

    var hasButterShot: Boolean
        get() = this.entityData.get(HAS_BUTTER)
        set(value) = this.entityData.set(HAS_BUTTER, value)

    override fun defineSynchedData(entityData: SynchedEntityData.Builder) {
        super.defineSynchedData(entityData)
        entityData.define(HAS_BUTTER, false)
    }

    override fun registerGoals() {
        super.registerGoals()

        this.goalSelector.addGoal(2, ProjectileAttackGoal(
            usingEntity = this,
            projectileFactory = {
                if (hasButterShot) Butter(level(), this, spawnOffset = Vec2(-1f, 1f))
                else Kernel(level(), this, spawnOffset = Vec2(-1f, 1f))
            },
            velocity = 1.0,
            useHighArc = true,
            cooldownTime = 45,
            actionDelay = 12,
            actionStartEffect = { hasButterShot = random.nextFloat() < 0.25 }))
    }
}