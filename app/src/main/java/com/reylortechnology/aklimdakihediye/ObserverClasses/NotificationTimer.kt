package com.reylortechnology.aklimdakihediye.ObserverClasses

enum class NotificationTimer(val label: String, val daysBefore: Int)  {
    ONE_DAY_BEFORE("1 gün önce", 1),
    THREE_DAYS_BEFORE("3 gün önce", 3),
    ONE_WEEK_BEFORE("1 hafta önce", 7),
    TWO_WEEKS_BEFORE("2 hafta önce", 14);

    override fun toString(): String = label
}