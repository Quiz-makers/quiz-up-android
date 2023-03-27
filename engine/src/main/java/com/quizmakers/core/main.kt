package com.quizmakers.core

import com.quizmakers.core.data.auth.remote.AuthService
import com.quizmakers.core.data.auth.remote.UserAuthenticateRequest
import com.quizmakers.core.di.CoreModule
import kotlinx.coroutines.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

class TestImplementation : KoinComponent {
    val testModule: AuthService by inject()

}

fun main() {
    startKoin {
        modules(
            CoreModule().module
        )

    }
    runBlocking {

        runCatching {
            TestImplementation().testModule.signIn(
                UserAuthenticateRequest(
                    email = "asy@dodtx.com",
                    password = "test1234"
                )
            )
        }.onFailure {
            it
        }.onSuccess {
            it
        }
    }
}