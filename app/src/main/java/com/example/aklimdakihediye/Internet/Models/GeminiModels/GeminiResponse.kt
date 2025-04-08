package com.example.aklimdakihediye.Internet.Models.GeminiModels

import kotlinx.serialization.Serializable

@Serializable
data class GeminiResponse(
    val response: ResponseData,
    val status: String
)

@Serializable
data class ResponseData(
    val text: String,
    val usage: UsageData
)

@Serializable
data class UsageData(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
)
