package org.jan1k.plugin.trimeditor.gui

import org.bukkit.Material
import org.bukkit.entity.Player
import org.jan1k.plugin.trimeditor.trim.TrimMaterialOption

class MaterialScreen : Screen {
    override val id = ScreenId.MATERIALS
    override val title = "ᴛʀɪᴍ ᴇᴅɪᴛᴏʀ › ᴍᴀᴛᴇʀɪᴀʟѕ"
    override val size = 18

    override fun buttons(context: ScreenRenderContext): List<Button> {
        val settings = context.config.requirementSettings()
        val options = context.requirements.visibleMaterials(context.player.inventory, settings)
            .filter { context.player.canUse(it) }

        val back = Button(17, actionButtonItem(Material.ARROW, "« ʙᴀᴄᴋ")) {
            gui.show(player, session, ScreenId.PATTERNS)
        }

        return options.zip(optionSlots)
            .map { (option, slot) -> material(slot, option) } + back
    }

    private fun material(slot: Int, option: TrimMaterialOption): Button =
        Button(slot, buttonItem(option.ingredientItem)) {
            session.material = option.material
            gui.show(player, session, ScreenId.CONFIRM)
        }

    private fun Player.canUse(option: TrimMaterialOption): Boolean {
        val key = option.material.key().value()
        return hasPermission("trimeditor.material.*") || hasPermission("trimeditor.material.$key")
    }

    companion object {
        private val optionSlots = listOf(2, 3, 4, 5, 6, 11, 12, 13, 14, 15)
    }
}
