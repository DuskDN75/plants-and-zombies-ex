<<<<<<<< HEAD:src/client/kotlin/joshxviii/plantz/renderer/RenderingUtils.kt
package joshxviii.plantz.renderer

import joshxviii.plantz.PazEntities.MAGIC_NAMES
import joshxviii.plantz.pazResource
========
package duskdn.plantz

import duskdn.plantz.init.PazEntities.MAGIC_NAMES
import duskdn.plantz.util.pazResource
>>>>>>>> 68eac8a988f75e82769978a50f4547f227e4f5a3:src/client/kotlin/duskdn/plantz/RenderingUtils.kt
import net.minecraft.client.Minecraft
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
    for (suffix in suffixes.permutationsDescending()) {
        if (suffix.isEmpty()) break
        val candidate = pazResource("${base}_${suffix}.png")
        if (rm.getResource(candidate).isPresent) return candidate
    }
    return null
}

fun LivingEntityRenderState.isMagicName(name: String): String {
    val type = this.entityType
    MAGIC_NAMES.forEach { (entityType, magicName) ->
        if (entityType == type && magicName == name.lowercase()) return magicName
    }
    return ""
}

fun LivingEntityRenderState.getTextureLocation(basePath: String, suffixes: MutableList<String> = mutableListOf()): Identifier {
    val entityName = entityType.toShortString().lowercase()
    val base = "${basePath}/${entityName}/${entityName}"
    val rm = Minecraft.getInstance().resourceManager

    val textureLocation = resolveTextureLocation(base, rm, suffixes)
    return textureLocation?: pazResource("${base}.png")
}

fun LivingEntityRenderState.getEmissiveTextureLocation(basePath: String, suffixes: MutableList<String> = mutableListOf()): Identifier? {
    val entityName = entityType.toShortString().lowercase()
    val base = "${basePath}/${entityName}/${entityName}"
    val rm = Minecraft.getInstance().resourceManager

    return resolveTextureLocation(base, rm, suffixes.apply { add("emissive") })
}