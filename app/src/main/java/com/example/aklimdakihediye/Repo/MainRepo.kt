package com.example.aklimdakihediye.Repo

import com.example.aklimdakihediye.BuildConfig
import com.example.aklimdakihediye.Internet.ApiResponse
import com.example.aklimdakihediye.Internet.Models.GeminiModels.Content
import com.example.aklimdakihediye.Internet.Models.GeminiModels.GeminiRequest
import com.example.aklimdakihediye.Internet.Models.GeminiModels.GeminiResponse
import com.example.aklimdakihediye.Internet.Models.GeminiModels.GenerationConfig
import com.example.aklimdakihediye.Internet.Models.GeminiModels.Part
import com.example.aklimdakihediye.Internet.Models.SearchModels.DuckDuckGoResponse
import com.example.aklimdakihediye.Internet.safeApiCall
import com.example.aklimdakihediye.LocalDatabase.Dao.UserInformationDao
import com.example.aklimdakihediye.LocalDatabase.Models.LocalUserInformation
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MainRepo
@Inject constructor(
    private val userInformationDao: UserInformationDao,
    val client: HttpClient
) {
    val getUserInformation = userInformationDao.getLocalInformation().flowOn(Dispatchers.IO)

    suspend fun addInformation(userInformation: LocalUserInformation) {
        userInformationDao.addLocalInformation(userInformation)
    }

    suspend fun takeResultFromSearch(query: String): Flow<ApiResponse<DuckDuckGoResponse>> =
        safeApiCall {
            client.get {
                url("https://api.duckduckgo.com/")
                parameter("q", query)
                parameter("format", "json")
                parameter("no_redirect", 1)
                parameter("no_html", 1)
            }
        }

    suspend fun askGemini(prompt: String): Flow<ApiResponse<GeminiResponse>> = safeApiCall {
        val GEMINI_API_KEY = BuildConfig.API_KEY
        val MODEL_ID = "gemini-2.0-flash"
        val GENERATE_CONTENT_API = "generateContent"
        client.post(
            "${BuildConfig.BASE_URL} v1beta/models/${MODEL_ID}:${GENERATE_CONTENT_API}?key=${GEMINI_API_KEY}"
        ) {
            contentType(ContentType.Application.Json)
            setBody(
                GeminiRequest(
                    contents = listOf<Content>(Content(parts = listOf<Part>(Part(prompt)))),
                    generationConfig = GenerationConfig(response_mime_type = "text/plain")
                )
            )
        }
    }
}