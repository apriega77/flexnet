package com.flexnet.data

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object TimestampConverter {

    private const val DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss"

    /**
     * Convert milliseconds (epoch time) to a formatted date string.
     */
    fun fromMillisToFormatted(timestampMillis: Long, format: String = DEFAULT_FORMAT): String {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(Date(timestampMillis))
    }

    /**
     * Convert seconds (epoch time) to a formatted date string.
     */
    fun fromSecondsToFormatted(timestampSeconds: Long, format: String = DEFAULT_FORMAT): String {
        return fromMillisToFormatted(timestampSeconds * 1000, format)
    }

    /**
     * Convert a formatted date string to milliseconds.
     */
    fun fromFormattedToMillis(dateString: String, format: String = DEFAULT_FORMAT): Long? {
        return try {
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            sdf.timeZone = TimeZone.getDefault()
            sdf.parse(dateString)?.time
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Convert a formatted date string to seconds.
     */
    fun fromFormattedToSeconds(dateString: String, format: String = DEFAULT_FORMAT): Long? {
        return fromFormattedToMillis(dateString, format)?.div(1000)
    }

    /**
     * Convert milliseconds to ISO-8601 format.
     */
    fun fromMillisToISO8601(timestampMillis: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(Date(timestampMillis))
    }

    /**
     * Convert ISO-8601 string to milliseconds.
     */
    fun fromISO8601ToMillis(isoString: String): Long? {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            sdf.parse(isoString)?.time
        } catch (e: Exception) {
            null
        }
    }
}
