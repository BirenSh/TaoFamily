package com.example.taofamily.core.platform
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSBundle
import platform.Foundation.NSData
import platform.Foundation.NSURL
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.dataWithContentsOfURL
import platform.posix.memcpy
actual fun getServiceAccountProvider(): ServiceAccountProvider {
    return ServiceAccountProviderImpl()
}

// Concrete Implementation (Does not need the Context parameter)
class ServiceAccountProviderImpl : ServiceAccountProvider {
    @OptIn(ExperimentalForeignApi::class)
    override fun getCredentialsData(): ByteArray {

        // 1. Locate the file path in the main application bundle
        val path = NSBundle.mainBundle.pathForResource("google-services", ofType = "json")
            ?: throw IllegalStateException("Service Account JSON file not found in iOS bundle.")

        // 2. Load the file data into NSData (Native iOS data type)
        val fileURL = NSURL.fileURLWithPath(path)
        val data = NSData.dataWithContentsOfURL(fileURL)
            ?: throw IllegalStateException("Failed to load data from JSON file path.")

        // 3. Convert NSData to Kotlin ByteArray
        val byteArray = ByteArray(data.length.toInt())

        // Use Kotlin/Native interop to safely copy data bytes
        byteArray.usePinned { pinned ->
            memcpy(pinned.addressOf(0), data.bytes, data.length)
        }

        return byteArray
    }
}
