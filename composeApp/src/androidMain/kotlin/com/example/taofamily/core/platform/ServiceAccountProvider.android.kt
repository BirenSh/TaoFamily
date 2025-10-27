package com.example.taofamily.core.platform

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import org.koin.mp.KoinPlatformTools

@RequiresApi(Build.VERSION_CODES.O)
actual fun getServiceAccountProvider(): ServiceAccountProvider {
    val context = KoinPlatformTools.defaultContext().get().get<Context>()
    return ServiceAccountProviderImpl(context)
 }

//Concrete Implementation (remains the same)
class ServiceAccountProviderImpl(private val context: Context) : ServiceAccountProvider {
    override fun getCredentialsData(): ByteArray {
        return try {
            // Read the entire InputStream from the assets folder into a ByteArray
            context.assets.open("google-services.json").use { inputStream ->
                inputStream.readBytes()
            }
        } catch (e: Exception) {
            // Throw a specific error if the file is missing or corrupted
            throw IllegalStateException("Failed to load Service Account JSON from Android assets: ${e.message}", e)
        }
    }
}