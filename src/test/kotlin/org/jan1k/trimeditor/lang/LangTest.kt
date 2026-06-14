package org.jan1k.trimeditor.lang

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LangTest {
    @Test
    fun `prefix is substituted into admin messages`() {
        val lang = Lang.load("en")

        assertTrue(lang.message("admin.reload").startsWith(lang.prefix))
        assertFalse(lang.message("admin.reload").contains("<prefix>"))
    }

    @Test
    fun `placeholders are substituted`() {
        val lang = Lang.load("en")

        val rendered = lang.message("admin.reload", "time" to "42")

        assertTrue(rendered.contains("42"))
        assertFalse(rendered.contains("<time>"))
    }

    @Test
    fun `missing key returns the path unchanged`() {
        val lang = Lang.load("en")

        assertEquals("errors.does-not-exist", lang.message("errors.does-not-exist"))
    }

    @Test
    fun `unknown locale falls back to english`() {
        val lang = Lang.load("zz")

        assertTrue(lang.message("errors.not-armor").isNotBlank())
    }
}
