package com.quizmakers.quizup.presentation.quizzes.playground

import androidx.lifecycle.viewModelScope
import com.quizmakers.core.api.exception.ErrorMapper
import com.quizmakers.core.data.quizzes.remote.Question
import com.quizmakers.core.domain.quizzes.useCases.CoreGetQuizDetailsUseCase
import com.quizmakers.core.domain.quizzes.useCases.CoreSendQuizResultUseCase
import com.quizmakers.quizup.core.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

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

    init {
        getQuiz()
    }

    fun getQuiz() {
        viewModelScope.launch {
            _quizState.emit(QuizState.Loading)
            runCatching {
                delay(1200)
                coreGetQuizDetailsUseCase.invoke()
            }.onFailure {
                errorMapper.map(it).also { errorMessage ->
                    sendMessageEvent(MessageEvent.Error(errorMessage))
                    _quizState.emit(QuizState.Error(errorMessage))
                }
            }.onSuccess {
                _quizState.emit(QuizState.Success(it))
            }
        }
    }

    private fun sendQuizResult(questionId: String) {
        //quizId
        viewModelScope.launch {
            coreSendQuizResultUseCase.invoke()
        }
    }

    sealed class QuizState {
        object None : QuizState()
        object Loading : QuizState()
        data class Success(val quizzesList: List<Question>) : QuizState()
        data class Error(val error: String) : QuizState()
    }
}