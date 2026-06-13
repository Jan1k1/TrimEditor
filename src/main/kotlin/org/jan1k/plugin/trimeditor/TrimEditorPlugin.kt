package org.jan1k.plugin.trimeditor

import org.bukkit.plugin.java.JavaPlugin

open class TrimEditorPlugin : JavaPlugin() {
    override fun onEnable() {
        saveDefaultConfig()
    }
}
