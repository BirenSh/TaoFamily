package com.example.taofamily.core.platform

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.example.taofamily.db.InitiationDatabase

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = InitiationDatabase.Schema,
            name = "InitiationDatabase.db"
        )
    }
}