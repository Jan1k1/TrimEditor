package org.jan1k.trimeditor.command

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.jan1k.trimeditor.TrimEditorPlugin
import org.jan1k.trimeditor.api.TrimEditorOpenResult
import org.jan1k.trimeditor.api.TrimEditorProvider
import org.jan1k.trimeditor.gui.EditorGui
import org.jan1k.trimeditor.session.SessionManager
import java.util.logging.Level

class TrimEditorCommand(
    private val plugin: TrimEditorPlugin,
    private val sessions: SessionManager,
    private val gui: EditorGui,
) : CommandExecutor, TabCompleter {
    private val mini = MiniMessage.miniMessage()

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.firstOrNull()?.equals("reload", ignoreCase = true) == true) {
            return reload(sender)
        }

        if (sender !is Player) {
            send(sender, plugin.lang.message("errors.player-only"))
            return true
        }

        when (TrimEditorProvider.require().openEditor(sender)) {
            TrimEditorOpenResult.OPENED -> Unit
            TrimEditorOpenResult.NO_PERMISSION -> send(sender, plugin.lang.message("errors.no-permission"))
            TrimEditorOpenResult.NOT_ARMOR -> send(sender, plugin.lang.message("errors.not-armor"))
            TrimEditorOpenResult.MISSING_REQUIREMENTS -> send(sender, plugin.lang.message("errors.requirements"))
        }
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
            send(sender, plugin.lang.message("errors.no-permission"))
            return true
        }

        val started = System.nanoTime()
        return try {
            sessions.closeAll()
            plugin.reloadSettings()
            plugin.reloadLang()
            gui.updateCooldownMillis(plugin.clickCooldownMillis())

            val elapsed = (System.nanoTime() - started) / 1_000_000
            send(sender, plugin.lang.message("admin.reload", "time" to elapsed.toString()))
            true
        } catch (ex: Exception) {
            plugin.logger.log(Level.SEVERE, "TrimEditor reload failed", ex)
            send(sender, plugin.lang.message("admin.reload-failed"))
            true
        }
    }

    private fun send(sender: CommandSender, raw: String) {
        sender.sendMessage(mini.deserialize(raw))
    }
}
