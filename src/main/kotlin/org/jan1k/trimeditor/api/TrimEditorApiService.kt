package org.jan1k.trimeditor.api

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import org.jan1k.trimeditor.TrimEditorPlugin
import org.jan1k.trimeditor.cost.RequirementService
import org.jan1k.trimeditor.cost.TrimRequirementSettings
import org.jan1k.trimeditor.gui.EditorGui
import org.jan1k.trimeditor.item.ArmorTrimEditor
import org.jan1k.trimeditor.trim.TrimCatalog

class TrimEditorApiService(
    private val plugin: TrimEditorPlugin,
    private val gui: EditorGui,
) : TrimEditorApi {
    private val requirements = RequirementService()

    override fun apiVersion(): Int {
        return API_VERSION
    }

    override fun openEditor(player: Player): TrimEditorOpenResult {
        return openEditor(player, true)
    }

    override fun openEditor(player: Player, checkPermission: Boolean): TrimEditorOpenResult {
        if (checkPermission && !player.hasPermission("trimeditor.use")) {
            return TrimEditorOpenResult.NO_PERMISSION
        }

        if (!ArmorTrimEditor.isEditableArmor(player.inventory.itemInMainHand)) {
            return TrimEditorOpenResult.NOT_ARMOR
        }

        val precheck = requirements.precheck(player.inventory, requirementSettings(player))
        if (!precheck.canOpen) {
            return TrimEditorOpenResult.MISSING_REQUIREMENTS
        }

        return if (gui.open(player)) TrimEditorOpenResult.OPENED else TrimEditorOpenResult.NOT_ARMOR
    }

    override fun isEditableArmor(item: ItemStack?): Boolean {
        return ArmorTrimEditor.isEditableArmor(item)
    }

    override fun withTrim(item: ItemStack, pattern: TrimPattern, material: TrimMaterial): ItemStack? {
        if (!ArmorTrimEditor.isEditableArmor(item)) {
            return null
        }

        return ArmorTrimEditor.applyTrim(item, ArmorTrim(material, pattern))
    }

    override fun patterns(): List<TrimPattern> {
        return TrimCatalog.patterns.map { it.pattern }
    }

    override fun materials(): List<TrimMaterial> {
        return TrimCatalog.materials.map { it.material }
    }

    override fun visiblePatterns(player: Player): List<TrimPattern> {
        return requirements.visiblePatterns(player.inventory, requirementSettings(player))
            .filter { option ->
                val key = option.pattern.key().value()
                player.hasPermission("trimeditor.pattern.*") || player.hasPermission("trimeditor.pattern.$key")
            }
            .map { it.pattern }
    }

    override fun visibleMaterials(player: Player): List<TrimMaterial> {
        return requirements.visibleMaterials(player.inventory, requirementSettings(player))
            .filter { option ->
                val key = option.material.key().value()
                player.hasPermission("trimeditor.material.*") || player.hasPermission("trimeditor.material.$key")
            }
            .map { it.material }
    }

    private fun requirementSettings(player: Player): TrimRequirementSettings {
        return if (player.hasPermission("trimeditor.bypass.cost")) {
            TrimRequirementSettings()
        } else {
            plugin.pluginConfig.requirementSettings()
        }
    }

    companion object {
        const val API_VERSION = 1
    }
}
