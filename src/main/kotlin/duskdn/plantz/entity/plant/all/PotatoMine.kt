package duskdn.plantz.entity.plant.all

import duskdn.plantz.ai.goal.ExplodeGoal
import duskdn.plantz.entity.plant.init.ExplosivePlant
import duskdn.plantz.entity.plant.init.PazPlant
import duskdn.plantz.entity.plant.utils.gravelSurvivalCheck
import duskdn.plantz.entity.plant.utils.sandSurvivalCheck
import duskdn.plantz.init.NukeBlastParticleOptions
import duskdn.plantz.init.NukeSmokeParticleOptions
import duskdn.plantz.init.NukeWaveParticleOptions
import duskdn.plantz.init.PazEntities
import duskdn.plantz.init.PazSounds
import duskdn.plantz.util.hasSameRootOwner
import net.minecraft.core.Holder
import net.minecraft.core.particles.ItemParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.SpawnGroupData
import net.minecraft.world.entity.ai.goal.Goal
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Items
import net.minecraft.world.level.Level
import net.minecraft.world.level.ServerLevelAccessor
import net.minecraft.world.level.block.state.BlockState

class PotatoMine(type: EntityType<out ExplosivePlant>, level: Level) : ExplosivePlant(PazEntities.POTATO_MINE, level) {

    var explodeGoal: ExplodeGoal<PotatoMine>? = null

    init {
        active = false
    }

    override fun registerGoals() {
        super.registerGoals()

        explodeGoal = ExplodeGoal(
            usingEntity = this,
            attackRadius = 1f,
            destroyBlocks = true,
            soundEvent = PazSounds.POTATOMINE_EXPLODE,
            actionEndEffect = {
                addParticlesAroundSelf(
                    particle = ItemParticleOption(
                        ParticleTypes.ITEM,
                        Items.POTATO
                ),
                    amount = 22..24,
                    speed = 0.2,
                )
                addParticlesAroundSelf(
                    particle = ParticleTypes.LARGE_SMOKE,
                    amount = 3..3,
                    speed = 0.1,
                )
            }
        )

        this.goalSelector.addGoal(1, explodeGoal as Goal)
    }

    override fun finalizeSpawn(
        level: ServerLevelAccessor,
        difficulty: DifficultyInstance,
        spawnReason: EntitySpawnReason,
        groupData: SpawnGroupData?
    ): SpawnGroupData? {
        cooldown = 190 + random.nextInt(-20, 20)
        return super.finalizeSpawn(level, difficulty, spawnReason, groupData)
    }

    override fun tick() {
        super.tick()
        if (cooldown>0) coolDownAnimationState.startIfStopped(tickCount)
    }

    override fun getMaxActiveTime() = 4

    override fun doPush(entity: Entity) {
        if (isGrowingSeeds || cooldown > 0) return
        if (entity is PazPlant || (entity is Player && isTame) || this.hasSameRootOwner(entity)) return
        active = true
    }

    override fun canSurviveOn(block: BlockState): Boolean {
        return super.canSurviveOn(block) || sandSurvivalCheck(block) || gravelSurvivalCheck(block)
    }
}