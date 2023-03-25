package com.quizmakers.core

import com.quizmakers.core.data.auth.remote.UserRegisterRequest
import com.quizmakers.core.di.CoreModule
import com.quizmakers.core.domain.auth.useCases.CoreSignUpUseCase
import kotlinx.coroutines.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

class TestImplementation : KoinComponent {
    val testModule: CoreSignUpUseCase by inject()

}

fun main() {
    startKoin {
        modules(
            CoreModule().module
        )

    }
    runBlocking {
        val userAuthenticate = UserRegisterRequest(
            name = "Pawel",
            surname = "Krzysciak",
            userName = "Lukieoo",
            email = "lukieoo@gmail.com",
            password = "test1234",
        )
    }
}