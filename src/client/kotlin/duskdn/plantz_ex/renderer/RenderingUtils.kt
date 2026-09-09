package duskdn.plantz_ex.renderer

import duskdn.plantz_ex.init.PazEntities.MAGIC_NAMES
import duskdn.plantz_ex.renderer.entity.PlantRenderState
import duskdn.plantz_ex.renderer.getAdditiveTextureLocation
import duskdn.plantz_ex.util.pazResource
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.entity.state.EntityRenderState
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import net.minecraft.resources.Identifier
import net.minecraft.server.packs.resources.ResourceManager
import kotlin.collections.component1
import kotlin.collections.component2



// MODEL RENDERING
fun List<String>.permutationsDescending(): List<String> = buildList {
    add(this@permutationsDescending.joinToString("_"))
    for (i in size - 1 downTo 1) {
        add(this@permutationsDescending.subList(0, i).joinToString(""))
    }
}

fun resolveTextureLocation(base: String, rm: ResourceManager, suffixes: List<String> = listOf()): Identifier? {

    val permutations = suffixes.permutationsDescending()

    for (suffix in permutations) {
        if (suffix.isEmpty()) break
        val candidate = pazResource("${base}_${suffix}.png")

        if (rm.getResource(candidate).isPresent) return candidate
    }
    return null
}

fun EntityRenderState.isMagicName(name: String): String {
    val type = this.entityType
    MAGIC_NAMES.forEach { (entityType, magicName) ->
        if (entityType == type && magicName == name.lowercase()) return magicName
    }
    return ""
}

fun EntityRenderState.getTextureLocation(basePath: String, suffixes: MutableList<String> = mutableListOf()): Identifier {
    val entityName = entityType.toShortString().lowercase()
    val base = "${basePath}/${entityName}/${entityName}"
    val rm = Minecraft.getInstance().resourceManager

    val textureLocation = resolveTextureLocation(base, rm, suffixes)
    return textureLocation?: pazResource("${base}.png")
}

fun EntityRenderState.getEmissiveTextureLocation(basePath: String, suffixes: MutableList<String> = mutableListOf()): Identifier? {
    val entityName = entityType.toShortString().lowercase()
    val base = "${basePath}/${entityName}/${entityName}"
    val rm = Minecraft.getInstance().resourceManager

    suffixes.add("emissive")

    return resolveTextureLocation(base, rm, suffixes) ?: resolveTextureLocation(base, rm, suffixes.apply { remove("sleep") })
}

fun EntityRenderState.getAdditiveTextureLocation(basePath: String, suffixes: MutableList<String> = mutableListOf()): Identifier? {
    val entityName = entityType.toShortString().lowercase()
    val base = "${basePath}/${entityName}/${entityName}"
    val rm = Minecraft.getInstance().resourceManager

    suffixes.add("additive")

    return resolveTextureLocation(base, rm, suffixes) ?: resolveTextureLocation(base, rm, suffixes.apply { remove("sleep") })
}

fun PlantRenderState.getEyesTextureLocation(basePath: String, suffixes: MutableList<String> = mutableListOf(), closed: Boolean): Identifier? {

//    suffixes.remove("sleep")

    val entityName = entityType.toShortString().lowercase()
    val base = "${basePath}/${entityName}/${entityName}"
    val rm = Minecraft.getInstance().resourceManager
    


    if (closed) suffixes.add("eyes_closed") else suffixes.add("eyes")

//    println("SUFFIXES ARE: $suffixes")

    return resolveTextureLocation(base, rm, suffixes) ?: resolveTextureLocation(base, rm, suffixes.apply { remove("sleep") })
}

fun PlantRenderState.getEmissiveEyesTextureLocation(basePath: String, suffixes: MutableList<String> = mutableListOf(), closed: Boolean): Identifier? {

    if (closed) suffixes.add("eyes_closed") else suffixes.add("eyes")

    return getEmissiveTextureLocation(basePath, suffixes)
}
