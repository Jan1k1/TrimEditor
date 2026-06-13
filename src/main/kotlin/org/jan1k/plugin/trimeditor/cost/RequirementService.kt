package org.jan1k.plugin.trimeditor.cost

import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import org.jan1k.plugin.trimeditor.trim.TrimCatalog
import org.jan1k.plugin.trimeditor.trim.TrimMaterialOption
import org.jan1k.plugin.trimeditor.trim.TrimPatternOption

class RequirementService {
    fun visiblePatterns(
        inventory: Inventory,
        settings: TrimRequirementSettings,
        options: List<TrimPatternOption> = TrimCatalog.patterns,
    ): List<TrimPatternOption> {
        if (!settings.enabled) {
            return options
        }

        return options.filter { inventory.hasAtLeast(it.templateItem, 1) }
    }

    fun visibleMaterials(
        inventory: Inventory,
        settings: TrimRequirementSettings,
        options: List<TrimMaterialOption> = TrimCatalog.materials,
    ): List<TrimMaterialOption> {
        if (!settings.enabled) {
            return options
        }

        return options.filter { inventory.hasAtLeast(it.ingredientItem, 1) }
    }

    fun precheck(
        inventory: Inventory,
        settings: TrimRequirementSettings,
        patterns: List<TrimPatternOption> = TrimCatalog.patterns,
        materials: List<TrimMaterialOption> = TrimCatalog.materials,
    ): RequirementPrecheck {
        if (!settings.enabled) {
            return RequirementPrecheck(canOpen = true, missingTemplate = false, missingMaterial = false)
        }

        val missingTemplate = visiblePatterns(inventory, settings, patterns).isEmpty()
        val missingMaterial = visibleMaterials(inventory, settings, materials).isEmpty()

        return RequirementPrecheck(
            canOpen = !missingTemplate && !missingMaterial,
            missingTemplate = missingTemplate,
            missingMaterial = missingMaterial,
        )
    }

    fun hasSelection(
        inventory: Inventory,
        settings: TrimRequirementSettings,
        pattern: TrimPattern,
        material: TrimMaterial,
    ): Boolean {
        if (!settings.enabled) {
            return true
        }

        val templateItem = TrimCatalog.templateItem(pattern) ?: return false
        val ingredientItem = TrimCatalog.ingredientItem(material) ?: return false

        return inventory.hasAtLeast(templateItem, 1) && inventory.hasAtLeast(ingredientItem, 1)
    }

    fun consumeSelection(
        inventory: Inventory,
        settings: TrimRequirementSettings,
        pattern: TrimPattern,
        material: TrimMaterial,
    ): Boolean {
        if (!settings.enabled) {
            return true
        }

        if (!hasSelection(inventory, settings, pattern, material)) {
            return false
        }

        val templateItem = TrimCatalog.templateItem(pattern) ?: return false
        val ingredientItem = TrimCatalog.ingredientItem(material) ?: return false

        if (settings.consumeTemplate) {
            inventory.takeOne(templateItem)
        }

        if (settings.consumeMaterial) {
            inventory.takeOne(ingredientItem)
        }

        return true
    }

    private fun Inventory.hasAtLeast(type: Material, amount: Int): Boolean {
        return contents.filterNotNull().filter { it.type == type }.sumOf { it.amount } >= amount
    }

    private fun Inventory.takeOne(type: Material): Boolean {
        for (slot in 0 until size) {
            val item = getItem(slot) ?: continue

            if (item.type != type) {
                continue
            }

            if (item.amount > 1) {
                item.amount -= 1
                setItem(slot, item)
            } else {
                setItem(slot, null)
            }

            return true
        }

        return false
    }
}
