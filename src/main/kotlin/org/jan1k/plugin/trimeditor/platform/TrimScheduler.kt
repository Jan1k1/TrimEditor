package org.jan1k.plugin.trimeditor.platform

import org.bukkit.plugin.java.JavaPlugin
import java.util.function.Consumer

fun interface TrimScheduler {
    fun run(task: () -> Unit)
}

class FoliaAwareScheduler(private val plugin: JavaPlugin) : TrimScheduler {
    override fun run(task: () -> Unit) {
        val globalScheduler = runCatching {
            plugin.server.javaClass.getMethod("getGlobalRegionScheduler").invoke(plugin.server)
        }.getOrNull()

        val runMethod = globalScheduler?.javaClass?.methods?.firstOrNull {
            it.name == "run" && it.parameterCount == 2
        }

        if (globalScheduler != null && runMethod != null) {
            runMethod.invoke(globalScheduler, plugin, Consumer<Any> { task() })
            return
        }

        plugin.server.scheduler.runTask(plugin, Runnable { task() })
    }
}
