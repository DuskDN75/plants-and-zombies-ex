package duskdn.plantz.entity.zombie

import duskdn.plantz.ai.goal.FloatingPathfindGoal
import duskdn.plantz.entity.Balloon
import duskdn.plantz.entity.interfaces.FloatingMob
import duskdn.plantz.init.PazBlocks
import duskdn.plantz.init.PazEntities
import duskdn.plantz.init.PazSounds
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.util.RandomSource
import net.minecraft.world.Difficulty
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.control.FlyingMoveControl
import net.minecraft.world.entity.ai.control.MoveControl
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation
import net.minecraft.world.entity.ai.navigation.PathNavigation
import net.minecraft.world.item.DyeColor
import net.minecraft.world.level.Level
import net.minecraft.world.level.ServerLevelAccessor
import net.minecraft.world.phys.Vec3
import java.util.EnumSet
import net.minecraft.world.entity.ai.attributes.Attributes.FLYING_SPEED
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import kotlin.math.absoluteValue
import kotlin.math.sign

class BalloonZombie(type: EntityType<out BalloonZombie> = PazEntities.BALLOON_ZOMBIE, level: Level) : PazZombie(type, level), FloatingMob {

    companion object {
        fun checkBalloonZombieSpawnRules(
            type: EntityType<out Mob>,
            level: ServerLevelAccessor,
            spawnReason: EntitySpawnReason,
            pos: BlockPos,
            random: RandomSource
        ): Boolean {
            return level.difficulty != Difficulty.PEACEFUL
                    && (EntitySpawnReason.ignoresLightRequirements(spawnReason))
//                    && checkMobSpawnRules(type, level, spawnReason, pos, random)
                    && pos.y > level.seaLevel + 8
        }
    }

    override fun getAmbientSound(): SoundEvent {
        return PazSounds.BROWNCOAT_AMBIENT
    }
    override fun getHurtSound(source: DamageSource): SoundEvent {
        return PazSounds.BROWNCOAT_HURT
    }
    override fun getDeathSound(): SoundEvent {
        return PazSounds.BROWNCOAT_DEATH
    }
    override fun getStepSound(): SoundEvent {
        return SoundEvents.ZOMBIE_STEP
    }

    override fun doHurtTarget(level: ServerLevel, target: Entity): Boolean {
        val result = super.doHurtTarget(level, target)
        return result
    }

    override fun isBaby(): Boolean = isBabyZombie()

    override var flyingNavigation: PathNavigation? = null
    var groundNavigation: PathNavigation? = null

    override fun createNavigation(level: Level): PathNavigation {
        flyingNavigation = FlyingPathNavigation(this, level)
        groundNavigation = GroundPathNavigation(this, level)

        if (isFloating) {
            this.moveControl = FlyingMoveControl(this, 20, false)
            return flyingNavigation as PathNavigation
        } else {
            this.moveControl = MoveControl(this)
            return groundNavigation as PathNavigation
        }
    }

    override var checkedSpawn = false

    override var spawnPos: Vec3? = null

    override var isFloating: Boolean = true

    var spawnedBalloons: Boolean = false

    var balloonCount: Int = 1

    var balloons : MutableList<Balloon> = mutableListOf()

    override fun tick() {
        super.tick()

        if (!level().isClientSide) {

            if (balloons.isEmpty() && !spawnedBalloons && !checkedSpawn && !firstTick) {

                checkedSpawn = true

                spawnPos = position()

                spawnBalloon(level() as ServerLevel, EntitySpawnReason.MOB_SUMMONED)
                return
            }

            if (isFloating) {

                balloons.removeIf { balloon ->
                    !balloon.isAlive || balloon.leashHolder != this
                }

                if (balloons.isEmpty()) stopFloating()

            }
        }
    }

    fun stopFloating() {

        if (!isFloating) return

        if (groundNavigation == null) return

        println("STOPPING FLOATING")

        isFloating = false

        navigation = groundNavigation as PathNavigation
        moveControl = MoveControl(this)

        this.getAttribute(FLYING_SPEED)?.baseValue = 0.0
    }

    override fun registerGoals() {
        super.registerGoals()

        this.goalSelector.addGoal(4, BalloonZombieChaseGoal(this))
    }

