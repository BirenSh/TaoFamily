package com.example.taofamily.core.platform.auth

import io.ktor.client.HttpClient

expect fun createAuthHttpClient(): HttpClient
