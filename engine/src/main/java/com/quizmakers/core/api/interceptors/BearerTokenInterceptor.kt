package com.quizmakers.core.api.interceptors

import android.content.SharedPreferences
import com.quizmakers.core.base.TOKEN_NAME_SP
import okhttp3.Interceptor
import okhttp3.Response

class BearerTokenInterceptor(private val sharedPreferences: SharedPreferences) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val modifiedRequest = sharedPreferences.getString(TOKEN_NAME_SP, null)?.let {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $it")
                .build()
        } ?: return chain.proceed(originalRequest.newBuilder().build())

        return chain.proceed(modifiedRequest)
    }
}
