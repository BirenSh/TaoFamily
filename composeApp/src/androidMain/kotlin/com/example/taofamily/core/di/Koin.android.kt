package com.example.taofamily.core.di

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.taofamily.core.platform.DriverFactory
import com.example.taofamily.db.DatabaseWrapper
import org.koin.core.module.Module
import org.koin.dsl.module

@RequiresApi(Build.VERSION_CODES.O)
actual fun platformModule(): Module  = module{

    single {
        DriverFactory(context = get<Context>())
    }
    single {
        DatabaseWrapper(driverFactory = get())

    }

}