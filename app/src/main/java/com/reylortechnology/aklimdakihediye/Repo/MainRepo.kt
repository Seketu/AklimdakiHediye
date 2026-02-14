package com.reylortechnology.aklimdakihediye.Repo

import android.net.Uri
import android.util.Log
import com.reylortechnology.aklimdakihediye.BuildConfig
import com.reylortechnology.aklimdakihediye.Internet.ApiResponse
import com.reylortechnology.aklimdakihediye.Internet.Models.GeminiModels.Content
import com.reylortechnology.aklimdakihediye.Internet.Models.GeminiModels.GeminiRequest
import com.reylortechnology.aklimdakihediye.Internet.Models.GeminiModels.GeminiResponse
import com.reylortechnology.aklimdakihediye.Internet.Models.GeminiModels.GenerationConfig
import com.reylortechnology.aklimdakihediye.Internet.Models.GeminiModels.Part
import com.reylortechnology.aklimdakihediye.Internet.safeApiCall
import com.reylortechnology.aklimdakihediye.LocalDatabase.Dao.PeoplesDao
import com.reylortechnology.aklimdakihediye.LocalDatabase.Dao.UserInformationDao
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.LocalUserInformation
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.SavedGifts
import com.reylortechnology.aklimdakihediye.Services.DuckDuckGoSearch
import com.reylortechnology.aklimdakihediye.Services.ImageSearchService
import com.reylortechnology.aklimdakihediye.models.ComposeModels.SearchCardModel
import com.reylortechnology.aklimdakihediye.models.GiftInformationModels.Products
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.headers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import javax.inject.Inject

class MainRepo
@Inject constructor(
    private val userInformationDao : UserInformationDao,
    private val giftInformationDao : SavedVariableRepo,
    private val peopleInformationDao : PeoplesDao,
    private val duckSearch: DuckDuckGoSearch,
    private val bingSearchService: ImageSearchService,
    val client: HttpClient,
) {
    val SYSTEM_PROMPT = "Role: You are the expert AI Gift Advisor for the mobile application \"Aklımdaki Hediye\" (Gift in My Mind).\n" +
            "Task: Based on the user's input (age, gender, interests, budget, occasion), recommend exactly 10 distinct, creative, and available gift ideas suitable for the Turkish market.\n" +
            "\n" +
            "Constraints & Rules:\n" +
            "1. Output MUST be a strictly valid JSON array. Do not include any markdown formatting (like ```json), introduction, or explanation text outside the JSON.\n" +
            "2. Market: Focus on products available in Turkey (Trendyol, Hepsiburada, Amazon TR, etc.).\n" +
            "3. Safety: Never recommend illegal, dangerous, or explicit (+18) items.\n" +
            "4. Language: The keys of the JSON must be in English, but the *values* (content like descriptions) must be in Turkish (unless the user explicitly asks in English).\n" +
            "\n" +
            "JSON Schema:\n" +
            "[\n" +
            "  {\n" +
            "    \"gift_name\": \"Name of the product\",\n" +
            "    \"estimated_price\": \"Price range in TL (e.g., 1500-2000 TL)\"\n" +
            "  }\n" +
            "]"
    val getUserInformation = userInformationDao.getLocalInformation().flowOn(Dispatchers.IO)
    val peoplesInformation = peopleInformationDao.getAllPeoples().flowOn(Dispatchers.IO)
    suspend fun addInformation(userInformation: LocalUserInformation) {
        userInformationDao.addLocalInformation(userInformation)
    }

    suspend fun saveNewGift(gift: SavedGifts){
        giftInformationDao.saveGift(gift)
    }


    fun getGiftsAndUrls(
        products : List<Products>
    ) = flow<List<SearchCardModel>>{
        coroutineScope {
            val result = products.map {gift->
                async {
                    val searchResult = bingSearchService.searchProduct(gift.name)
                    SearchCardModel(
                        name = gift.name,
                        url = searchResult.giftUrl ?: "https://www.trendyol.com/sr?q=${Uri.encode(gift.name)}",
                        imageUrl = searchResult.imageUrl,
                        price = gift.price
                    )
                }
            }.awaitAll()
            Log.d("Gifts and Urls",result.toString())
            emit(result)
        }
    }

    suspend fun askGemini(prompt: String): Flow<ApiResponse<GeminiResponse>> = safeApiCall {
        val GEMINI_API_KEY = BuildConfig.GEMINI_API_KEY
        val MODEL_ID = "gemini-2.0-flash"
        val GENERATE_CONTENT_API = "generateContent"
        client.post(
            "${BuildConfig.BASE_URL}v1beta/models/${MODEL_ID}:${GENERATE_CONTENT_API}?key=${GEMINI_API_KEY}"
        ) {
            contentType(ContentType.Application.Json)
            setBody(
                GeminiRequest(
                    systemInstruction = Content(
                        parts = listOf(Part(text = SYSTEM_PROMPT))
                    ),
                    contents = listOf(
                        Content(
                            role = "user",
                            parts = listOf<Part>(Part(prompt))
                        )
                    ),
                    generationConfig = GenerationConfig(
                        responseMimeType = "application/json",
                        temperature = 0.9f
                    )
                )
            )
        }.body()
    }
}