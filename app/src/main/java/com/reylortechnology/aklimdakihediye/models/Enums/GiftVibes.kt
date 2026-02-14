package com.reylortechnology.aklimdakihediye.models.Enums

import androidx.annotation.StringRes
import com.reylortechnology.aklimdakihediye.R

enum class GiftVibes(
    val id : Int,
     @param:StringRes val textSource: Int
) {
    Romance(id = 1 , textSource = R.string.gift_vibe_romance),
    Emotional(id = 2, textSource = R.string.gift_vibe_emotional),
    ForUse(id = 3 , textSource = R.string.gift_vibe_for_use),
    Humors(id = 4 , textSource = R.string.gift_vibe_humors),
    Retro(id = 5 , textSource = R.string.gift_vibe_retro),
    Luxury(id = 6 , textSource = R.string.gift_vibe_luxury),
    Memorable(id = 7 , textSource = R.string.gift_vibe_memorable),
    Minimalist(id = 8 , textSource = R.string.gift_vibe_minimalist)
}
