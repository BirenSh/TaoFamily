package com.example.taofamily.features.initiation.data.remote

import com.example.taofamily.features.initiation.domain.model.InitiationFormFiled
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class InitRemoteDataSourceImpl(
    private val client: HttpClient // Injected via Koin
): InitRemoteDataSource {

    private fun mapToSheetRow (entry: InitiationFormFiled): List<Any>{
        return listOf(
            entry.personId,
            entry.personName,
            entry.personAge,
            entry.contact,
            entry.gender.label,       // Send Enum label as String
            entry.education,
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
    override suspend fun fetchAllEntries(): List<InitiationFormFiled> {
        TODO("Not yet implemented")
    }

    override suspend fun pushEntry(entry: InitiationFormFiled) {
       val rowValues = mapToSheetRow(entry)

        // The body must be formatted into a simple ValueRange JSON structure:
        val jsonBody = mapOf(
            "majorDimension" to "ROWS",
            "values" to listOf(rowValues)
        )

        // Construct the API endpoint for appending data
        val endpoint =
            "https://sheets.googleapis.com/v4/spreadsheets/SPREADSHEET_ID/values/SHEET_NAME!A1:Z1?valueInputOption=USER_ENTERED"
        try {
            val response = client.post {
                url(endpoint)
                contentType(ContentType.Application.Json)
                // Use kotlinx.serialization.json.Json to convert the Map to a JSON string
                setBody(Json.encodeToString(jsonBody))

            }

            // Check for success (200-level status codes)
            if (response.status.value !in 200..299) {
                val errorBody = response.bodyAsText()
                throw Exception("Sheets API Upload failed: ${response.status} - $errorBody")
            }
            println("🌐 Sheet upload successful for entry: ${entry.personName}")

        }catch (e: Exception){
            throw Exception("Network or Serialization Error during sheet upload: ${e.message}", e)
        }
    }

    override suspend fun deleteEntry(id: Long) {
        TODO("Not yet implemented")
    }

}