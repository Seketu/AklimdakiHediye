package com.reylortechnology.aklimdakihediye.models.Enums

import androidx.annotation.StringRes
import com.reylortechnology.aklimdakihediye.R

enum class Prices(
    val id : Int,
    @param:StringRes val textSource : Int
) {

    Low(id = 1 , textSource = R.string.prices_low),
    UpperLow(id = 2 , textSource = R.string.upper_low),
    Medium(id = 3 , textSource = R.string.prices_medium),
    High(id = 4 , textSource = R.string.prices_high),
    UnLimit(id = 5 , textSource = R.string.prices_unlimit)
}