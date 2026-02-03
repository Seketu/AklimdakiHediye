package com.reylortechnology.aklimdakihediye.models.Enums

import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.reylortechnology.aklimdakihediye.R
import com.reylortechnology.aklimdakihediye.ui.theme.adultCardBg
import com.reylortechnology.aklimdakihediye.ui.theme.childCardBg
import com.reylortechnology.aklimdakihediye.ui.theme.olderCardBg
import com.reylortechnology.aklimdakihediye.ui.theme.teenCardBg

enum class AgeDescStates(
    val id : Int ,
    @param:StringRes val text : Int,
    @param:DrawableRes val iconSource : Int,
    val color : Color
) {
    Child(
        id = 1 ,
        text = R.string.age_desc_child,
        iconSource = R.drawable.kids_icon,
        color = childCardBg
    ),
    Teen(
        id = 2,
        text = R.string.age_desc_teen,
        iconSource = R.drawable.young_icon,
        color = teenCardBg
    ),
    Adult(
        id = 3,
        text = R.string.age_desc_adult,
        iconSource = R.drawable.adult_icon,
        color = adultCardBg
    ),
    Older(
        id = 4 ,
        text = R.string.age_desc_older,
        iconSource = R.drawable.old_man_icon,
        color = olderCardBg
    )
}