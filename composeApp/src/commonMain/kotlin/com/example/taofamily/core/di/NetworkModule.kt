package com.example.taofamily.core.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.mp.KoinPlatformTools


// --- Interface and Placeholder for Token ---
interface AuthTokenProvider {
    fun getToken(): String
}


class PlaceholderTokenProvider : AuthTokenProvider {
    override fun getToken(): String {
        return "MOCK_GOOGLE_SHEET_ACCESS_TOKEN" // Placeholder
    }
}
//---------------------------------

val networkModule: Module = module {

    // A. PROVIDE ACCESS TOKEN PROVIDER
    single<AuthTokenProvider> { PlaceholderTokenProvider() }

    // B. PROVIDE KTOR HTTP CLIENT
    single {
        HttpClient() {

            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                })
            }

            defaultRequest {

                val tokenProvider: AuthTokenProvider = get()
                header("Authorization", "Bearer ${tokenProvider.getToken()}")
                url("https://sheets.googleapis.com/v4/spreadsheets/")
            }

            // Note: You can add an optional expect/actual block here if you need
            // platform-specific engine configuration (e.g., certificate pinning).
        }
    }
}