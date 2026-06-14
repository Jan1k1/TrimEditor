package org.jan1k.trimeditor.gui

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import java.util.UUID

class EditorHolder(
    val playerId: UUID,
    val screenId: ScreenId,
    buttons: List<Button>,
) : InventoryHolder {
    private var inventoryRef: Inventory? = null
    private val buttonsBySlot = buttons.associateBy { it.slot }

    fun attach(inventory: Inventory) {
        inventoryRef = inventory
    }

    fun button(slot: Int): Button? = buttonsBySlot[slot]

    override fun getInventory(): Inventory = inventoryRef ?: error("Inventory not attached")
}
