package com.example.taofamily.db

import app.cash.sqldelight.ColumnAdapter
import com.example.taofamily.features.initiation.domain.model.Gender
import com.example.taofamily.features.initiation.domain.model.Master
import com.example.taofamily.features.initiation.domain.model.Temple

// Boolean adapter
//object BooleanColumnAdapter : ColumnAdapter<Boolean, Long> {
//    override fun decode(databaseValue: Long): Boolean = databaseValue == 1L
//    override fun encode(value: Boolean): Long = if (value) 1L else 0L
//}

// Gender adapter
object GenderColumnAdapter : ColumnAdapter<Gender, String> {
    override fun decode(databaseValue: String): Gender =
        Gender.entries.find { it.name == databaseValue } ?: Gender.NONE
    override fun encode(value: Gender): String = value.name
}

// Master adapter
object MasterColumnAdapter : ColumnAdapter<Master, String> {
    override fun decode(databaseValue: String): Master =
        Master.entries.find { it.name == databaseValue } ?: Master.NONE
    override fun encode(value: Master): String = value.name
}

// Temple adapter
object TempleColumnAdapter : ColumnAdapter<Temple, String> {
    override fun decode(databaseValue: String): Temple =
        Temple.entries.find { it.name == databaseValue } ?: Temple.NONE
    override fun encode(value: Temple): String = value.name
}

object LongColumnAdapter : ColumnAdapter<Long, Long> {
    // Database Long -> Kotlin Long
    override fun decode(databaseValue: Long): Long = databaseValue

    // Kotlin Long -> Database Long
    override fun encode(value: Long): Long = value
}