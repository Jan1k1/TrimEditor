package org.jan1k.plugin.trimeditor.api

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern

interface TrimEditorApi {
    fun apiVersion(): Int

    fun openEditor(player: Player): TrimEditorOpenResult

    fun openEditor(player: Player, checkPermission: Boolean): TrimEditorOpenResult

    fun isEditableArmor(item: ItemStack?): Boolean

    fun withTrim(item: ItemStack, pattern: TrimPattern, material: TrimMaterial): ItemStack?

    fun patterns(): List<TrimPattern>

    fun materials(): List<TrimMaterial>

    fun visiblePatterns(player: Player): List<TrimPattern>

    fun visibleMaterials(player: Player): List<TrimMaterial>
}
