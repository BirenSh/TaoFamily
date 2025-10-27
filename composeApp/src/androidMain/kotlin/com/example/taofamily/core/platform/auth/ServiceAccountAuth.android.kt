package com.example.taofamily.core.platform.auth

import android.util.Base64
import java.security.KeyFactory
import java.security.Signature
import java.security.spec.PKCS8EncodedKeySpec

actual fun platformSign(data: String, privateKey: String): String {
    // Clean PEM and decode
    val keyString = privateKey
        .replace("-----BEGIN PRIVATE KEY-----", "")
        .replace("-----END PRIVATE KEY-----", "")
        .replace("\n", "")
        .trim()

    val keyBytes = Base64.decode(keyString, Base64.DEFAULT)
    val spec = PKCS8EncodedKeySpec(keyBytes)
    val keyFactory = KeyFactory.getInstance("RSA")
    val privateKeyObj = keyFactory.generatePrivate(spec)

    // Sign using SHA256withRSA
    val signature = Signature.getInstance("SHA256withRSA")
    signature.initSign(privateKeyObj)
    signature.update(data.toByteArray())
    val signedBytes = signature.sign()

    // Encode Base64 URL-safe (Google requires URL-safe JWT signature)
    return Base64.encodeToString(signedBytes, Base64.URL_SAFE or Base64.NO_WRAP)
        .replace("=", "")
}
