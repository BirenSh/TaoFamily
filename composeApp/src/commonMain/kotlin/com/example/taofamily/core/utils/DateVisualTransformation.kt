// features/initiation/core/utils/DateVisualTransformation.kt

package com.example.taofamily.core.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class DateVisualTransformation : VisualTransformation {
    private val mask = "xxxx-xx-xx"

    override fun filter(text: AnnotatedString): TransformedText {
        val rawDigits = text.text.filter { it.isDigit() }.take(8) // Limit input to 8 digits (YYYYMMDD)

        val output = StringBuilder()
        var digitIndex = 0

        // 1. Build Formatted Output String
        for (i in 0 until mask.length) {
            if (digitIndex >= rawDigits.length) break

            val maskChar = mask[i]
            if (maskChar == 'x') {
                output.append(rawDigits[digitIndex])
                digitIndex++ // Move to the next digit
            } else {
                // Insert the hyphen
                output.append(maskChar)
            }
        }

        val outputLength = output.length

        // 2. Define the OffsetMapping (Forcing the cursor jump)
        val offsetTranslator = object : OffsetMapping {

            override fun originalToTransformed(offset: Int): Int {
                // Maps the raw 8-digit offset to the transformed (10-character) offset
                var transformedOffset = offset

                // If cursor is after 4th digit (i.e., offset > 4), jump 1 for the first hyphen
                if (offset > 4) transformedOffset++

                // If cursor is after 6th digit (i.e., offset > 6), jump 1 more for the second hyphen
                if (offset > 6) transformedOffset++

                // CRITICAL FOR AUTO-ADVANCE UX:
                // We must ensure we jump PAST the hyphen position if we are at it.
                if (offset == 4 && outputLength >= 4) transformedOffset++
                if (offset == 6 && outputLength >= 7) transformedOffset++

                return transformedOffset.coerceAtMost(outputLength)
            }

            override fun transformedToOriginal(offset: Int): Int {
                // Map the transformed cursor position back to the raw input index
                var originalOffset = offset

                // Subtract 1 for the first hyphen if offset is past index 4 (the hyphen position)
                if (offset > 4) originalOffset--

                // Subtract 1 for the second hyphen if offset is past index 7 (the hyphen position)
                if (offset > 7) originalOffset--

                return originalOffset.coerceAtMost(rawDigits.length)
            }
        }

        return TransformedText(
            text = AnnotatedString(output.toString()),
            offsetMapping = offsetTranslator
        )
    }
}