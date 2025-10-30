package com.example.taofamily.core.di

import com.example.taofamily.core.platform.auth.ServiceAccountAuth
import com.example.taofamily.core.platform.getServiceAccountProvider
import com.example.taofamily.features.initiation.domain.model.ServiceAuth
import io.ktor.client.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

val networkModule: Module = module {
    // HttpClient for auth token exchange (no auth needed)
    single(qualifier = named("authClient")) {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                })
            }
            defaultRequest {
                header(io.ktor.http.HttpHeaders.Accept, io.ktor.http.ContentType.Application.Json)
            }
            install(Logging) {
                level = LogLevel.INFO
            }
        }
    }


    // Service Account Auth - lazy initialization
    single {
        ServiceAccountAuth(
            credentialsProvider = { loadServiceAccountCredentials() },
            httpClient = get(qualifier = named("authClient"))
        )
    }

    // Main HttpClient with auto-auth
    single(qualifier = named("apiClient")) {
        val auth: ServiceAccountAuth = get()

        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                })
            }


            install(Logging) {
                level = LogLevel.ALL

            }

            // Add auth interceptor
            install(Auth) {
                bearer {
                    loadTokens {
                        try {
                            val token = auth.getAccessToken()
                            BearerTokens(token, token)
                        } catch (e: Exception) {
                            println("❌ Failed to load token: ${e.message}")
                            null
                        }
                    }

                    refreshTokens {
                        try {
                            val token = auth.getAccessToken()
                            BearerTokens(token, token)
                        } catch (e: Exception) {
                            println("❌ Failed to refresh token: ${e.message}")
                            null
                        }
                    }
                }
            }
        }
    }
}

suspend fun loadServiceAccountCredentials(): ServiceAuth.ServiceAccountCredentials {
    val jsonString = getServiceAccountProvider().getCredentialsData().decodeToString()
    return Json.decodeFromString(jsonString)
}