package com.example.taofamily.features.initiation.data.local

import com.example.taofamily.features.initiation.domain.model.InitiationFormFiled
import kotlinx.coroutines.flow.Flow

interface InitLocalDataSource {
    fun getAllEntries(): Flow<List<InitiationFormFiled>>

    suspend fun saveEntry(entry: InitiationFormFiled)

    suspend fun updateEntry(entry: InitiationFormFiled)

    suspend fun deleteEntry(id: String)

    suspend fun replaceAllEntries(entries: List<InitiationFormFiled>)
    suspend fun getEntryById(id: String): InitiationFormFiled?


}