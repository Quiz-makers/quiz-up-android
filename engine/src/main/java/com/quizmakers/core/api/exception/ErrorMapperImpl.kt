package com.quizmakers.core.api.exception

import android.content.Context
import androidx.annotation.StringRes
import com.quizmakers.core.R
import org.koin.core.annotation.Factory
import retrofit2.HttpException

@Factory
class ErrorMapperImpl(private val context: Context) : ErrorMapper {

    override fun map(throwable: Throwable): String {
        return when (throwable) {
            is ServerException -> mapServerException(throwable)
            else -> getMessage(R.string.error_unknown)
        }
    }

    private fun mapServerException(serverException: ServerException): String {
        return when (serverException) {
            is ServerException.Internal -> getMessage(R.string.error_internal)
            is ServerException.BadRequest -> getMessage(R.string.error_bad_request)
            is ServerException.AccessDenied -> getMessage(R.string.access_denied)
            else -> getMessage(R.string.error_unknown)
        }
    }

    override fun getErrorBody(serverException: ServerException): String? {
        return when (serverException) {
            is ServerException.BadRequest -> (serverException.cause as HttpException).response()
                ?.errorBody()?.string()
            else -> null
        }
    }

    override fun getMessage(@StringRes stringRes: Int) = context.getString(stringRes)

}