package org.jan1k.plugin.trimeditor.gui

import org.bukkit.entity.Player
import org.jan1k.plugin.trimeditor.config.PluginConfig
import org.jan1k.plugin.trimeditor.cost.RequirementService
import org.jan1k.plugin.trimeditor.session.EditSession

enum class ScreenId {
    PATTERNS,
    MATERIALS,
    CONFIRM,
}

interface Screen {
    val id: ScreenId
    val title: String

    fun buttons(context: ScreenRenderContext): List<Button>
}

class ScreenRenderContext(
    val player: Player,
    val session: EditSession,
    val config: PluginConfig,
    val requirements: RequirementService,
)
