package com.reylortechnology.aklimdakihediye.models.GiftInformationModels

import javax.inject.Inject


data class ForGiftInformation
    @Inject constructor(
        val minPrice : String,
        val maxPrice : String,
        val giftMean : String,
)