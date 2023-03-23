package com.quizmakers.core.api.exception

interface ErrorMapper {
    fun map(throwable: Throwable): String
}