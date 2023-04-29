package com.quizmakers.quizup.presentation.auth.register

import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.quizmakers.core.api.exception.ErrorMapper
import com.quizmakers.core.api.exception.ServerException
import com.quizmakers.core.data.auth.remote.ErrorCatcher
import com.quizmakers.core.data.auth.remote.Errors
import com.quizmakers.core.domain.auth.useCases.CoreRegisterUseCase
import com.quizmakers.quizup.R
import com.quizmakers.quizup.core.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class RegisterViewModel(
    private val coreRegisterUseCase: CoreRegisterUseCase,
    private val errorMapper: ErrorMapper,
) : BaseViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.None)
    val authState = _authState.asStateFlow()

    fun register(name: String, surname: String, userName: String, email: String, password: String) {
        if (validate(name, surname, userName, email, password)) {
            viewModelScope.launch {

                _authState.emit(AuthState.Loading)
                runCatching {
                    coreRegisterUseCase.invoke(name, surname, userName, email, password)
                }.onFailure {
                    errorMapper.map(it).also { errorMessage ->
                        sendMessageEvent(MessageEvent.Error(errorMessage))
                        _authState.emit(
                            AuthState.Error(
                                parseErrorBodyToValidateForm(it, errorMessage)
                            )
                        )
                    }
                }.onSuccess {
                    sendMessageEvent(MessageEvent.Success)
                }
            }
        }
    }

    private fun parseErrorBodyToValidateForm(
        throwable: Throwable,
        error: String
    ): ArrayList<ErrorValidation> =
        errorMapper.getErrorBody(throwable as ServerException)?.let { errorBody ->
            Gson().fromJson(
                errorBody,
                ErrorCatcher::class.java
            ).errors.map { errors ->
                ErrorValidation(
                    errors.message,
                    errors.toSignOutFieldInfo()
                )
            } as ArrayList
        } ?: arrayListOf(
            ErrorValidation(
                error,
                SignOutFieldInfo.ONLY_MESSAGE
            )
        )

    private fun validate(
        name: String,
        surname: String,
        userName: String,
        email: String,
        password: String
    ): Boolean {
        val errorField: ArrayList<ErrorValidation> = arrayListOf()
        if (name.isEmpty()) {
            errorField.add(
                ErrorValidation(
                    errorMapper.getMessage(R.string.empty_field),
                    SignOutFieldInfo.NAME
                )
            )
        }
        if (surname.isEmpty()) {
            errorField.add(
                ErrorValidation(
                    errorMapper.getMessage(R.string.empty_field),
                    SignOutFieldInfo.SURNAME
                )
            )
        }
        if (userName.isEmpty()) {
            errorField.add(
                ErrorValidation(
                    errorMapper.getMessage(R.string.empty_field),
                    SignOutFieldInfo.USERNAME
                )
            )
        }
        if (email.isEmpty()) {
            errorField.add(
                ErrorValidation(
                    errorMapper.getMessage(R.string.empty_field),
                    SignOutFieldInfo.EMAIL
                )
            )
        }
        if (password.isEmpty()) {
            errorField.add(
                ErrorValidation(
                    errorMapper.getMessage(R.string.empty_field),
                    SignOutFieldInfo.PASSWORD
                )
            )
        }
        viewModelScope.launch {
            _authState.emit(AuthState.Error(errorField))
        }
        return errorField.isEmpty()
    }

    fun Errors.toSignOutFieldInfo(): SignOutFieldInfo {
        return when (this.field.uppercase()) {
            "EMAIL" -> SignOutFieldInfo.EMAIL
            "NAME" -> SignOutFieldInfo.NAME
            "SURNAME" -> SignOutFieldInfo.SURNAME
            "USERNAME" -> SignOutFieldInfo.USERNAME
            "PASSWORD" -> SignOutFieldInfo.PASSWORD
            else -> SignOutFieldInfo.ONLY_MESSAGE
        }
    }

    enum class SignOutFieldInfo {
        NAME, SURNAME, USERNAME, EMAIL, PASSWORD, ONLY_MESSAGE
    }

    data class ErrorValidation(
        val error: String,
        val signInField: SignOutFieldInfo,
    )


    sealed class AuthState {
        object None : AuthState()
        object Loading : AuthState()
        data class Error(val errorField: ArrayList<ErrorValidation>) : AuthState()
    }
}