package com.example.aklimdakihediye.Repo

import android.net.Uri
import android.util.Log
import com.example.aklimdakihediye.BuildConfig
import com.example.aklimdakihediye.Internet.ApiResponse
import com.example.aklimdakihediye.Internet.Models.GeminiModels.Content
import com.example.aklimdakihediye.Internet.Models.GeminiModels.GeminiRequest
import com.example.aklimdakihediye.Internet.Models.GeminiModels.GeminiResponse
import com.example.aklimdakihediye.Internet.Models.GeminiModels.GenerationConfig
import com.example.aklimdakihediye.Internet.Models.GeminiModels.Part
import com.example.aklimdakihediye.Internet.safeApiCall
import com.example.aklimdakihediye.LocalDatabase.Dao.UserInformationDao
import com.example.aklimdakihediye.LocalDatabase.Models.LocalUserInformation
import com.example.aklimdakihediye.LocalDatabase.Models.SavedGifts
import com.example.aklimdakihediye.models.ComposeModels.SearchCardModel
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import org.jsoup.Jsoup
import javax.inject.Inject

class MainRepo
@Inject constructor(
    private val userInformationDao: UserInformationDao,
    private val giftInformationDao: SavedVariableRepo,
    val client: HttpClient
) {
    val getUserInformation = userInformationDao.getLocalInformation().flowOn(Dispatchers.IO)

    suspend fun addInformation(userInformation: LocalUserInformation) {
        userInformationDao.addLocalInformation(userInformation)
    }

    suspend fun getDuckDuckGoHtml(query: String): String {
        val url = "https://html.duckduckgo.com/html/?q=${query.replace(" ", "+")}"

        val response: HttpResponse = client.get(url) {
            headers {
                append(HttpHeaders.UserAgent, "Mozilla/5.0")
            }
        }
        return response.bodyAsText()
    }

    suspend fun saveNewGift(gift: SavedGifts){
        giftInformationDao.saveGift(gift)
    }

    suspend fun searchWithKtorAndJsoup(query: String): List<SearchCardModel> {
        val html = getDuckDuckGoHtml(query)
        val doc = Jsoup.parse(html)

        return doc.select("div.result").mapNotNull { element ->
            val rawLink = element.select("a").attr("href")
            val decodedLink = decodeDuckLink(rawLink)

            val domain = extractDomain(decodedLink)?.removePrefix("www.") ?: return@mapNotNull null
            val desc = element.select(".result__snippet").text()

            val imgEl = element.selectFirst("img")
            val imageUrl = imgEl?.attr("src")?.takeIf { it.isNotBlank() }
            val fullImageUrl = imageUrl?.let {
                if (it.startsWith("//")) "https:$it" else it
            }

            Log.d("TAG", "image: $fullImageUrl")
            Log.d("TAG", "link: $decodedLink")
            Log.d("TAG", "name: $domain")
            Log.d("TAG", "desc: $desc")

            SearchCardModel(
                name = domain,
                url = decodedLink,
                description = desc,
                imageUrl = fullImageUrl
            )
        }
    }

    fun decodeDuckLink(link: String): String {
        return try {
            val uri = Uri.parse("https:$link")
            val encoded = uri.getQueryParameter("uddg")
            Uri.decode(encoded)
        } catch (e: Exception) {
            link // fallback
        }
    }

    fun extractDomain(url: String): String? {
        return try {
            Uri.parse(url).host
        } catch (e: Exception) {
            null
        }
    }
    suspend fun askGemini(prompt: String): Flow<ApiResponse<GeminiResponse>> = safeApiCall {
        val GEMINI_API_KEY = BuildConfig.API_KEY
        val MODEL_ID = "gemini-2.0-flash"
        val GENERATE_CONTENT_API = "generateContent"
        client.post(
            "${BuildConfig.BASE_URL}v1beta/models/${MODEL_ID}:${GENERATE_CONTENT_API}?key=${GEMINI_API_KEY}"
        ) {
            contentType(ContentType.Application.Json)
            setBody(
                GeminiRequest(
                    contents = listOf<Content>(Content(parts = listOf<Part>(Part(prompt)))),
                    generationConfig = GenerationConfig(response_mime_type = "text/plain")
                )
            )
        }.body()
    }
}