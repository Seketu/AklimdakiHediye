package com.reylortechnology.aklimdakihediye.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reylortechnology.aklimdakihediye.Repo.SavedVariableRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedVariablesViewModel
@Inject constructor(
    val savedVariablesRepo: SavedVariableRepo
) : ViewModel(){

    val savedGifts = savedVariablesRepo.savedGifts

    fun deleteGift(giftId : List<Int>){
        viewModelScope.launch {
            savedVariablesRepo.deleteGift(giftId)
        }
    }
}