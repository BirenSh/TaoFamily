package com.example.taofamily.core.utils

import cafe.adriel.voyager.navigator.Navigator
import com.example.taofamily.core.platform.checkInternetConnection
import com.example.taofamily.features.initiation.presentation.login_screen.LoginScreen

object  Helper {
     fun extractStartRowFromRange(range: String?): Long? {
        if (range == null) return null
        // Example: "2025!A1:O100" or "Sheet1!A2:O100"
        val match = Regex("![A-Z]+(\\d+):").find(range)
        return match?.groupValues?.getOrNull(1)?.toLongOrNull()
    }

    /**
     * Checks for an active internet connection by attempting to connect to Google's public DNS server.
     * This is a simple, platform-agnostic way to check for internet access.
     * It should be called from a coroutine, as it performs a network operation.
     *
     * @return true if the connection is successful, false otherwise.
     */
    suspend fun isInternetAvailable(): Boolean {
        return checkInternetConnection()

    }

    fun logoutApp(navigator: Navigator?) {
        navigator?.replaceAll(LoginScreen())
    }
}

