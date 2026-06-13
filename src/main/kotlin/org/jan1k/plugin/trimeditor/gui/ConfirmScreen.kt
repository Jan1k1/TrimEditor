package org.jan1k.plugin.trimeditor.gui

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.jan1k.plugin.trimeditor.item.ArmorTrimEditor

class ConfirmScreen : Screen {
    override val id = ScreenId.CONFIRM
    override val title = "ᴛʀɪᴍ ᴇᴅɪᴛᴏʀ › ᴄᴏɴꜰɪʀᴍ"
    override val size = 9

    override fun buttons(context: ScreenRenderContext): List<Button> = listOf(
        Button(3, previewItem(context)) {},
        Button(4, buttonItem(Material.LIME_STAINED_GLASS_PANE)) {
            gui.tell(player, gui.messageFor(gui.applySelection(player, session)))
            gui.close(player)
        },
        Button(5, buttonItem(Material.ARROW)) {
            gui.show(player, session, ScreenId.MATERIALS)
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
