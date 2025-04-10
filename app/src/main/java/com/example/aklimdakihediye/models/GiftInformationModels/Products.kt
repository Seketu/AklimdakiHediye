package com.example.aklimdakihediye.models.GiftInformationModels

import javax.inject.Inject

data class Products
    @Inject constructor(
    val name : String,
    val price : String,
    val brand : String
)