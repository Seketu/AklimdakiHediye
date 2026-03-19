package com.reylortechnology.aklimdakihediye.Services

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters


class CalculatorSpecialDays {

    private val currentDate = LocalDate.now()

    fun calculateValentineDay(): String? {
        var valentineDay = LocalDate.of(currentDate.year, 2, 14)

        // Eğer bu yıl geçtiyse, seneye al
        if (currentDate.isAfter(valentineDay)) {
            valentineDay = valentineDay.plusYears(1)
        }

        val inMonth = currentDate.plusMonths(1)

        // 1 ay içinde mi kontrol et
        if (valentineDay.isBefore(inMonth) || valentineDay.isEqual(inMonth)) {
            return ChronoUnit.DAYS.between(currentDate, valentineDay).toString()
        }

        return null
    }

    fun calculateMothersDay(): String? {
        var currentYear = currentDate.year
        var mothersDay = LocalDate.of(currentYear, 5, 1)
            .with(TemporalAdjusters.dayOfWeekInMonth(2, DayOfWeek.SUNDAY))

        // Eğer bu yıl geçtiyse, seneye al ve o yılın 2. Pazar gününü bul
        if (currentDate.isAfter(mothersDay)) {
            currentYear += 1
            mothersDay = LocalDate.of(currentYear, 5, 1)
                .with(TemporalAdjusters.dayOfWeekInMonth(2, DayOfWeek.SUNDAY))
        }

        val inMonth = currentDate.plusMonths(1)

        // 1 ay içinde mi kontrol et
        if (mothersDay.isBefore(inMonth) || mothersDay.isEqual(inMonth)) {
            return ChronoUnit.DAYS.between(currentDate, mothersDay).toString()
        }

        return null
    }

    fun calculateFathersDay(): String? {
        var currentYear = currentDate.year
        var fathersDay = LocalDate.of(currentYear, 6, 1)
            .with(TemporalAdjusters.dayOfWeekInMonth(3, DayOfWeek.SUNDAY))

        // Eğer bu yıl geçtiyse, seneye al ve o yılın 3. Pazar gününü bul
        if (currentDate.isAfter(fathersDay)) {
            currentYear += 1
            fathersDay = LocalDate.of(currentYear, 6, 1)
                .with(TemporalAdjusters.dayOfWeekInMonth(3, DayOfWeek.SUNDAY))
        }

        val inMonth = currentDate.plusMonths(1)

        // 1 ay içinde mi kontrol et
        if (fathersDay.isBefore(inMonth) || fathersDay.isEqual(inMonth)) {
            return ChronoUnit.DAYS.between(currentDate, fathersDay).toString()
        }

        return null
    }

    fun calculateBirthday(birthday: LocalDate): String? {
        var nextBirthday = birthday.withYear(currentDate.year)

        if (currentDate.isAfter(nextBirthday)) {
            nextBirthday = nextBirthday.plusYears(1)
        }

        val inMonth = currentDate.plusMonths(1)

        if (nextBirthday.isBefore(inMonth) || nextBirthday.isEqual(inMonth)) {
            return ChronoUnit.DAYS.between(currentDate, nextBirthday).toString()
        }

        // 1 aydan fazla varsa hiçbir şey gösterme
        return null
    }
}