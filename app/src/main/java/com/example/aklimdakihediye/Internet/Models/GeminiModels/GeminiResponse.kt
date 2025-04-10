package com.example.aklimdakihediye.Internet.Models.GeminiModels

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class GeminiResponse(
    val candidates: List<GeminiCandidate>,
    val usageMetadata: GeminiUsageMetadata? = null,
    val modelVersion: String? = null
)

@Serializable
data class GeminiCandidate(
    val content: GeminiResponseContent,
    val finishReason: String? = null,
    val avgLogprobs: Double? = null
)

@Serializable
data class GeminiResponseContent(
    val parts: List<GeminiResponsePart>,
    val role: String? = null
)

@Serializable
data class GeminiResponsePart(
    val text: String
)

@Serializable
data class GeminiUsageMetadata(
    val promptTokenCount: Int? = null,
    val candidatesTokenCount: Int? = null,
    val totalTokenCount: Int? = null
)
