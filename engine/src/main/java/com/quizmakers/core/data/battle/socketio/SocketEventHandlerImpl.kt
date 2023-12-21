package com.quizmakers.core.data.battle.socketio

import com.google.gson.Gson
import com.quizmakers.core.data.battle.remote.BattleResultMessageState
import com.quizmakers.core.data.battle.remote.ContentInfoMessageState
import com.quizmakers.core.data.battle.remote.QuestionMessageState
import com.quizmakers.core.data.battle.remote.AnswerMessageState
import com.quizmakers.core.data.battle.remote.ServerInfo
import com.quizmakers.core.data.battle.remote.ServerMessage
import com.quizmakers.core.data.battle.remote.toServerInfo
import com.quizmakers.core.domain.battle.socketio.SocketEventHandler
import com.quizmakers.core.domain.battle.socketio.WebSocketService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.annotation.Single


@Single
class SocketEventHandlerImpl(val webSocket: WebSocketService) : SocketEventHandler {

    override val stateServer: MutableStateFlow<ContentInfoMessageState> =
        MutableStateFlow(ContentInfoMessageState(serverInfo = ServerInfo.NONE.name))
    override val stateBattle: MutableStateFlow<BattleResultMessageState?> = MutableStateFlow(null)
    override val stateAnswer: MutableStateFlow<AnswerMessageState?> = MutableStateFlow(null)
    override val stateQuestion: MutableStateFlow<QuestionMessageState?> = MutableStateFlow(null)
    override val stateError: MutableSharedFlow<ContentInfoMessageState?> = MutableStateFlow(null)

    init {
        errorObserver()
        connectObserver()
    }

    override fun connectObserver() {
        webSocket.eventObserver(SocketEvents.ServerConnect) {}
    }

    override fun errorObserver() {
        webSocket.eventObserver(SocketEvents.ServerError) {
            (it[0] as? Exception)?.printStackTrace()
        }
    }

    override fun serverMessageObserver() {
        webSocket.eventObserver(SocketEvents.ServerMessage) {
            transformServerMessage(it[0].toString())
        }
    }

    override fun battleMessageObserver() {
        webSocket.eventObserver(SocketEvents.BattleMessage) {
            transformServerMessage(it[0].toString())
        }
    }

    private fun transformServerMessage(serverMessage: String) {
        when (Gson().fromJson(serverMessage, ServerMessage::class.java).serverInfo.toServerInfo()) {
            ServerInfo.IN_WAITING_ROOM -> stateServer.value =
                Gson().fromJson(serverMessage, ContentInfoMessageState::class.java)

            ServerInfo.IN_BATTLE_ROOM -> stateServer.value =
                Gson().fromJson(serverMessage, ContentInfoMessageState::class.java)

            ServerInfo.QUESTION_MESSAGE -> stateQuestion.value =
                Gson().fromJson(serverMessage, QuestionMessageState::class.java)

            ServerInfo.ANSWER_RESULT -> stateAnswer.value =
                Gson().fromJson(serverMessage, AnswerMessageState::class.java)

            ServerInfo.BATTLE_RESULT -> stateBattle.value =
                Gson().fromJson(serverMessage, BattleResultMessageState::class.java)

            ServerInfo.ERROR_MESSAGE -> stateError.tryEmit(
                Gson().fromJson(serverMessage, ContentInfoMessageState::class.java)
            )

            ServerInfo.NONE -> Unit //content is null
        }
    }
}