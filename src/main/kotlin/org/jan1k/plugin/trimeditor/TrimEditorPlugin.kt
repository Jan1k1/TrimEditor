package org.jan1k.plugin.trimeditor

import org.bukkit.plugin.java.JavaPlugin
import org.jan1k.plugin.trimeditor.command.TrimEditorCommand
import org.jan1k.plugin.trimeditor.config.ConfigLoader
import org.jan1k.plugin.trimeditor.config.PluginConfig
import org.jan1k.plugin.trimeditor.cost.ReflectiveVaultEconomyService
import org.jan1k.plugin.trimeditor.gui.ClickGuard
import org.jan1k.plugin.trimeditor.gui.EditorGui
import org.jan1k.plugin.trimeditor.gui.EditorListener
import org.jan1k.plugin.trimeditor.lang.Lang
import org.jan1k.plugin.trimeditor.session.SessionManager

open class TrimEditorPlugin : JavaPlugin() {
    lateinit var sessionManager: SessionManager
        private set

    lateinit var lang: Lang
        private set

    lateinit var pluginConfig: PluginConfig
        private set

    private lateinit var gui: EditorGui

    override fun onEnable() {
        reloadSettings()
        lang = Lang.load()
        sessionManager = SessionManager()
        gui = EditorGui(
            sessionManager,
            clickCooldownMillis(),
            { pluginConfig },
            ReflectiveVaultEconomyService.resolve(server),
            { lang },
        )

        val command = TrimEditorCommand(this, sessionManager, gui)
        getCommand("trimeditor")?.setExecutor(command)
        getCommand("trimeditor")?.tabCompleter = command
        server.pluginManager.registerEvents(EditorListener(gui), this)
    }

    override fun onDisable() {
        if (::sessionManager.isInitialized) {
            sessionManager.closeAll()
        }
    }

    fun reloadLang() {
        lang = Lang.load()
    }

    fun reloadSettings() {
        pluginConfig = ConfigLoader().load(dataFolder.toPath().resolve("config.yml")).config
        reloadConfig()
    }

    fun clickCooldownMillis(): Long {
        if (!::pluginConfig.isInitialized) {
            return ClickGuard.DEFAULT_COOLDOWN_MILLIS
        }
        return pluginConfig.gui.cooldownMillis.coerceAtLeast(0)
    }
}
