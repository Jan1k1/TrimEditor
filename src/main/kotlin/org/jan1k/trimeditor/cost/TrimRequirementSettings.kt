package org.jan1k.trimeditor.cost

data class TrimRequirementSettings(
    val enabled: Boolean = false,
    val requireTemplate: Boolean = false,
    val requireMaterial: Boolean = false,
    val consumeTemplate: Boolean = true,
    val consumeMaterial: Boolean = true,
) {
    fun needsTemplate(): Boolean {
        return enabled && requireTemplate
    }

    fun needsMaterial(): Boolean {
        return enabled && requireMaterial
    }
}

data class RequirementPrecheck(
    val canOpen: Boolean,
    val missingTemplate: Boolean,
    val missingMaterial: Boolean,
)
