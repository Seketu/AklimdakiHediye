package com.reylortechnology.aklimdakihediye.ViewModels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.Peoples
import com.reylortechnology.aklimdakihediye.Repo.PeopleRepo
import com.reylortechnology.aklimdakihediye.models.ComposeModels.SnackBarEvent
import com.reylortechnology.aklimdakihediye.models.GiftInformationModels.GiftInformation
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ToFriendsGiftViewModel
    @Inject constructor(
        val peopleRepo: PeopleRepo,
        savedStateHandle : SavedStateHandle
    )
    : ViewModel() {
    private val peopleId: Int = savedStateHandle.get<Int>("peopleId") ?: -1
        init {
            if (peopleId != -1){
                takePeopleInformation(peopleId)
            }
            Log.e("ToFriendsGiftViewModel" , "peopleId : $peopleId")
        }

    private val _snackBarObserver = MutableSharedFlow<SnackBarEvent>(extraBufferCapacity = 1)
    val snackBarObserver : SharedFlow<SnackBarEvent> = _snackBarObserver.asSharedFlow()

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

    fun saveGiftInformation(giftInformation: GiftInformation){
        this.giftInformation.value = giftInformation
    }
}