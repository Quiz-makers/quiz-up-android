package com.quizmakers.core.api.exception

import org.koin.core.annotation.Factory
import retrofit2.HttpException

@Factory
class ErrorWrapperImpl(override var errorBody: String?) : ErrorWrapper {

    override fun wrap(throwable: Throwable): Throwable {
        return when (throwable) {
            is HttpException -> wrapServerError(throwable)
            else -> throwable
        }
    }

    private fun wrapServerError(httpException: HttpException): Throwable {
        return with(httpException) {
            when (code()) {
                500 -> ServerException.Internal(message())
                400 -> {
                    errorBody = getErrorBody(httpException)
                    ServerException.BadRequest(message())
                }
                403 -> ServerException.AccessDenied(message())
                else -> ServerException.Unknown(message())
            }
        }
    }

    private fun getErrorBody(httpException: HttpException): String? {
        return httpException.response()?.errorBody()?.string()
    }
}
