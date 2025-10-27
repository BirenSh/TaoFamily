package com.example.taofamily.features.initiation.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SheetsRequestDto(
    val majorDimension: String,
    val values: List<List<String>>
)
