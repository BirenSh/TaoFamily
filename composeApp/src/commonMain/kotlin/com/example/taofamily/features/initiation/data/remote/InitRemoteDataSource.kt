package com.example.taofamily.features.initiation.data.remote

import com.example.taofamily.features.initiation.domain.model.InitiationFormFiled

interface InitRemoteDataSource {
    suspend fun fetchAllEntries(): List<InitiationFormFiled>

    suspend fun pushEntry(entry: InitiationFormFiled)
    suspend fun updateEntry(entry: InitiationFormFiled)

    suspend fun deleteEntry(id: String)

}
