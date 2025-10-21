package com.example.taofamily.features.initiation.data.repository

import com.example.taofamily.features.initiation.domain.model.InitiationFormFiled
import kotlinx.coroutines.flow.Flow

interface InitiationRepository {

    /**
     * R - Read Operation: Streams all initiation records reactively from the local database.
     * This is the Single Source of Truth for the UI.
     */
    fun getAllEntries(): Flow<List<InitiationFormFiled>>

    /**
     * C/U - Create/Update Operation: Saves the entry locally first, then asynchronously
     * pushes the change to the remote Google Sheet (the sync logic).
     */
    suspend fun saveEntry(entry: InitiationFormFiled)


    /**
     * D - Delete Operation: Deletes the entry locally first, then asynchronously
     * deletes it from the remote Google Sheet.
     */
    suspend fun deleteEntry(id: Long)


    /**
     * Special Sync Operation: Handles the first-time data loading.
     * Fetches ALL data from the Google Sheet and inserts it into the local database.
     * This is called immediately after a successful login.
     */
    suspend fun syncInitialData()


}