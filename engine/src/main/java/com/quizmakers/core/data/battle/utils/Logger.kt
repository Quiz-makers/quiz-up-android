package com.quizmakers.core.data.battle.utils

import android.util.Log
import com.quizmakers.core.data.battle.socketio.SocketEvents


fun SocketEvents.log(className: String, track: Array<Any>) {
    when (this) {
        SocketEvents.BattleMessage -> logger(className, "Wiadmość battle : ${track[0]}")
        SocketEvents.ServerConnect -> logger(className, "Połączono do serwera")
        SocketEvents.ServerError -> logger(className, "Problem z połączeniem: ${track[0]}")
        SocketEvents.ServerMessage -> logger(className, "Wiadmość serwera:${track[0]}")
        SocketEvents.AnswerMessage -> logger(className, "Udzielono odpowiedzi :${track[0]}")
    }
}

private fun logger(className: String, printStack: String) {
    Log.e(className, printStack)
}