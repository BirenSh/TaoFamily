package com.example.taofamily.features.initiation.data.local

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set

class SettingPrefImp(
    private val settings: Settings
): SettingPreFrance {
    // Define the keys for persistence
    private val KEY_LOGGED_IN = "is_logged_in"
    private val KEY_SYNC_COMPLETE = "is_initial_sync_complete"

    override fun getIsLoggedIn(): Boolean {
        return settings.getBoolean(KEY_LOGGED_IN, false)
    }

    override suspend fun setIsLoggedIn(value: Boolean) {
        settings[KEY_LOGGED_IN] = value
    }

}