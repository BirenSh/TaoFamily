package com.example.taofamily.features.initiation.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.taofamily.db.DatabaseWrapper
import com.example.taofamily.features.initiation.domain.model.InitiationFormFiled
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex

class InitLocalDataSourceImpl(
    private val dbWrapper: DatabaseWrapper
) : InitLocalDataSource {

    private val mutex = Mutex()
    private val queries = dbWrapper.initiationQueries

    // Map from SQLDelight Initiation to domain InitiationFormFiled
    private fun mapToDomain(dbRecord: com.example.taofamily.db.Initiation): InitiationFormFiled {
        return InitiationFormFiled(
            personId = dbRecord.personId,
            personName = dbRecord.personName,
            personAge = dbRecord.personAge.toInt(), // Long → Int
            contact = dbRecord.contact,
            gender = dbRecord.gender, // Already Gender enum (thanks to adapter)
            education = dbRecord.education,
            fullAddress = dbRecord.fullAddress,
            masterName = dbRecord.masterName, // Already Master enum
            introducerName = dbRecord.introducerName,
            guarantorName = dbRecord.guarantorName,
            templeName = dbRecord.templeName, // Already Temple enum
            initiationDate = dbRecord.initiationDate,
            meritFee = dbRecord.meritFee,
            is2DaysDharmaClassAttend = dbRecord.is2DaysDharmaClassAttend, // Already Boolean
            dharmaMeetingDate = dbRecord.dharmaMeetingDate
        )
    }

    override fun getAllEntries(): Flow<List<InitiationFormFiled>> {
        val entriesFlow = queries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { list -> list.map { mapToDomain(it) } }

        return entriesFlow
    }

    override suspend fun saveEntry(entry: InitiationFormFiled) {

        queries.transaction {
                queries.insert(
                    personId = entry.personId,
                    personName = entry.personName,
                    personAge = entry.personAge.toLong(), // Int → Long
                    contact = entry.contact,
                    gender = entry.gender, // Enum (adapter handles conversion)
                    education = entry.education,
                    fullAddress = entry.fullAddress,
                    masterName = entry.masterName, // Enum
                    introducerName = entry.introducerName,
                    guarantorName = entry.guarantorName,
                    templeName = entry.templeName, // Enum
                    initiationDate = entry.initiationDate,
                    meritFee = entry.meritFee,
                    is2DaysDharmaClassAttend = entry.is2DaysDharmaClassAttend, // Boolean
                    dharmaMeetingDate = entry.dharmaMeetingDate
                )
            // Verify the save
            val count = queries.countAll().executeAsOne()
        }
    }

    override suspend fun updateEntry(entry: InitiationFormFiled) {
        queries.transaction {
            queries.update(
                personId = entry.personId,
                personName = entry.personName,
                personAge = entry.personAge.toLong(), // Int → Long
                contact = entry.contact,
                gender = entry.gender, // Enum (adapter handles conversion)
                education = entry.education,
                fullAddress = entry.fullAddress,
                masterName = entry.masterName, // Enum
                introducerName = entry.introducerName,
                guarantorName = entry.guarantorName,
                templeName = entry.templeName, // Enum
                initiationDate = entry.initiationDate,
                meritFee = entry.meritFee,
                is2DaysDharmaClassAttend = entry.is2DaysDharmaClassAttend, // Boolean
                dharmaMeetingDate = entry.dharmaMeetingDate
            )
            // Verify the save
            val count = queries.countAll().executeAsOne()
        }
    }

    override suspend fun deleteEntry(id: String) {
        queries.deleteById(id)
    }

    override suspend fun replaceAllEntries(entries: List<InitiationFormFiled>) {
        queries.transaction {
            queries.deleteAll()
            entries.forEach { entry ->
                queries.insert(
                    personId = entry.personId,
                    personName = entry.personName,
                    personAge = entry.personAge.toLong(),
                    contact = entry.contact,
                    gender = entry.gender,
                    education = entry.education,
                    fullAddress = entry.fullAddress,
                    masterName = entry.masterName,
                    introducerName = entry.introducerName,
                    guarantorName = entry.guarantorName,
                    templeName = entry.templeName,
                    initiationDate = entry.initiationDate,
                    meritFee = entry.meritFee,
                    is2DaysDharmaClassAttend = entry.is2DaysDharmaClassAttend,
                    dharmaMeetingDate = entry.dharmaMeetingDate
                )
            }
        }
    }

    override suspend fun getEntryById(id: String) : InitiationFormFiled?{
        val record = queries
            .selectByPersonId(id)
            .executeAsOneOrNull() // CRITICAL: Executes the query once

        // 2. Map the resulting database record to your Domain Entity.
        return record?.let { mapToDomain(it) }
    }
}