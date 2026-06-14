package org.jan1k.trimeditor.api

import org.bukkit.Bukkit

object TrimEditorProvider {
    fun get(): TrimEditorApi? {
        return Bukkit.getServicesManager().load(TrimEditorApi::class.java)
    }

    fun require(): TrimEditorApi {
        return get() ?: error("TrimEditor API is not registered")
    }
}
