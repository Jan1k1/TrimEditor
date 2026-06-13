package org.jan1k.plugin.trimeditor.cost

import org.bukkit.entity.Player

interface EconomyService {
    fun canPay(player: Player, amount: Double): Boolean

    fun withdraw(player: Player, amount: Double): Boolean
}

object NoEconomyService : EconomyService {
    override fun canPay(player: Player, amount: Double): Boolean {
        return amount <= 0.0
    }

    override fun withdraw(player: Player, amount: Double): Boolean {
        return amount <= 0.0
    }
}
