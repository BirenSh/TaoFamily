package com.example.taofamily.core.platform

expect fun getServiceAccountProvider(): ServiceAccountProvider

interface ServiceAccountProvider {
    fun getCredentialsData(): ByteArray
}