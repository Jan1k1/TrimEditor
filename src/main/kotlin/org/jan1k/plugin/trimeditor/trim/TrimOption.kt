package org.jan1k.plugin.trimeditor.trim

import org.bukkit.Material
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

data class TrimPatternOption(
    val pattern: TrimPattern,
    val templateItem: Material,
)

data class TrimMaterialOption(
    val material: TrimMaterial,
    val ingredientItem: Material,
)
