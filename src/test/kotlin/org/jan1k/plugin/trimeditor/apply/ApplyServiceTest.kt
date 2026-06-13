package org.jan1k.plugin.trimeditor.apply

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ArmorMeta
import org.bukkit.inventory.meta.trim.ArmorTrim
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import org.jan1k.plugin.trimeditor.cost.EconomyService
import org.jan1k.plugin.trimeditor.cost.RequirementService
import org.jan1k.plugin.trimeditor.cost.TrimRequirementSettings
import org.jan1k.plugin.trimeditor.item.ItemFingerprint
import org.mockbukkit.mockbukkit.MockBukkit
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplyServiceTest {
    private val trim = ArmorTrim(TrimMaterial.DIAMOND, TrimPattern.SENTRY)
    private lateinit var economy: RecordingEconomy
    private lateinit var service: ApplyService

    @BeforeTest
    fun setUp() {
        MockBukkit.mock()
        economy = RecordingEconomy()
        service = ApplyService(RequirementService(), economy)
    }

    @AfterTest
    fun tearDown() {
        MockBukkit.unmock()
    }

    @Test
    fun `costs are not consumed when held item fingerprint changed`() {
        val player = MockBukkit.getMock()!!.addPlayer()
        val original = ItemStack(Material.DIAMOND_CHESTPLATE)
        player.inventory.heldItemSlot = 0
        player.inventory.setItem(0, original)
        val fingerprint = ItemFingerprint.of(original)
        player.inventory.setItem(0, ItemStack(Material.IRON_CHESTPLATE))
        player.inventory.addItem(
            ItemStack(Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE),
            ItemStack(Material.DIAMOND),
        )

        val result = service.apply(
            player,
            TrimApplyRequest(
                handSlot = 0,
                fingerprint = fingerprint,
                trim = trim,
                requirements = TrimRequirementSettings(enabled = true),
                moneyCost = 5.0,
            ),
        )

        assertEquals(TrimApplyResult.HeldItemChanged, result)
        assertEquals(1, player.inventory.count(Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE))
        assertEquals(1, player.inventory.count(Material.DIAMOND))
        assertEquals(0, economy.withdraws)
    }

    @Test
    fun `costs are consumed after held item validation passes`() {
        val player = MockBukkit.getMock()!!.addPlayer()
        val armor = ItemStack(Material.DIAMOND_CHESTPLATE)
        player.inventory.heldItemSlot = 0
        player.inventory.setItem(0, armor)
        player.inventory.addItem(
            ItemStack(Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE),
            ItemStack(Material.DIAMOND),
        )

        val result = service.apply(
            player,
            TrimApplyRequest(
                handSlot = 0,
                fingerprint = ItemFingerprint.of(armor),
                trim = trim,
                requirements = TrimRequirementSettings(enabled = true),
                moneyCost = 5.0,
            ),
        )

        assertEquals(TrimApplyResult.Applied, result)
        assertEquals(0, player.inventory.count(Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE))
        assertEquals(0, player.inventory.count(Material.DIAMOND))
        assertEquals(1, economy.withdraws)
        assertEquals(trim, (player.inventory.getItem(0)!!.itemMeta as ArmorMeta).trim)
    }

    private fun Inventory.count(type: Material): Int {
        return contents.filterNotNull().filter { it.type == type }.sumOf { it.amount }
    }

    private class RecordingEconomy : EconomyService {
        var withdraws = 0

        override fun canPay(player: Player, amount: Double): Boolean {
            return true
        }

        override fun withdraw(player: Player, amount: Double): Boolean {
            withdraws++
            return true
        }
    }
}
