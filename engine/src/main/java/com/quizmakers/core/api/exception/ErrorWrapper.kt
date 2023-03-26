package com.quizmakers.core.api.exception

interface ErrorWrapper {
    var errorBody: String?
    fun wrap(throwable: Throwable): Throwable
}