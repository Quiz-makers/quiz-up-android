package com.quizmakers.core.domain.battle.socketio

import com.quizmakers.core.data.battle.socketio.SocketEvents
import org.json.JSONObject

interface WebSocketService {
    fun connect()
    fun disconnect()
    fun eventObserver(socketEvents: SocketEvents, eventObserver: (Array<Any>) -> Unit)
    fun emitEvent(socketEvents: SocketEvents, message: JSONObject)
}
