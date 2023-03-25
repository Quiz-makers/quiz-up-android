package com.quizmakers.quizup.presentation.auth.signOut

import androidx.lifecycle.viewModelScope
import com.quizmakers.core.api.exception.ErrorMapper
import com.quizmakers.core.api.exception.ErrorWrapper
import com.quizmakers.core.domain.auth.useCases.CoreSignUpUseCase
import com.quizmakers.quizup.core.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SignOutViewModel(
    private val getSignUpUseCase: CoreSignUpUseCase,
    private val errorWrapper: ErrorWrapper,
    private val errorMapper: ErrorMapper,
) : BaseViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.None)
    val authState = _authState.asStateFlow()

    fun signOut(name: String, surname: String, userName: String, email: String, password: String) {
        if (validate(name, surname, userName, email, password)) {
            viewModelScope.launch {
                _authState.emit(AuthState.Loading)
                runCatching {
                    getSignUpUseCase.invoke(name, surname, userName, email, password)
                }.onFailure {
                    val error = errorMapper.map(errorWrapper.wrap(it))
                    sendMessageEvent(AuthEvent.Error(error))
                    _authState.emit(
                        AuthState.Error(
                            arrayListOf(
                                ErrorValidation(
                                    error,
                                    SignInFieldInfo.ONLY_MESSAGE
                                )
                            )
                        )
                    )
                }.onSuccess {
                    sendMessageEvent(AuthEvent.Success)
                }
            }
        }
    }

    private fun validate(
        name: String,
        surname: String,
        userName: String,
        email: String,
        password: String
    ): Boolean {
        val errorField: ArrayList<ErrorValidation> = arrayListOf()
        val emptyInfoState = "To pole nie może być puste"
        if (name.isEmpty()) {
            errorField.add(
                ErrorValidation(
                    emptyInfoState,
                    SignInFieldInfo.NAME
                )
            )
        }
        if (surname.isEmpty()) {
            errorField.add(
                ErrorValidation(
                    emptyInfoState,
                    SignInFieldInfo.SURNAME
                )
            )
        }
        if (userName.isEmpty()) {
            errorField.add(
                ErrorValidation(
                    emptyInfoState,
                    SignInFieldInfo.USERNAME
                )
            )
        }
        if (email.isEmpty()) {
            errorField.add(
                ErrorValidation(
                    emptyInfoState,
                    SignInFieldInfo.EMAIL
                )
            )
        }
        if (password.isEmpty()) {
            errorField.add(
                ErrorValidation(
                    emptyInfoState,
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
        NAME, SURNAME, USERNAME, EMAIL, PASSWORD, ONLY_MESSAGE
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