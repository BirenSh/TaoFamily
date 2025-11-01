package com.example.taofamily.core.platform

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import platform.Foundation.NSURL
import platform.Foundation.NSURLConnection
import platform.Foundation.NSURLRequest
import platform.Foundation.sendSynchronousRequest
import platform.posix.exception

@OptIn(ExperimentalForeignApi::class)
actual suspend fun checkInternetConnection(): Boolean  = withContext(Dispatchers.Default){
    try {
        val url = NSURL(string = "https://dns.google")
        val request = NSURLRequest.requestWithURL(url)
        val response = NSURLConnection.sendSynchronousRequest(request, null, null)
        return@withContext response != null
    }catch (e: Exception){
        return@withContext false
    }

}