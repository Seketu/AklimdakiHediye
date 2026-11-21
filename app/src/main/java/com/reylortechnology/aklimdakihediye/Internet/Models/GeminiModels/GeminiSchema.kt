package com.reylortechnology.aklimdakihediye.Internet.Models.GeminiModels

import kotlinx.serialization.Serializable

/**
 * Gemini "responseSchema" alanı (OpenAPI Schema alt kümesi).
 * type değerleri: "STRING", "NUMBER", "INTEGER", "BOOLEAN", "ARRAY", "OBJECT".
 */
@Serializable
data class GeminiSchema(
    val type: String,
    val description: String? = null,
    val items: GeminiSchema? = null,
    val properties: Map<String, GeminiSchema>? = null,
    val required: List<String>? = null,
    val propertyOrdering: List<String>? = null,
    val minItems: Int? = null,
    val maxItems: Int? = null
)
