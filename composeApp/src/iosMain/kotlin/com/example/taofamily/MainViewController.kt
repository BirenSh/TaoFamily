package com.example.taofamily

import androidx.compose.ui.window.ComposeUIViewController
import com.example.taofamily.core.di.initKoin

fun MainViewController() = ComposeUIViewController(

    configure = {
        initKoin()
    }
) { App() }