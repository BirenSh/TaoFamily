package com.example.taofamily.core.platform

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetSocketAddress
import java.net.Socket

actual suspend fun checkInternetConnection(): Boolean  = withContext(Dispatchers.IO)  {
    return@withContext try {
        // Try connecting to Google's public DNS server
        Socket().use { socket ->
            val socketAddress = InetSocketAddress("8.8.8.8", 53)
            socket.connect(socketAddress, 1500) // 1.5s timeout
            true
        }
    } catch (e: Exception) {
        false
    }
}