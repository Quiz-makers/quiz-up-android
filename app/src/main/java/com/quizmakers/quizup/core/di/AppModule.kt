package com.quizmakers.quizup.core.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.quizmakers.quizup")
class AppModule

@Single
class StartedModule() {}