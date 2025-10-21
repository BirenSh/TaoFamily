package com.example.taofamily.core.platform

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.taofamily.db.InitiationDatabase

actual class DriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = InitiationDatabase.Schema,
            context = context,
            name = "InitiationDatabase.db"
        )
    }
}