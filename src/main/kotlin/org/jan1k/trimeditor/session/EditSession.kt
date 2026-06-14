package org.jan1k.trimeditor.session

import org.jan1k.trimeditor.gui.ScreenId
import org.jan1k.trimeditor.item.ItemFingerprint
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import java.util.UUID

class EditSession(
    val playerId: UUID,
    val handSlot: Int = -1,
    val fingerprint: ItemFingerprint? = null,
    private val closer: () -> Unit,
) {
    var pattern: TrimPattern? = null
    var material: TrimMaterial? = null
    var screen: ScreenId = ScreenId.PATTERNS

    fun close() {
        closer()
    }
}
