package duskdn.plantz_ex.entity.plant.utils

import com.google.common.base.Predicate
import duskdn.plantz_ex.entity.Balloon
import duskdn.plantz_ex.entity.plant.init.PazPlant
import duskdn.plantz_ex.init.PazComponents
import duskdn.plantz_ex.init.PazConfig
import duskdn.plantz_ex.init.PazEffects
import duskdn.plantz_ex.init.PazItems
import duskdn.plantz_ex.init.PazSounds
import duskdn.plantz_ex.init.PazTags
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.tags.EntityTypeTags
import net.minecraft.world.InteractionHand
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.entity.monster.Zombie
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemUtils
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.AABB

object PlantUtils {

    fun getPlantsAt(level: Level, aabb: AABB, plant: PazPlant?, predicate: Predicate<PazPlant>): MutableList<PazPlant> {
        val otherPlantsAtPos = level.getEntitiesOfClass(PazPlant::class.java, aabb) {
            (plant != null && it != plant && it != plant.vehicle) && it.isAlive && !it.isDeadOrDying && predicate.test(it)
        }
        return otherPlantsAtPos
    }

}

// PLANT ITEM INTERACTIONS
// sun interaction
fun PazPlant.processSunItem(player: Player, item: ItemStack, hand: InteractionHand, growNeeds: PlantGrowNeeds): Boolean {
    val hasStoredSun = item.get(PazComponents.STORED_SUN)?.hasSun() == true
    val isSunItem = item.`is`(PazItems.SUN)
    if (!hasStoredSun && !isSunItem) return false
    val level = level() as? ServerLevel?: return false
    var success = false

    if (isTame && health < maxHealth) {// heal
        sunHeal(1)
        success = true
    }
    else if (!isTame) {// try to tame
        if (random.nextFloat() < PazConfig.getTameChance(type)) {
            tame(player)
            level.broadcastEntityEvent(this, 7.toByte())
        } else level.broadcastEntityEvent(this, 6.toByte())
        success = true
    }
    else if (growNeeds == PlantGrowNeeds.SUN && verifyOwner(player)) {// grow seeds
        playSound(
            SoundEvents.BUBBLE_COLUMN_BUBBLE_POP, 1.0f,
            receivedSun.toFloat()/sunRequiredForSeeds() + 0.9f
        )
        if (receivedSun++ >= sunRequiredForSeeds()) awardSeedPacket(player)
        success = true
    }

    if (success) {
        if (hasStoredSun) item.set(PazComponents.STORED_SUN, item.get(PazComponents.STORED_SUN)?.removeSun(1))
        else item.consume(1, player)
    }
    return success
}
// watering interaction
fun PazPlant.processWateringItem(player: Player, item: ItemStack, hand: InteractionHand, growNeeds: PlantGrowNeeds): Boolean {
    if (growNeeds != PlantGrowNeeds.WATER) return false
    val isWaterBottle = item.components.get(DataComponents.POTION_CONTENTS)?.`is`(Potions.WATER) == true
    val isWaterBucket = item.`is`(Items.WATER_BUCKET)
    val hasStoredWater = item.get(PazComponents.STORED_WATER)?.hasWater() == true
    val waterAmount = when (true) {
        isWaterBottle -> {
            player.setItemInHand(hand, ItemUtils.createFilledResult(item, player, ItemStack(Items.GLASS_BOTTLE)))
            this.playSound(SoundEvents.BOTTLE_EMPTY)
            1
        }
        isWaterBucket -> {
            player.setItemInHand(hand, ItemStack(Items.BUCKET))
            this.playSound(SoundEvents.BUCKET_EMPTY, 1.0f, 1.0f)
            8
        }
        (hasStoredWater) -> {
            this.playSound(PazSounds.WATERING_CAN)
            item.set(PazComponents.STORED_WATER, item.get(PazComponents.STORED_WATER)?.removeWater(2))
            2
        }
        else -> 0
    }
    this.receivedWater+=waterAmount
    if (waterAmount>0) {
        addParticlesAroundSelf()
        funnyBounce()
        return true
    }
    return false
}

fun PazPlant.enemyCheck(target: LivingEntity): Boolean {
    return target.isAlive && target !is PazPlant // target is not plant
            && (
            target is Zombie // target is a zombie
                    || ( target is Enemy ) // or an enem
                    || ( BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(target.type).`is`(PazTags.EntityTypes.ATTACKS_PLANTS) )
                    || (target is Balloon && target.leashHolder != null && target.leashHolder is LivingEntity && enemyCheck(
                target.leashHolder as LivingEntity
            ))
            )
}

fun PazPlant.snowCheck(): Boolean {

    val cannotChill = BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(type).`is`(PazTags.EntityTypes.CANNOT_CHILL)

    println("CANNOT CHILL: $cannotChill")

    if (cannotChill) return false

    val blockIn = level().getBlockState(blockPosition())

    if (blockIn.`is`(Blocks.SNOW)) {

        println("IS IN SNOW: ${this.type}")

        if (!hasEffect(PazEffects.CHILLED)) addEffect(MobEffectInstance(PazEffects.CHILLED, 20))

        setIsInPowderSnow(true)

        if (ticksFrozen < ticksRequiredToFreeze) {
            ticksFrozen++
        }

        return true

    }

    return false
}

enum class PlantGrowNeeds {
    SOIL,
    SUN,
    WATER,
    TIME;
}