package org.jan1k.plugin.trimeditor.session

import java.util.UUID

class SessionManager {
    private val sessions = LinkedHashMap<UUID, EditSession>()

    val size: Int
        get() = sessions.size

    fun open(session: EditSession) {
        sessions.remove(session.playerId)?.close()
        sessions[session.playerId] = session
    }

    operator fun get(playerId: UUID): EditSession? = sessions[playerId]

    fun remove(playerId: UUID): EditSession? = sessions.remove(playerId)

    fun close(playerId: UUID) {
        sessions.remove(playerId)?.close()
    }

    fun closeAll() {
        val active = sessions.values.toList()
        sessions.clear()
        active.forEach { it.close() }
    }
}
