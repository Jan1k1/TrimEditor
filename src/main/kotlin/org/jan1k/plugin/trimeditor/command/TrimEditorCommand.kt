package org.jan1k.plugin.trimeditor.command

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.jan1k.plugin.trimeditor.TrimEditorPlugin
import org.jan1k.plugin.trimeditor.gui.EditorGui
import org.jan1k.plugin.trimeditor.session.SessionManager

class TrimEditorCommand(
    private val plugin: TrimEditorPlugin,
    private val sessions: SessionManager,
    private val gui: EditorGui,
) : CommandExecutor, TabCompleter {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.firstOrNull()?.equals("reload", ignoreCase = true) == true) {
            return reload(sender)
        }

        if (sender !is Player) {
            sender.sendMessage("Only players can use TrimEditor.")
            return true
        }

        if (!sender.hasPermission("trimeditor.use")) {
            sender.sendMessage("You do not have permission.")
            return true
        }

        gui.open(sender)
        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>,
    ): List<String> {
        if (args.size != 1 || !sender.hasPermission("trimeditor.reload")) return emptyList()
        return listOf("reload").filter { it.startsWith(args[0], ignoreCase = true) }
    }

    private fun reload(sender: CommandSender): Boolean {
        if (!sender.hasPermission("trimeditor.reload")) {
            sender.sendMessage("You do not have permission.")
            return true
        }

        val started = System.nanoTime()
        sessions.closeAll()
        plugin.reloadConfig()
        gui.updateCooldownMillis(plugin.clickCooldownMillis())
        val elapsed = (System.nanoTime() - started) / 1_000_000

        sender.sendMessage("TrimEditor reloaded in ${elapsed}ms.")
        return true
    }
}
