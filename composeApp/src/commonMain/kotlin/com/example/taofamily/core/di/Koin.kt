package com.example.taofamily.core.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

expect fun platformModule(): Module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {

    startKoin {
        appDeclaration()
        modules(
            networkModule,
            platformModule(), // <-- Load the platform-specific module here
            initiationModule,
        )
    }
}
