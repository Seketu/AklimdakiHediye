package com.reylortechnology.aklimdakihediye.ViewModels

import android.util.Log
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.LocalUserInformation
import com.reylortechnology.aklimdakihediye.Repo.UserSettingsRepo
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

    val userInformation = userSettingsRepo.getUserInformation

    fun setUserInformation(userInformation: LocalUserInformation, isModified: State<Boolean>){
        viewModelScope.launch {
            try {
                userSettingsRepo.updateUserInformation(userInformation)
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