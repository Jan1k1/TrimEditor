package org.jan1k.plugin.trimeditor.item

import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ArmorMeta
import org.bukkit.inventory.meta.Damageable

data class ItemFingerprint private constructor(
    private val serialized: Map<String, Any>,
    private val trim: String?,
) {
    companion object {
        fun of(item: ItemStack): ItemFingerprint {
            val normalized = item.clone()
            val meta = normalized.itemMeta

            if (meta is Damageable) {
                meta.damage = 0
                normalized.itemMeta = meta
            }

            // serialize() does not reliably capture the armor trim, so fold it in
            // explicitly: the fingerprint must change when pattern or material changes.
            val trim = (meta as? ArmorMeta)?.trim?.let { armorTrim ->
                "${armorTrim.pattern.key()}|${armorTrim.material.key()}"
            }

            return ItemFingerprint(normalized.serialize(), trim)
        }
    }
}
