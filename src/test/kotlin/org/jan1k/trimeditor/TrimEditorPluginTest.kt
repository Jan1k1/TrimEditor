package org.jan1k.trimeditor

import org.mockbukkit.mockbukkit.MockBukkit
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TrimEditorPluginTest {
    @AfterTest
    fun tearDown() {
        MockBukkit.unmock()
    }

    @Test
    fun `plugin enables`() {
        MockBukkit.mock()
        val plugin = MockBukkit.load(TrimEditorPlugin::class.java)

        assertTrue(plugin.isEnabled)
    }

    @Test
    fun `uses assigned bstats plugin id`() {
        assertEquals(31971, TrimEditorPlugin.BSTATS_PLUGIN_ID)
    }
}
