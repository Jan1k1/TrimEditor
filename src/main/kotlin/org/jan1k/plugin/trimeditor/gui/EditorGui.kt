package org.jan1k.plugin.trimeditor.gui

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.jan1k.plugin.trimeditor.session.EditSession
import org.jan1k.plugin.trimeditor.session.SessionManager
import java.util.UUID

class EditorGui(
    private val sessions: SessionManager,
    cooldownMillis: Long = ClickGuard.DEFAULT_COOLDOWN_MILLIS,
) {
    private val clickGuard = ClickGuard(cooldownMillis)
    private val switching = HashSet<UUID>()
    private val screens = mapOf(
        ScreenId.PATTERNS to PatternScreen(),
        ScreenId.MATERIALS to MaterialScreen(),
        ScreenId.CONFIRM to ConfirmScreen(),
    )

    fun updateCooldownMillis(cooldownMillis: Long) {
        clickGuard.cooldownMillis = cooldownMillis
    }

    fun open(player: Player) {
        val session = EditSession(player.uniqueId) { player.closeInventory() }
        sessions.open(session)
        show(player, session, ScreenId.PATTERNS)
    }

    fun show(player: Player, session: EditSession, screenId: ScreenId) {
        val screen = screens.getValue(screenId)
        val buttons = screen.buttons(session)
        val holder = EditorHolder(player.uniqueId, screenId, buttons)
        val inventory = Bukkit.createInventory(holder, 54, Component.text(screen.title))

        holder.attach(inventory)
        session.screen = screenId
        buttons.forEach { inventory.setItem(it.slot, it.icon) }

        switching.add(player.uniqueId)
        player.openInventory(inventory)
        switching.remove(player.uniqueId)
    }

    fun close(player: Player) {
        clickGuard.clear(player.uniqueId)
        sessions.close(player.uniqueId)
    }

    fun handleClick(event: InventoryClickEvent) {
        val holder = event.view.topInventory.holder as? EditorHolder ?: return
        event.isCancelled = true

        val player = event.whoClicked as? Player ?: return
        if (holder.playerId != player.uniqueId) return
        val button = holder.button(event.rawSlot) ?: return
        if (!clickGuard.accept(player.uniqueId, event.click, event.rawSlot, event.view.topInventory.size)) return

        val session = sessions[player.uniqueId] ?: return
        button.action(GuiContext(player, session, this))
    }

    fun handleDrag(event: InventoryDragEvent) {
        val holder = event.view.topInventory.holder as? EditorHolder ?: return
        val topSlots = 0 until event.view.topInventory.size
        if (event.rawSlots.any { it in topSlots }) {
            event.isCancelled = true
        }
    }

    fun handleClose(event: InventoryCloseEvent) {
        val holder = event.view.topInventory.holder as? EditorHolder ?: return
        val player = event.player as? Player ?: return
        if (holder.playerId != player.uniqueId) return
        if (switching.remove(player.uniqueId)) return

        clickGuard.clear(player.uniqueId)
        sessions.remove(player.uniqueId)
    }

    fun forget(playerId: UUID) {
        clickGuard.clear(playerId)
        sessions.remove(playerId)
    }
}
