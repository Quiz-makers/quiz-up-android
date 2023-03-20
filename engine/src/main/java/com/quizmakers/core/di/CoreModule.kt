package com.quizmakers.core.di

import com.quizmakers.core.base.ApiType
import com.quizmakers.core.data.quizzes.remote.QuizzesService
import com.quizmakers.core.base.BaseUrlInterceptor
import com.quizmakers.core.data.auth.remote.AuthService
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
        .baseUrl(ApiType.AUTH.url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Single
    fun provideInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Single
    fun provideBaseUrlInterceptor(
    ): BaseUrlInterceptor = BaseUrlInterceptor()


    @Single
    fun provideOkHttpClient(
        baseUrlInterceptor: BaseUrlInterceptor,
        interceptor: HttpLoggingInterceptor
    ) = OkHttpClient.Builder()
       // .addInterceptor(baseUrlInterceptor) //TODO Add this after /quizzes url will be available
        .addInterceptor(interceptor)
        .build()

    @Single
    fun provideAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)


    @Single
    fun provideQuizUpApiService(retrofit: Retrofit): QuizzesService =
        retrofit.create(QuizzesService::class.java)

}

@Single
class StartedModule() {}