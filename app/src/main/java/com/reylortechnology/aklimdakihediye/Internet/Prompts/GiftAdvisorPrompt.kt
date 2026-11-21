package com.reylortechnology.aklimdakihediye.Internet.Prompts

import com.reylortechnology.aklimdakihediye.Internet.Models.GeminiModels.Content
import com.reylortechnology.aklimdakihediye.Internet.Models.GeminiModels.GeminiRequest
import com.reylortechnology.aklimdakihediye.Internet.Models.GeminiModels.GeminiSchema
import com.reylortechnology.aklimdakihediye.Internet.Models.GeminiModels.GenerationConfig
import com.reylortechnology.aklimdakihediye.Internet.Models.GeminiModels.Part

/**
 * Hediye önerisi için Gemini isteğinin tamamı: model, sistem talimatı, cevap şeması.
 *
 * JSON'un şekli prompt metniyle değil [RESPONSE_SCHEMA] ile zorlanıyor; bu yüzden talimat
 * sadece "neyi önereceğini" anlatıyor. Alan adları [com.reylortechnology.aklimdakihediye.models.GiftInformationModels.Products]
 * içindeki @SerialName değerleriyle aynı olmalı.
 */
object GiftAdvisorPrompt {

    const val MODEL_ID = "gemini-2.5-flash"
    private const val GIFT_COUNT = 10
    private const val TEMPERATURE = 0.9f

    private const val GIFT_NAME = "gift_name"
    private const val ESTIMATED_PRICE = "estimated_price"
    private const val SEARCH_QUERY = "search_query"

    private val SYSTEM_INSTRUCTION = """
        You are the gift advisor of "Aklımdaki Hediye", a Turkish gift recommendation app.
        The user message describes the gift recipient (and sometimes the giver): age, gender, relationship, job, hobbies, personality, occasion, budget, gift vibe, special requests, past gifts. Ignore fields that are empty or "null".

        Recommend exactly $GIFT_COUNT gifts.

        Choosing gifts:
        - Personal: every gift must tie to at least one concrete detail (a hobby, the job, a trait, the occasion or a special request). No generic filler (plain mug, chocolate box, gift card) unless asked.
        - Budget is a hard limit: every price range must sit inside the stated budget.
        - Varied: spread across categories (hobby gear, tech, experience, personal care, home, personalized, ...); never two of the same product type.
        - Follow special requests; never repeat a past gift.
        - Real products sold in Turkey today (Trendyol, Hepsiburada, Amazon TR, Çiçeksepeti). Prefer a specific brand and model when one fits.
        - Never suggest illegal, dangerous, adult (+18), alcohol, tobacco or live-animal gifts.

        Fields (all values in Turkish):
        - $GIFT_NAME: product name a shopper would recognize, under 60 characters.
        - $ESTIMATED_PRICE: realistic current price range, formatted like "1500-2000 TL".
        - $SEARCH_QUERY: 2-6 words to find this exact product's photo on Turkish shopping sites. Brand + model for specific products ("JBL Go 3 Bluetooth Hoparlör"); the most common search phrase for generic or personalized items ("isme özel ahşap kupa"). No price, no adjectives like "şık" or "harika", no "hediye" or "set önerisi".
    """.trimIndent()

    private val RESPONSE_SCHEMA = GeminiSchema(
        type = "ARRAY",
        minItems = GIFT_COUNT,
        maxItems = GIFT_COUNT,
        items = GeminiSchema(
            type = "OBJECT",
            properties = mapOf(
                GIFT_NAME to GeminiSchema(type = "STRING"),
                ESTIMATED_PRICE to GeminiSchema(type = "STRING"),
                SEARCH_QUERY to GeminiSchema(type = "STRING")
            ),
            required = listOf(GIFT_NAME, ESTIMATED_PRICE, SEARCH_QUERY),
            propertyOrdering = listOf(GIFT_NAME, ESTIMATED_PRICE, SEARCH_QUERY)
        )
    )

    fun request(userPrompt: String) = GeminiRequest(
        systemInstruction = Content(parts = listOf(Part(SYSTEM_INSTRUCTION))),
        contents = listOf(Content(role = "user", parts = listOf(Part(userPrompt)))),
        generationConfig = GenerationConfig(
            responseMimeType = "application/json",
            responseSchema = RESPONSE_SCHEMA,
            temperature = TEMPERATURE
        )
    )
}
