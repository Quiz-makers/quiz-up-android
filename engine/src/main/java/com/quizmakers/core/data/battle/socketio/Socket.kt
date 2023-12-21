package com.quizmakers.core.data.battle.socketio

import io.socket.client.Socket

sealed class SocketEvents(val eventName: String) {
    object ServerConnect : SocketEvents(Socket.EVENT_CONNECT)
    object ServerError : SocketEvents(Socket.EVENT_CONNECT_ERROR)
    object ServerMessage : SocketEvents("server_message")
    object BattleMessage : SocketEvents("battle_message")
    object AnswerMessage : SocketEvents("battle_answer")
}


