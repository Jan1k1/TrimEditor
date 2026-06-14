package org.jan1k.trimeditor.cost

import org.bukkit.OfflinePlayer
import org.bukkit.Server
import org.bukkit.entity.Player

object ReflectiveVaultEconomyService {
    fun resolve(server: Server): EconomyService {
        if (server.pluginManager.getPlugin("Vault") == null) {
            return NoEconomyService
        }

        return runCatching {
            val economyClass = Class.forName("net.milkbowl.vault.economy.Economy")
            val getRegistration = server.servicesManager.javaClass.getMethod("getRegistration", Class::class.java)
            val registration = getRegistration.invoke(server.servicesManager, economyClass) ?: return NoEconomyService
            val provider = registration.javaClass.getMethod("getProvider").invoke(registration)
            ReflectionEconomy(provider)
        }.getOrDefault(NoEconomyService)
    }

    private class ReflectionEconomy(private val provider: Any) : EconomyService {
        private val has = provider.javaClass.getMethod("has", OfflinePlayer::class.java, java.lang.Double.TYPE)
        private val withdraw = provider.javaClass.getMethod("withdrawPlayer", OfflinePlayer::class.java, java.lang.Double.TYPE)

        override fun canPay(player: Player, amount: Double): Boolean {
            return amount <= 0.0 || has.invoke(provider, player, amount) as Boolean
        }

        override fun withdraw(player: Player, amount: Double): Boolean {
            if (amount <= 0.0) {
                return true
            }

            val response = withdraw.invoke(provider, player, amount)
            return response.javaClass.getMethod("transactionSuccess").invoke(response) as Boolean
        }
    }
}
