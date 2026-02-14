package com.reylortechnology.aklimdakihediye.models.Enums

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.reylortechnology.aklimdakihediye.R

enum class ForWhyGifts(
    val id : Int,
    @param:StringRes val textRes : Int,
    @param:DrawableRes val imageRes : Int
) {
    JustBecause(id = 1 , textRes = R.string.for_why_just_because, imageRes = R.drawable.just_because_icon),
    Birthday(id= 2 , textRes = R.string.for_why_birthday , imageRes = R.drawable.birthday_icon),
    Anniversary(id = 3 , textRes = R.string.for_why_anniversary , imageRes = R.drawable.anniversary_icon),
    Education(id = 4 , textRes = R.string.for_why_education , imageRes = R.drawable.education_icon),
    HouseWarming(id = 5 , textRes = R.string.for_why_house_warming, imageRes = R.drawable.housewarming_icon),
    Holiday(id = 6 , textRes = R.string.for_why_holiday, imageRes = R.drawable.holiday_icon )
}