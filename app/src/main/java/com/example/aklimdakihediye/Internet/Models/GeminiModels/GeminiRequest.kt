package com.example.aklimdakihediye.Internet.Models.GeminiModels

import kotlinx.serialization.Serializable

@Serializable
data class GeminiRequest(
    val contents: List<Content>,
    val generationConfig: GenerationConfig
)
@Serializable
data class Content(
    val parts: List<Part>
)
@Serializable
data class Part(
    val text: String
)

@Serializable
data class GenerationConfig(
    val response_mime_type: String
)