package com.reylortechnology.aklimdakihediye.models.GiftInformationModels

import com.reylortechnology.aklimdakihediye.models.ComposeModels.SearchCardModel

data class SearchWithLabel(
    val label : String,
    val searchCardItems : List<SearchCardModel>
)