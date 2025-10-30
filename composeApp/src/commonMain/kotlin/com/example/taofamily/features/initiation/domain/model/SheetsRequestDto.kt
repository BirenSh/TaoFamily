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