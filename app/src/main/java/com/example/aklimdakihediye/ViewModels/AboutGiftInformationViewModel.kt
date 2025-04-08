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
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun takeInformationFromLocal(){
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
                                Log.e("Tag", it.message)
                            }
                            is ApiResponse.Succes<GeminiResponse> -> {
                                Log.d("Tag", it.body.response.text)
                            }
                        }
                    }
                }.catch {
                    Log.e("Error",it.message.toString())
                }
            }catch (e : Exception){
                Log.e("Error in Vm",e.message.toString())
            }
        }

    }

    fun makePrompt() : String {
        return buildAnnotatedString {
            append("Ben bir hediye alacağım. Lütfen bana doğrudan ürün ve marka ismini şu formatta ver: '//ürün-marka//'. Önerilerim minimum ve maksimum fiyat aralığında kalmalı.\n\n") // Daha temiz bir ayraç ve yeni satır

            append("Benim hakkımda bilgiler :\n")

                append("ismim : ${_userInformation.value?.name}")
                append("yaşım : ${_userInformation.value?.old}")
                append("burçum : ${_userInformation.value?.zodiac}")
                append("iyi yönler : ${_userInformation.value?.bestSide}")
                append("hobilerim : ${_userInformation.value?.hobbies}")
                append("Karakterimin iyi yönleri : ${_userInformation.value?.caracter}")
                append("ismim : ${_userInformation.value?.name}")
                append("yaşım : ${_userInformation.value?.old}")
                append("burçum : ${_userInformation.value?.zodiac}")
                append("iyi yönler : ${_userInformation.value?.bestSide}")
                append("hobilerim : ${_userInformation.value?.hobbies}")
                append("Karakterimin iyi yönleri : ${_userInformation.value?.caracter}")


            append("Hediye Alınacak Kişi Hakkında Bilgiler:\n") // Bilgileri gruplamak için başlık
            append("İsim: ${personInformation.value!!.name}\n")
            append("Hediye Alma Sebebi: ${personInformation.value!!.forWho}\n")
            append("Yaş: ${personInformation.value!!.old}\n")
            append("Burç: ${personInformation.value!!.zodiac}\n")
            append("Meslek: ${personInformation.value!!.job}\n")
            append("İyi Yönleri: ${personInformation.value!!.bestSide}\n")
            append("Hobileri: ${personInformation.value!!.hobbies}\n")
            append("En Son Aldığım Hediyeler: ${personInformation.value!!.lastGifts}\n")
            if (personInformation.value!!.relationship != null) {
                append("İlişki Durumumuz: ${personInformation.value!!.relationship}\n")
            }

            append("\nHediye Bilgileri:\n") // Bilgileri gruplamak için başlık
            append("Minimum Fiyat: ${giftInformation.value!!.minPrice} TL\n")
            append("Maksimum Fiyat: ${giftInformation.value!!.maxPrice} TL\n")
            append("Hediye Türü: ${giftInformation.value!!.giftType}\n")
            append("İstediğim Tür: ${giftInformation.value!!.giftClass}\n")

            append("Lütfen ürün adını ve markasını şu formatta verin: '//ürün-marka (fiyat) //'. Örneğin: '//Akıllı Saat-Samsung (499 TL)//'. Fiyat, Türk Lirası cinsinden ve parantez içinde belirtilmelidir. Sadece bu formattaki cevaplar kabul edilecektir.")        }.toString()
    }
}