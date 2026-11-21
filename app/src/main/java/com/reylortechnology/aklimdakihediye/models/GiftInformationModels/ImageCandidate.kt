package com.reylortechnology.aklimdakihediye.models.GiftInformationModels

import kotlinx.serialization.Serializable

@Serializable // Test fixture'larına kaydedilebilsin diye
data class ImageCandidate(
    val imageUrl: String,      // Orijinal resim (Bing "murl")
    val thumbUrl: String?,     // Bing küçük resmi (Bing "turl") - her zaman açılır
    val pageUrl: String?,      // Resmin bulunduğu sayfa (Bing "purl")
    val title: String          // Resim başlığı (Bing "t")
)
