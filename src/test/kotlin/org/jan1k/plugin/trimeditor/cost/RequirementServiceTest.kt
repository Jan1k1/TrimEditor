package org.jan1k.plugin.trimeditor.cost

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import org.jan1k.plugin.trimeditor.trim.TrimMaterialOption
import org.jan1k.plugin.trimeditor.trim.TrimPatternOption
import org.mockbukkit.mockbukkit.MockBukkit
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RequirementServiceTest {
    private val patterns = listOf(
        TrimPatternOption(TrimPattern.SENTRY, Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE),
        TrimPatternOption(TrimPattern.DUNE, Material.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE),
    )
    private val materials = listOf(
        TrimMaterialOption(TrimMaterial.DIAMOND, Material.DIAMOND),
        TrimMaterialOption(TrimMaterial.EMERALD, Material.EMERALD),
    )
    private val service = RequirementService()

    @BeforeTest
    fun setUp() {
        MockBukkit.mock()
    }

    @AfterTest
    fun tearDown() {
        MockBukkit.unmock()
    }

    @Test
    fun `default free requirements expose all options and pass precheck`() {
        val player = MockBukkit.getMock()!!.addPlayer()
        val settings = TrimRequirementSettings()

        assertEquals(patterns, service.visiblePatterns(player.inventory, settings, patterns))
        assertEquals(materials, service.visibleMaterials(player.inventory, settings, materials))
        assertTrue(service.precheck(player.inventory, settings, patterns, materials).canOpen)
    }

    @Test
    fun `enabled requirements hide missing pattern and material options`() {
        val player = MockBukkit.getMock()!!.addPlayer()
        player.inventory.addItem(
            ItemStack(Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE),
            ItemStack(Material.DIAMOND),
        )
        val settings = TrimRequirementSettings(
            enabled = true,
            requireTemplate = true,
            requireMaterial = true,
        )

        assertEquals(
            listOf(TrimPattern.SENTRY),
            service.visiblePatterns(player.inventory, settings, patterns).map { it.pattern },
        )
        assertEquals(
            listOf(TrimMaterial.DIAMOND),
            service.visibleMaterials(player.inventory, settings, materials).map { it.material },
        )
    }

    @Test
    fun `enabled requirements need one template and one material before open`() {
        val player = MockBukkit.getMock()!!.addPlayer()
        val settings = TrimRequirementSettings(
            enabled = true,
            requireTemplate = true,
            requireMaterial = true,
        )

        val empty = service.precheck(player.inventory, settings, patterns, materials)
        assertFalse(empty.canOpen)
        assertTrue(empty.missingTemplate)
        assertTrue(empty.missingMaterial)

        player.inventory.addItem(ItemStack(Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE))
        val missingMaterial = service.precheck(player.inventory, settings, patterns, materials)
        assertFalse(missingMaterial.canOpen)
        assertFalse(missingMaterial.missingTemplate)
        assertTrue(missingMaterial.missingMaterial)

        player.inventory.addItem(ItemStack(Material.DIAMOND))
        val ready = service.precheck(player.inventory, settings, patterns, materials)
        assertTrue(ready.canOpen)
    }

    @Test
    fun `sub options decide what is hidden and checked`() {
        val player = MockBukkit.getMock()!!.addPlayer()
        player.inventory.addItem(ItemStack(Material.DIAMOND))
        val settings = TrimRequirementSettings(
            enabled = true,
            requireTemplate = false,
            requireMaterial = true,
        )

        val precheck = service.precheck(player.inventory, settings, patterns, materials)

        assertTrue(precheck.canOpen)
        assertFalse(precheck.missingTemplate)
        assertFalse(precheck.missingMaterial)
        assertEquals(patterns, service.visiblePatterns(player.inventory, settings, patterns))
        assertEquals(listOf(TrimMaterial.DIAMOND), service.visibleMaterials(player.inventory, settings, materials).map { it.material })
    }
}
