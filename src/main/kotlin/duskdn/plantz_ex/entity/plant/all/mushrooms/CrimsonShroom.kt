package duskdn.plantz_ex.entity.plant.all.mushrooms

import duskdn.plantz_ex.init.PazEntities
import duskdn.plantz_ex.ai.goal.ExplodeGoal
import duskdn.plantz_ex.entity.plant.init.ExplosivePlant
import duskdn.plantz_ex.entity.plant.init.PazPlant
import duskdn.plantz_ex.entity.plant.init.PultPlant
import duskdn.plantz_ex.entity.plant.interfaces.IExplosivePlant
import duskdn.plantz_ex.entity.plant.interfaces.IWarmingPlant
import duskdn.plantz_ex.entity.plant.utils.mushroomSurvivalCheck
import duskdn.plantz_ex.entity.plant.utils.stoneSurvivalCheck
import duskdn.plantz_ex.init.PazConfig
import duskdn.plantz_ex.init.PazEffects
import net.minecraft.core.Holder
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.AreaEffectCloud
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class CrimsonShroom(type: EntityType<out PazPlant>, level: Level) : PultPlant(PazEntities.ICE_SHROOM, level), IWarmingPlant {



}