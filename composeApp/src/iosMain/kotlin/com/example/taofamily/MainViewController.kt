package com.example.taofamily

import androidx.compose.ui.window.ComposeUIViewController
import com.example.taofamily.core.di.initKoin
import platform.UIKit.UIViewController

private var koinInitialized = false

fun MainViewController(): UIViewController {
    // Initialize Koin ONCE, not on every call
    if (!koinInitialized) {
        initKoin()
        koinInitialized = true
    }

    return ComposeUIViewController(
        configure = {
            enforceStrictPlistSanityCheck = false
        }
    ) {
        App()
    }
}