package com.reylortechnology.aklimdakihediye.util

import java.util.Calendar
import java.util.TimeZone

/**
 * Material DatePicker seçilen günü UTC gece yarısı olarak döndürür.
 * Aynı takvim gününün cihaz saat dilimindeki gece yarısına çevirir;
 * aksi halde UTC'nin gerisindeki bölgelerde gün bir önceye kayar.
 */
fun utcDateToLocalMidnight(utcMillis: Long): Long {
    val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { timeInMillis = utcMillis }
    return Calendar.getInstance().apply {
        clear()
        set(utc.get(Calendar.YEAR), utc.get(Calendar.MONTH), utc.get(Calendar.DAY_OF_MONTH))
    }.timeInMillis
}

/** Bugünün yerel tarihini DatePicker'ın kullandığı UTC gece yarısı formatında verir. */
fun localTodayAsUtcMidnight(): Long {
    val local = Calendar.getInstance()
    return Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
        clear()
        set(local.get(Calendar.YEAR), local.get(Calendar.MONTH), local.get(Calendar.DAY_OF_MONTH))
    }.timeInMillis
}
