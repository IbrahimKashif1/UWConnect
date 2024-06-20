package com.uwconnect.android.presentation.components

import androidx.compose.runtime.Composable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun FormatDateTime(dateTimeStr: String?): String {
    return if (dateTimeStr != null) {
        try {
            val dateTime = LocalDateTime.parse(dateTimeStr)
            val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
            dateTime.format(formatter)
        } catch (e: Exception) {
            "Invalid date"
        }
    } else {
        "Not available"
    }
}
