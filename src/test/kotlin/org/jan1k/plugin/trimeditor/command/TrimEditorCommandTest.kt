package org.jan1k.plugin.trimeditor.command

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.jan1k.plugin.trimeditor.TrimEditorPlugin
import org.jan1k.plugin.trimeditor.session.EditSession
import org.mockbukkit.mockbukkit.MockBukkit
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TrimEditorCommandTest {
    @AfterTest
    fun tearDown() {
        MockBukkit.unmock()
    }

    @Test
    fun `reload closes open editor sessions`() {
        val server = MockBukkit.mock()
        val plugin = MockBukkit.load(TrimEditorPlugin::class.java)
        val player = server.addPlayer()
        var closed = 0

        player.addAttachment(plugin, "trimeditor.reload", true)
        plugin.sessionManager.open(EditSession(player.uniqueId) { closed++ })

        server.dispatchCommand(player, "te reload")

        assertEquals(0, plugin.sessionManager.size)
        assertEquals(1, closed)
    }

    @Test
    fun `editor does not open without held armor`() {
        val server = MockBukkit.mock()
        val plugin = MockBukkit.load(TrimEditorPlugin::class.java)
        val player = server.addPlayer()

        server.dispatchCommand(player, "te")

        assertEquals(0, plugin.sessionManager.size)
    }

    @Test
    fun `editor does not open when required items are missing`() {
        val server = MockBukkit.mock()
        val plugin = MockBukkit.load(TrimEditorPlugin::class.java)
        val player = server.addPlayer()

        plugin.config.set("requirements.enabled", true)
        plugin.config.set("requirements.require-template", true)
        plugin.config.set("requirements.require-material", true)
        plugin.saveConfig()
        plugin.reloadSettings()
        player.inventory.setItemInMainHand(ItemStack(Material.DIAMOND_CHESTPLATE))

        server.dispatchCommand(player, "te")

        assertEquals(0, plugin.sessionManager.size)
    }

    @Test
    fun `editor opens for held armor when free`() {
        val server = MockBukkit.mock()
        val plugin = MockBukkit.load(TrimEditorPlugin::class.java)
        val player = server.addPlayer()
        player.inventory.setItemInMainHand(ItemStack(Material.DIAMOND_CHESTPLATE))

        server.dispatchCommand(player, "te")

        assertEquals(1, plugin.sessionManager.size)
    }
}
