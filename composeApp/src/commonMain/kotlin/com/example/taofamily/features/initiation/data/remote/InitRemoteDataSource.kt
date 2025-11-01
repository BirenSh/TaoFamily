package com.example.taofamily.features.initiation.data.remote

import com.example.taofamily.features.initiation.domain.model.AppendResponse
import com.example.taofamily.features.initiation.domain.model.InitiationFormFiled
import com.example.taofamily.features.initiation.domain.model.UpdateResponse

interface InitRemoteDataSource {
    suspend fun fetchAllEntries(): List<InitiationFormFiled>

    suspend fun pushEntry(entry: InitiationFormFiled): AppendResponse?
    suspend fun updateEntry(entry: InitiationFormFiled): UpdateResponse?

    suspend fun deleteEntry(id: String)

}
