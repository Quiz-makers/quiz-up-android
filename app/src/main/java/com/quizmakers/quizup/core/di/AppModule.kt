package com.quizmakers.quizup.core.di

import com.quizmakers.quizup.BuildConfig
import com.quizmakers.quizup.core.api.QuizUpApiService
import com.quizmakers.quizup.core.api.Server
import okhttp3.OkHttpClient
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import retrofit2.Retrofit
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory

@Module
@ComponentScan("com.quizmakers.quizup")
class AppModule {
    @Single
    fun provideQuizUpApiService(retrofit: Retrofit): QuizUpApiService =
        retrofit.create(QuizUpApiService::class.java)

    @Single
    fun provideRetrofit(client: OkHttpClient) = Retrofit.Builder()
        .baseUrl(Server.SERVER_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    @Single
    fun provideInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Single
    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor) = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()
}

@Single
class StartedModule() {}