package org.jan1k.trimeditor

import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.ServicePriority
import org.bstats.bukkit.Metrics
import org.jan1k.trimeditor.api.TrimEditorApi
import org.jan1k.trimeditor.api.TrimEditorApiService
import org.jan1k.trimeditor.command.TrimEditorCommand
import org.jan1k.trimeditor.config.ConfigLoader
import org.jan1k.trimeditor.config.PluginConfig
import org.jan1k.trimeditor.cost.ReflectiveVaultEconomyService
import org.jan1k.trimeditor.gui.ClickGuard
import org.jan1k.trimeditor.gui.EditorGui
import org.jan1k.trimeditor.gui.EditorListener
import org.jan1k.trimeditor.lang.Lang
import org.jan1k.trimeditor.session.SessionManager

open class TrimEditorPlugin : JavaPlugin() {
    lateinit var sessionManager: SessionManager
        private set

    lateinit var lang: Lang
        private set

    lateinit var pluginConfig: PluginConfig
        private set

    private lateinit var gui: EditorGui
    private lateinit var api: TrimEditorApi

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
        api = TrimEditorApiService(this, gui)
        server.servicesManager.register(TrimEditorApi::class.java, api, this, ServicePriority.Normal)

        val command = TrimEditorCommand(this, sessionManager, gui)
        getCommand("trimeditor")?.setExecutor(command)
        getCommand("trimeditor")?.tabCompleter = command
        server.pluginManager.registerEvents(EditorListener(gui), this)
        startMetrics()
    }

    override fun onDisable() {
        server.servicesManager.unregisterAll(this)

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

    private fun startMetrics() {
        if (Metrics::class.java.name.startsWith(unrelocatedBstatsPackage())) {
            return
        }

        Metrics(this, BSTATS_PLUGIN_ID)
    }

    private fun unrelocatedBstatsPackage(): String {
        return arrayOf("org", "bstats").joinToString(".") + "."
    }

    companion object {
        const val BSTATS_PLUGIN_ID = 31971
    }
}
