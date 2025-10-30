package com.example.taofamily.features.initiation.data.remote

import com.example.taofamily.core.platform.auth.ServiceAccountAuth
import com.example.taofamily.core.utils.DateUtils
import com.example.taofamily.features.initiation.domain.model.Gender
import com.example.taofamily.features.initiation.domain.model.InitiationFormFiled
import com.example.taofamily.features.initiation.domain.model.Master
import com.example.taofamily.features.initiation.domain.model.SheetsRequestDto
import com.example.taofamily.features.initiation.domain.model.SheetsResponse
import com.example.taofamily.features.initiation.domain.model.Temple
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.pluginOrNull
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
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
    companion object{
        private const val SPREADSHEET_ID = "1BTWLrvIMjLQpWqG5QWWCoGPJGUyvGQPf7cBW7ZtSjW8"
        private val SHEET_NAME = DateUtils.getCurrentYearMonthDay().first?:2025
        private const  val baseUrl = "https://sheets.googleapis.com/v4/spreadsheets"
        val SHEET_RANGE = "${SHEET_NAME}!A:O"


    }


    override suspend fun fetchAllEntries(): List<InitiationFormFiled> {
        val endpoint = "$baseUrl/$SPREADSHEET_ID/values/$SHEET_RANGE"
        val token = auth.getAccessToken()
         return try {

             // 2. Execute GET request and receive the response as the SheetsResponse DTO
            val response = client.get {
                url(endpoint)
                header(HttpHeaders.Authorization, "Bearer $token")
            }.bodyAsText()

             val parsed = try {
                 Json.decodeFromString<SheetsResponse>(response)
             } catch (e: Exception) {
                 throw e
             }

            // 3. Extract the nested list of values (rows), skip header (Row 1)
            val sheetRows = parsed.values?.drop(1) ?: return emptyList()

            // 4. Map each JSON row into your InitiationFormFiled entity
             sheetRows.mapNotNull { row ->
                mapRowToDomain(row)
            }

        } catch (e: Exception) {
            println("Ktor Fetch Error (ReadAll): ${e.message}")
             throw e
        }

    }

    override suspend fun pushEntry(entry: InitiationFormFiled) {

        val token = auth.getAccessToken() // Get token here

        val rowValues = mapToSheetRow(entry).map { it.toString() }
        // The body must be formatted into a simple ValueRange JSON structure:
        val jsonBody = SheetsRequestDto(
            majorDimension = "ROWS",
            values = listOf(rowValues) // List of lists: [[value1, value2, ...]]
        )

        // Construct the API endpoint for appending data
        val endpoint = "$baseUrl/$SPREADSHEET_ID/values/$SHEET_NAME!A:Z:append?valueInputOption=USER_ENTERED"

        try {
            val response = client.post(endpoint) {
                header("Authorization", "Bearer $token") // Add manually
                contentType(ContentType.Application.Json)
                setBody(jsonBody)

            }

            // Check for success (200-level status codes)
            if (response.status.value !in 200..299) {
                val errorBody = response.bodyAsText()
                throw Exception("Sheets API Upload failed: ${response.status} - $errorBody")
            }

        }catch (e: Exception){
            throw Exception("Network or Serialization Error during sheet upload: ${e.message}", e)
        }
    }

    override suspend fun updateEntry(entry: InitiationFormFiled) {

    }

    override suspend fun deleteEntry(id: String) {
        TODO("Not yet implemented")
    }


    private fun mapRowToDomain(row: List<JsonElement>): InitiationFormFiled? {
        // Helper to safely extract String content from JsonElement
        fun JsonElement?.safeString(): String = (this?.toString()?.trim('"') ?: "").trim()

        // Safely parse the row data, assuming 0-based column index (A=0, B=1, ...)
        val rowData = row.map { it.safeString() }

        return try {
            InitiationFormFiled(
                personId = rowData.getOrNull(0) ?: "",
                personName = rowData.getOrNull(1) ?: "",
                personAge = rowData.getOrNull(2)?.toIntOrNull() ?: 0,
                contact = rowData.getOrNull(3) ?: "",
                gender = Gender.entries.find { it.label.equals(rowData.getOrNull(4), true) } ?: Gender.NONE,
                education = rowData.getOrNull(5) ?: "",
                fullAddress = rowData.getOrNull(6) ?: "",
                masterName = Master.entries.find { it.label.equals(rowData.getOrNull(7), true) } ?: Master.NONE,
                introducerName = rowData.getOrNull(8) ?: "",
                guarantorName = rowData.getOrNull(9) ?: "",
                templeName = Temple.entries.find { it.label.equals(rowData.getOrNull(10), true) } ?: Temple.NONE,
                initiationDate = rowData.getOrNull(11) ?: "",
                meritFee = rowData.getOrNull(12)?.toDoubleOrNull() ?: 0.0,
                is2DaysDharmaClassAttend = rowData.getOrNull(13)?.toBooleanStrictOrNull() ?: false,
                dharmaMeetingDate = rowData.getOrNull(14) ?: ""
            )
        } catch (e: Exception) {
            println("MAPPING FAILED for row: $rowData. Error: ${e.message}")
            null // Return null to discard a bad row
        }
    }


    private fun mapToSheetRow (entry: InitiationFormFiled): List<Any>{
        return listOf(
            entry.personId,
            entry.personName,
            entry.personAge,
            entry.gender.label,       // Send Enum label as String
            entry.education,
            entry.contact,
            entry.fullAddress,
            entry.masterName.label,   // Send Enum label as String
            entry.introducerName,
            entry.guarantorName,
            entry.templeName.label,   // Send Enum label as String
            entry.initiationDate,
            entry.meritFee,
            entry.is2DaysDharmaClassAttend, // Sends boolean (Sheets reads as TRUE/FALSE)
            entry.dharmaMeetingDate
        )

    }

}