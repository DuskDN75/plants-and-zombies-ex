package duskdn.plantz_ex.entity.zombie

import duskdn.plantz_ex.ai.ZombieState
import duskdn.plantz_ex.entity.Balloon
import duskdn.plantz_ex.entity.plant.init.PazPlant.Companion.PEA_DAMAGE
import duskdn.plantz_ex.init.*
import duskdn.plantz_ex.init.PazDataSerializers.DATA_ZOMBIE_STATE
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.BlockParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.FluidTags
import net.minecraft.util.RandomSource
import net.minecraft.world.Difficulty
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.*
import net.minecraft.world.entity.ai.attributes.AttributeSupplier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.control.FlyingMoveControl
import net.minecraft.world.entity.ai.control.MoveControl
import net.minecraft.world.entity.ai.goal.MoveThroughVillageGoal
import net.minecraft.world.entity.ai.goal.SpearUseGoal
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation
import net.minecraft.world.entity.ai.navigation.PathNavigation
import net.minecraft.world.entity.animal.golem.IronGolem
import net.minecraft.world.entity.animal.turtle.Turtle
import net.minecraft.world.entity.monster.zombie.Zombie
import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin
import net.minecraft.world.entity.npc.villager.AbstractVillager
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.ServerLevelAccessor
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput

abstract class PazZombie(type: EntityType<out PazZombie>, level: Level) : Zombie(type, level) {

    val emergeAnimation : AnimationState = AnimationState()

    val floatAnimation : AnimationState = AnimationState()

    companion object {

        /**
         * The default speed of a zombie
         */
        const val ZOMBIE_SPEED = 0.2

        fun checkPazZombieSpawnRules(
            type: EntityType<out Mob>,
            level: ServerLevelAccessor,
            spawnReason: EntitySpawnReason,
            pos: BlockPos,
            random: RandomSource
        ): Boolean {
            if (level.difficulty == Difficulty.PEACEFUL) return false

            val biome = level.getBiome(pos)
            val isRaining = level.level.isRaining
            val inWater = level.getFluidState(pos).`is`(FluidTags.WATER)

            // light / day requirements
            val canSpawn = (inWater && isRaining) || EntitySpawnReason.ignoresLightRequirements(spawnReason) || biome.`is`(PazTags.Biomes.DAY_SPAWNS) || isDarkEnoughToSpawn(level, pos, random)
            if (!canSpawn) return false

            // water spawning
            if (inWater) {
                val rainBonus = if (isRaining) 2.75f else 1.25f
                val spawnChance = if (biome.`is`(PazTags.Biomes.WATER_SPAWNS)) 0.085f else 0.015f
                return EntitySpawnReason.isSpawner(spawnReason) ||
                        (random.nextFloat() < (spawnChance * rainBonus) && pos.y > level.seaLevel - 3)
            }

            // land spawning
            return checkMobSpawnRules(type, level, spawnReason, pos, random)
        }

        data class ZombieAttributes(
            val maxHealth: Double = PEA_DAMAGE*6,
            val attackDamage: Double = PEA_DAMAGE,
            val attackKnockback: Double = 0.4,
            val attackRange: Double = 2.5,
            val movementSpeed: Double = ZOMBIE_SPEED,
            val followRange: Double = 50.0,
            val jumpStrength: Double = 0.42,
            val armor: Double = 0.0,
            val spawnReinforcementsChance: Double = 0.0,
            val explosionKnockbackResistance: Double = 0.0,
            val knockbackResistance: Double = 0.2,
            val stepHeight: Double = 0.6,
            val interactionRange: Double = 1.7,
            val scale: Double = 1.0,
            val flyingSpeed: Double = 0.0,
            val waterMovementEfficiency: Double = 0.0
        ) {
            fun apply(builder: AttributeSupplier.Builder): AttributeSupplier.Builder {
                return builder
                    .add(Attributes.MAX_HEALTH, maxHealth)
                    .add(Attributes.FOLLOW_RANGE, followRange)
                    .add(Attributes.ATTACK_DAMAGE, attackDamage)
                    .add(Attributes.ATTACK_KNOCKBACK, attackKnockback)
                    .add(Attributes.ENTITY_INTERACTION_RANGE, attackRange)
                    .add(Attributes.MOVEMENT_SPEED, movementSpeed)
                    .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, spawnReinforcementsChance)
                    .add(Attributes.ARMOR, armor)
                    .add(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE, explosionKnockbackResistance)
                    .add(Attributes.KNOCKBACK_RESISTANCE, knockbackResistance)
                    .add(Attributes.STEP_HEIGHT, stepHeight)
                    .add(Attributes.ENTITY_INTERACTION_RANGE, interactionRange)
                    .add(Attributes.SCALE, scale)
                    .add(Attributes.FLYING_SPEED, flyingSpeed)
                    .add(Attributes.JUMP_STRENGTH, jumpStrength)
                    .add(Attributes.WATER_MOVEMENT_EFFICIENCY, waterMovementEfficiency)
            }
        }

