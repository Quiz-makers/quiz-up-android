package com.quizmakers.core.api.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val authToken: String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request().newBuilder().also { builder ->
            authToken?.let {
                builder.addHeader("Authorization", "Bearer $authToken")
            }
        }.build())
    }
}