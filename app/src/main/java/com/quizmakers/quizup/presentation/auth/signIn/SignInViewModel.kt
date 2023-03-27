package com.quizmakers.quizup.presentation.auth.signIn

import androidx.lifecycle.viewModelScope
import com.quizmakers.core.api.exception.ErrorMapper
import com.quizmakers.core.domain.auth.useCases.CoreSignInUseCase
import com.quizmakers.quizup.R
import com.quizmakers.quizup.core.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SignInViewModel(
    private val coreSignInUseCase: CoreSignInUseCase,
    private val errorMapper: ErrorMapper,
) : BaseViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.None)
    val authState = _authState.asStateFlow()

    fun signIn(email: String, password: String) {
        if (validate(email, password)) {
            viewModelScope.launch {
                _authState.emit(AuthState.Loading)
                runCatching {
                    coreSignInUseCase.invoke(email, password)
                }.onFailure {
                    sendErrorEvents(it)
                }.onSuccess {
                    sendMessageEvent(AuthEvent.Success)
                }
            }
        }
    }

    private suspend fun sendErrorEvents(it: Throwable) {
        errorMapper.map(it).also { errorMessage ->
            sendMessageEvent(AuthEvent.Error(errorMessage))
            _authState.emit(
                AuthState.Error(
                    arrayListOf(
                        ErrorValidation(
                            errorMessage,
                            SignInFieldInfo.ONLY_MESSAGE
                        )
                    )
                )
            )
        }
    }

    private fun validate(email: String, password: String): Boolean {
        val errorField: ArrayList<ErrorValidation> = arrayListOf()
        if (email.isEmpty()) {
            errorField.add(
                ErrorValidation(
                    errorMapper.getMessage(R.string.empty_field),
                    SignInFieldInfo.EMAIL
                )
            )
        }
        if (password.isEmpty()) {
            errorField.add(
                ErrorValidation(
                    errorMapper.getMessage(R.string.empty_field),
                    SignInFieldInfo.PASSWORD
                )
            )
        }
        viewModelScope.launch {
            _authState.emit(AuthState.Error(errorField))
        }
        return errorField.isEmpty()
    }

    enum class SignInFieldInfo {
        EMAIL, PASSWORD, ONLY_MESSAGE
    }

    data class ErrorValidation(
        val error: String,
        val signInField: SignInFieldInfo,
    )

    sealed class AuthState {
        object None : AuthState()
        object Loading : AuthState()
        data class Error(val errorField: ArrayList<ErrorValidation>) : AuthState()
    }
}