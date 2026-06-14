package org.jan1k.trimeditor.gui

import org.bukkit.Material
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.jan1k.trimeditor.item.ArmorTrimEditor

class ConfirmScreen : Screen {
    override val id = ScreenId.CONFIRM
    override val title = "ᴛʀɪᴍ ᴇᴅɪᴛᴏʀ › ᴄᴏɴꜰɪʀᴍ"
    override val size = 5
    override val inventoryType = InventoryType.HOPPER

    override fun buttons(context: ScreenRenderContext): List<Button> = listOf(
        Button(1, actionButtonItem(Material.ARROW, "« ʙᴀᴄᴋ")) {
            gui.show(player, session, ScreenId.MATERIALS)
        },
        Button(2, previewItem(context)) {},
        Button(3, actionButtonItem(Material.LIME_DYE, "ᴀᴘᴘʟʏ ➜")) {
            gui.tell(player, gui.messageFor(gui.applySelection(player, session)))
            gui.close(player)
        }
    )

    private fun previewItem(context: ScreenRenderContext): ItemStack {
        val pattern = context.session.pattern
        val material = context.session.material
        val held = context.player.inventory.getItem(context.session.handSlot)

        if (pattern == null || material == null || held == null || !ArmorTrimEditor.isEditableArmor(held)) {
            return buttonItem(Material.BARRIER)
        }

        return ArmorTrimEditor.applyTrim(held, ArmorTrim(material, pattern))
    }
}
