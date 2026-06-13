package org.jan1k.plugin.trimeditor.config

import org.jan1k.plugin.trimeditor.cost.TrimRequirementSettings

data class PluginConfig(
    val configVersion: Int,
    val requirements: RequirementsConfig,
    val economy: EconomyConfig,
    val gui: GuiConfig,
    val sounds: Map<SoundAction, SoundConfig>,
) {
    fun requirementSettings(): TrimRequirementSettings {
        return TrimRequirementSettings(
            enabled = requirements.enabled,
            requireTemplate = requirements.requireTemplate,
            requireMaterial = requirements.requireMaterial,
            consumeTemplate = requirements.consumeTemplate,
            consumeMaterial = requirements.consumeMaterial,
        )
    }

    fun moneyCost(): Double {
        return if (economy.enabled) economy.cost.coerceAtLeast(0.0) else 0.0
    }
}

data class RequirementsConfig(
    val enabled: Boolean,
    val requireTemplate: Boolean,
    val requireMaterial: Boolean,
    val consumeTemplate: Boolean,
    val consumeMaterial: Boolean,
)

data class EconomyConfig(
    val enabled: Boolean,
    val cost: Double,
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
