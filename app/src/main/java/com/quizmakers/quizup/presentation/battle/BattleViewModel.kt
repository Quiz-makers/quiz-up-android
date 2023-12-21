package com.quizmakers.quizup.presentation.battle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.quizmakers.core.data.battle.remote.AnswerMessage
import com.quizmakers.core.data.battle.remote.AnswerMessageState
import com.quizmakers.core.data.battle.remote.ContentInfoMessageState
import com.quizmakers.core.data.battle.remote.ServerInfo
import com.quizmakers.core.data.battle.socketio.SocketEvents
import com.quizmakers.core.domain.battle.socketio.SocketEventHandler
import com.quizmakers.core.domain.battle.socketio.WebSocketService
import com.quizmakers.core.domain.battle.useCases.CoreGetUserNameUseCase
import com.quizmakers.core.domain.battle.useCases.GetTimerUseCase
import com.quizmakers.core.domain.battle.useCases.TimerState
import com.quizmakers.quizup.presentation.battle.data.AnswerPlayerResult
import com.quizmakers.quizup.presentation.battle.data.AnswerResultState
import com.quizmakers.quizup.presentation.battle.data.GameResult
import com.quizmakers.quizup.presentation.battle.data.Player
import com.quizmakers.quizup.presentation.battle.data.ResultSummarisingState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class BattleViewModel(
    private val getUserName: CoreGetUserNameUseCase,
    private val timerUseCase: GetTimerUseCase,
    private val webSocket: WebSocketService,
    private val socketEventHandler: SocketEventHandler
) : ViewModel() {
    private val userName
        get() = getUserName()

    private var opponentName: String? = null

    private val _battleState = socketEventHandler.stateBattle.asStateFlow()

    //Summary Screen states
    private val _battleResultState =
        MutableStateFlow<ResultSummarisingState>(ResultSummarisingState.None)
    val battleResultState = _battleResultState.asStateFlow()

    private val _stateAnswer = socketEventHandler.stateAnswer.asStateFlow()

    //Answer result Screen states
    private val _answerResultState = MutableStateFlow<AnswerResultState>(AnswerResultState.None)
    val answerResultState = _answerResultState.asStateFlow()

    private val _scoreState = MutableStateFlow<GameResult?>(null)
    val scoreState = _scoreState.asStateFlow()

    val stateQuestion = socketEventHandler.stateQuestion.asStateFlow()

    val stateError = socketEventHandler.stateError.asSharedFlow() //OK
    val stateServer = socketEventHandler.stateServer.asStateFlow() //OK

    private var job: Job? = null

    private val _timerState = MutableStateFlow<TimerState>(TimerState.None)
    val timerState = _timerState.asStateFlow()

    fun startBattle() {
        webSocket.connect()
        socketEventHandler.battleMessageObserver()
        socketEventHandler.serverMessageObserver()

        questionObserver()
        checkAnswerObserver()
        battleSummaryObserver()
    }

    fun startTimerTick(time: Int = 10) {
        job?.cancel()
        job = viewModelScope.launch {
            timerUseCase(time).collect {
                _timerState.value = it
            }
        }
    }

    fun emitAnswer(id: Long, answer: String) {
        val gson = Gson()
        val jsonString = gson.toJson(AnswerMessage(id.toString(), answer))
        val jsonObject = JSONObject(jsonString)
        webSocket.emitEvent(
            SocketEvents.AnswerMessage, jsonObject
        )
    }

    private fun battleSummaryObserver() {
        viewModelScope.launch {
            _battleState.collect {
                it?.let {
                    val resultMap = mutableMapOf<String, Int>()
                    for (questionResult in it.content.questionResults) {
                        for ((name, result) in questionResult.resultList) {
                            resultMap.merge(name, if (result.correct) 1 else 0, Integer::sum)
                        }
                    }
                    val myResult = resultMap[userName] ?: 0
                    if (opponentName == null) {
                        opponentName =
                            resultMap.keys.find { key -> key != userName }!!
                    }

                    val opponentResult = resultMap[opponentName] ?: 0
                    _battleResultState.emit(
                        ResultSummarisingState.ResultReady(
                            GameResult(
                                me = Player(
                                    name = userName, myResult
                                ),
                                opponent = Player(
                                    opponentName!!, opponentResult
                                ),
                            )
                        )
                    )
                }
            }
        }
    }

    private fun checkAnswerObserver() {
        viewModelScope.launch {
            _stateAnswer.collect {
                it?.let { answer ->
                    val myResult = answer.content.resultList[userName]?.answerId ?: 0

                    if (opponentName == null) {
                        opponentName =
                            answer.content.resultList.keys.find { key -> key != userName }!!
                    }

                    val opponentResult = answer.content.resultList[opponentName]?.answerId ?: 0

                    scoreObserver(answer)
                    _answerResultState.emit(
                        AnswerResultState.AnswerResult(
                            answerResult = AnswerPlayerResult(
                                myAnswer = myResult, opponentAnswer = opponentResult
                            )
                        )
                    )
                }
            }
        }
    }

    private fun scoreObserver(it: AnswerMessageState) {
        val myResult = if (it.content.resultList[userName]?.correct == true) 1 else 0
        val opponentResult = if (it.content.resultList[opponentName]?.correct == true) 1 else 0

        if (_scoreState.value == null) {
            _scoreState.value = GameResult(
                me = Player(name = userName, score = myResult),
                opponent = Player(name = opponentName!!, score = opponentResult)
            )
        } else {
            val tmpGameResult = _scoreState.value!!
            _scoreState.value = GameResult(
                me = tmpGameResult.me.copy(score = tmpGameResult.me.score + myResult),
                opponent = tmpGameResult.opponent.copy(score = tmpGameResult.opponent.score + opponentResult),
            )
        }
    }

    private fun questionObserver() {
        viewModelScope.launch {
            stateQuestion.collect {
                _answerResultState.value = AnswerResultState.None
            }
        }
    }

    fun onClear() {
        webSocket.disconnect()
        _scoreState.value = null
        socketEventHandler.stateBattle.value = null
        socketEventHandler.stateAnswer.value = null
        socketEventHandler.stateQuestion.value = null
        socketEventHandler.stateServer.value =
            ContentInfoMessageState(serverInfo = ServerInfo.NONE.name)
    }
}