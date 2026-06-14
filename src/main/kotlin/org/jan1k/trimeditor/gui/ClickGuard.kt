package org.jan1k.trimeditor.gui

import org.bukkit.event.inventory.ClickType
import java.util.UUID

class ClickGuard(cooldownMillis: Long = DEFAULT_COOLDOWN_MILLIS) {
    private val lastAccepted = HashMap<UUID, Long>()
    var cooldownMillis: Long = cooldownMillis.coerceAtLeast(0)
        set(value) {
            field = value.coerceAtLeast(0)
        }

    fun accept(
        playerId: UUID,
        click: ClickType,
        rawSlot: Int,
        topSize: Int,
        nowMillis: Long = System.currentTimeMillis(),
    ): Boolean {
        if (rawSlot !in 0 until topSize) return false
        if (click != ClickType.LEFT && click != ClickType.RIGHT) return false

        val previous = lastAccepted[playerId]
        if (previous != null && nowMillis - previous < cooldownMillis) return false

        lastAccepted[playerId] = nowMillis
        return true
    }

    fun clear(playerId: UUID) {
        lastAccepted.remove(playerId)
    }

    companion object {
        const val DEFAULT_COOLDOWN_MILLIS = 300L
    }
}
