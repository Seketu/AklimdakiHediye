package com.reylortechnology.aklimdakihediye.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reylortechnology.aklimdakihediye.Internet.ApiResponse
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.Peoples
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.SavedGifts
import com.reylortechnology.aklimdakihediye.ObserverClasses.QueryProductsStatus
import com.reylortechnology.aklimdakihediye.ObserverClasses.SearchResultStatus
import com.reylortechnology.aklimdakihediye.Repo.MainRepo
import com.reylortechnology.aklimdakihediye.Repo.PeopleRepo
import com.reylortechnology.aklimdakihediye.models.ComposeModels.SearchCardModel
import com.reylortechnology.aklimdakihediye.models.GiftInformationModels.GiftInformation
import com.reylortechnology.aklimdakihediye.models.GiftInformationModels.Products
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.utils.io.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class MainSearchGiftViewModel
    @Inject constructor(
        val peopleRepo: PeopleRepo,
        val mainRepo: MainRepo
    ) : ViewModel(){

    private val _toastEvent = Channel<String>()

    val toastEvent = _toastEvent.receiveAsFlow()
    private val _searchResultStatus = MutableStateFlow<SearchResultStatus>(SearchResultStatus.None)
    val searchResultStatus = _searchResultStatus.asStateFlow()
    private val _geminiQueryState =  MutableStateFlow<QueryProductsStatus>(QueryProductsStatus.None)
    val geminiQueryState = _geminiQueryState.asStateFlow()

    private val _peopleInformation = MutableStateFlow<Peoples?>(null)
    val peopleInformation = _peopleInformation.asStateFlow()

    private val _giftInformation = MutableStateFlow<GiftInformation?>(null)
    val giftInformation = _giftInformation.asStateFlow()

    private val _searchResults = MutableStateFlow<List<SearchCardModel>>(emptyList())
    val searchResults = _searchResults.asStateFlow()

    fun initPeopleInformation(peoples: Peoples){
        _peopleInformation.value = peoples
    }

    fun setGiftInformation(giftInformation: GiftInformation) {
        _giftInformation.value = giftInformation
        searchGifts()
    }

    private fun searchGifts() {
        val people = _peopleInformation.value
        val giftInfo = _giftInformation.value

        if (people != null && giftInfo != null) {
            _geminiQueryState.value = QueryProductsStatus.Loading

            val prompt = createGiftSearchPrompt(people, giftInfo)

            viewModelScope.launch {
                try {
                    mainRepo.askGemini(prompt).collect { response ->
                        when (response) {
                            is ApiResponse.Succes -> {
                                val content = response.body.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text
                                if (content != null) {
                                    try {
                                        val json = Json { ignoreUnknownKeys = true }
                                        val products: List<Products> = json.decodeFromString(content)

                                        _geminiQueryState.value = QueryProductsStatus.Success(products)

                                        // Search results'ı başlat
                                        getSearchResults(products)

                                    } catch (e: Exception) {
                                        Log.e("MainSearchGiftViewModel", "JSON parsing error: ${e.message}")
                                        _geminiQueryState.value = QueryProductsStatus.Error("Hediye önerileri alınırken bir hata oluştu: ${e.message}")
                                    }
                                } else {
                                    Log.e("Erro at MSgiftVİewModel" , "Hata content null")
                                    _geminiQueryState.value = QueryProductsStatus.Error("Hediye önerileri alınamadı")
                                }
                            }
                            is ApiResponse.Error -> {
                                Log.e("MainSearchGiftViewModel", "API error: ${response.message}")
                                _geminiQueryState.value = QueryProductsStatus.Error("API hatası: ${response.message}")
                            }
                        }
                    }
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    Log.e("MainSearchGiftViewModel", "searchGifts error: ${e.message}")
                    _geminiQueryState.value = QueryProductsStatus.Error("Hediye arama sırasında hata: ${e.message}")
                }
            }
        }
    }

    private fun createGiftSearchPrompt(people: Peoples, giftInfo: GiftInformation): String {
        val hobbiesText = people.hobbies.joinToString(", ")
        val characterText = people.character.joinToString(", ")

        return """
            Kişi Bilgileri:
            - İsim: ${people.peopleName}
            - Yaş: ${people.age}
            - Cinsiyet: ${people.gender.name}
            - İlişki: ${people.relationship.name}
            - Meslek: ${people.job}
            - Hobiler: $hobbiesText
            - Karakter Özellikleri: $characterText
            - En İyi Yanı: ${people.bestSide}
            
            Hediye Bilgileri:
            - Hediye Nedeni: ${giftInfo.forWhy}
            - Özel İstekler: ${giftInfo.specialDemand}
            - Hediye Ruhu: ${giftInfo.giftVibe.name}
            - Bütçe: ${giftInfo.price.name}
            
            Bu bilgilere göre 10 adet hediye önerisi ver.
        """.trimIndent()
    }

    private fun getSearchResults(products: List<Products>) {
        _searchResultStatus.value = SearchResultStatus.Loading

        viewModelScope.launch {
            try {
                mainRepo.getGiftsAndUrls(products).collect { searchCardModels ->
                    _searchResults.value = searchCardModels
                    _searchResultStatus.value = SearchResultStatus.Success(searchCardModels)
                }
            } catch (e: Exception) {
                Log.e("MainSearchGiftViewModel", "getSearchResults error: ${e.message}")
                _searchResultStatus.value = SearchResultStatus.Error("Arama sonuçları alınırken hata: ${e.message}")
            }
        }
    }

    fun savePeople(peoples: Peoples){
        _peopleInformation.value = peoples
        viewModelScope.launch {
            try {
                _peopleInformation.value?.let {
                  val job =   peopleRepo.addPeople(it)
                    job.onSuccess {
                        _toastEvent.send("Kişi bilgileri başarıyla kaydedildi.")
                    }.onFailure {
                        _toastEvent.send("Kişi bilgileri kaydedilirken bir hata oluştu: ${it.message}")
                    }
                }
            }catch (e : CancellationException){
                throw e // CancellationException'ı yeniden fırlat
            }catch (e : Exception){
                Log.e("MainSearchGiftViewModel", "savePeople: Kişi bilgileri kaydedilirken hata oluştu.", e)
                _toastEvent.send("Kişi bilgileri kaydedilirken bir hata oluştu: ${e.message}")
            }
        }
    }

    fun saveGift(): (SavedGifts) -> Unit = { gift ->
        viewModelScope.launch {
            try {
                mainRepo.saveNewGift(gift)
                _toastEvent.send("Hediye başarıyla kaydedildi!")
            } catch (e: Exception) {
                Log.e("MainSearchGiftViewModel", "saveGift error: ${e.message}")
                _toastEvent.send("Hediye kaydedilirken hata oluştu: ${e.message}")
            }
        }
    }

    fun takeSearchResultStatus(): (List<Products>) -> Unit = { products ->
        getSearchResults(products)
    }

}