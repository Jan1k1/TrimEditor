package org.jan1k.trimeditor.gui

import org.bukkit.entity.Player
import org.jan1k.trimeditor.trim.TrimPatternOption

class PatternScreen : Screen {
    override val id = ScreenId.PATTERNS
    override val title = "ᴛʀɪᴍ ᴇᴅɪᴛᴏʀ › ᴘᴀᴛᴛᴇʀɴѕ"
    override val size = 18

    override fun buttons(context: ScreenRenderContext): List<Button> {
        val settings = context.config.requirementSettings()
        val options = context.requirements.visiblePatterns(context.player.inventory, settings)
            .filter { context.player.canUse(it) }

        return options.zip(optionSlots)
            .map { (option, slot) -> pattern(slot, option) }
    }

    private fun pattern(slot: Int, option: TrimPatternOption): Button =
        Button(slot, buttonItem(option.templateItem)) {
            session.pattern = option.pattern
            gui.show(player, session, ScreenId.MATERIALS)
        }

    private fun Player.canUse(option: TrimPatternOption): Boolean {
        val key = option.pattern.key().value()
        return hasPermission("trimeditor.pattern.*") || hasPermission("trimeditor.pattern.$key")
    }

    companion object {
        private val optionSlots = (0 until 18).toList()
    }
}
