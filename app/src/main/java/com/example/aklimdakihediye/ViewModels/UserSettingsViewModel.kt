package com.example.aklimdakihediye.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.aklimdakihediye.LocalDatabase.Models.LocalUserInformation
import com.example.aklimdakihediye.Repo.UserSettingsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserSettingsViewModel
@Inject constructor(
    val userSettingsRepo : UserSettingsRepo
) : ViewModel(){

    val userInformation = userSettingsRepo.getUserInformation

    fun setUserInformation(userInformation : LocalUserInformation){
        viewModelScope.launch {
            try {
                userSettingsRepo.addUserInformation(userInformation)
            }catch (e: Exception){
                Log.e("UserSettingsViewModel", "setUserInformation: ${e.message}")
            }
        }
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