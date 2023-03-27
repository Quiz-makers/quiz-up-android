package com.quizmakers.core.api.exception

import retrofit2.HttpException

sealed class ServerException(message: String?, httpException: Throwable? = null) :
    Throwable(message, httpException) {
    class Internal(message: String?) : ServerException(message)
    class BadRequest(message: String?, httpException: HttpException?) : ServerException(message, httpException)
    class AccessDenied(message: String?) : ServerException(message)
    class Unknown(message: String?) : ServerException(message)
}