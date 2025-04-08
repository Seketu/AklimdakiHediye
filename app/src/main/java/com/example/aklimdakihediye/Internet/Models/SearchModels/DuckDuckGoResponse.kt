package com.example.aklimdakihediye.Internet.Models.SearchModels

import kotlinx.serialization.Serializable

@Serializable
data class DuckDuckGoResponse(
    val RelatedTopics: List<DuckDuckGoTopic>
)

@Serializable
data class DuckDuckGoTopic(
    val Text: String ,
    val FirstURL: String ,
    val Icon: DuckDuckGoIcon
)

@Serializable
data class DuckDuckGoIcon(
    val URL: String
)
