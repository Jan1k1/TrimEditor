package org.jan1k.plugin.trimeditor.gui

import org.bukkit.Material
import org.jan1k.plugin.trimeditor.session.EditSession

class MaterialScreen : Screen {
    override val id = ScreenId.MATERIALS
    override val title = "TrimEditor · Materials"

    override fun buttons(session: EditSession): List<Button> = listOf(
        Button(45, buttonItem(Material.ARROW, "Back")) {
            gui.show(player, session, ScreenId.PATTERNS)
        },
        material(10, "quartz", "Quartz", Material.QUARTZ),
        material(12, "diamond", "Diamond", Material.DIAMOND),
        material(14, "emerald", "Emerald", Material.EMERALD),
        material(16, "amethyst", "Amethyst", Material.AMETHYST_SHARD),
        Button(49, buttonItem(Material.BARRIER, "Close")) {
            gui.close(player)
        },
    )

    private fun material(slot: Int, key: String, label: String, icon: Material): Button =
        Button(slot, buttonItem(icon, label)) {
            session.material = key
            gui.show(player, session, ScreenId.CONFIRM)
        }
}
