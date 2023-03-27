package com.quizmakers.core.api.exception

import androidx.annotation.StringRes

interface ErrorMapper {
    fun map(throwable: Throwable): String
    fun getMessage(@StringRes stringRes: Int): String // In Future Enum with Case of Validation
    fun getErrorBody(serverException: ServerException): String?
}