    fun spawnBalloon(level: ServerLevel, spawnReason: EntitySpawnReason) {

        for (i in 0 until balloonCount) {

            val balloon: Balloon? = PazEntities.BALLOON.create(level, spawnReason)

            if (balloon != null) {

                balloon.dyeColor = DyeColor.RED

                val randomX = (random.nextDouble() - 0.5) * 2
                val randomZ = (random.nextDouble() - 0.5) * 2

                balloon.snapTo(this.x+randomX, this.y + 2.0, this.z+randomZ)

                level.addFreshEntity(balloon)

                balloon.setLeashedTo(this, true)

                isFloating = true

                balloons.add(balloon)

            }

        }

        spawnedBalloons = true
    }

    override fun readAdditionalSaveData(input: ValueInput) {
        super.readAdditionalSaveData(input)

        this.isFloating = input.getBooleanOr("isFloating", true)
        this.balloonCount = input.getIntOr("balloonCount", 1)
        this.spawnedBalloons = input.getBooleanOr("spawnedBalloons", false)
        this.checkedSpawn = input.getBooleanOr("checkedSpawn", false)
    }

    override fun addAdditionalSaveData(output: ValueOutput) {
        super.addAdditionalSaveData(output)

        output.putBoolean("isFloating", isFloating)
        output.putInt("balloonCount", balloonCount)
        output.putBoolean("spawnedBalloons", spawnedBalloons)
        output.putBoolean("checkedSpawn", checkedSpawn)
    }

    override fun finalizeSpawn(
        level: ServerLevelAccessor,
        difficulty: DifficultyInstance,
        spawnReason: EntitySpawnReason,
        groupData: SpawnGroupData?
    ): SpawnGroupData? {
        val data = super.finalizeSpawn(level, difficulty, spawnReason, groupData)

        if (getItemBySlot(EquipmentSlot.HEAD).isEmpty && spawnReason != EntitySpawnReason.COMMAND){
            if (random.nextFloat() < 0.25) {
                balloonCount = 3
                setItemSlot(EquipmentSlot.HEAD, PazBlocks.CONE.asItem().defaultInstance)
                setDropChance(EquipmentSlot.HEAD, 0.2f)
            }
            else if (random.nextFloat() < 0.1 && getItemBySlot(EquipmentSlot.HEAD).isEmpty) {
                balloonCount = 7
                setItemSlot(EquipmentSlot.HEAD, Items.BUCKET.defaultInstance)
            }
        }

        if (level is ServerLevel) {

            spawnBalloon(level, spawnReason)

        }

        return data
    }

    override fun actuallyHurt(level: ServerLevel, source: DamageSource, damage: Float) {
        super.actuallyHurt(level, source, damage)
    }

    private class BalloonZombieChaseGoal(
        entity: BalloonZombie
    ): FloatingPathfindGoal<BalloonZombie>(entity) {

        init {
            this.flags = EnumSet.of(Flag.MOVE, Flag.LOOK)
        }

        override fun canUse(): Boolean {
            return entity.isFloating && entity.target != null && entity.target?.isAlive == true
        }

        override fun setEntityDelta(distance: Vec3, speed: Double) {

            val dis = distance.normalize()

            entity.deltaMovement = Vec3(
                dis.x * speed,
                dis.y * speed * (2*entity.balloonCount),
                dis.z * speed
            )

        }

        override fun tick() {

            if (!(entity.isFloating && entity.target != null && entity.target?.isAlive == true)) return



            val target = entity.target as LivingEntity

            val targetDirection: Vec3 = target.position().subtract(entity.position())



            var flyingSpeed = entity.getAttribute(Attributes.FLYING_SPEED)?.value ?: 0.0



            val groundHeight = entity.level().getHeight(Heightmap.Types.WORLD_SURFACE, entity.blockPosition())

            val groundDistance = entity.y - groundHeight.toDouble()

            val groundBlock = entity.level().getBlockState(entity.blockPosition().atY(groundHeight))



            val targetDistance = targetDirection.length()

            if (targetDistance <= 3.0) flyingSpeed *= targetDistance/3.0

            if (targetDistance <= 0.2) flyingSpeed = 0.0

            setEntityDelta(targetDirection, flyingSpeed)

            setEntityControls()
        }

    }
}