package org.jan1k.plugin.trimeditor.gui

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.jan1k.plugin.trimeditor.session.EditSession

class GuiContext(
    val player: Player,
    val session: EditSession,
    val gui: EditorGui,
)

class Button(
    val slot: Int,
    val icon: ItemStack,
    val action: GuiContext.() -> Unit,
)

fun buttonItem(material: Material): ItemStack = ItemStack(material)

fun actionButtonItem(material: Material, name: String): ItemStack {
    val item = ItemStack(material)
    val meta = item.itemMeta
    meta.displayName(
        Component.text(name, pink)
            .decoration(TextDecoration.ITALIC, false)
    )
    item.itemMeta = meta
    return item
}

private val pink = TextColor.color(0xff4fd8)
