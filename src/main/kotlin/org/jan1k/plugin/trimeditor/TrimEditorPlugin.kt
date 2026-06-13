package org.jan1k.plugin.trimeditor

import org.jan1k.plugin.trimeditor.command.TrimEditorCommand
import org.jan1k.plugin.trimeditor.gui.ClickGuard
import org.jan1k.plugin.trimeditor.gui.EditorGui
import org.jan1k.plugin.trimeditor.gui.EditorListener
import org.jan1k.plugin.trimeditor.lang.Lang
import org.jan1k.plugin.trimeditor.session.SessionManager
import org.bukkit.plugin.java.JavaPlugin

open class TrimEditorPlugin : JavaPlugin() {
    lateinit var sessionManager: SessionManager
        private set

    lateinit var lang: Lang
        private set

    private lateinit var gui: EditorGui

    override fun onEnable() {
        saveDefaultConfig()
        lang = Lang.load()
        sessionManager = SessionManager()
        gui = EditorGui(sessionManager, clickCooldownMillis())

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

    fun clickCooldownMillis(): Long {
        val nested = config.getLong("gui.cooldown-ms", Long.MIN_VALUE)
        if (nested != Long.MIN_VALUE) return nested.coerceAtLeast(0)
        return config.getLong("click-cooldown-ms", ClickGuard.DEFAULT_COOLDOWN_MILLIS).coerceAtLeast(0)
    }
}
