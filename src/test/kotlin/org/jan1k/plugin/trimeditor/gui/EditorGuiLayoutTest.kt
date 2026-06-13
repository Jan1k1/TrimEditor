package org.jan1k.plugin.trimeditor.gui

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ArmorMeta
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import org.jan1k.plugin.trimeditor.config.EconomyConfig
import org.jan1k.plugin.trimeditor.config.GuiConfig
import org.jan1k.plugin.trimeditor.config.PluginConfig
import org.jan1k.plugin.trimeditor.config.RequirementsConfig
import org.jan1k.plugin.trimeditor.cost.RequirementService
import org.jan1k.plugin.trimeditor.item.ItemFingerprint
import org.jan1k.plugin.trimeditor.session.EditSession
import org.mockbukkit.mockbukkit.MockBukkit
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class EditorGuiLayoutTest {
    @BeforeTest
    fun setUp() {
        MockBukkit.mock()
    }

    @AfterTest
    fun tearDown() {
        MockBukkit.unmock()
    }

    @Test
    fun `screens use compact inventory sizes`() {
        assertEquals(27, PatternScreen().size)
        assertEquals(27, MaterialScreen().size)
        assertEquals(9, ConfirmScreen().size)
    }

    @Test
    fun `button icons keep vanilla item names`() {
        val item = buttonItem(Material.ARROW)

        assertFalse(item.itemMeta.hasDisplayName())
    }

    @Test
    fun `confirm screen has preview apply and back only`() {
        val player = MockBukkit.getMock()!!.addPlayer()
        val armor = ItemStack(Material.DIAMOND_CHESTPLATE)
        player.inventory.heldItemSlot = 0
        player.inventory.setItem(0, armor)
        val session = EditSession(player.uniqueId, 0, ItemFingerprint.of(armor)) {}
        session.pattern = TrimPattern.SENTRY
        session.material = TrimMaterial.DIAMOND
        val context = ScreenRenderContext(player, session, config(), RequirementService())

        val buttons = ConfirmScreen().buttons(context)
        val preview = buttons.firstOrNull { it.slot == 3 }?.icon

        assertEquals(listOf(3, 4, 5), buttons.map { it.slot })
        assertNotNull(preview)
        assertFalse(preview.itemMeta.hasDisplayName())
        val meta = preview.itemMeta as ArmorMeta
        assertEquals(TrimPattern.SENTRY, meta.trim!!.pattern)
        assertEquals(TrimMaterial.DIAMOND, meta.trim!!.material)
    }

    @Test
    fun `material screen keeps back button separate from materials`() {
        val player = MockBukkit.getMock()!!.addPlayer()
        player.isOp = true
        val session = EditSession(player.uniqueId, 0, ItemFingerprint.of(ItemStack(Material.DIAMOND_CHESTPLATE))) {}
        val context = ScreenRenderContext(player, session, config(), RequirementService())

        val buttons = MaterialScreen().buttons(context)

        assertEquals(buttons.map { it.slot }.toSet().size, buttons.size)
        assertEquals(Material.ARROW, buttons.first { it.slot == 22 }.icon.type)
    }

    @Test
    fun `pattern and material icons keep vanilla item names`() {
        val player = MockBukkit.getMock()!!.addPlayer()
        player.isOp = true
        val session = EditSession(player.uniqueId, 0, ItemFingerprint.of(ItemStack(Material.DIAMOND_CHESTPLATE))) {}
        val context = ScreenRenderContext(player, session, config(), RequirementService())
        val icons = PatternScreen().buttons(context).map { it.icon } + MaterialScreen().buttons(context).map { it.icon }

        assertTrue(icons.isNotEmpty())
        assertTrue(icons.all { !it.itemMeta.hasDisplayName() })
    }

    private fun config(): PluginConfig {
        return PluginConfig(
            configVersion = 2,
            requirements = RequirementsConfig(
                enabled = false,
                requireTemplate = false,
                requireMaterial = false,
                consumeTemplate = true,
                consumeMaterial = true,
            ),
            economy = EconomyConfig(enabled = false, cost = 0.0),
            gui = GuiConfig(cooldownMillis = 0),
            sounds = emptyMap(),
        )
    }
}
