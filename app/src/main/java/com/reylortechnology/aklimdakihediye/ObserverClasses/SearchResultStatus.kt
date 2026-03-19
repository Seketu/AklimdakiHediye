package com.reylortechnology.aklimdakihediye.ObserverClasses

import com.reylortechnology.aklimdakihediye.models.ComposeModels.SearchCardModel

sealed class SearchResultStatus {
    data class Success(val searchCardModels: List<SearchCardModel>) : SearchResultStatus()
    data class Error(val message: String) : SearchResultStatus()
    object Loading : SearchResultStatus()
    object None : SearchResultStatus()
}