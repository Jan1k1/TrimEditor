package org.jan1k.plugin.trimeditor.gui

import org.bukkit.Material
import org.bukkit.entity.Player
import org.jan1k.plugin.trimeditor.trim.TrimMaterialOption

class MaterialScreen : Screen {
    override val id = ScreenId.MATERIALS
    override val title = "ᴛʀɪᴍ ᴇᴅɪᴛᴏʀ › ᴍᴀᴛᴇʀɪᴀʟѕ"

    override fun buttons(context: ScreenRenderContext): List<Button> {
        val settings = context.config.requirementSettings()
        val options = context.requirements.visibleMaterials(context.player.inventory, settings)
            .filter { context.player.canUse(it) }

        val back = Button(45, buttonItem(Material.ARROW, "« ${smallCaps("back")}")) {
            gui.show(player, session, ScreenId.PATTERNS)
        }
        val close = Button(49, buttonItem(Material.BARRIER, "« ${smallCaps("close")}")) {
            gui.close(player)
        }

        return listOf(back) + options.zip(optionSlots)
            .map { (option, slot) -> material(slot, option) } +
            close
    }

    private fun material(slot: Int, option: TrimMaterialOption): Button =
        Button(slot, buttonItem(option.ingredientItem, smallCaps(option.material.key().value()))) {
            session.material = option.material
            gui.show(player, session, ScreenId.CONFIRM)
        }

    private fun Player.canUse(option: TrimMaterialOption): Boolean {
        val key = option.material.key().value()
        return hasPermission("trimeditor.material.*") || hasPermission("trimeditor.material.$key")
    }

    companion object {
        private val optionSlots = listOf(10, 11, 12, 13, 14, 15, 16, 21, 22, 23)
    }
}
