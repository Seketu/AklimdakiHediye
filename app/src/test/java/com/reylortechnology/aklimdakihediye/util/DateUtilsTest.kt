package com.reylortechnology.aklimdakihediye.util

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Calendar
import java.util.TimeZone

class DateUtilsTest {

    private lateinit var originalTz: TimeZone

    // UTC'nin önünde ve gerisindeki uç bölgeler dahil
    private val zones = listOf(
        "Europe/Istanbul", "UTC", "America/New_York", "America/Los_Angeles",
        "Pacific/Pago_Pago", "Pacific/Kiritimati", "Asia/Kolkata"
    )

    @Before
    fun saveTz() {
        originalTz = TimeZone.getDefault()
    }

    @After
    fun restoreTz() {
        TimeZone.setDefault(originalTz)
    }

    private fun utcMidnight(year: Int, month: Int, day: Int): Long =
        Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            clear()
            set(year, month, day)
        }.timeInMillis

    @Test
    fun utcDateToLocalMidnight_keepsSameCalendarDay_inEveryZone() {
        zones.forEach { zone ->
            TimeZone.setDefault(TimeZone.getTimeZone(zone))
            val result = utcDateToLocalMidnight(utcMidnight(2026, Calendar.DECEMBER, 5))

            val local = Calendar.getInstance().apply { timeInMillis = result }
            assertEquals("$zone yıl", 2026, local.get(Calendar.YEAR))
            assertEquals("$zone ay", Calendar.DECEMBER, local.get(Calendar.MONTH))
            assertEquals("$zone gün", 5, local.get(Calendar.DAY_OF_MONTH))
            assertEquals("$zone saat", 0, local.get(Calendar.HOUR_OF_DAY))
            assertEquals("$zone dakika", 0, local.get(Calendar.MINUTE))
        }
    }

    @Test
    fun utcDateToLocalMidnight_handlesYearBoundary() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"))
        val result = utcDateToLocalMidnight(utcMidnight(2027, Calendar.JANUARY, 1))
        val local = Calendar.getInstance().apply { timeInMillis = result }
        assertEquals(2027, local.get(Calendar.YEAR))
        assertEquals(Calendar.JANUARY, local.get(Calendar.MONTH))
        assertEquals(1, local.get(Calendar.DAY_OF_MONTH))
    }

    @Test
    fun localTodayAsUtcMidnight_matchesLocalToday_inEveryZone() {
        zones.forEach { zone ->
            TimeZone.setDefault(TimeZone.getTimeZone(zone))
            val today = Calendar.getInstance()
            val result = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                timeInMillis = localTodayAsUtcMidnight()
            }
            assertEquals("$zone gün", today.get(Calendar.DAY_OF_MONTH), result.get(Calendar.DAY_OF_MONTH))
            assertEquals("$zone ay", today.get(Calendar.MONTH), result.get(Calendar.MONTH))
            assertEquals("$zone saat", 0, result.get(Calendar.HOUR_OF_DAY))
        }
    }

    @Test
    fun roundTrip_localTodayIsSelectableAndConvertsBackToToday() {
        zones.forEach { zone ->
            TimeZone.setDefault(TimeZone.getTimeZone(zone))
            val back = Calendar.getInstance().apply {
                timeInMillis = utcDateToLocalMidnight(localTodayAsUtcMidnight())
            }
            val today = Calendar.getInstance()
            assertEquals(zone, today.get(Calendar.DAY_OF_YEAR), back.get(Calendar.DAY_OF_YEAR))
        }
    }
}
