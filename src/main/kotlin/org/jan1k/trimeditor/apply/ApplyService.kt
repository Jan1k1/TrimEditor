package org.jan1k.trimeditor.apply

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.jan1k.trimeditor.cost.EconomyService
import org.jan1k.trimeditor.cost.NoEconomyService
import org.jan1k.trimeditor.cost.RequirementService
import org.jan1k.trimeditor.cost.TrimRequirementSettings
import org.jan1k.trimeditor.item.ArmorTrimEditor
import org.jan1k.trimeditor.item.ItemFingerprint

data class TrimApplyRequest(
    val handSlot: Int,
    val fingerprint: ItemFingerprint,
    val trim: ArmorTrim,
    val requirements: TrimRequirementSettings = TrimRequirementSettings(),
    val moneyCost: Double = 0.0,
)

enum class TrimApplyResult {
    Applied,
    HeldItemChanged,
    NotTrimmableArmor,
    MissingRequirements,
    InsufficientFunds,
}

class ApplyService(
    private val requirements: RequirementService = RequirementService(),
    private val economy: EconomyService = NoEconomyService,
) {
    fun apply(player: Player, request: TrimApplyRequest): TrimApplyResult {
        val inventory = player.inventory

        if (inventory.heldItemSlot != request.handSlot || request.handSlot !in 0 until inventory.size) {
            return TrimApplyResult.HeldItemChanged
        }

        val held = inventory.getItem(request.handSlot)

        if (held == null || held.type == Material.AIR || ItemFingerprint.of(held) != request.fingerprint) {
            return TrimApplyResult.HeldItemChanged
        }

        if (!ArmorTrimEditor.isEditableArmor(held)) {
            return TrimApplyResult.NotTrimmableArmor
        }

        if (!requirements.hasSelection(inventory, request.requirements, request.trim.pattern, request.trim.material)) {
            return TrimApplyResult.MissingRequirements
        }

        if (!economy.canPay(player, request.moneyCost) || !economy.withdraw(player, request.moneyCost)) {
            return TrimApplyResult.InsufficientFunds
        }

        if (!requirements.consumeSelection(inventory, request.requirements, request.trim.pattern, request.trim.material)) {
            return TrimApplyResult.MissingRequirements
        }

        inventory.setItem(request.handSlot, ArmorTrimEditor.applyTrim(held, request.trim))
        return TrimApplyResult.Applied
    }
}
