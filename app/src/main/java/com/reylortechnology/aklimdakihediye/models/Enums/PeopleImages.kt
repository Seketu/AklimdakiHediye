package com.reylortechnology.aklimdakihediye.models.Enums

import androidx.annotation.DrawableRes

enum class PeopleImages(
    val id : Int,
    @param:DrawableRes val imageRes: Int
) {
    Man1(1, com.reylortechnology.aklimdakihediye.R.drawable.man_1),
    Man2(2, com.reylortechnology.aklimdakihediye.R.drawable.man_2),
    Woman2(3, com.reylortechnology.aklimdakihediye.R.drawable.woman_2),
    Mom1(4, com.reylortechnology.aklimdakihediye.R.drawable.mom_1),
    Mom2(5, com.reylortechnology.aklimdakihediye.R.drawable.mom_2)
}