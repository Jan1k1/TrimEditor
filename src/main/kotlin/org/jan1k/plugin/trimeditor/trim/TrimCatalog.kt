package org.jan1k.plugin.trimeditor.trim

import org.bukkit.Material
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

object TrimCatalog {
    val patterns = listOf(
        TrimPatternOption(TrimPattern.SENTRY, Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE),
        TrimPatternOption(TrimPattern.DUNE, Material.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE),
        TrimPatternOption(TrimPattern.COAST, Material.COAST_ARMOR_TRIM_SMITHING_TEMPLATE),
        TrimPatternOption(TrimPattern.WILD, Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE),
        TrimPatternOption(TrimPattern.WARD, Material.WARD_ARMOR_TRIM_SMITHING_TEMPLATE),
        TrimPatternOption(TrimPattern.EYE, Material.EYE_ARMOR_TRIM_SMITHING_TEMPLATE),
        TrimPatternOption(TrimPattern.VEX, Material.VEX_ARMOR_TRIM_SMITHING_TEMPLATE),
        TrimPatternOption(TrimPattern.TIDE, Material.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE),
        TrimPatternOption(TrimPattern.SNOUT, Material.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE),
        TrimPatternOption(TrimPattern.RIB, Material.RIB_ARMOR_TRIM_SMITHING_TEMPLATE),
        TrimPatternOption(TrimPattern.SPIRE, Material.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE),
        TrimPatternOption(TrimPattern.WAYFINDER, Material.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE),
        TrimPatternOption(TrimPattern.SHAPER, Material.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE),
        TrimPatternOption(TrimPattern.SILENCE, Material.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE),
        TrimPatternOption(TrimPattern.RAISER, Material.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE),
        TrimPatternOption(TrimPattern.HOST, Material.HOST_ARMOR_TRIM_SMITHING_TEMPLATE),
        TrimPatternOption(TrimPattern.FLOW, Material.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE),
        TrimPatternOption(TrimPattern.BOLT, Material.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE),
    )

    val materials = listOf(
        TrimMaterialOption(TrimMaterial.QUARTZ, Material.QUARTZ),
        TrimMaterialOption(TrimMaterial.IRON, Material.IRON_INGOT),
        TrimMaterialOption(TrimMaterial.NETHERITE, Material.NETHERITE_INGOT),
        TrimMaterialOption(TrimMaterial.REDSTONE, Material.REDSTONE),
        TrimMaterialOption(TrimMaterial.COPPER, Material.COPPER_INGOT),
        TrimMaterialOption(TrimMaterial.GOLD, Material.GOLD_INGOT),
        TrimMaterialOption(TrimMaterial.EMERALD, Material.EMERALD),
        TrimMaterialOption(TrimMaterial.DIAMOND, Material.DIAMOND),
        TrimMaterialOption(TrimMaterial.LAPIS, Material.LAPIS_LAZULI),
        TrimMaterialOption(TrimMaterial.AMETHYST, Material.AMETHYST_SHARD),
    )

    fun templateItem(pattern: TrimPattern): Material? {
        return patterns.firstOrNull { it.pattern == pattern }?.templateItem
    }

    fun ingredientItem(material: TrimMaterial): Material? {
        return materials.firstOrNull { it.material == material }?.ingredientItem
    }
}
