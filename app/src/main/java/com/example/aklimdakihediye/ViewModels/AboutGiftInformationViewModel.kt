package com.example.aklimdakihediye.ViewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.buildAnnotatedString
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.aklimdakihediye.Internet.ApiResponse
import com.example.aklimdakihediye.Internet.Models.GeminiModels.GeminiResponse
import com.example.aklimdakihediye.LocalDatabase.Models.LocalUserInformation
import com.example.aklimdakihediye.NavController.LocalNavController
import com.example.aklimdakihediye.Repo.MainRepo
import com.example.aklimdakihediye.models.GiftInformationModels.ForGiftInformation
import com.example.aklimdakihediye.models.GiftInformationModels.ForPersonInformation
import com.example.aklimdakihediye.models.GiftInformationModels.Products
import com.example.aklimdakihediye.ObserverClasses.QueryProductsStatus
import com.example.aklimdakihediye.ObserverClasses.SearchResultStatus
import com.example.aklimdakihediye.models.GiftInformationModels.SearchWithLabel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AboutGiftInformationViewModel
@Inject constructor(
    val mainRepo: MainRepo,
) : ViewModel() {
    val personInformation = mutableStateOf<ForPersonInformation?>(null)
    val giftInformation = mutableStateOf<ForGiftInformation?>(null)
    private val _userInformation = mutableStateOf<LocalUserInformation?>(null)
    val mtb = MutableStateFlow<Boolean>(_userInformation.value == null)
    val userForUserInformation = mtb.asStateFlow()


    private val _searchResults = MutableStateFlow<List<SearchWithLabel>>(emptyList())

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

    fun backMainMenu(navController: NavController) {
        navController.navigate(LocalNavController.MainScreen) {
            popUpTo(LocalNavController.MainScreen) { inclusive = true }
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
        viewModelScope.launch {
            try {
                products.map { product ->
                    async {
                        val result = mainRepo.searchWithKtorAndJsoup(product.name).take(2)
                        val resultWithLabel = SearchWithLabel(product.name, result)
                        _searchResults.value = _searchResults.value + resultWithLabel
                    }.await().also {
                        _totalState.value = SearchResultStatus.Success(_searchResults.value)
                    }
                }
            }catch (e : Exception){
                _totalState.value = SearchResultStatus.Error("Arama sırasında hata oluştu: ${e.message}")
                Log.e("Error", e.message.toString())
            }
        }
    }

    fun saveGiftInformation(forGiftInformation: ForGiftInformation) {
        giftInformation.value = ForGiftInformation(
            minPrice = forGiftInformation.minPrice,
            maxPrice = forGiftInformation.maxPrice,
            giftType = forGiftInformation.giftType,
            giftClass = forGiftInformation.giftClass
        )



        val prompt = makePrompt()

        viewModelScope.launch {
            try {
                mainRepo.askGemini(prompt).apply {
                    collect {
                        when (it) {
                            is ApiResponse.Error -> {
                                Log.e("Tag Gemini error", it.message)
                            }

                            is ApiResponse.Succes<GeminiResponse> -> {
                                try {
                                    val geminiResponse = it.body.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text.toString()

                                    val productRegex = Regex("""//(.+?)\s*-\s*(.+?) \((\d+)\s*TL\)//""")

                                    val products = productRegex.findAll(geminiResponse).map {
                                        val (name , brand , price) = it.destructured
                                        Products(name = name.trim(), brand = brand.trim(), price = price.trim())
                                    }.toList()
                                    _queryState.value = QueryProductsStatus.Success(products)
                                }catch (e: Exception){
                                    Log.e("Error", e.message.toString())
                                }
                            }
                        }
                    }
                }.catch {
                    Log.e("Error", it.message.toString())
                }
            } catch (e: Exception) {
                Log.e("Error in Vm", e.message.toString())
            }
        }
    }

    fun makePrompt(): String {
        return buildAnnotatedString {
            append("Ben bir hediye alacağım. Lütfen bana doğrudan 3 ürün ve marka ismini sadece şu formatta ver: '//ürün-marka (fiyat)//'. Başka hiçbir bilgi, açıklama veya selamlama istemiyorum. Sadece 3 satır ürün önerisi dön.\n\n")

            append("Benim hakkımda bilgiler:\n")
            append("İsim: ${_userInformation.value?.name}\n")
            append("Yaş: ${_userInformation.value?.old}\n")
            append("Burç: ${_userInformation.value?.zodiac}\n")
            append("İyi Yönler: ${_userInformation.value?.bestSide}\n")
            append("Hobiler: ${_userInformation.value?.hobbies}\n")
            append("Karakter Özellikleri: ${_userInformation.value?.caracter}\n")

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
            append("Hediye Türü: ${giftInformation.value!!.giftType}\n")
            append("İstenen Hediye Sınıfı: ${giftInformation.value!!.giftClass}\n")

            append("\nLütfen sadece şu formatta yanıt ver: '//ürün-marka (fiyat)//'. Başka hiçbir açıklama ya da cümle istemiyorum. Sadece 3 adet öneri dön.")
        }.toString()

    }
}
