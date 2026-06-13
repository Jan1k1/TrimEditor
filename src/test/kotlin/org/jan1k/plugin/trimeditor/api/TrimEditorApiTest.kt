package org.jan1k.plugin.trimeditor.api

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ArmorMeta
import org.bukkit.inventory.meta.trim.TrimMaterial
import org.bukkit.inventory.meta.trim.TrimPattern
import org.jan1k.plugin.trimeditor.TrimEditorPlugin
import org.mockbukkit.mockbukkit.MockBukkit
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TrimEditorApiTest {
    @AfterTest
    fun tearDown() {
        MockBukkit.unmock()
    }

    @Test
    fun `plugin registers public api service`() {
        val server = MockBukkit.mock()
        MockBukkit.load(TrimEditorPlugin::class.java)

        val api = server.servicesManager.load(TrimEditorApi::class.java)

        assertNotNull(api)
        assertEquals(api, TrimEditorProvider.get())
        assertEquals(1, api.apiVersion())
    }

    @Test
    fun `api opens held armor editor`() {
        val server = MockBukkit.mock()
        MockBukkit.load(TrimEditorPlugin::class.java)
        val player = server.addPlayer()
        player.inventory.setItemInMainHand(ItemStack(Material.DIAMOND_CHESTPLATE))

        val result = TrimEditorProvider.require().openEditor(player)

        assertEquals(TrimEditorOpenResult.OPENED, result)
    }

    @Test
    fun `api refuses non armor held item`() {
        val server = MockBukkit.mock()
        MockBukkit.load(TrimEditorPlugin::class.java)
        val player = server.addPlayer()
        player.inventory.setItemInMainHand(ItemStack(Material.DIAMOND))

        val result = TrimEditorProvider.require().openEditor(player)

        assertEquals(TrimEditorOpenResult.NOT_ARMOR, result)
    }

    @Test
    fun `api clones item with trim`() {
        MockBukkit.mock()
        MockBukkit.load(TrimEditorPlugin::class.java)
        val item = ItemStack(Material.DIAMOND_CHESTPLATE)

        val edited = TrimEditorProvider.require().withTrim(item, TrimPattern.SENTRY, TrimMaterial.DIAMOND)

        assertNotNull(edited)
        assertEquals(Material.DIAMOND_CHESTPLATE, edited.type)
        assertEquals(TrimPattern.SENTRY, (edited.itemMeta as ArmorMeta).trim!!.pattern)
        assertEquals(TrimMaterial.DIAMOND, (edited.itemMeta as ArmorMeta).trim!!.material)
        assertNull((item.itemMeta as ArmorMeta).trim)
    }

    @Test
    fun `api exposes visible options`() {
        val server = MockBukkit.mock()
        val plugin = MockBukkit.load(TrimEditorPlugin::class.java)
        val player = server.addPlayer()
        plugin.config.set("requirements.enabled", true)
        plugin.config.set("requirements.require-template", true)
        plugin.config.set("requirements.require-material", true)
        plugin.saveConfig()
        plugin.reloadSettings()
        player.inventory.addItem(
            ItemStack(Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE),
            ItemStack(Material.DIAMOND),
        )

        val api = TrimEditorProvider.require()

        assertEquals(listOf(TrimPattern.SENTRY), api.visiblePatterns(player))
        assertEquals(listOf(TrimMaterial.DIAMOND), api.visibleMaterials(player))
        assertTrue(api.patterns().contains(TrimPattern.SENTRY))
        assertTrue(api.materials().contains(TrimMaterial.DIAMOND))
    }
}
