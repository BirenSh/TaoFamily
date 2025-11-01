package com.example.taofamily.core.platform

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import org.koin.mp.KoinPlatformTools
import java.util.Locale

private var appContext: Context? = null

actual fun applyLanguage(langCode: String) {
    val context = KoinPlatformTools.defaultContext().get().get<Context>() ?: return

    val locale = when (langCode) {
        "zh" -> Locale.CHINESE
        "hi" -> Locale("hi", "IN")
        else -> Locale.ENGLISH
    }

    Locale.setDefault(locale)

    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)

    context.createConfigurationContext(config)

    @Suppress("DEPRECATION")
    context.resources.updateConfiguration(config, context.resources.displayMetrics)
}