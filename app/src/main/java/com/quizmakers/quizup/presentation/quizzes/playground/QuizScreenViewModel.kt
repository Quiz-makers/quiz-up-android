package com.quizmakers.quizup.presentation.quizzes.playground

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.quizmakers.core.api.exception.ErrorMapper
import com.quizmakers.core.data.quizzes.remote.AnswerDto
import com.quizmakers.core.data.quizzes.remote.QuestionApi
import com.quizmakers.core.data.quizzes.remote.QuizResult
import com.quizmakers.core.domain.quizzes.useCases.CoreGetQuizDetailsUseCase
import com.quizmakers.core.domain.quizzes.useCases.CoreSendQuizResultUseCase
import com.quizmakers.core.domain.quizzes.useCases.questionsMocks
import com.quizmakers.quizup.core.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.time.LocalDateTime

@KoinViewModel
class QuizScreenViewModel(
    private val quizId: String,
    private val coreGetQuizDetailsUseCase: CoreGetQuizDetailsUseCase,
    private val coreSendQuizResultUseCase: CoreSendQuizResultUseCase,
    private val errorMapper: ErrorMapper,
) : BaseViewModel() {
    private val _quizState =
        MutableStateFlow<QuizState>(QuizState.None)
    val quizState = _quizState.asStateFlow()

    private val answerDtoList = mutableListOf<AnswerDto>()

    init {
        getQuiz()
    }

    fun addAnswer(answer: AnswerDto) {
        answerDtoList.add(answer)
    }

    fun getQuiz() {
        viewModelScope.launch {

            _quizState.emit(QuizState.Loading)
            runCatching {
                //  delay(1200)
                coreGetQuizDetailsUseCase.invoke(quizId)
            }.onFailure {
                errorMapper.map(it).also { errorMessage ->
                    _quizState.emit(QuizState.Success(questionsMocks))
              //      sendMessageEvent(MessageEvent.Error(errorMessage))
                  //  _quizState.emit(QuizState.Error(errorMessage))
                }
            }.onSuccess {
                _quizState.emit(QuizState.Success(it.questionDtoSet))
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun finishQuiz() {
        viewModelScope.launch {
            coreSendQuizResultUseCase.invoke(
                QuizResult(
                    quizId = quizId.toInt(),
                    answerDtoList = answerDtoList,
                    startTime = LocalDateTime.now(),
                    finishTime = LocalDateTime.now(),
                )
            )
        }
    }

    sealed class QuizState {
        object None : QuizState()
        object Loading : QuizState()
        data class Success(val quizzesList: List<QuestionApi>) : QuizState()
        data class Error(val error: String) : QuizState()
    }
}