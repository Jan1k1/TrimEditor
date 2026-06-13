package org.jan1k.plugin.trimeditor.config

data class PluginConfig(
    val configVersion: Int,
    val requirements: RequirementsConfig,
    val gui: GuiConfig,
    val sounds: Map<SoundAction, SoundConfig>,
)

data class RequirementsConfig(
    val enabled: Boolean,
)

data class GuiConfig(
    val cooldownMillis: Long,
)

enum class SoundAction(val key: String) {
    OPEN("open"),
    SELECT_PATTERN("select-pattern"),
    SELECT_MATERIAL("select-material"),
    APPLY("apply"),
    PAGE("page"),
    CLOSE("close"),
    DENY("deny"),
}

data class SoundConfig(
    val enabled: Boolean,
    val sound: String,
    val volume: Float,
    val pitch: Float,
)
