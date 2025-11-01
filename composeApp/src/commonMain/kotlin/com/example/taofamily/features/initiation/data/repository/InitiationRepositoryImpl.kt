package com.example.taofamily.features.initiation.data.repository

import com.example.taofamily.core.utils.AppConstant
import com.example.taofamily.core.utils.Helper.extractStartRowFromRange
import com.example.taofamily.features.initiation.data.local.InitLocalDataSource
import com.example.taofamily.features.initiation.data.remote.InitRemoteDataSource
import com.example.taofamily.features.initiation.domain.model.InitiationFormFiled
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class InitiationRepositoryImpl(
    val localDataSource: InitLocalDataSource,
    val remoteDataSource: InitRemoteDataSource,
    private val dispatcher: CoroutineContext // Injected via Koin (Dispatchers.Default or IO)

) : InitiationRepository {

    override fun getAllEntries(): Flow<List<InitiationFormFiled>> {
        return localDataSource.getAllEntries()
    }

    override suspend fun saveEntry(entry: InitiationFormFiled) = withContext(dispatcher){
        val updateEntry = entry.copy(personId = AppConstant.getRandomUniqueId())
        //Asynchronously push to server
        try {
            // remote save first
            val sheetResponse = remoteDataSource.pushEntry(updateEntry)
            if (sheetResponse != null){
                val rowIndex = extractStartRowFromRange(sheetResponse.updates?.updatedRange)
                // Save locally with correct sheetRowIndex
                val entryWithRow = updateEntry.copy(sheetRowIndex = rowIndex)
                localDataSource.saveEntry(entryWithRow)
            }

        }catch (e: Exception){
            throw e
        }

    }

    override suspend fun deleteEntry(id: String)  = withContext(dispatcher){
        localDataSource.deleteEntry(id)
        try {
            remoteDataSource.deleteEntry(id)
        }catch (e: Exception){
            throw e
        }
    }

    override suspend fun syncInitialData() = withContext(dispatcher){
        try {
            val fetchData = remoteDataSource.fetchAllEntries()
            if (fetchData.isNotEmpty()){
                localDataSource.replaceAllEntries(fetchData)
            }
        }catch (e: Exception){
            throw e
        }
    }

    override suspend fun updateEntry(entry: InitiationFormFiled) {
        //Asynchronously push to server
        try {
            // remote save first
            remoteDataSource.updateEntry(entry)

            // if remote success without exception save locally
            localDataSource.updateEntry(entry)

        }catch (e: Exception){
            throw e
        }

    }

    override suspend fun getEntryById(id: String): InitiationFormFiled? {
        return localDataSource.getEntryById(id = id)

    }


}