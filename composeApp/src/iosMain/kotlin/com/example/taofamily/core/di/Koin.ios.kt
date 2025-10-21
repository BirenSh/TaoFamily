package com.example.taofamily.core.di

import com.example.taofamily.core.platform.DriverFactory
import com.example.taofamily.db.DatabaseWrapper
import org.koin.dsl.module
import org.koin.core.module.Module

actual fun platformModule(): Module  = module{
    single {
        DriverFactory()
    }
    single {
        DatabaseWrapper(driverFactory = get())
    }
}