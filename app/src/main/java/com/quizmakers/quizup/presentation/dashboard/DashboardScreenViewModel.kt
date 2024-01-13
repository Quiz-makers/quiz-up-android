package com.quizmakers.quizup.presentation.dashboard

import androidx.lifecycle.viewModelScope
import com.quizmakers.core.api.exception.ErrorMapper
import com.quizmakers.core.data.quizzes.local.QuizGeneralDisplayable
import com.quizmakers.core.domain.dashboard.useCases.CoreLogOutUseCase
import com.quizmakers.core.domain.dashboard.useCases.CoreGetPublicQuizzesUseCase
import com.quizmakers.core.domain.dashboard.useCases.CoreGetQuizByCodeUseCase
import com.quizmakers.core.domain.dashboard.useCases.CoreGetUserQuizzesUseCase
import com.quizmakers.core.domain.session.SessionManager
import com.quizmakers.quizup.R
import com.quizmakers.quizup.core.base.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel


@KoinViewModel
class DashboardScreenViewModel(
    private val coreLogOutUseCase: CoreLogOutUseCase,
    private val coreGetPublicQuizzesUseCase: CoreGetPublicQuizzesUseCase,
    private val getQuizByCodeUseCase: CoreGetQuizByCodeUseCase,
    private val coreGetUserQuizzesUseCase: CoreGetUserQuizzesUseCase,
    private val sessionManager: SessionManager,
    private val errorMapper: ErrorMapper,
) : BaseViewModel() {

    private val _dashboardState =
        MutableStateFlow<DashboardState>(DashboardState.None)
    val dashboardState = _dashboardState.asStateFlow()

    fun getQuizByCode(code: String) {
        viewModelScope.launch {
            runCatching {
                getQuizByCodeUseCase.invoke(code)
            }.onFailure {
                sendMessageEvent(
                    MessageEvent.Error(
                        errorMapper.getMessage(R.string.not_found)
                    )
                )
            }.onSuccess {
                sendMessageEvent(MessageEvent.Success)
                getQuizzes()
            }
        }
    }

    fun getUserName() = sessionManager.getUserName()
    fun getQuizzes() {
        viewModelScope.launch {
            _dashboardState.emit(DashboardState.Loading)
            runCatching {
                Pair(coreGetPublicQuizzesUseCase(), coreGetUserQuizzesUseCase())
            }.onFailure {
                errorMapper.map(it).also { errorMessage ->
                    sendMessageEvent(MessageEvent.Error(errorMessage))
                    _dashboardState.emit(DashboardState.Error(errorMessage))
                }
            }.onSuccess {
                _dashboardState.emit(DashboardState.Success(it))
            }
        }
    }

    fun logOutFromApp() = coreLogOutUseCase.invoke()

    sealed class DashboardState {
        object None : DashboardState()
        object Loading : DashboardState()
        data class Success(val data: Pair<List<QuizGeneralDisplayable>, List<QuizGeneralDisplayable>>) :
            DashboardState()

        data class Error(val error: String) : DashboardState()
    }
}