package org.jan1k.plugin.trimeditor.command

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.jan1k.plugin.trimeditor.TrimEditorPlugin
import org.jan1k.plugin.trimeditor.cost.RequirementService
import org.jan1k.plugin.trimeditor.cost.TrimRequirementSettings
import org.jan1k.plugin.trimeditor.gui.EditorGui
import org.jan1k.plugin.trimeditor.item.ArmorTrimEditor
import org.jan1k.plugin.trimeditor.session.SessionManager
import java.util.logging.Level

class TrimEditorCommand(
    private val plugin: TrimEditorPlugin,
    private val sessions: SessionManager,
    private val gui: EditorGui,
) : CommandExecutor, TabCompleter {
    private val mini = MiniMessage.miniMessage()
    private val requirements = RequirementService()

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (args.firstOrNull()?.equals("reload", ignoreCase = true) == true) {
            return reload(sender)
        }

        if (sender !is Player) {
            send(sender, plugin.lang.message("errors.player-only"))
            return true
        }

        if (!sender.hasPermission("trimeditor.use")) {
            send(sender, plugin.lang.message("errors.no-permission"))
            return true
        }

        if (!ArmorTrimEditor.isEditableArmor(sender.inventory.itemInMainHand)) {
            send(sender, plugin.lang.message("errors.not-armor"))
            return true
        }

        val precheck = requirements.precheck(sender.inventory, requirementSettings(sender))
        if (!precheck.canOpen) {
            send(sender, plugin.lang.message("errors.requirements"))
            return true
        }

        if (!gui.open(sender)) {
            send(sender, plugin.lang.message("errors.not-armor"))
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

    private fun requirementSettings(player: Player): TrimRequirementSettings {
        return if (player.hasPermission("trimeditor.bypass.cost")) {
            TrimRequirementSettings()
        } else {
            plugin.pluginConfig.requirementSettings()
        }
    }
}
