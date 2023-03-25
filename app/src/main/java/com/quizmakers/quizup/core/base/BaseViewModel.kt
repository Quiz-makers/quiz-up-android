package com.quizmakers.quizup.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    private val _authEvent = MutableSharedFlow<AuthEvent>()
    val authEvent = _authEvent.asSharedFlow()

    fun sendMessageEvent(error: AuthEvent) {
        viewModelScope.launch {
            _authEvent.emit(error)
        }
    }

    sealed class AuthEvent {
        object Success : AuthEvent()
        data class Error(val error: String) : AuthEvent()
    }

}