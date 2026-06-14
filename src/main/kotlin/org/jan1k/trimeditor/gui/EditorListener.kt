package org.jan1k.trimeditor.gui

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.player.PlayerQuitEvent

class EditorListener(private val gui: EditorGui) : Listener {
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        gui.handleClick(event)
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        gui.handleClose(event)
    }

    @EventHandler
    fun onInventoryDrag(event: InventoryDragEvent) {
        gui.handleDrag(event)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        gui.forget(event.player.uniqueId)
    }
}
