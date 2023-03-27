package com.quizmakers.core.api.exception

interface ErrorWrapper {
    fun wrap(throwable: Throwable): Throwable
}