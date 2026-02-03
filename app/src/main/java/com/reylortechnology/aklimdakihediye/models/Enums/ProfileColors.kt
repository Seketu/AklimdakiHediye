package com.reylortechnology.aklimdakihediye.models.Enums

import androidx.compose.ui.graphics.Color
import com.reylortechnology.aklimdakihediye.ui.theme.profileColorsBlack
import com.reylortechnology.aklimdakihediye.ui.theme.profileColorsBlue
import com.reylortechnology.aklimdakihediye.ui.theme.profileColorsBrown
import com.reylortechnology.aklimdakihediye.ui.theme.profileColorsDarkGreen
import com.reylortechnology.aklimdakihediye.ui.theme.profileColorsGreen
import com.reylortechnology.aklimdakihediye.ui.theme.profileColorsLightBrown
import com.reylortechnology.aklimdakihediye.ui.theme.profileColorsPurple
import com.reylortechnology.aklimdakihediye.ui.theme.profileColorsTurquoise
import com.reylortechnology.aklimdakihediye.ui.theme.profileColorsYellow
import com.reylortechnology.aklimdakihediye.ui.theme.stylizedYellow

enum class ProfileColors(
    val color: Color
) {
    Yellow(profileColorsYellow),
    Blue(profileColorsBlue),
    Green(profileColorsGreen),
    Purple(profileColorsPurple),
    Turquoise(profileColorsTurquoise),
    Brown(profileColorsBrown),
    Black(profileColorsBlack),
    LightBrown(profileColorsLightBrown),
    DarkGreen(profileColorsDarkGreen)
}