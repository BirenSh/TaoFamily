package com.example.taofamily.features.initiation.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class SheetsRequestDto(
    val majorDimension: String,
    val values: List<List<String>>
)

@Serializable
data class SheetsResponse(
    // Identifies the range requested (e.g., "Sheet1!A1:O1000")
    val range: String? = null,

    // Indicates how the major dimension is structured ("ROWS" or "COLUMNS")
    val majorDimension: String? = null,

    // We use JsonElement to safely handle varied types (strings, numbers, booleans)
    val values: List<List<JsonElement>>? = null
)


@Serializable
data class AppendResponse(
    val spreadsheetId: String? = null,
    val tableRange: String? = null,
    val updates: UpdateData? = null
)

@Serializable
data class UpdateData(
    val spreadsheetId: String? = null,
    val updatedRange: String? = null,
    val updatedRows: Int? = null,
    val updatedColumns: Int? = null,
    val updatedCells: Int? = null
)


@Serializable
data class UpdateResponse(
    val spreadsheetId: String? = null,
    val updatedRange: String? = null,
    val updatedRows: Int? = null,
    val updatedColumns: Int? = null,
    val updatedCells: Int? = null
)
