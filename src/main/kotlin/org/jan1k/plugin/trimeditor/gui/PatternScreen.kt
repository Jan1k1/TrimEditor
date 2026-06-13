package org.jan1k.plugin.trimeditor.gui

import org.bukkit.Material
import org.jan1k.plugin.trimeditor.session.EditSession

class PatternScreen : Screen {
    override val id = ScreenId.PATTERNS
    override val title = "TrimEditor · Patterns"

    override fun buttons(session: EditSession): List<Button> = listOf(
        pattern(10, "sentry", "Sentry", Material.PAPER),
        pattern(12, "dune", "Dune", Material.MAP),
        pattern(14, "coast", "Coast", Material.PRISMARINE_SHARD),
        pattern(16, "ward", "Ward", Material.ECHO_SHARD),
        Button(49, buttonItem(Material.BARRIER, "Close")) {
            gui.close(player)
        },
    )

    private fun pattern(slot: Int, key: String, label: String, icon: Material): Button =
        Button(slot, buttonItem(icon, label)) {
            session.pattern = key
            gui.show(player, session, ScreenId.MATERIALS)
        }
}
