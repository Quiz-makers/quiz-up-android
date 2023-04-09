package com.quizmakers.quizup.presentation.quizzes.manage

import androidx.lifecycle.viewModelScope
import com.quizmakers.core.api.exception.ErrorMapper
import com.quizmakers.core.data.quizzes.remote.CategoryApi
import com.quizmakers.core.data.quizzes.remote.QuestionsRequestApi
import com.quizmakers.core.data.quizzes.remote.QuizRequestApi
import com.quizmakers.core.domain.quizzes.useCases.CoreAddNewQuizUseCase
import com.quizmakers.core.domain.quizzes.useCases.CoreGetCategoriesUseCase
import com.quizmakers.quizup.core.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class QuizManagerViewModel(
    private val coreGetCategoriesUseCase: CoreGetCategoriesUseCase,
    private val coreAddNewQuizUseCase: CoreAddNewQuizUseCase,
    private val errorMapper: ErrorMapper
) : BaseViewModel() {

    private val _quizManagerState = MutableStateFlow<QuizManagerState>(QuizManagerState.None)
    val categoriesState = _quizManagerState.asStateFlow()

    private val _addedState = MutableStateFlow<AddQuizState>(AddQuizState.Loaded)
    val addedState = _addedState.asStateFlow()

    init {
        getCategories()
    }

    fun getCategories() {
        viewModelScope.launch {
            _quizManagerState.emit(QuizManagerState.Loading)
            runCatching {
              //  delay(1200)
                coreGetCategoriesUseCase.invoke()
            }.onFailure {
                errorMapper.map(it).also { errorMessage ->
                    sendMessageEvent(MessageEvent.Error(errorMessage))
                    _quizManagerState.emit(QuizManagerState.Error(errorMessage))
                }
            }.onSuccess {
                _quizManagerState.emit(QuizManagerState.Success(it))
            }
        }
    }

    fun addNewQuiz(
        title: String,
        description: String,
        publicAvailable: Boolean,
        selectedCategoryId: Int,
        questionsRequestApi: QuestionsRequestApi
    ) {
        viewModelScope.launch {
            _addedState.emit(AddQuizState.Loading)
            runCatching {
               // delay(1200)
                coreAddNewQuizUseCase.invoke(
                    QuizRequestApi(
                        ownerId = 3,
                        title = title,
                        metaTitle = "",
                        summary = "Summary",
                        description = description,
                        type = 1,
                        categoryId = selectedCategoryId,
                        score = 10,
                        publicAvailable = publicAvailable
                    )
                )
            }.onFailure {
                errorMapper.map(it).also { errorMessage ->
                    sendMessageEvent(MessageEvent.Error(errorMessage))
                    _addedState.emit(AddQuizState.Loaded)
                }
            }.onSuccess {
                _addedState.emit(AddQuizState.Loaded)
            }
        }
    }

    sealed class AddQuizState {
        object Loaded : AddQuizState()
        object Loading : AddQuizState()
    }

    sealed class QuizManagerState {
        object None : QuizManagerState()
        object Loading : QuizManagerState()
        data class Success(val categories: List<CategoryApi>) : QuizManagerState()
        data class Error(val error: String) : QuizManagerState()
    }
}