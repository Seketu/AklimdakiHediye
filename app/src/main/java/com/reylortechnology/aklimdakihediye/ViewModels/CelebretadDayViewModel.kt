package com.reylortechnology.aklimdakihediye.ViewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.buildAnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.reylortechnology.aklimdakihediye.Internet.ApiResponse
import com.reylortechnology.aklimdakihediye.Internet.Models.GeminiModels.GeminiResponse
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.LocalUserInformation
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.SavedGifts
import com.reylortechnology.aklimdakihediye.NavController.LocalNavController
import com.reylortechnology.aklimdakihediye.Repo.MainRepo
import com.reylortechnology.aklimdakihediye.models.GiftInformationModels.ForGiftInformation
import com.reylortechnology.aklimdakihediye.models.GiftInformationModels.ForPersonInformation
import com.reylortechnology.aklimdakihediye.models.GiftInformationModels.Products
import com.reylortechnology.aklimdakihediye.ObserverClasses.QueryProductsStatus
import com.reylortechnology.aklimdakihediye.ObserverClasses.SearchResultStatus
import com.reylortechnology.aklimdakihediye.models.ComposeModels.SearchCardModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class CelebretadDayViewModel
@Inject constructor(
    val mainRepo: MainRepo,
) : ViewModel() {
    val personInformation = mutableStateOf<ForPersonInformation?>(null)
    val giftInformation = mutableStateOf<ForGiftInformation?>(null)
    private val _userInformation = mutableStateOf<LocalUserInformation?>(null)
    val mtb = MutableStateFlow<Boolean>(_userInformation.value == null)
    val userForUserInformation = mtb.asStateFlow()


    private val _totalState = MutableStateFlow<SearchResultStatus>(SearchResultStatus.Loading)
    val totalState = _totalState.asStateFlow()

    private val _queryState = MutableStateFlow<QueryProductsStatus>(QueryProductsStatus.Loading)
    val queryState = _queryState.asStateFlow()

    fun setForAnotherInformation(localUserInformation: LocalUserInformation) {
        _userInformation.value = localUserInformation
        Log.d("TAG", "setForAnotherInformation: ${_userInformation.value?.name}")
    }

    fun saveLocalInformation(userLocalInformation: LocalUserInformation) {
        viewModelScope.launch {
            mainRepo.addInformation(userLocalInformation)
        }
    }

    fun saveGift(gift: SavedGifts){
        viewModelScope.launch {
            mainRepo.saveNewGift(gift)
        }
    }

    fun backMainMenu(navController: NavController) {
        navController.navigate(LocalNavController.MainScreen) {
            popUpTo(0) { inclusive = true }
        }
    }

    fun takeInformationFromLocal() {
        viewModelScope.launch {
            try {
                mainRepo.getUserInformation.apply {
                    collect {
                        _userInformation.value = it.first()
                    }
                }.catch {
                    Log.e("Error at giftVm", it.message.toString())
                }
            } catch (e: Exception) {
                Log.e("Error", e.message.toString())
            }
        }
    }

    fun savePersonInformation(forPersonInformation: ForPersonInformation) {
        personInformation.value = ForPersonInformation(
            forWho = forPersonInformation.forWho,
            name = forPersonInformation.name,
            old = forPersonInformation.old,
            zodiac = forPersonInformation.zodiac,
            job = forPersonInformation.job,
            bestSide = forPersonInformation.bestSide,
            hobbies = forPersonInformation.hobbies,
            lastGifts = forPersonInformation.lastGifts,
            relationship = forPersonInformation.relationship ?: null
        )
        Log.d("TAG", "savePersonInformation: ${personInformation.value}")
        Log.d("TAG", "Another Information: ${_userInformation.value?.name}")
    }

    fun takeSearchResult(products: List<Products>) {
        _totalState.value = SearchResultStatus.Loading
        Log.d(GIFT_FLOW_TAG, "${products.size} ürün için link/görsel aranıyor")
        viewModelScope.launch {
            mainRepo.getGiftsAndUrls(
                products
            )
                .catch {
                    Log.e(GIFT_FLOW_TAG, "Ürün araması başarısız: ${it.message}", it)
                    _totalState.value = SearchResultStatus.Error(GIFT_ERROR_MESSAGE)
                }
                .onEach {
                    Log.d(GIFT_FLOW_TAG, "Ürün araması tamamlandı: ${it.size} kart")
                    _totalState.value = SearchResultStatus.Success(it)
            }.collect()
        }
    }

    fun saveGiftInformation(forGiftInformation: ForGiftInformation) {
        giftInformation.value = ForGiftInformation(
            minPrice = forGiftInformation.minPrice,
            maxPrice = forGiftInformation.maxPrice,
            giftMean = forGiftInformation.giftMean,
        )

        _queryState.value = QueryProductsStatus.Loading

        viewModelScope.launch {
            try {
                val prompt = makePrompt()
                Log.d(GIFT_FLOW_TAG, "Gemini isteği gönderiliyor")

                mainRepo.askGemini(prompt).collect { response ->
                    when (response) {
                        is ApiResponse.Error -> {
                            failQuery("Gemini isteği başarısız: ${response.message}")
                        }

                        is ApiResponse.Succes<GeminiResponse> -> {
                            val candidate = response.body.candidates.firstOrNull()
                            val rawJsonResponse = candidate?.content?.parts?.firstOrNull()?.text
                            Log.d(
                                GIFT_FLOW_TAG,
                                "Gemini cevap verdi. finishReason=${candidate?.finishReason}, " +
                                        "metin uzunluğu=${rawJsonResponse?.length}"
                            )

                            if (rawJsonResponse.isNullOrBlank()) {
                                failQuery("Gemini boş cevap döndü (finishReason=${candidate?.finishReason})")
                                return@collect
                            }

                            val giftList = try {
                                val cleanedJson = rawJsonResponse
                                    .trim()
                                    .removePrefix("```json")
                                    .removeSuffix("```")
                                    .trim()
                                Json { ignoreUnknownKeys = true }
                                    .decodeFromString<List<Products>>(cleanedJson)
                            } catch (e: Exception) {
                                failQuery("Gemini cevabı parse edilemedi: ${e.message}\nCevap: $rawJsonResponse")
                                return@collect
                            }

                            if (giftList.isEmpty()) {
                                failQuery("Gemini boş hediye listesi döndü")
                                return@collect
                            }

                            Log.d(GIFT_FLOW_TAG, "${giftList.size} hediye önerisi alındı")
                            _queryState.value = QueryProductsStatus.Success(giftList)
                        }
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                failQuery("Hediye önerisi alınırken beklenmeyen hata: ${e.message}", e)
            }
        }
    }

    private fun failQuery(logMessage: String, throwable: Throwable? = null) {
        Log.e(GIFT_FLOW_TAG, logMessage, throwable)
        _queryState.value = QueryProductsStatus.Error(GIFT_ERROR_MESSAGE)
    }

    fun makePrompt(): String {
        return buildAnnotatedString {
            append("Benim hakkımda bilgiler:\n")
            append("İsim: ${_userInformation.value?.name}\n")
            append("Yaş: ${_userInformation.value?.old}\n")
            append("Burç: ${_userInformation.value?.zodiac}\n")
            append("İyi Yönler: ${_userInformation.value?.bestSide}\n")
            append("Hobiler: ${_userInformation.value?.hobbies}\n")
            append("Karakter Özellikleri: ${_userInformation.value?.character}\n")

            append("\nHediye Alınacak Kişi Hakkında Bilgiler:\n")
            append("İsim: ${personInformation.value!!.name}\n")
            append("Hediye Alma Sebebi: ${personInformation.value!!.forWho}\n")
            append("Yaş: ${personInformation.value!!.old}\n")
            append("Burç: ${personInformation.value!!.zodiac}\n")
            append("Meslek: ${personInformation.value!!.job}\n")
            append("İyi Yönleri: ${personInformation.value!!.bestSide}\n")
            append("Hobiler: ${personInformation.value!!.hobbies}\n")
            append("Son Aldığım Hediyeler: ${personInformation.value!!.lastGifts}\n")
            if (personInformation.value!!.relationship != null) {
                append("İlişki Durumu: ${personInformation.value!!.relationship}\n")
            }
            append("\nHediye Bilgileri:\n")
            append("Minimum Fiyat: ${giftInformation.value!!.minPrice} TL\n")
            append("Maksimum Fiyat: ${giftInformation.value!!.maxPrice} TL\n")
            append("Hediye Türü: ${giftInformation.value!!.giftMean}\n")
        }.toString()

    }

    private companion object {
        const val GIFT_FLOW_TAG = "GiftFlow"
        const val GIFT_ERROR_MESSAGE = "Hediye önerileri alınırken bir hata oluştu. Lütfen tekrar deneyin."
    }
}
