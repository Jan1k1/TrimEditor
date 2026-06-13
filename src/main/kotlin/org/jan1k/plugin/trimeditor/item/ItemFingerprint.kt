package org.jan1k.plugin.trimeditor.item

import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ArmorMeta
import org.bukkit.inventory.meta.Damageable

class ItemFingerprint private constructor(
    private val serialized: Map<String, Any>,
    private val trim: String?,
) {
    override fun equals(other: Any?): Boolean {
        return other is ItemFingerprint && serialized == other.serialized && trim == other.trim
    }

    override fun hashCode(): Int {
        return 31 * serialized.hashCode() + trim.hashCode()
    }

    companion object {
        fun of(item: ItemStack): ItemFingerprint {
            val normalized = item.clone()
            val meta = normalized.itemMeta

            if (meta is Damageable) {
                meta.damage = 0
                normalized.itemMeta = meta
            }

            val trim = (meta as? ArmorMeta)?.trim?.let { armorTrim ->
                "${armorTrim.pattern.key()}|${armorTrim.material.key()}"
            }

            return ItemFingerprint(normalized.serialize(), trim)
        }
    }
}
