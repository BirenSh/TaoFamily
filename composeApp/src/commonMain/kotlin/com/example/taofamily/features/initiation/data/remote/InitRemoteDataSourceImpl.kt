package com.example.taofamily.features.initiation.data.remote

import com.example.taofamily.core.platform.auth.ServiceAccountAuth
import com.example.taofamily.core.platform.getServiceAccountProvider
import com.example.taofamily.features.initiation.domain.model.InitiationFormFiled
import com.example.taofamily.features.initiation.domain.model.SheetsRequestDto
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class InitRemoteDataSourceImpl(
    private val client: HttpClient, // Injected via Koin,
    private val auth: ServiceAccountAuth

): InitRemoteDataSource {
    companion object{
        const val SHEETID = "1BTWLrvIMjLQpWqG5QWWCoGPJGUyvGQPf7cBW7ZtSjW8"
         const val SHEET_NAME = "2025"
        const  val baseUrl = "https://sheets.googleapis.com/v4/spreadsheets"


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
    override suspend fun fetchAllEntries(): List<InitiationFormFiled> {
        TODO("Not yet implemented")
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
        val endpoint = "$baseUrl/$SHEETID/values/$SHEET_NAME!A:Z:append?valueInputOption=USER_ENTERED"

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

    override suspend fun deleteEntry(id: Long) {
        TODO("Not yet implemented")
    }

}