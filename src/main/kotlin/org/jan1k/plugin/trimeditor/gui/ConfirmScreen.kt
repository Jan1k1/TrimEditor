package org.jan1k.plugin.trimeditor.gui

import org.bukkit.Material
import org.jan1k.plugin.trimeditor.session.EditSession

class ConfirmScreen : Screen {
    override val id = ScreenId.CONFIRM
    override val title = "TrimEditor · Confirm"

    override fun buttons(session: EditSession): List<Button> = listOf(
        Button(22, buttonItem(Material.LIME_STAINED_GLASS_PANE, "Apply")) {
            player.sendMessage("Trim selected: ${session.pattern ?: "none"} / ${session.material ?: "none"}")
            gui.close(player)
        },
        Button(45, buttonItem(Material.ARROW, "Back")) {
            gui.show(player, session, ScreenId.MATERIALS)
        },
        Button(49, buttonItem(Material.RED_STAINED_GLASS_PANE, "Cancel")) {
            gui.close(player)
        },
    )
}
