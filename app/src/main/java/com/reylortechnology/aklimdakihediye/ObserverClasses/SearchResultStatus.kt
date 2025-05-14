package com.reylortechnology.aklimdakihediye.ObserverClasses

import com.reylortechnology.aklimdakihediye.models.GiftInformationModels.SearchWithLabel

sealed class SearchResultStatus {
    data class Success(val searchWithLabelList: List<SearchWithLabel>) : SearchResultStatus()
    data class Error(val message: String) : SearchResultStatus()
    object Loading : SearchResultStatus()
}