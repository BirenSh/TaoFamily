package com.example.taofamily

import android.app.Application
import org.koin.android.ext.koin.androidContext
import com.example.taofamily.core.di.initKoin
class AndroidApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        // This initializes the KMP shared module's Koin graph
        initKoin {
            // This line registers the Android Context as a dependency in Koin.
            androidContext(this@AndroidApplication)
        }
    }
}