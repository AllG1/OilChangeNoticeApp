package com.example.oilchangenotice

import android.content.Context
import androidx.core.content.edit
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.abs

/**
 * Utility functions, persistence, and data structure for Oil Filter Day calculation.
 */
object OilFilterUtils {

    /**
     * Default base reference date as specified: 18/11/2026.
     */
    val DEFAULT_BASE_DATE: LocalDate = LocalDate.of(2026, 11, 18)

    private const val PREFS_NAME = "oil_filter_prefs"
    private const val KEY_BASE_DATE = "base_oil_filter_date"

    private val DISPLAY_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")
    private val SHORT_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    /**
     * Checks if the given target date is an oil filter day relative to the base date.
     * Algorithm: (targetDate - baseDate in days) % 2 == 0
     */
    fun isOilFilterDay(
        targetDate: LocalDate = LocalDate.now(),
        baseDate: LocalDate = DEFAULT_BASE_DATE
    ): Boolean {
        val daysBetween = ChronoUnit.DAYS.between(baseDate, targetDate)
        return (abs(daysBetween) % 2L) == 0L
    }

    /**
     * Returns total days between base date and target date.
     */
    fun getDaysBetween(
        targetDate: LocalDate = LocalDate.now(),
        baseDate: LocalDate = DEFAULT_BASE_DATE
    ): Long {
        return ChronoUnit.DAYS.between(baseDate, targetDate)
    }

    /**
     * Gets the next upcoming oil filter date starting from targetDate.
     */
    fun getNextOilFilterDate(
        targetDate: LocalDate = LocalDate.now(),
        baseDate: LocalDate = DEFAULT_BASE_DATE
    ): LocalDate {
        return if (isOilFilterDay(targetDate, baseDate)) {
            targetDate
        } else {
            targetDate.plusDays(1)
        }
    }

    /**
     * Formats a LocalDate into a friendly display string (e.g. Wednesday, November 18, 2026).
     */
    fun formatDateDisplay(date: LocalDate): String {
        return date.format(DISPLAY_FORMATTER)
    }

    /**
     * Formats a LocalDate into short format (e.g. 18/11/2026).
     */
    fun formatDateShort(date: LocalDate): String {
        return date.format(SHORT_FORMATTER)
    }

    /**
     * Parses short date string (dd/MM/yyyy) to LocalDate.
     */
    fun parseDateShort(dateStr: String): LocalDate? {
        return try {
            LocalDate.parse(dateStr, SHORT_FORMATTER)
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Saves the base oil filter date to SharedPreferences.
     */
    fun saveBaseDate(context: Context, date: LocalDate) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit { putString(KEY_BASE_DATE, date.toString()) }
    }

    /**
     * Loads the base oil filter date from SharedPreferences, defaulting to DEFAULT_BASE_DATE (18/11/2026).
     */
    fun loadBaseDate(context: Context): LocalDate {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedStr = prefs.getString(KEY_BASE_DATE, null)
        return if (savedStr != null) {
            try {
                LocalDate.parse(savedStr)
            } catch (_: Exception) {
                DEFAULT_BASE_DATE
            }
        } else {
            DEFAULT_BASE_DATE
        }
    }
}
