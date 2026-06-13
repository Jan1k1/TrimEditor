package org.jan1k.plugin.trimeditor.gui

import org.bukkit.Material
import org.bukkit.entity.Player
import org.jan1k.plugin.trimeditor.trim.TrimPatternOption

class PatternScreen : Screen {
    override val id = ScreenId.PATTERNS
    override val title = "ᴛʀɪᴍ ᴇᴅɪᴛᴏʀ › ᴘᴀᴛᴛᴇʀɴѕ"

    override fun buttons(context: ScreenRenderContext): List<Button> {
        val settings = context.config.requirementSettings()
        val options = context.requirements.visiblePatterns(context.player.inventory, settings)
            .filter { context.player.canUse(it) }

        val close = Button(49, buttonItem(Material.BARRIER, "« ${smallCaps("close")}")) {
            gui.close(player)
        }

        return options.zip(optionSlots)
            .map { (option, slot) -> pattern(slot, option) } +
            close
    }

    private fun pattern(slot: Int, option: TrimPatternOption): Button =
        Button(slot, buttonItem(option.templateItem, smallCaps(option.pattern.key().value()))) {
            session.pattern = option.pattern
            gui.show(player, session, ScreenId.MATERIALS)
        }

    private fun Player.canUse(option: TrimPatternOption): Boolean {
        val key = option.pattern.key().value()
        return hasPermission("trimeditor.pattern.*") || hasPermission("trimeditor.pattern.$key")
    }

    companion object {
        private val optionSlots = listOf(
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43,
        )
    }
}
