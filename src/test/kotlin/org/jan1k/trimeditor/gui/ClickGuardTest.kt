package org.jan1k.trimeditor.gui

import org.bukkit.event.inventory.ClickType
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ClickGuardTest {
    @Test
    fun `accepts only normal top inventory left and right clicks`() {
        val guard = ClickGuard(300)
        val playerId = UUID.randomUUID()

        assertTrue(guard.accept(playerId, ClickType.LEFT, rawSlot = 0, topSize = 54, nowMillis = 1_000))
        assertTrue(guard.accept(UUID.randomUUID(), ClickType.RIGHT, rawSlot = 53, topSize = 54, nowMillis = 1_000))
        assertFalse(guard.accept(UUID.randomUUID(), ClickType.SHIFT_LEFT, rawSlot = 0, topSize = 54, nowMillis = 1_000))
        assertFalse(guard.accept(UUID.randomUUID(), ClickType.SHIFT_RIGHT, rawSlot = 0, topSize = 54, nowMillis = 1_000))
        assertFalse(guard.accept(UUID.randomUUID(), ClickType.NUMBER_KEY, rawSlot = 0, topSize = 54, nowMillis = 1_000))
        assertFalse(guard.accept(UUID.randomUUID(), ClickType.DOUBLE_CLICK, rawSlot = 0, topSize = 54, nowMillis = 1_000))
        assertFalse(guard.accept(UUID.randomUUID(), ClickType.LEFT, rawSlot = 54, topSize = 54, nowMillis = 1_000))
        assertFalse(guard.accept(UUID.randomUUID(), ClickType.RIGHT, rawSlot = -999, topSize = 54, nowMillis = 1_000))
    }

    @Test
    fun `enforces configurable cooldown per player`() {
        val guard = ClickGuard(300)
        val playerId = UUID.randomUUID()

        assertTrue(guard.accept(playerId, ClickType.LEFT, rawSlot = 4, topSize = 54, nowMillis = 1_000))
        assertFalse(guard.accept(playerId, ClickType.RIGHT, rawSlot = 4, topSize = 54, nowMillis = 1_299))
        assertTrue(guard.accept(playerId, ClickType.RIGHT, rawSlot = 4, topSize = 54, nowMillis = 1_300))
        assertTrue(guard.accept(UUID.randomUUID(), ClickType.LEFT, rawSlot = 4, topSize = 54, nowMillis = 1_299))
    }

    @Test
    fun `zero cooldown accepts repeated clicks`() {
        val guard = ClickGuard(0)
        val playerId = UUID.randomUUID()

        assertTrue(guard.accept(playerId, ClickType.LEFT, rawSlot = 4, topSize = 54, nowMillis = 1_000))
        assertTrue(guard.accept(playerId, ClickType.RIGHT, rawSlot = 4, topSize = 54, nowMillis = 1_000))
    }
}
