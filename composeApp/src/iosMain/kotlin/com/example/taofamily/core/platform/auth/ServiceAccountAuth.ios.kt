package com.example.taofamily.core.platform.auth

import kotlinx.cinterop.*
import platform.CoreFoundation.*
import platform.Foundation.*
import platform.Security.*

@OptIn(ExperimentalForeignApi::class)
actual fun platformSign(data: String, privateKey: String): String {
    return "mock token"
// TODO: ios need backend to generate token
}
