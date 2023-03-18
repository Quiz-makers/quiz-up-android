package com.quizmakers.quizup

import android.app.Application
import com.quizmakers.quizup.core.di.AppModule
import com.quizmakers.quizup.core.di.StartedModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.ksp.generated.module

class QuizUpApp : Application() {

    val startedModule: StartedModule by inject()
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