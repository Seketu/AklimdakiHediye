package com.reylortechnology.aklimdakihediye.Repo

import android.net.Uri
import android.util.Log
import com.reylortechnology.aklimdakihediye.BuildConfig
import com.reylortechnology.aklimdakihediye.Internet.ApiResponse
import com.reylortechnology.aklimdakihediye.Internet.Models.GeminiModels.GeminiResponse
import com.reylortechnology.aklimdakihediye.Internet.Prompts.GiftAdvisorPrompt
import com.reylortechnology.aklimdakihediye.Internet.safeApiCall
import com.reylortechnology.aklimdakihediye.LocalDatabase.Dao.PeoplesDao
import com.reylortechnology.aklimdakihediye.LocalDatabase.Dao.UserInformationDao
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.LocalUserInformation
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.SavedGifts
import com.reylortechnology.aklimdakihediye.Services.DuckDuckGoSearch
import com.reylortechnology.aklimdakihediye.Services.ImageSearchService
import com.reylortechnology.aklimdakihediye.models.ComposeModels.SearchCardModel
import com.reylortechnology.aklimdakihediye.models.Enums.MarketPlaces
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
    val getUserInformation = userInformationDao.getLocalInformation().flowOn(Dispatchers.IO)
    val peoplesInformation = peopleInformationDao.getAllPeoples().flowOn(Dispatchers.IO)

    suspend fun getUserInformationOnce() : List<LocalUserInformation>{
        return withContext(Dispatchers.IO){
            userInformationDao.getLocalInformationOnce()
        }
    }

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
                    val searchResult = bingSearchService.searchProduct(
                        gift.searchQuery?.takeIf { it.isNotBlank() } ?: gift.name
                    )
                    SearchCardModel(
                        name = gift.name,
                        url = searchResult.giftUrl ?: MarketPlaces.searchUrl(gift.name),
                        imageUrl = searchResult.imageUrl,
                        price = gift.price
                    )
                }
            }.awaitAll()
            Log.d("Gifts and Urls",result.toString())
            emit(result)
        }
    }

    fun askGemini(prompt: String): Flow<ApiResponse<GeminiResponse>> = safeApiCall<GeminiResponse> {
        client.post(
            "${BuildConfig.BASE_URL}v1beta/models/${GiftAdvisorPrompt.MODEL_ID}:generateContent?key=${BuildConfig.GEMINI_API_KEY}"
        ) {
            contentType(ContentType.Application.Json)
            setBody(GiftAdvisorPrompt.request(prompt))
        }.body()
    }.flowOn(Dispatchers.IO)
}
