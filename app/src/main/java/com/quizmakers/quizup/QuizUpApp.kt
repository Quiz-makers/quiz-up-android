package com.quizmakers.quizup

import android.app.Application
import com.quizmakers.quizup.core.di.AppModule
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.ksp.generated.module

class QuizUpApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin()
    }

    private fun startKoin() {
        org.koin.core.context.startKoin {
            androidContext(this@QuizUpApp)
            modules(AppModule().module)
        }
    }
}