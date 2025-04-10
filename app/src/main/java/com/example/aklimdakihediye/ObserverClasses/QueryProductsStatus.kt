package com.example.aklimdakihediye.ObserverClasses

import com.example.aklimdakihediye.models.GiftInformationModels.Products

sealed class QueryProductsStatus {
    data class Success(val products: List<Products>) : QueryProductsStatus()
    data class Error(val message: String) : QueryProductsStatus()
    object Loading : QueryProductsStatus()
}