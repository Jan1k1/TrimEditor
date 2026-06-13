package org.jan1k.plugin.trimeditor.command

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
}
