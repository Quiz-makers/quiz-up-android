package com.quizmakers.quizup.presentation.auth.signIn

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quizmakers.core.api.exception.ErrorMapper
import com.quizmakers.core.api.exception.ErrorWrapper
import com.quizmakers.core.domain.auth.useCases.CoreSignInUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SignInViewModel(
    private val getSignInUseCase: CoreSignInUseCase,
    private val errorWrapper: ErrorWrapper,
    private val errorMapper: ErrorMapper,
) : ViewModel() {
    private val _authEvent = MutableSharedFlow<AuthEvent>()
    val authEvent = _authEvent.asSharedFlow()

    private val _authState = MutableStateFlow<AuthState>(AuthState.None)
    val authState = _authState.asStateFlow()

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.emit(AuthState.Loading)
            runCatching {
                getSignInUseCase.invoke(email, password)
            }.onFailure {
                val error = errorMapper.map(errorWrapper.wrap(it))
                _authEvent.emit(AuthEvent.Error(error))
                _authState.emit(AuthState.Error(error))
            }.onSuccess {
                _authEvent.emit(AuthEvent.Success)
            }
        }
    }

    sealed class AuthEvent {
        object Success : AuthEvent()
        data class Error(val error: String) : AuthEvent()
    }

    sealed class AuthState {
        object None : AuthState()
        object Loading : AuthState()
        data class Error(val error: String) : AuthState()
    }
}