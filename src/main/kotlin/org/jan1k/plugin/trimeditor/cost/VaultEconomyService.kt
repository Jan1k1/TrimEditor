package org.jan1k.plugin.trimeditor.cost

import net.milkbowl.vault.economy.Economy
import org.bukkit.entity.Player

class VaultEconomyService(
    private val economy: Economy,
) : EconomyService {
    override fun canPay(player: Player, amount: Double): Boolean {
        return amount <= 0.0 || economy.has(player, amount)
    }

    override fun withdraw(player: Player, amount: Double): Boolean {
        return amount <= 0.0 || economy.withdrawPlayer(player, amount).transactionSuccess()
    }
}
