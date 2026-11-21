package com.reylortechnology.aklimdakihediye.models.GiftInformationModels

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import javax.inject.Inject

@Serializable
data class Products (
    @SerialName("gift_name")
    val name : String,
    @SerialName("estimated_price")
    val price : String,
    // Görsel aramasında kullanılan kısa, marka/model içeren sorgu. Gemini vermezse gift_name kullanılır.
    @SerialName("search_query")
    val searchQuery : String? = null
)