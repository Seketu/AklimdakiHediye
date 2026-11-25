package com.reylortechnology.aklimdakihediye.models.Enums

enum class ReminderScheduleResult {
    SCHEDULED,
    // Seçilen etkinlik günü geçmişte
    DATE_IN_PAST,
    // Etkinlik ileride ama "X gün önce 09:00" anı geçmiş
    NOTIFY_TIME_PASSED,
    ERROR
}
