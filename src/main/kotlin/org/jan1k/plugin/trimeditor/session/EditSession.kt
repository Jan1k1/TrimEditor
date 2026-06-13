package org.jan1k.plugin.trimeditor.session

import org.jan1k.plugin.trimeditor.gui.ScreenId
import java.util.UUID

class EditSession(
    val playerId: UUID,
    private val closer: () -> Unit,
) {
    var pattern: String? = null
    var material: String? = null
    var screen: ScreenId = ScreenId.PATTERNS

    fun close() {
        closer()
    }
}
