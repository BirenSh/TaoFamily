package com.example.taofamily.features.initiation.data.local

interface SettingPreFrance {

    // Checks if a user token or session flag is set
    fun getIsLoggedIn(): Boolean
    suspend fun setIsLoggedIn(value: Boolean)
}