package org.jan1k.plugin.trimeditor.gui

import org.bukkit.Material

class ConfirmScreen : Screen {
    override val id = ScreenId.CONFIRM
    override val title = "ᴛʀɪᴍ ᴇᴅɪᴛᴏʀ › ᴄᴏɴꜰɪʀᴍ"

    override fun buttons(context: ScreenRenderContext): List<Button> = listOf(
        Button(22, buttonItem(Material.LIME_STAINED_GLASS_PANE, "➜ ${smallCaps("apply")}")) {
            gui.tell(player, gui.messageFor(gui.applySelection(player, session)))
            gui.close(player)
        },
        Button(45, buttonItem(Material.ARROW, "« ${smallCaps("back")}")) {
            gui.show(player, session, ScreenId.MATERIALS)
        },
        Button(49, buttonItem(Material.RED_STAINED_GLASS_PANE, "« ${smallCaps("cancel")}")) {
            gui.close(player)
        },
    )
}
