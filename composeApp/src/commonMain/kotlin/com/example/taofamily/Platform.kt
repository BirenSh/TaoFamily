package com.example.taofamily

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform