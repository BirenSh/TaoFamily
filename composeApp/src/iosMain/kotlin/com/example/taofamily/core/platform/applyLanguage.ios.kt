package com.example.taofamily.core.platform

import platform.Foundation.NSUserDefaults

actual fun applyLanguage(langCode: String) {

    // Store language preference
    NSUserDefaults.standardUserDefaults.setObject(
        listOf(langCode),
        forKey = "AppleLanguages"
    )
    NSUserDefaults.standardUserDefaults.synchronize()

    println("✅ Language set to: $langCode")
}