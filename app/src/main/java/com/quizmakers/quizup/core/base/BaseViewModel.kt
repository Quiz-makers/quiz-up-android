package com.quizmakers.quizup.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    private val _messageEvent = MutableSharedFlow<MessageEvent>()
    val messageEvent = _messageEvent.asSharedFlow()

    fun sendMessageEvent(error: MessageEvent) {
        viewModelScope.launch {
            _messageEvent.emit(error)
        }
    }

    sealed class MessageEvent {
        object Success : MessageEvent()
        data class Error(val error: String) : MessageEvent()
    }

}