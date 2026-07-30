package joshxviii.plantz.raid

import com.google.common.collect.Maps
import com.google.common.collect.Sets
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import joshxviii.plantz.*
import joshxviii.plantz.advancement.ZombieRaidContext
import joshxviii.plantz.block.entity.FlagBlockEntity
import joshxviii.plantz.entity.zombie.BrownCoat
import joshxviii.plantz.entity.zombie.BrownCoatVariant
import net.minecraft.SharedConstants
import net.minecraft.ChatFormatting
import net.minecraft.core.BlockPos
import net.minecraft.core.BlockPos.MutableBlockPos
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket
import net.minecraft.network.protocol.game.ClientboundSoundPacket
import net.minecraft.server.level.ServerBossEvent
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import net.minecraft.util.StringRepresentable
import net.minecraft.world.BossEvent
import net.minecraft.world.Difficulty
import net.minecraft.world.entity.EntitySpawnReason
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.SpawnGroupData
import net.minecraft.world.entity.monster.zombie.Zombie
import net.minecraft.world.item.Items
import net.minecraft.world.level.levelgen.Heightmap
import java.util.function.Predicate
import java.util.Optional
import java.util.UUID

class ZombieRaid(
    val center: BlockPos,
    var started: Boolean = false,
    private var active: Boolean = false,
    var ticksActive: Long = 0,
    var zombieRaidOmenLevel: Int = 0,
    var wavesSpawned: Int = 0,
    var raidCooldownTicks: Int = PRE_RAID_TICKS,
    var postRaidTicks: Int = 0,
    var totalHealth: Float = 0f,
    var numWaves: Int = 0,
    var waveTimer: Int = 0,
    var status: ZombieRaidStatus = ZombieRaidStatus.ONGOING,
    var startedBy: UUID? = null,
    var starterHasSeenCredits: Boolean = false,
    val difficulty: Difficulty = Difficulty.NORMAL,
) {
    companion object {
        fun getWaveSpawnCount(difficulty: Difficulty, omenLevel: Int, creditsUnlocked: Boolean): Int {
            val baseWaves = 3 + difficulty.id * 2
            val omenBonus = omenLevel.coerceAtLeast(0)
            val creditsBonus = if (creditsUnlocked) 1 else 0
            return baseWaves + omenBonus + creditsBonus
        }
        fun getStartMessage(creditsUnlocked: Boolean): Component {
            return if (creditsUnlocked) ZOMBIE_RAID_BAR_START_CREDITS else ZOMBIE_RAID_BAR_START
        }
        val MAP_CODEC : MapCodec<ZombieRaid> = RecordCodecBuilder.mapCodec { r ->
            r.group(
                BlockPos.CODEC.fieldOf("center").forGetter<ZombieRaid> { it.center },
                Codec.BOOL.fieldOf("started").forGetter<ZombieRaid> { it.started },
                Codec.BOOL.fieldOf("active").forGetter<ZombieRaid> { it.active },
                Codec.LONG.fieldOf("ticks_active").forGetter<ZombieRaid> { it.ticksActive },
                Codec.INT.fieldOf("raid_omen_level").forGetter<ZombieRaid> { it.zombieRaidOmenLevel },
                Codec.INT.fieldOf("waves_spawned").forGetter<ZombieRaid> { it.wavesSpawned },
                Codec.INT.fieldOf("cooldown_ticks").forGetter<ZombieRaid> { it.raidCooldownTicks },
                Codec.INT.fieldOf("post_raid_ticks").forGetter<ZombieRaid> { it.postRaidTicks },
                Codec.FLOAT.fieldOf("total_health").forGetter<ZombieRaid> { it.totalHealth },
                Codec.INT.fieldOf("wave_count").forGetter<ZombieRaid> { it.numWaves },
                Codec.INT.fieldOf("wave_timer").forGetter<ZombieRaid> { it.waveTimer },
                ZombieRaidStatus.CODEC.fieldOf("status").forGetter<ZombieRaid> { it.status },
                Codec.STRING.xmap(UUID::fromString, UUID::toString)
                    .optionalFieldOf("started_by")
                    .xmap({ it.orElse(null) }, { Optional.ofNullable(it) })
                    .forGetter<ZombieRaid> { it.startedBy },
                Codec.BOOL.optionalFieldOf("starter_has_seen_credits", false).forGetter<ZombieRaid> { it.starterHasSeenCredits },
            ).apply<ZombieRaid>(r, ::ZombieRaid )
        }
        val ZOMBIE_RAID_BAR: Component = Component.translatable("event.plantz.zombie_raid")
        val ZOMBIE_RAID_BAR_START: Component = Component.translatable("event.plantz.zombie_raid.start")
        val ZOMBIE_RAID_BAR_START_CREDITS: Component = Component.translatable("event.plantz.zombie_raid.start.credits")
        val ZOMBIE_RAID_BAR_VICTORY: Component = Component.translatable("event.plantz.zombie_raid.victory")
        val ZOMBIE_RAID_BAR_DEFEAT: Component = Component.translatable("event.plantz.zombie_raid.defeat")
        const val WAVE_DURATION_TICKS: Int = 3000 // 2.5 minutes
        const val PRE_RAID_TICKS: Int = 300
        const val POST_RAID_TICKS: Int = 80
    }

    private val waveToLeaderMap: MutableMap<Int, Zombie> = Maps.newHashMap<Int, Zombie>()
    private val random: RandomSource = RandomSource.create()
    val zombieRaidEvent = ServerBossEvent(Mth.createInsecureUUID(random), ZOMBIE_RAID_BAR_START, BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.NOTCHED_10)
    private val waveZombieMap: MutableMap<Int, MutableSet<Zombie>> = Maps.newHashMap<Int, MutableSet<Zombie>>()
    var waveSpawnPos : BlockPos? = null

    data class WaveSpawnEntry(
        val type: ZombieRaiderType,
        val count: Int,
        val configure: (Zombie) -> Unit = {},
    )

    enum class ZombieRaidStatus(private val state: String) : StringRepresentable {
        ONGOING("ongoing"),
        NEXT_WAVE("next_wave"),
        VICTORY("victory"),
        LOSS("loss"),
        STOPPED("stopped");

        override fun getSerializedName(): String = state

        companion object {
            val CODEC: Codec<ZombieRaidStatus> = StringRepresentable.fromEnum<ZombieRaidStatus> { entries.toTypedArray() }
        }
    }

    init {
        active = true
        zombieRaidEvent.progress = 0.0f
        numWaves = getWaveSpawnCount(difficulty, zombieRaidOmenLevel, starterHasSeenCredits)
    }

    fun tick(level: ServerLevel) {
        if (!active) return
        ticksActive++
        refreshRaidProfile(level)

        if (SharedConstants.DEBUG_RAIDS) {// DEBUG INFO
            val event: ServerBossEvent = zombieRaidEvent
            val title = Component
                .literal("Wave: $wavesSpawned/$numWaves")
                .append(", Zombies Alive: ${getTotalZombiesAlive()}")
                .append(", Health: ${getHealthOfZombies()}/$totalHealth")
                .append(", Status: ${status.getSerializedName().uppercase()}")
                .append(", Timer: ${waveTimer.tickTimeFormat()}")
            event.setName(title)

            level.playSound(null, center, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.05f, 1.5f)

            level.sendParticles(
                ParticleTypes.HAPPY_VILLAGER,
                center.x + 0.5,
                center.y + 0.5,
                center.z + 0.5,
                1, 0.1, 0.1, 0.1, 0.1
            )
        }
        
        if (postRaidTicks > 0) { postRaidTicks-- // post-loading time
            if (postRaidTicks <= 0) stop()
            return
        }

        if (
            (getTotalZombiesAlive() == 0 && raidCooldownTicks > 0) ||
            (waveTimer == 0 && wavesSpawned < numWaves)
        ) {
            raidCooldownTicks-- // pre-loading time
            waveTimer = 0
            if (raidCooldownTicks <= 0) {
                status = ZombieRaidStatus.ONGOING
                raidCooldownTicks = PRE_RAID_TICKS
                zombieRaidEvent.name = ZOMBIE_RAID_BAR
            }
            else {
                status = ZombieRaidStatus.NEXT_WAVE
                zombieRaidEvent.progress = (zombieRaidEvent.progress + 1f/PRE_RAID_TICKS).coerceAtMost(1.0f)
                zombieRaidEvent.setName(Component.translatable("event.plantz.zombie_raid.wave", wavesSpawned, 0.tickTimeFormat()))
                return
            }
        }

        if (waveTimer>0) waveTimer--

        // victory condition (all zombies dead and all waves completed)
        if (getTotalZombiesAlive() == 0 && wavesSpawned >= numWaves) {
            status = ZombieRaidStatus.VICTORY
            zombieRaidEvent.progress = 1.0f
            zombieRaidEvent.color = BossEvent.BossBarColor.YELLOW
            zombieRaidEvent.name = ZOMBIE_RAID_BAR_VICTORY
            postRaidTicks = POST_RAID_TICKS
            zombieRaidEvent.players.forEach { player ->// advancement
                PazCriteria.WIN_ZOMBIE_RAID.trigger(player, ZombieRaidContext(center))
            }
            return
        }
        // lose condition (flag destroyed)
        else if (!level.getBlockState(center).`is`(PazBlocks.PLANTZ_FLAG)) {
            status = ZombieRaidStatus.LOSS
            zombieRaidEvent.progress = 1.0f
            zombieRaidEvent.color = BossEvent.BossBarColor.RED
            zombieRaidEvent.name = ZOMBIE_RAID_BAR_DEFEAT
            postRaidTicks = POST_RAID_TICKS
            return
        }
        else {
            zombieRaidEvent.setName(Component.translatable("event.plantz.zombie_raid.wave", wavesSpawned, waveTimer.tickTimeFormat()))
        }

        if (shouldSpawnNextWave()) {
            val spawnPos = findRandomSpawnPos(level, 20) ?: center
            spawnNextWave(level, spawnPos)
            //raidCooldownTicks = 200 + random.nextInt(200)  // Delay next wave
        }
        else if (waveTimer <= 0) {// destroy flag when timer runs out
            (level.getBlockEntity(center) as? FlagBlockEntity)?.hurt(999f)
        }

        updateBossbar()

        if (ticksActive % 20L == 0L) {
            updatePlayers(level)
            updateZombieRaiders(level)
        }

    }

    private fun validPlayer(): Predicate<ServerPlayer> {
        return Predicate { player: ServerPlayer ->
            val pos = player.blockPosition()
            player.isAlive && player.level().getZombieRaids().getNearbyRaid(pos, 9216) === this
        }
    }

    private fun updatePlayers(level: ServerLevel) {
        val currentPlayersInRaid: MutableSet<ServerPlayer> = Sets.newHashSet<ServerPlayer>(zombieRaidEvent.players)
        val newPlayersInRaid = level.getPlayers(validPlayer())
        for (player in newPlayersInRaid) if (!currentPlayersInRaid.contains(player)) zombieRaidEvent.addPlayer(player)
        for (player in currentPlayersInRaid) if (!newPlayersInRaid.contains(player)) zombieRaidEvent.removePlayer(player)
    }

    private fun updateZombieRaiders(level: ServerLevel) {
        val zombies: MutableIterator<MutableSet<Zombie>> = waveZombieMap.values.iterator()
        val toRemove: HashSet<Zombie> = Sets.newHashSet<Zombie>()
        while (zombies.hasNext()) {
            val zombieSet = zombies.next()
            for (zombie in zombieSet) {
                val zombiePos = zombie.blockPosition()
                if (zombie.isRemoved || zombie.level().dimension() !== level.dimension() || this.center.distSqr(
                        zombiePos
                    ) >= 12544.0
                ) {
                    toRemove.add(zombie)
                    continue
                }
                if (zombie.tickCount <= 600) continue
                if (level.getEntity(zombie.getUUID()) == null) toRemove.add(zombie)
            }
        }
        for (zombie in toRemove) removeFromRaid(level, zombie, true)
    }

    fun absorbRaidOmen(player: ServerPlayer): Boolean {
        val effect = player.getEffect(PazEffects.ZOMBIE_OMEN)?: return false
        zombieRaidOmenLevel += effect.amplifier + 1
        starterHasSeenCredits = starterHasSeenCredits || player.seenCredits
        return true
    }


    private fun spawnNextWave(level: ServerLevel, pos: BlockPos) {
        waveTimer = WAVE_DURATION_TICKS
        var leaderSet = false
        totalHealth = 0.0f
        val creditsUnlocked = hasRaidStarterSeenCredits(level)
        val specialWave = pickSpecialWave(creditsUnlocked)
        if (specialWave != null) announceSpecialWave(specialWave)
        val waveEntries = specialWave?.spawnEntries(this, creditsUnlocked) ?: buildRegularWaveEntries(creditsUnlocked)

        for (spawnEntry in waveEntries) {
            for (i in 0..<spawnEntry.count) {
                val zombie = spawnEntry.type.entityType.create(level, EntitySpawnReason.EVENT) ?: break
                if (!leaderSet) {
                    setLeader(wavesSpawned + 1, zombie)
                    leaderSet = true
                }
                val randX = pos.x + random.nextInt(11) - 5
                val randZ = pos.z + random.nextInt(11) - 5
                val randY = level.getHeight(Heightmap.Types.OCEAN_FLOOR, randX, randZ)
                val randomPos = MutableBlockPos(randX,randY,randZ)
                joinRaid(level, zombie, wavesSpawned + 1, false, randomPos, spawnEntry.configure)
            }
        }

        wavesSpawned++
    }

    fun joinRaid(
        level: ServerLevel,
        zombie: Zombie,
        waveNumber: Int = wavesSpawned,
        exists: Boolean = true,
        pos: BlockPos? = null,
        configure: (Zombie) -> Unit = {},
    ) {
        if (status != ZombieRaidStatus.ONGOING) return
        val added = addWaveMob(zombie, waveNumber)
        if (added) {
            if (!exists && pos != null) {
                zombie.setPos(pos.x.toDouble() + 0.5, pos.y.toDouble() + 1.0, pos.z.toDouble() + 0.5)
                zombie.finalizeSpawn(
                    level,
                    level.getCurrentDifficultyAt(pos),
                    EntitySpawnReason.EVENT,
                    null as SpawnGroupData?
                )
                zombie.setOnGround(true)
                zombie.setPersistenceRequired()
                configure(zombie)
                level.addFreshEntityWithPassengers(zombie)
            }
        }
    }

    fun removeFromRaid(level: ServerLevel, zombie: Zombie, removeFromTotalHealth: Boolean = true) {
        val zombies: MutableSet<Zombie>? = waveZombieMap[wavesSpawned]
        if (zombies != null && (zombies.remove(zombie))) {
            if (removeFromTotalHealth) totalHealth -= zombie.health
            updateBossbar()
            setDirty(level)
        }
    }
    private fun setDirty(level: ServerLevel) { level.getZombieRaids().setDirty() }

    fun setLeader(wave: Int, zombie: Zombie) {
        waveToLeaderMap[wave] = zombie
        zombie.setItemSlot(EquipmentSlot.OFFHAND, PazBlocks.BRAINZ_FLAG.asItem().defaultInstance)
        zombie.setDropChance(EquipmentSlot.OFFHAND, 0.0f)
    }
    fun getLeader(wave: Int): Zombie? = waveToLeaderMap[wave]

    fun addWaveMob(zombie: Zombie, wave: Int = wavesSpawned): Boolean {
        waveZombieMap.computeIfAbsent(wave) { Sets.newHashSet<Zombie>() }
        val zombies = waveZombieMap[wave] as MutableSet<Zombie>
        if (zombies.contains(zombie)) return false
        var existingCopy: Zombie? = null

        for (r in zombies) {
            if (r.getUUID() == zombie.getUUID()) {
                existingCopy = r
                break
            }
        }

        if (existingCopy != null) {
            zombies.remove(existingCopy)
            zombies.add(zombie)
        }

        zombies.add(zombie)
        totalHealth += zombie.maxHealth

        updateBossbar()
        return true
    }

    fun updateBossbar() {
        zombieRaidEvent.setProgress(if (totalHealth <= 0f) 0.0f else Mth.clamp(getHealthOfZombies() / totalHealth, 0.0f, 1.0f))
    }

    fun getTotalZombiesAlive(): Int = waveZombieMap.values.stream().mapToInt { it.map { z -> if (z.isAlive) 1 else 0 }.sum() }.sum()
    fun getHealthOfZombies(): Float = waveZombieMap.values.map { it.map { z -> if (z.isAlive) z.health else 0f }.sum() }.sum()

    private fun shouldSpawnNextWave(): Boolean {
        return (getTotalZombiesAlive() == 0 || waveTimer == 0)
            && status == ZombieRaidStatus.ONGOING
            && wavesSpawned < numWaves
    }

    private fun refreshRaidProfile(level: ServerLevel) {
        if (!starterHasSeenCredits) starterHasSeenCredits = hasRaidStarterSeenCredits(level)
        numWaves = getWaveSpawnCount(difficulty, zombieRaidOmenLevel, starterHasSeenCredits)
    }

    private fun hasRaidStarterSeenCredits(level: ServerLevel): Boolean {
        val starter = startedBy ?: return starterHasSeenCredits
        val player = level.server.playerList.getPlayer(starter) ?: return starterHasSeenCredits
        return starterHasSeenCredits || player.seenCredits
    }

    private fun buildRegularWaveEntries(creditsUnlocked: Boolean): List<WaveSpawnEntry> {
        return ZombieRaiderType.VALUES
            .filter { it.isAvailable(creditsUnlocked) }
            .mapNotNull { type ->
                val count = getSpawnCountFor(type, creditsUnlocked)
                if (count <= 0) null else WaveSpawnEntry(type, count)
            }
    }

    private fun announceSpecialWave(wave: SpecialWave) {
        val title: MutableComponent = Component.translatable("event.plantz.zombie_raid.special_wave.title")
        val subtitle: MutableComponent = wave.popupMessage().copy()
        title.withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD)
        subtitle.withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.BOLD)
        zombieRaidEvent.players.forEach {
            it.connection.send(ClientboundSoundPacket(PazSounds.SPECIAL_WAVE, SoundSource.UI, it.x, it.y, it.z, 1.0f, 1.0f, random.nextLong()))
            it.connection.send(ClientboundSetTitleTextPacket(title))
            it.connection.send(ClientboundSetSubtitleTextPacket(subtitle))
        }
    }

    private fun pickSpecialWave(creditsUnlocked: Boolean): SpecialWave? {
        val eligible = SpecialWave.entries.filter { it.isAvailable(this, creditsUnlocked) }
        val totalWeight = eligible.sumOf { it.weight(this, creditsUnlocked).toDouble() }.toFloat()
        if (totalWeight <= 0f) return null

        val roll = random.nextFloat() * totalWeight
        var runningWeight = 0f
        for (wave in eligible) {
            runningWeight += wave.weight(this, creditsUnlocked)
            if (roll < runningWeight) return wave
        }
        return null
    }

    private fun getSpawnCountFor(type: ZombieRaiderType, creditsUnlocked: Boolean): Int {
        val baseCount = type.spawnCountForWave(wavesSpawned)
        if (baseCount <= 0) return 0
        val omenBonus = (zombieRaidOmenLevel / 2).coerceAtLeast(0)
        val creditsBonus = if (creditsUnlocked) 1 else 0
        val lateWaveBonus = if (creditsUnlocked && wavesSpawned >= 4) 1 else 0
        return baseCount + getPotentialBonusSpawns(type, random, creditsUnlocked) + omenBonus + creditsBonus + lateWaveBonus
    }


    private fun findRandomSpawnPos(level: ServerLevel, maxTries: Int): BlockPos? {
        val secondsRemaining = raidCooldownTicks / 20
        val howFar = 0.22f * secondsRemaining - 0.24f
        val spawnPos = MutableBlockPos()
        val startAngle = random.nextFloat() * (Math.PI * 2).toFloat()

        for (i in 0..<maxTries) {
            val angle = startAngle + Math.PI.toFloat() * i / 8.0f
            val spawnX = center.x + Mth.floor(Mth.cos(angle.toDouble()) * 32.0f * howFar) + random.nextInt(3) * Mth.floor(howFar)
            val spawnZ = center.z + Mth.floor(Mth.sin(angle.toDouble()) * 32.0f * howFar) + random.nextInt(3) * Mth.floor(howFar)
            val spawnY = level.getHeight(Heightmap.Types.WORLD_SURFACE, spawnX, spawnZ)
            spawnPos.set(spawnX, spawnY, spawnZ)
            if (level.isPositionEntityTicking(spawnPos)) return spawnPos
        }
        return null
    }

    fun stop() {
        active = false
        zombieRaidEvent.removeAllPlayers()
        status = ZombieRaidStatus.STOPPED
    }
    fun isStopped(): Boolean = status == ZombieRaidStatus.STOPPED

    private fun getPotentialBonusSpawns(
        type: ZombieRaiderType,
        random: RandomSource,
        creditsUnlocked: Boolean,
    ): Int {
        val isEasy = difficulty == Difficulty.EASY
        val isNormal = difficulty == Difficulty.NORMAL
        val wave = wavesSpawned
        val bonusSpawns: Int
        when (type) {
            ZombieRaiderType.BROWN_COAT -> bonusSpawns =
                if (isEasy) random.nextInt(2)
                else if (isNormal) 3
                else 4
            ZombieRaiderType.NEWSPAPER_ZOMBIE -> return 0
            ZombieRaiderType.DIGGER_ZOMBIE -> return 0
            ZombieRaiderType.DISCO_ZOMBIE -> {
                if (isEasy || wave <= 2 || wave == 4) return 0
                bonusSpawns = 1
            }
            ZombieRaiderType.ALL_STAR -> bonusSpawns = if (!isEasy && wave > 2) random.nextInt(2 + (zombieRaidOmenLevel / 2)) else 0
            ZombieRaiderType.ZOMBIE_YETI -> bonusSpawns = if (!isEasy && wave > 3) 1 + (zombieRaidOmenLevel / 3) else 0
            ZombieRaiderType.IMP -> bonusSpawns = if (wave > 2) random.nextInt(wave + (zombieRaidOmenLevel / 2)) else 0
            ZombieRaiderType.GARGANTUAR -> bonusSpawns = if (creditsUnlocked && wave > 4) 1 else 0
            ZombieRaiderType.ENGINEER_ZOMBIE -> bonusSpawns = if (creditsUnlocked && wave > 1) 1 + (zombieRaidOmenLevel / 3) else 0
            ZombieRaiderType.SUPER_BRAINZ -> bonusSpawns = if (creditsUnlocked && wave > 3) 1 else 0
            ZombieRaiderType.ROBO_ZOMBIE -> bonusSpawns = if (creditsUnlocked && wave > 4) 1 else 0
            ZombieRaiderType.SOLDIER_ZOMBIE -> bonusSpawns = if (creditsUnlocked && wave > 2) 1 + random.nextInt(2) else 0
        }

        return if (bonusSpawns > 0) random.nextInt(bonusSpawns + 1) else 0
    }

    private fun spawnBucketHead(zombie: Zombie) {
        zombie.setItemSlot(EquipmentSlot.HEAD, Items.BUCKET.defaultInstance)
        zombie.setDropChance(EquipmentSlot.HEAD, 0.0f)
    }

    private fun spawnSnowCoat(zombie: Zombie) {
        if (zombie is BrownCoat) zombie.variant = BrownCoatVariant.SNOW
    }

    enum class SpecialWave(
        private val minWave: Int,
        private val maxWave: Int,
        private val creditsRequired: Boolean,
        private val weightFn: (ZombieRaid, Boolean) -> Float,
        private val spawnFn: (ZombieRaid, Boolean) -> List<WaveSpawnEntry>,
    ) {
        BUCKET_BRIGADE(
            minWave = 1,
            maxWave = 3,
            creditsRequired = false,
            weightFn = { raid, _ ->
                0.11f + (raid.zombieRaidOmenLevel * 0.02f)
            },
            spawnFn = { raid, _ ->
                val count = 4 + raid.wavesSpawned / 2 + (raid.zombieRaidOmenLevel / 2)
                listOf(WaveSpawnEntry(ZombieRaiderType.BROWN_COAT, count.coerceAtLeast(3), raid::spawnBucketHead))
            }
        ),
        ALL_FOOTBALL(
            minWave = 2,
            maxWave = 4,
            creditsRequired = false,
            weightFn = { raid, credits ->
                0.11f + (raid.zombieRaidOmenLevel * 0.03f) + if (credits) 0.04f else 0f
            },
            spawnFn = { raid, _ ->
                val count = 3 + raid.wavesSpawned / 2 + (raid.zombieRaidOmenLevel / 2)
                listOf(WaveSpawnEntry(ZombieRaiderType.ALL_STAR, count.coerceAtLeast(2)))
            }
        ),
        WINTER_WONDERLAND(
            minWave = 3,
            maxWave = 9,
            creditsRequired = false,
            weightFn = { raid, credits ->
                0.12f + (raid.zombieRaidOmenLevel * 0.04f) + if (credits) 0.05f else 0f
            },
            spawnFn = { raid, _ ->
                val browncoatCount = 6 + raid.wavesSpawned / 2 + (raid.zombieRaidOmenLevel / 2)
                val yetiCount = 2 + raid.wavesSpawned / 3 + (raid.zombieRaidOmenLevel / 2)
                listOf(
                    WaveSpawnEntry(ZombieRaiderType.BROWN_COAT, browncoatCount.coerceAtLeast(5), raid::spawnSnowCoat),
                    WaveSpawnEntry(ZombieRaiderType.ZOMBIE_YETI, yetiCount.coerceAtLeast(2))
                )
            }
        ),
        SUPER_BRAINZ_STANDOFF(
            minWave = 4,
            maxWave = 10,
            creditsRequired = true,
            weightFn = { raid, credits ->
                if (!credits) 0f else 0.05f + (raid.zombieRaidOmenLevel * 0.02f)
            },
            spawnFn = { raid, _ ->
                val count = 1 + raid.wavesSpawned / 4 + (raid.zombieRaidOmenLevel / 3)
                listOf(WaveSpawnEntry(ZombieRaiderType.SUPER_BRAINZ, count.coerceAtLeast(1)))
            }
        ),
        ROBO_ARMY(
            minWave = 5,
            maxWave = 10,
            creditsRequired = true,
            weightFn = { raid, credits ->
                if (!credits) 0f else 0.05f + (raid.zombieRaidOmenLevel * 0.02f)
            },
            spawnFn = { raid, _ ->
                val roboCount = 1 + raid.wavesSpawned / 4
                val engineerCount = 1 + raid.wavesSpawned / 2
                val soldierCount = 2 + raid.wavesSpawned / 3
                listOf(
                    WaveSpawnEntry(ZombieRaiderType.ROBO_ZOMBIE, roboCount.coerceAtLeast(1)),
                    WaveSpawnEntry(ZombieRaiderType.ENGINEER_ZOMBIE, engineerCount.coerceAtLeast(2)),
                    WaveSpawnEntry(ZombieRaiderType.SOLDIER_ZOMBIE, soldierCount.coerceAtLeast(2))
                )
            }
        );

        fun isAvailable(raid: ZombieRaid, creditsUnlocked: Boolean): Boolean {
            return raid.wavesSpawned in minWave..maxWave && (!creditsRequired || creditsUnlocked)
        }

        fun weight(raid: ZombieRaid, creditsUnlocked: Boolean): Float = weightFn(raid, creditsUnlocked)

        fun spawnEntries(raid: ZombieRaid, creditsUnlocked: Boolean): List<WaveSpawnEntry> = spawnFn(raid, creditsUnlocked)

        fun popupMessage(): Component {
            return Component.translatable("event.plantz.zombie_raid.special_wave.${name.lowercase()}")
        }
    }

    enum class ZombieRaiderType(
        val entityType: EntityType<out Zombie>,
        val spawnsPerWaveBeforeBonus: IntArray,
        private val requiresCredits: Boolean = false,
    ) {
        // default spawns per wave
        BROWN_COAT(PazEntities.BROWN_COAT,             intArrayOf(2,      5,      7,      9,      9,      12,     19,     20,     30,   25)),
        NEWSPAPER_ZOMBIE(PazEntities.NEWSPAPER_ZOMBIE, intArrayOf(0,      1,      0,      1,      0,      1,      2,      1,      3,    2 )),
        DIGGER_ZOMBIE(PazEntities.DIGGER_ZOMBIE,       intArrayOf(0,      0,      1,      0,      4,      1,      1,      2,      3,    3 )),
        DISCO_ZOMBIE(PazEntities.DISCO_ZOMBIE,         intArrayOf(0,      0,      1,      2,      3,      3,      4,      2,      3,    5 )),
        ALL_STAR(PazEntities.ALL_STAR,                 intArrayOf(0,      0,      1,      3,      2,      4,      5,      3,      5,    5 )),
        ZOMBIE_YETI(PazEntities.ZOMBIE_YETI,           intArrayOf(0,      0,      0,      1,      2,      0,      3,      1,      2,    3 )),
        IMP(PazEntities.IMP,                           intArrayOf(0,      0,      0,      0,      2,      4,      5,      5,      4,    8 )),
        GARGANTUAR(PazEntities.GARGANTUAR,             intArrayOf(0,      0,      0,      0,      0,      1,      0,      2,      1,    3 )),
        ENGINEER_ZOMBIE(PazEntities.ENGINEER_ZOMBIE,   intArrayOf(2,      1,      1,      3,      1,      1,      2,      2,      3,    4 ), true),
        SUPER_BRAINZ(PazEntities.SUPER_BRAINZ,         intArrayOf(0,      0,      0,      0,      0,      1,      1,      1,      2,    4 ), true),
        ROBO_ZOMBIE(PazEntities.ROBO_ZOMBIE,           intArrayOf(0,      0,      1,      0,      1,      2,      1,      2,      2,    4 ), true),
        SOLDIER_ZOMBIE(PazEntities.SOLDIER_ZOMBIE,     intArrayOf(0,      2,      1,      1,      2,      4,      6,      5,      8,    10), true);

        companion object {
            val VALUES = entries.toTypedArray()
        }

        fun isAvailable(creditsUnlocked: Boolean): Boolean = !requiresCredits || creditsUnlocked

        fun spawnCountForWave(wave: Int): Int {
            val index = wave.coerceIn(0, spawnsPerWaveBeforeBonus.lastIndex)
            return spawnsPerWaveBeforeBonus[index]
        }
    }
}
