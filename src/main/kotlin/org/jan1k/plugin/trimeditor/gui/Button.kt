package org.jan1k.plugin.trimeditor.gui

import net.kyori.adventure.text.Component
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

fun buttonItem(material: Material, name: String): ItemStack {
    val stack = ItemStack(material)
    val meta = stack.itemMeta
    if (meta != null) {
        meta.displayName(Component.text(name))
        stack.itemMeta = meta
    }
    return stack
}

fun smallCaps(value: String): String {
    val letters = mapOf(
        'a' to 'ᴀ',
        'b' to 'ʙ',
        'c' to 'ᴄ',
        'd' to 'ᴅ',
        'e' to 'ᴇ',
        'f' to 'ꜰ',
        'g' to 'ɢ',
        'h' to 'ʜ',
        'i' to 'ɪ',
        'j' to 'ᴊ',
        'k' to 'ᴋ',
        'l' to 'ʟ',
        'm' to 'ᴍ',
        'n' to 'ɴ',
        'o' to 'ᴏ',
        'p' to 'ᴘ',
        'q' to 'ǫ',
        'r' to 'ʀ',
        's' to 'ѕ',
        't' to 'ᴛ',
        'u' to 'ᴜ',
        'v' to 'ᴠ',
        'w' to 'ᴡ',
        'x' to 'х',
        'y' to 'ʏ',
        'z' to 'ᴢ',
    )
    return value.lowercase().map { letters[it] ?: it }.joinToString("")
}
