package com.reylortechnology.aklimdakihediye.Internet.Models.GeminiModels

import kotlinx.serialization.Serializable
@Serializable
data class GeminiRequest(
    val contents: List<Content>,
    val systemInstruction: Content? = null, // YENİ: Sistem talimatı alanı
    val generationConfig: GenerationConfig? = null
)

@Serializable
data class GenerationConfig(
    val responseMimeType: String? = null, // JSON zorlaması için
    val temperature: Float? = 1.0f
)

// Content ve Part sınıfların muhtemelen zaten şöyledir, aynen kalsın:
@Serializable
data class Content(
    val parts: List<Part>,
    val role: String? = null
)

@Serializable
data class Part(
    val text: String
)