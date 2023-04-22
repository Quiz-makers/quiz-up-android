package com.quizmakers.core.api.interceptors

import android.content.SharedPreferences
import com.quizmakers.core.base.TOKEN_NAME_SP
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val sharedPreferences: SharedPreferences) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request().newBuilder().also { builder ->
            if (chain.request().header("No-Authentication") == null) {
                sharedPreferences.getString(TOKEN_NAME_SP, null)?.let {
                    builder.addHeader("Authorization", "Bearer $it")
                }
            }
        }.build())
    }
}