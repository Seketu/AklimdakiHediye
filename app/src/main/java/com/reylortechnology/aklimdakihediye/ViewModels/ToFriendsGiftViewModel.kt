package com.reylortechnology.aklimdakihediye.ViewModels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.buildAnnotatedString
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reylortechnology.aklimdakihediye.Internet.ApiResponse
import com.reylortechnology.aklimdakihediye.Internet.Models.GeminiModels.GeminiResponse
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.LocalUserInformation
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.Peoples
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.SavedGifts
import com.reylortechnology.aklimdakihediye.ObserverClasses.QueryProductsStatus
import com.reylortechnology.aklimdakihediye.ObserverClasses.SearchResultStatus
import com.reylortechnology.aklimdakihediye.Repo.MainRepo
import com.reylortechnology.aklimdakihediye.Repo.PeopleRepo
import com.reylortechnology.aklimdakihediye.models.ComposeModels.SnackBarEvent
import com.reylortechnology.aklimdakihediye.models.GiftInformationModels.GiftInformation
import com.reylortechnology.aklimdakihediye.models.GiftInformationModels.Products
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

@HiltViewModel
class ToFriendsGiftViewModel
    @Inject constructor(
        val peopleRepo: PeopleRepo,
        savedStateHandle : SavedStateHandle,
        val mainRepo : MainRepo,
        context: Context
    )
    : ViewModel() {
    private val userInformation = mutableStateOf<LocalUserInformation?>(null)
    private val peopleId: Int = savedStateHandle.get<Int>("peopleId") ?: -1
        init {
            if (peopleId != -1){
                takePeopleInformation(peopleId)
            }
            viewModelScope.launch {
                userInformation.value = mainRepo.getUserInformationOnce().firstOrNull()
            }
            Log.e("ToFriendsGiftViewModel" , "peopleId : $peopleId")
        }

    private val _snackBarObserver = MutableSharedFlow<SnackBarEvent>(extraBufferCapacity = 1)
    val snackBarObserver : SharedFlow<SnackBarEvent> = _snackBarObserver.asSharedFlow()

    private val _geminiResponse = MutableStateFlow<QueryProductsStatus>(QueryProductsStatus.None)
    val geminiResponse : StateFlow<QueryProductsStatus> = _geminiResponse.asStateFlow()

    private val _searchResultStatus = MutableStateFlow<SearchResultStatus>(SearchResultStatus.None)
    val searchResultStatus : StateFlow<SearchResultStatus> = _searchResultStatus.asStateFlow()
    val peopleInformation = MutableStateFlow<Peoples?>(null)

    private val giftInformation = MutableStateFlow<GiftInformation?>(null)
    fun takePeopleInformation(peopleId : Int){
        viewModelScope.launch {
           val process = peopleRepo.takePeopleInformation(peopleId)
                process.onSuccess {
                    peopleInformation.value = it
                    Log.d("ToFriendsGiftViewModel" , "People Information : ${it.peopleName} - ${it.bestSide}")
                }.onFailure {
                    _snackBarObserver.tryEmit(
                        SnackBarEvent.ShowSnackBar(
                            message = "Kişi bilgileri alınırken bir hata oluştu"
                        )
                    )
                }
        }
    }

    fun takeSearchResult(products: List<Products>){
        viewModelScope.launch {
            _searchResultStatus.emit(SearchResultStatus.Loading)
            mainRepo.getGiftsAndUrls(products)
                .catch {
                    Log.e("ToFriendsGiftViewModel" , "Error at getGiftsAndUrls : ${it.message}")
                    _searchResultStatus.emit(SearchResultStatus.Error("Hediye önerileri alınırken bir hata oluştu"))
                }
                .onEach { response ->
                    _searchResultStatus.emit(SearchResultStatus.Success(response))
                }
                .launchIn(viewModelScope)
        }
    }

    fun saveGift(gift: SavedGifts){
        viewModelScope.launch {
            mainRepo.saveNewGift(
                gift
            )
        }
    }

    fun saveGiftInformation(
        giftInformation: GiftInformation,
        context: Context
    ){
        this.giftInformation.value = giftInformation

        val prompt = makePrompt(context)

        if (prompt != null){

                mainRepo.askGemini(prompt)
                    .catch {
                        Log.e("ToFriendsGiftViewModel" , "Error at askGemini : ${it.message}")
                        _geminiResponse.emit(QueryProductsStatus.Error("Hediye önerileri alınırken bir hata oluştu"))
                    }
                    .onEach {result->
                        when(result){
                            is ApiResponse.Error -> {
                                Log.e("ToFriendsGiftViewModel" , "Error at askGemini API : ${result.message}")
                                _geminiResponse.emit(QueryProductsStatus.Error(result.message))
                            }

                            is ApiResponse.Succes<GeminiResponse> -> {
                                val rawJsonResponse = result.body.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text
                                Log.d("ToFriendsGiftViewModel" , "Raw Gemini Response : $rawJsonResponse")
                                if (rawJsonResponse != null) {
                                    try {

                                        val cleanedJson = rawJsonResponse
                                            .removePrefix("```json")
                                            .removeSuffix("```")
                                            .trim()

                                        val giftList = Json.decodeFromString<List<Products>>(cleanedJson)
                                        _geminiResponse.emit(QueryProductsStatus.Success(giftList))
                                    } catch (e: Exception) {
                                        Log.e("JSON_ERROR", "Parse edilemedi: ${e.message}")
                                    }
                                }
                            }

                        }
                    }
                    .launchIn(viewModelScope)

        } else {
            Log.e("ToFriendsGiftViewModel" , "Prompt oluşturulamadı")
        }
    }

    fun makePrompt(
        context: Context
    ) : String?{
        val personInfo = peopleInformation.value
        val giftInfo = giftInformation.value

        if (personInfo == null || giftInfo == null || userInformation.value == null){
            _snackBarObserver.tryEmit(
                SnackBarEvent.ShowSnackBar(
                    message = "Lütfen önce tüm bilgileri doldurun"
                )
            )
            return null
        }
        return buildAnnotatedString {
            append("kendi bilgilerim : \n " +
                    "isim : ${userInformation.value?.name} \n " +
                    "yaş : ${userInformation.value?.old} \n " +
                    "burç : ${userInformation.value?.zodiac} \n " +
                    "en iyi yön : ${userInformation.value?.bestSide} \n " +
                    "hobiler : ${userInformation.value?.hobbies} \n "

            )

            append("hediye alacağım kişi : \n " +
                    "isim : ${personInfo.peopleName} \n " +
                    "yaş : ${personInfo.age} \n " +
                    "burç : ${personInfo.zodiac} \n " +
                    "en iyi yön : ${personInfo.bestSide} \n " +
                    "hobiler : ${personInfo.hobbies} \n " +
                    "karakter özellikleri : ${personInfo.character} \n "
            )

            append(
                   "hediyeden beklentilerim : \n " +
                   "fiyat aralığım : ${context.getString(giftInfo.price.textSource)} \n " +
                   "hediyenin anlamı : ${context.getString(giftInfo.giftVibe.textSource)} \n " +
                   "Özel istekler : ${giftInfo.specialDemand} \n " +
                   "hediyenin sebebi : ${giftInfo.forWhy} \n "
            )

        }.toString()
    }


}