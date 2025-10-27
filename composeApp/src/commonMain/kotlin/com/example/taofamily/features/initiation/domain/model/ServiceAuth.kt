package com.example.taofamily.features.initiation.domain.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

class ServiceAuth {
    @OptIn(ExperimentalSerializationApi::class)
    @kotlinx.serialization.json.JsonIgnoreUnknownKeys

    @Serializable
    data class ServiceAccountCredentials(
        val type: String,
        val project_id: String,
        val private_key_id: String,
        val private_key: String,
        val client_email: String,
        val client_id: String,
        val auth_uri: String,
        val token_uri: String
    )

    @Serializable
    data class TokenResponse(
        val access_token: String,
        val expires_in: Int,
        val token_type: String
    )
}