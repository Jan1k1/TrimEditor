package org.jan1k.plugin.trimeditor.cost

data class TrimRequirementSettings(
    val enabled: Boolean = false,
    val consumeTemplate: Boolean = true,
    val consumeMaterial: Boolean = true,
)

data class RequirementPrecheck(
    val canOpen: Boolean,
    val missingTemplate: Boolean,
    val missingMaterial: Boolean,
)
