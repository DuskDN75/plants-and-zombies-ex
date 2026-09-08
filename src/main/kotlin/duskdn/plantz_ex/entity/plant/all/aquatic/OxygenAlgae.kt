package duskdn.plantz_ex.entity.plant.all.aquatic

import duskdn.plantz_ex.ai.goal.EffectApplyGoal
import duskdn.plantz_ex.ai.goal.MeleeAttackActionGoal
import duskdn.plantz_ex.init.ElectricArcParticleOptions
import duskdn.plantz_ex.entity.plant.init.AttackingPlant
import duskdn.plantz_ex.entity.plant.init.PazPlant
import duskdn.plantz_ex.entity.plant.interfaces.IAquaticPlant
import duskdn.plantz_ex.entity.plant.utils.enemyCheck
import duskdn.plantz_ex.entity.plant.utils.gravelSurvivalCheck
import duskdn.plantz_ex.entity.plant.utils.sandSurvivalCheck
import duskdn.plantz_ex.entity.plant.utils.waterSurvivalCheck
import duskdn.plantz_ex.init.PazDamageTypes
import duskdn.plantz_ex.init.PazEffects
import duskdn.plantz_ex.init.PazEntities
import duskdn.plantz_ex.util.hasSameRootOwner
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.Mth
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.animal.fish.AbstractFish
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.monster.zombie.Zombie
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import kotlin.math.sqrt

class OxygenAlgae(level: Level) : PazPlant(PazEntities.OXYGEN_ALGAE, level), IAquaticPlant {

    companion object {
//        private val TANGLE_ATTACK_MODIFIER = AttributeModifier(
//            pazResource("tangle_attack"), 100.0, AttributeModifier.Operation.ADD_MULTIPLIED_BASE
//        )
        val AIR_GIVE_AMOUNT: Double = 4.0
    }

    override fun canSurviveOn(block: BlockState): Boolean {
        return waterSurvivalCheck(block) && (super.canSurviveOn(block) || sandSurvivalCheck(block) || gravelSurvivalCheck(block))
    }

    override fun doWaterSplashEffect() {

    }

    override fun registerGoals() {
        super.registerGoals()

        addEffect(MobEffectInstance(MobEffects.WATER_BREATHING, -1, 0, true, false))

        val effectApplyGoal = EffectApplyGoal(
            usingEntity = this,
            cooldownTime = 10,
            effectFactory = { target ->
                if (target is PazPlant || target == rootOwner) {
                    target.addEffect(MobEffectInstance(MobEffects.WATER_BREATHING, 10, 0))
                }
            }
        )

        goalSelector.addGoal(2, effectApplyGoal)
    }

}