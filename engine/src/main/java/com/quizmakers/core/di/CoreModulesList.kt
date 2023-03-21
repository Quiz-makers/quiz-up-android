package com.quizmakers.core.di

import org.koin.core.context.loadKoinModules
import org.koin.ksp.generated.module

class CoreModulesList {
    fun init() = loadKoinModules(
        listOf(CoreModule().module)
    )
}