package com.example.aklimdakihediye.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.aklimdakihediye.LocalDatabase.Models.LocalUserInformation
import com.example.aklimdakihediye.Repo.UserSettingsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserSettingsViewModel
@Inject constructor(
    val userSettingsRepo : UserSettingsRepo
) : ViewModel(){

    val _isEdit = MutableStateFlow<Boolean>(false)
    val isEdit = _isEdit.asStateFlow()
    val userInformation = userSettingsRepo.getUserInformation

    fun setUserInformation(userInformation : LocalUserInformation){
        viewModelScope.launch {
            try {
                userSettingsRepo.updateUserInformation(userInformation)
                _isEdit.value = false
            }catch (e: Exception){
                Log.e("UserSettingsViewModel", "setUserInformation: ${e.message}")
            }
        }
    }

    fun setEdit(){
        _isEdit.value = true
    }

    fun exitScreen(navController: NavController) {
        try {
            navController.popBackStack()
        }catch (
            e: Exception
        ) {
            Log.e("UserSettingsViewModel", "exitScreen: ${e.message}")
        }
    }
}