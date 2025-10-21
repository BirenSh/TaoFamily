package com.example.taofamily.db

import com.example.taofamily.core.platform.DriverFactory

class DatabaseWrapper(driverFactory: DriverFactory) {
    private val database: InitiationDatabase = InitiationDatabase(
        driver = driverFactory.createDriver(),
        InitiationAdapter = Initiation.Adapter(
            genderAdapter = GenderColumnAdapter,
            masterNameAdapter = MasterColumnAdapter,
            templeNameAdapter = TempleColumnAdapter
        )

    )


    val initiationQueries: InitiationQueries
        get() = database.initiationQueries
}