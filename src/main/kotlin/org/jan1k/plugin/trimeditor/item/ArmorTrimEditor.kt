package org.jan1k.plugin.trimeditor.item

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ArmorMeta
import org.bukkit.inventory.meta.trim.ArmorTrim

object ArmorTrimEditor {
    fun isEditableArmor(item: ItemStack?): Boolean {
        return item != null && item.type != Material.AIR && item.itemMeta is ArmorMeta
    }

    fun applyTrim(item: ItemStack, trim: ArmorTrim): ItemStack {
        val edited = item.clone()
        val meta = edited.itemMeta

        require(meta is ArmorMeta) { "Item is not trimmable armor" }

        meta.trim = trim
        edited.itemMeta = meta
        return edited
    }
}
