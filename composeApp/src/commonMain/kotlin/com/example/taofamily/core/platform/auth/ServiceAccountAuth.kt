package com.example.taofamily.core.platform.auth

import com.example.taofamily.features.initiation.domain.model.ServiceAuth
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.time.ExperimentalTime

class ServiceAccountAuth(
    private val credentialsProvider: suspend () -> ServiceAuth.ServiceAccountCredentials,
    private val httpClient: HttpClient
) {
    private var cachedToken: String? = null
    private var tokenExpiry: Long = 0
    private var credentials: ServiceAuth.ServiceAccountCredentials? = null
    private val mutex = Mutex()

    @OptIn(ExperimentalTime::class)
    suspend fun getAccessToken(): String = mutex.withLock {

        try {
            // Load credentials lazily on first use
            if (credentials == null) {
                credentials = credentialsProvider()

            }

            // Check if cached token is still valid
            val now = kotlin.time.Clock.System.now().epochSeconds
            if (cachedToken != null && now < tokenExpiry) {
                return cachedToken!!
            }

            // Generate new token
            val jwt = createJWT(credentials!!)
            val response = exchangeJWTForToken(jwt, credentials!!)

            cachedToken = response.access_token
            tokenExpiry = now + response.expires_in - 60 // Refresh 60s early

            return cachedToken!!
        }catch (e: Exception){
            return@withLock ""
        }

    }

    @OptIn(ExperimentalEncodingApi::class, ExperimentalTime::class)
    private fun createJWT(creds: ServiceAuth.ServiceAccountCredentials): String {
        val now = kotlin.time.Clock.System.now().epochSeconds
        val expiry = now + 3600 // 1 hour

        val header = """{"alg":"RS256","typ":"JWT"}"""
        val claims = """{
            "iss":"${creds.client_email}",
            "scope":"https://www.googleapis.com/auth/spreadsheets",
            "aud":"${creds.token_uri}",
            "exp":$expiry,
            "iat":$now
        }"""

        val encodedHeader = kotlin.io.encoding.Base64.UrlSafe.encode(header.encodeToByteArray())
            .trimEnd('=')
        val encodedClaims = kotlin.io.encoding.Base64.UrlSafe.encode(claims.encodeToByteArray())
            .trimEnd('=')

        val signatureInput = "$encodedHeader.$encodedClaims"
        val signature = platformSign(signatureInput, creds.private_key)

        return "$signatureInput.$signature"
    }

    private suspend fun exchangeJWTForToken(
        jwt: String,
        creds: ServiceAuth.ServiceAccountCredentials
    ): ServiceAuth.TokenResponse {
        val response: HttpResponse = httpClient.post(creds.token_uri) {
            contentType(ContentType.Application.FormUrlEncoded)
            setBody(
                "grant_type=urn:ietf:params:oauth:grant-type:jwt-bearer&assertion=$jwt"
            )
        }

        if (!response.status.isSuccess()) {
            val error = response.bodyAsText()
            throw Exception("Token exchange failed: ${response.status} - $error")
        }

        val json = response.bodyAsText()
        return Json.decodeFromString(json)
    }
}

expect fun platformSign(data: String, privateKey: String): String