package com.example.aklimdakihediye.models.GiftInformationModels

import com.example.aklimdakihediye.models.ComposeModels.SearchCardModel

data class SearchWithLabel(
    val label : String,
    val searchCardItems : List<SearchCardModel>
)