        val ZOMBIE_STATE: EntityDataAccessor<ZombieState> = SynchedEntityData.defineId<ZombieState>(PazZombie::class.java, DATA_ZOMBIE_STATE)
    }

    override fun onEquipItem(slot: EquipmentSlot, oldStack: ItemStack, stack: ItemStack) {
        if (stack.`is`(PazItems.DUCKY_TUBE) && slot == EquipmentSlot.LEGS) this.getNavigation().setCanFloat(true);
        else if (oldStack.`is`(PazItems.DUCKY_TUBE) && slot == EquipmentSlot.LEGS) this.getNavigation().setCanFloat(false);
        super.onEquipItem(slot, oldStack, stack)
    }

    override fun equipmentHasChanged(previous: ItemStack, current: ItemStack): Boolean {
        if(mainHandItem.`is`(PazBlocks.SCREEN_DOOR.asItem())) {
            this.startUsingItem(usedItemHand)
            this.setLivingEntityFlag(LIVING_ENTITY_FLAG_IS_USING, true)
        }
        else if(mainHandItem.`is`(PazItems.NEWSPAPER)) {
            this.startUsingItem(usedItemHand)
            this.setLivingEntityFlag(LIVING_ENTITY_FLAG_IS_USING, true)
        }
        else {
            this.setLivingEntityFlag(LIVING_ENTITY_FLAG_IS_USING, false)
        }

        return super.equipmentHasChanged(previous, current)
    }

    var state: ZombieState
        get() = this.entityData.get(ZOMBIE_STATE)
        set(value) { this.entityData.set(ZOMBIE_STATE, value) }

    private val noMoveControl = object : MoveControl<Mob>(this) {
        override fun getSpeedModifier(): Double = 0.0
    }

    val flyControl = object : FlyingMoveControl<Mob>(this, 20, true) {
        override fun getSpeedModifier(): Double = 1.0
    }

    var flyingNavigation: PathNavigation? = null

    override fun createNavigation(level: Level): PathNavigation {

        flyingNavigation = FlyingPathNavigation(this, level)

        if ((state == ZombieState.FLOATING)) {
            return flyingNavigation as PathNavigation
        } else {
            return super.createNavigation(level)
        }
    }

    override fun getMoveControl(): MoveControl<Mob> {
        if (state == ZombieState.EMERGING) return noMoveControl
        if (state == ZombieState.FLOATING) return flyControl
        return super.getMoveControl()
    }

    var balloons : MutableList<Balloon> = mutableListOf()
    open var balloonCount = 0

    override fun registerGoals() {
        super.registerGoals()
    }

    fun addBehaviourGoalsNoMelee() {
        this.goalSelector.addGoal(2, SpearUseGoal<Zombie>(this, 1.0, 1.0, 10.0f, 2.0f))
        this.goalSelector.addGoal(6, MoveThroughVillageGoal(this, 1.0, true, 4) { this.canBreakDoors() })
        this.goalSelector.addGoal(7, WaterAvoidingRandomStrollGoal(this, 1.0))
        this.targetSelector.addGoal(1, HurtByTargetGoal(this).setAlertOthers(ZombifiedPiglin::class.java))
        this.targetSelector.addGoal(2, NearestAttackableTargetGoal(this, Player::class.java, true))
        this.targetSelector.addGoal(3, NearestAttackableTargetGoal(this, AbstractVillager::class.java, false))
        this.targetSelector.addGoal(3, NearestAttackableTargetGoal(this, IronGolem::class.java, true))
        this.targetSelector.addGoal(5, NearestAttackableTargetGoal(this, Turtle::class.java, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR))
    }

    fun updateMovement() {
        navigation = createNavigation(level())
        moveControl = getMoveControl()
    }

    var waterTime = -1
    override fun tick() {
        super.tick()
        val serverLevel = level() as? ServerLevel

//        if (isEyeInFluid(FluidTags.WATER)) {
//            waterTime++
//            if (waterTime>=250 && canEquipDuckyInWater()) {
//                if(!getItemBySlot(EquipmentSlot.LEGS).isEmpty) {
//                    val currentLegs = getItemBySlot(EquipmentSlot.LEGS)
//                    val dropChance = dropChances.byEquipment(EquipmentSlot.LEGS)
//                    if (!currentLegs.isEmpty && max(this.random.nextFloat() - 0.1f, 0.0f).toDouble() < dropChance) {
//                        if (serverLevel!=null) spawnAtLocation(serverLevel, currentLegs)
//                    }
//                }
//                setItemSlot(EquipmentSlot.LEGS, PazItems.DUCKY_TUBE.defaultInstance)
//                setDropChance(EquipmentSlot.LEGS, 0.0f)
//            }
//        } else waterTime = -1

        when (state) {
            ZombieState.IDLE -> {
                emergeAnimation.stop()
                floatAnimation.stop()
                if (serverLevel!=null && balloons.isNotEmpty()) {
                    state = ZombieState.FLOATING
                    updateMovement()
                }
            }
            ZombieState.FLOATING -> {
                floatAnimation.startIfStopped(tickCount)
                if (serverLevel!=null) {
                    balloons.removeIf { !it.isAlive || it.leashHolder != this }
                    if (balloons.isEmpty()) {
//                        debugPrint("CHANGED TO IDLE")
                        state = ZombieState.IDLE
                        navigation.stop()
                        isNoGravity = false
                        updateMovement()
                    }
                }
            }
            ZombieState.EMERGING -> {
                emergeAnimation.startIfStopped(tickCount)
                if (tickCount < 15) {
                    if(tickCount==1) playSound(SoundEvents.ROOTED_DIRT_HIT, 1.0f, 0.2f)
                    serverLevel?.sendParticles(
                        BlockParticleOption(ParticleTypes.BLOCK, serverLevel.getBlockState(blockPosition().below())),
                        x, y + 0.05, z, 8, 0.25, 0.0, 0.25, 0.4
                    )
                }
                if (tickCount > emergingTime()) state = ZombieState.IDLE
            }
        }
    }

    open fun spawnBalloons(count: Int = balloonCount) {
        val level = level() as? ServerLevel ?: return
        for (i in 0 until count) {
            val balloon = PazEntities.BALLOON.create(level, EntitySpawnReason.TRIGGERED) ?: return
            val randomX = (random.nextDouble() - 0.5) * 2 + x
            val randomZ = (random.nextDouble() - 0.5) * 2 + z
            balloon.snapTo(randomX, eyeY + 1.0, randomZ)
            balloon.dyeColor = DyeColor.RED
            level.addFreshEntity(balloon)
            balloon.setLeashedTo(this, true)
            balloons.add(balloon)
        }
    }

    override fun isImmobile(): Boolean {
        return if (state==ZombieState.EMERGING) true else super.isImmobile()
    }

    override fun defineSynchedData(entityData: SynchedEntityData.Builder) {
        super.defineSynchedData(entityData)
        entityData.define(ZOMBIE_STATE, ZombieState.IDLE)
    }

    override fun readAdditionalSaveData(input: ValueInput) {
        super.readAdditionalSaveData(input)

        this.waveStarted = input.getBooleanOr("waveStarted", true)
    }

    override fun addAdditionalSaveData(output: ValueOutput) {
        super.addAdditionalSaveData(output)

        output.putBoolean("waveStarted", waveStarted)
    }

    var waveStarted: Boolean = false

    override fun setTarget(target: LivingEntity?) {
        super.setTarget(target)
    }

    override fun hurtServer(level: ServerLevel, source: DamageSource, damage: Float): Boolean {
        return if (source.`is`(PazDamageTypes.ZOMBIE_SMASH)) false else super.hurtServer(level, source, damage)
    }
    override fun hurtClient(source: DamageSource): Boolean {
        return if (source.`is`(PazDamageTypes.ZOMBIE_SMASH)) false else super.hurtClient(source)
    }
    override fun actuallyHurt(level: ServerLevel, source: DamageSource, damage: Float) {
        if (source.`is`(PazDamageTypes.ZOMBIE_SMASH)) return
        super.actuallyHurt(level, source, damage)
    }

    override fun wantsToPickUp(level: ServerLevel, itemStack: ItemStack): Boolean {
        if (itemStack.`is`(PazBlocks.PLANTZ_FLAG.asItem())) return false
        return super.wantsToPickUp(level, itemStack)
    }

    open fun emergingTime(): Int = 40
    open fun canEquipDuckyInWater() = true

    override fun maxUpStep(): Float = if (isInWater) 0.5f else super.maxUpStep()
    override fun isSunSensitive(): Boolean = false
    override fun convertsInWater(): Boolean = false
    override fun canSpawnInLiquids(): Boolean = canEquipDuckyInWater()
    override fun checkSpawnObstruction(level: LevelReader): Boolean {
        return if (canSpawnInLiquids()) level.isUnobstructed(this)
        else super.checkSpawnObstruction(level)
    }
    override fun isBaby(): Boolean = false
    override fun populateDefaultEquipmentSlots(random: RandomSource, difficulty: DifficultyInstance) {}
    fun isBabyZombie() = super.isBaby()
    fun randomEquip(random: RandomSource, difficulty: DifficultyInstance) {
        super.populateDefaultEquipmentSlots(random, difficulty)
    }

    override fun finalizeSpawn(
        level: ServerLevelAccessor,
        difficulty: DifficultyInstance,
        spawnReason: EntitySpawnReason,
        groupData: SpawnGroupData?
    ): SpawnGroupData? {
        val data = super.finalizeSpawn(level, difficulty, spawnReason, groupData)
        if (spawnReason == EntitySpawnReason.REINFORCEMENT) state = ZombieState.EMERGING

        val fluidType = level.getBlockState(blockPosition()).fluidState.type

        val inFluid = fluidType == Fluids.WATER || fluidType == Fluids.LAVA

        if (canEquipDuckyInWater() && inFluid) {
            setItemSlot(EquipmentSlot.LEGS, PazItems.DUCKY_TUBE.defaultInstance)
            if (spawnReason != EntitySpawnReason.NATURAL) setDropChance(EquipmentSlot.LEGS, 0.0f)
            else setDropChance(EquipmentSlot.LEGS, 0.15f)
        }

        return data
    }
}