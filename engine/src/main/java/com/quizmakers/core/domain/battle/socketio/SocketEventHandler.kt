package com.quizmakers.core.domain.battle.socketio

import com.quizmakers.core.data.battle.remote.BattleResultMessageState
import com.quizmakers.core.data.battle.remote.ContentInfoMessageState
import com.quizmakers.core.data.battle.remote.QuestionMessageState
import com.quizmakers.core.data.battle.remote.AnswerMessageState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow

interface SocketEventHandler {
    val stateServer: MutableStateFlow<ContentInfoMessageState>

    val stateBattle: MutableStateFlow<BattleResultMessageState?>
    val stateAnswer: MutableStateFlow<AnswerMessageState?>
    val stateQuestion: MutableStateFlow<QuestionMessageState?>
    val stateError: MutableSharedFlow<ContentInfoMessageState?>
    fun connectObserver()
    fun errorObserver()
    fun serverMessageObserver()
    fun battleMessageObserver()
}