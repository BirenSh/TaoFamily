package com.example.taofamily.core.utils

object DateUtils {
    fun formatRawDateString(rawDigits: String): String {
        val digits = rawDigits.filter { it.isDigit() }.take(8) // Limit to 8 digits
        // YYYY
        if (digits.length <= 4) return digits // Return raw digits if 4 or less (e.g., "1111")

        // YYYY-MM
        val yearPart = digits.substring(0, 4)
        val monthPart = digits.substring(4).take(2)

        if (digits.length <= 6) return "$yearPart-$monthPart" // Return "1111-11"

        // YYYY-MM-DD
        val dayPart = digits.substring(6).take(2)
        return "$yearPart-$monthPart-$dayPart"
    }
}