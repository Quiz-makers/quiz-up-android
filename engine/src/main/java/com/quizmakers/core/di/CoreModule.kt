package com.quizmakers.core.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.quizmakers.core.api.Server
import com.quizmakers.core.api.interceptors.AuthInterceptor
import com.quizmakers.core.api.nullOnEmptyConverterFactory
import com.quizmakers.core.base.AUTH_CHANNEL_SP
import com.quizmakers.core.data.quizzes.remote.QuizzesService
import com.quizmakers.core.data.auth.remote.AuthService
import com.quizmakers.core.domain.session.SessionManager
import okhttp3.OkHttpClient
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import retrofit2.Retrofit
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory

@Module
@ComponentScan("com.quizmakers.core")
class CoreModule {
    @Single
    fun provideRetrofit(client: OkHttpClient) = Retrofit.Builder()
        .client(client)
        .baseUrl(Server.BASE_URL)
        .addConverterFactory(nullOnEmptyConverterFactory)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Single
    fun provideChuckerInterceptor(context: Context) = ChuckerInterceptor.Builder(context).collector(
        ChuckerCollector(
            context = context,
            showNotification = true,
            retentionPeriod = RetentionManager.Period.ONE_HOUR
        )
    ).maxContentLength(250_000L).redactHeaders(emptySet()).alwaysReadResponseBody(true).build()

    @Single
    fun provideInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Single
    fun getSharedPrefs(androidApplication: Application): SharedPreferences {
        return androidApplication.getSharedPreferences(
            AUTH_CHANNEL_SP,
            android.content.Context.MODE_PRIVATE
        )
    }

    @Single
    fun getSharedPrefsEdit(sharedPreferences: SharedPreferences): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }

    @Single
    fun provideAuthInterceptor(sessionManager: SessionManager) =
        AuthInterceptor(sessionManager.getToken())

    @Single
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        interceptor: HttpLoggingInterceptor,
        chuckerInterceptor: ChuckerInterceptor
    ) = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(interceptor)
        .addInterceptor(chuckerInterceptor)

    @Single
    fun provideAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)


    @Single
    fun provideQuizUpApiService(retrofit: Retrofit): QuizzesService =
        retrofit.create(QuizzesService::class.java)

}

@Single
class StartedModule() {}