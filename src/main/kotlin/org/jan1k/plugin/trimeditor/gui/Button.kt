package org.jan1k.plugin.trimeditor.gui

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
