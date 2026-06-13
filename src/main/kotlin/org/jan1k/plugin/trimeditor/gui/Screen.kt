package org.jan1k.plugin.trimeditor.gui

import org.jan1k.plugin.trimeditor.session.EditSession

enum class ScreenId {
    PATTERNS,
    MATERIALS,
    CONFIRM,
}

interface Screen {
    val id: ScreenId
    val title: String

    fun buttons(session: EditSession): List<Button>
}
