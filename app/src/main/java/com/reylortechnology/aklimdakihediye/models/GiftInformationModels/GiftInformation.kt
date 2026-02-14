package com.reylortechnology.aklimdakihediye.models.GiftInformationModels

import com.reylortechnology.aklimdakihediye.models.Enums.GiftVibes
import com.reylortechnology.aklimdakihediye.models.Enums.Prices

data class GiftInformation(
    val forWhy : String,
    val specialDemand : String,
    val giftVibe : GiftVibes,
    val price : Prices,
)
