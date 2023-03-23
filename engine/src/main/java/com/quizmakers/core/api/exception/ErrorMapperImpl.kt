package com.quizmakers.core.api.exception

import android.content.Context
import androidx.annotation.StringRes
import com.quizmakers.core.R
import org.koin.core.annotation.Factory

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
            else -> getMessage(R.string.error_unknown)
        }
    }

    private fun getMessage(@StringRes stringRes: Int) = context.getString(stringRes)

}