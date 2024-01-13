package com.quizmakers.quizup.presentation.quizzes.manage

import androidx.lifecycle.viewModelScope
import com.quizmakers.core.api.exception.ErrorMapper
import com.quizmakers.core.data.quizzes.remote.CategoryApi
import com.quizmakers.core.data.quizzes.remote.QuizGenerateRequest
import com.quizmakers.core.domain.quizzes.useCases.CoreGetCategoriesUseCase
import com.quizmakers.core.domain.quizzes.useCases.GenerateQuizUseCase
import com.quizmakers.quizup.core.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import java.net.SocketTimeoutException

@KoinViewModel
class GenerateQuizViewModel(
    private val coreGetCategoriesUseCase: CoreGetCategoriesUseCase,
    private val generateQuizUseCase: GenerateQuizUseCase,
    private val errorMapper: ErrorMapper
) : BaseViewModel() {

    private val _quizAiState = MutableStateFlow<QuizAiState>(
        QuizAiState.None
    )
    val quizManagerState = _quizAiState.asStateFlow()

    private val _addedState = MutableStateFlow<AddQuizState>(
        AddQuizState.Loaded
    )
    val addedState = _addedState.asStateFlow()

    init {
        getCategories()
    }

    fun generateQuiz(
        title: String,
        numberOfQuestions: Int,
        answersPerQuestion: Int,
        selectedCategoryId: Int,
        publicAvailable: Boolean
    ) {
        viewModelScope.launch {
            _addedState.emit(AddQuizState.Loading)
            runCatching {
                generateQuizUseCase.invoke(
                    QuizGenerateRequest(
                        title = title,
                        type = 1,
                        numberOfQuestions = numberOfQuestions,
                        answersPerQuestion = answersPerQuestion,
                        categoryId = selectedCategoryId,
                        publicAvailable = publicAvailable,
                        quizTime = null,
                        startsAt = null,
                        endsAt = null,
                    )
                )
            }.onFailure {
                if (it is SocketTimeoutException) {
                    sendMessageEvent(MessageEvent.Error("Zbyt długi czas oczekiwania.⌛️ Quiz pojawi się po czasie na ekranie startowym..."))

                } else {
                    errorMapper.map(it).also { errorMessage ->
                        sendMessageEvent(MessageEvent.Error(errorMessage))
                        _addedState.emit(AddQuizState.Loaded)
                    }
                }
            }.onSuccess {
                sendMessageEvent(MessageEvent.Success)
                _addedState.emit(AddQuizState.Loaded)
            }
        }
    }

    fun getCategories() {
        viewModelScope.launch {
            _quizAiState.emit(QuizAiState.Loading)
            runCatching {
                coreGetCategoriesUseCase.invoke()
            }.onFailure {
                errorMapper.map(it).also { errorMessage ->
                    sendMessageEvent(MessageEvent.Error(errorMessage))
                    _quizAiState.emit(QuizAiState.Error(errorMessage))
                }
            }.onSuccess {
                _quizAiState.emit(QuizAiState.Success(it))
            }
        }
    }

    sealed class AddQuizState {
        object Loaded : AddQuizState()
        object Loading : AddQuizState()
    }

    sealed class QuizAiState {
        object None : QuizAiState()
        object Loading : QuizAiState()
        data class Success(val categories: List<CategoryApi>) : QuizAiState()
        data class Error(val error: String) : QuizAiState()
    }
}