package com.quizmakers.quizup.presentation.dashboard

import androidx.lifecycle.viewModelScope
import com.quizmakers.core.api.exception.ErrorMapper
import com.quizmakers.core.domain.dashboard.useCases.CoreLogOutUseCase
import com.quizmakers.core.domain.dashboard.useCases.CoreGetQuizzesUseCase
import com.quizmakers.quizup.core.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DashboardScreenViewModel(
    private val coreLogOutUseCase: CoreLogOutUseCase,
    private val coreGetQuizzesUseCase: CoreGetQuizzesUseCase,
    private val errorMapper: ErrorMapper,
) : BaseViewModel() {

    private val _dashboardState =
        MutableStateFlow<DashboardState>(DashboardState.None)
    val dashboardState = _dashboardState.asStateFlow()

    init {
        getQuizzes()
    }

    fun getQuizzes() {
        viewModelScope.launch {
            _dashboardState.emit(DashboardState.Loading)
            runCatching {
                coreGetQuizzesUseCase.invoke()
            }.onFailure {
                errorMapper.map(it).also { errorMessage ->
                    sendMessageEvent(AuthEvent.Error(errorMessage))
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
        data class Success(val quizzesList: List<String>) : DashboardState()
        data class Error(val error: String) : DashboardState()
    }
}