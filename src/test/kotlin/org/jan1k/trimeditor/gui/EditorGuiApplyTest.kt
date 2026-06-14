package org.jan1k.trimeditor.gui

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ArmorMeta
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import org.jan1k.trimeditor.apply.TrimApplyResult
import org.jan1k.trimeditor.config.EconomyConfig
import org.jan1k.trimeditor.config.GuiConfig
import org.jan1k.trimeditor.config.PluginConfig
import org.jan1k.trimeditor.config.RequirementsConfig
import org.jan1k.trimeditor.item.ItemFingerprint
import org.jan1k.trimeditor.session.EditSession
import org.jan1k.trimeditor.session.SessionManager
import org.mockbukkit.mockbukkit.MockBukkit
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class EditorGuiApplyTest {
    @BeforeTest
    fun setUp() {
        MockBukkit.mock()
    }

    @AfterTest
    fun tearDown() {
        MockBukkit.unmock()
    }

    @Test
    fun `apply selection trims held armor`() {
        val player = MockBukkit.getMock()!!.addPlayer()
        val armor = ItemStack(Material.DIAMOND_CHESTPLATE)
        player.inventory.heldItemSlot = 0
        player.inventory.setItem(0, armor)
        player.inventory.addItem(
            ItemStack(Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE),
            ItemStack(Material.DIAMOND),
        )
        val session = EditSession(player.uniqueId, 0, ItemFingerprint.of(armor)) {}
        session.pattern = TrimPattern.SENTRY
        session.material = TrimMaterial.DIAMOND
        val gui = EditorGui(SessionManager(), 0, ::config)

        val result = gui.applySelection(player, session)

        assertEquals(TrimApplyResult.Applied, result)
        val meta = player.inventory.getItem(0)!!.itemMeta as ArmorMeta
        assertEquals(TrimPattern.SENTRY, meta.trim!!.pattern)
        assertEquals(TrimMaterial.DIAMOND, meta.trim!!.material)
    }

    private fun config(): PluginConfig {
        return PluginConfig(
            configVersion = 2,
            requirements = RequirementsConfig(
                enabled = true,
                requireTemplate = true,
                requireMaterial = true,
                consumeTemplate = true,
                consumeMaterial = true,
            ),
            economy = EconomyConfig(enabled = false, cost = 0.0),
            gui = GuiConfig(cooldownMillis = 0),
            sounds = emptyMap(),
        )
    }
}
