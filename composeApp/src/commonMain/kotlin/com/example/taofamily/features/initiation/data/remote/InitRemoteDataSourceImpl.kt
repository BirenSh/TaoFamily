package com.example.taofamily.features.initiation.data.remote

import com.example.taofamily.core.platform.auth.ServiceAccountAuth
import com.example.taofamily.core.utils.DateUtils
import com.example.taofamily.core.utils.Helper
import com.example.taofamily.core.utils.Helper.extractStartRowFromRange
import com.example.taofamily.features.initiation.domain.model.AppendResponse
import com.example.taofamily.features.initiation.domain.model.Gender
import com.example.taofamily.features.initiation.domain.model.InitiationFormFiled
import com.example.taofamily.features.initiation.domain.model.Master
import com.example.taofamily.features.initiation.domain.model.SheetsRequestDto
import com.example.taofamily.features.initiation.domain.model.SheetsResponse
import com.example.taofamily.features.initiation.domain.model.Temple
import com.example.taofamily.features.initiation.domain.model.UpdateResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement


class InitRemoteDataSourceImpl(
    private val client: HttpClient, // Injected via Koin,
    private val auth: ServiceAccountAuth

): InitRemoteDataSource {
    companion object {
        private const val SPREADSHEET_ID = "1BTWLrvIMjLQpWqG5QWWCoGPJGUyvGQPf7cBW7ZtSjW8"
        private val SHEET_NAME = DateUtils.getCurrentYearMonthDay().first ?: 2025
        private const val BASE_URL = "https://sheets.googleapis.com/v4/spreadsheets"
        private const val SHEET_RANGE = "!A:O"
        private val LOG = "InitRemoteDataSourceImpl_log"
    }


    override suspend fun fetchAllEntries(): List<InitiationFormFiled> {
        val endpoint = "$BASE_URL/$SPREADSHEET_ID/values/$SHEET_NAME$SHEET_RANGE"
        val token = auth.getAccessToken()

        try{
             // 2. Execute GET request and receive the response as the SheetsResponse DTO
            val response = client.get {
                url(endpoint)
                header(HttpHeaders.Authorization, "Bearer $token")
            }

            val responseText = response.bodyAsText()
            if (response.status.value !in 200..299) {
                throw Exception("Sheets API Fetch failed: ${response.status} - $responseText")
            }

            val parsed = Json.decodeFromString<SheetsResponse>(responseText)
            val sheetRows = parsed.values?.drop(1) ?: return emptyList()

            // Get starting row index from the range (e.g., "2025!A1:O100" -> 1)
            val startRow = extractStartRowFromRange(parsed.range)
            println("Sheet Start Row: $startRow")

            // Map each row to domain + compute sheet row index
            return sheetRows.mapIndexedNotNull { index, row ->
                // +1 because header was dropped, +startRow to get actual Sheet index
                val actualSheetRow = (startRow ?: 1) + index + 1
                mapRowToDomain(row)?.copy(sheetRowIndex = actualSheetRow)
            }

        } catch (e: Exception) {
            println("❌ Ktor Fetch Error: ${e.message}")
            throw e
        }
    }

    override suspend fun pushEntry(entry: InitiationFormFiled): AppendResponse? {
        val token = auth.getAccessToken()
        val rowValues = mapToSheetRow(entry).map { it.toString() }

        val jsonBody = SheetsRequestDto(
            majorDimension = "ROWS",
            values = listOf(rowValues)
        )

        val endpoint =
            "$BASE_URL/$SPREADSHEET_ID/values/$SHEET_NAME$SHEET_RANGE:append?valueInputOption=USER_ENTERED"

        try {
            val response = client.post(endpoint) {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(jsonBody)
            }

            val bodyText = response.bodyAsText()
            if (response.status.value !in 200..299) {
                throw Exception("Sheets API Upload failed: ${response.status} - $bodyText")
            }

           val filteredResponse =   Json.decodeFromString<AppendResponse?>(bodyText)
            println("fetchAllEntries: $filteredResponse")
            return filteredResponse

        } catch (e: Exception) {
            throw Exception("Network or Serialization Error during sheet upload: ${e.message}", e)
        }
    }


    override suspend fun updateEntry(entry: InitiationFormFiled): UpdateResponse? {
        val token = auth.getAccessToken()
        val rowValues = mapToSheetRow(entry).map { it.toString() }

        val rowIndex = entry.sheetRowIndex
        if (rowIndex == null) throw Exception("Entry with ID ${entry.personId} not found in sheet")

        val sheetRowIndex = rowIndex
        val jsonBody = SheetsRequestDto(
            majorDimension = "ROWS",
            values = listOf(rowValues)
        )

        val endpoint =
            "$BASE_URL/$SPREADSHEET_ID/values/$SHEET_NAME!A$sheetRowIndex?valueInputOption=USER_ENTERED"

        try {
            val response = client.put(endpoint) {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(jsonBody)
            }

            val bodyText = response.bodyAsText()
            if (response.status.value !in 200..299) {
                throw Exception("Sheets API Update failed: ${response.status.value} - $bodyText")
            }

            return Json.decodeFromString<UpdateResponse?>(bodyText)

        } catch (e: Exception) {
            println("===eror updateEntry: ${e.message}")
            throw Exception("Network or Serialization Error during sheet update: ${e.message}", e)
        }
    }

    override suspend fun deleteEntry(id: String) {
        TODO("Not yet implemented")
    }


    private fun mapRowToDomain(row: List<JsonElement>): InitiationFormFiled? {
        fun JsonElement?.safeString(): String = (this?.toString()?.trim('"') ?: "").trim()
        val rowData = row.map { it.safeString() }

        println("===name: ${rowData.getOrNull(1)} and check: ${ rowData.getOrNull(13)?.toBooleanStrictOrNull()} ")
        return try {
            InitiationFormFiled(
                personId = rowData.getOrNull(0) ?: "",
                personName = rowData.getOrNull(1) ?: "",
                personAge = rowData.getOrNull(2)?.toIntOrNull() ?: 0,
                gender = Gender.entries.find { it.label.equals(rowData.getOrNull(3), true) } ?: Gender.NONE,
                education = rowData.getOrNull(4) ?: "",
                contact = rowData.getOrNull(5) ?: "",
                fullAddress = rowData.getOrNull(6) ?: "",
                masterName = Master.entries.find { it.label.equals(rowData.getOrNull(7), true) } ?: Master.NONE,
                introducerName = rowData.getOrNull(8) ?: "",
                guarantorName = rowData.getOrNull(9) ?: "",
                templeName = Temple.entries.find { it.label.equals(rowData.getOrNull(10), true) } ?: Temple.NONE,
                initiationDate = rowData.getOrNull(11) ?: "",
                meritFee = rowData.getOrNull(12)?.toDoubleOrNull() ?: 0.0,
                is2DaysDharmaClassAttend = when (rowData.getOrNull(13)?.trim()?.lowercase()) {
                    "true", "yes", "1" -> true
                    else -> false
                },
                dharmaMeetingDate = rowData.getOrNull(14) ?: "NA",
                sheetRowIndex = rowData.getOrNull(15)?.toLongOrNull()
            )
        } catch (e: Exception) {
            println("❌ MAPPING FAILED for row: $rowData -> ${e.message}")
            null
        }
    }

    private fun mapToSheetRow(entry: InitiationFormFiled): List<Any> {
        return listOf(
            entry.personId,
            entry.personName,
            entry.personAge,
            entry.gender.label,
            entry.education,
            entry.contact,
            entry.fullAddress,
            entry.masterName.label,
            entry.introducerName,
            entry.guarantorName,
            entry.templeName.label,
            entry.initiationDate,
            entry.meritFee,
            entry.is2DaysDharmaClassAttend,
            entry.dharmaMeetingDate
        )
    }
    // endregion

}