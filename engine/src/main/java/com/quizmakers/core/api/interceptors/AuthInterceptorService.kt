package com.quizmakers.core.api.interceptors

import android.content.SharedPreferences
import com.quizmakers.core.base.TOKEN_NAME_SP
import com.quizmakers.core.data.auth.remote.UserAuthenticateRequest
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.POST
//TODO ADD REFRESH TOKEN IF 403
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