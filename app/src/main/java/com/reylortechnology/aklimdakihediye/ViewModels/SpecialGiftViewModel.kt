package com.reylortechnology.aklimdakihediye.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.LocalUserInformation
import com.reylortechnology.aklimdakihediye.NavController.LocalNavController
import com.reylortechnology.aklimdakihediye.ObserverClasses.QueryProductsStatus
import com.reylortechnology.aklimdakihediye.Repo.MainRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class SpecialGiftViewModel
@Inject constructor(
    val mainRepo: MainRepo
) : ViewModel() {
    private val _userInformation = MutableStateFlow<LocalUserInformation?>(null)
    val userInformation = _userInformation.asStateFlow()

    private val _queryState = MutableStateFlow<QueryProductsStatus>(QueryProductsStatus.Loading)
    val queryState = _queryState.asStateFlow()



    fun backMainMenu(navController: NavController) {
        navController.navigate(LocalNavController.MainScreen) {
            popUpTo(0) { inclusive = true }
        }
    }

    fun setForAnotherInformation(localUserInformation: LocalUserInformation) {
        _userInformation.value = localUserInformation
        Log.d("TAG", "setForAnotherInformation: ${_userInformation.value?.name}")
    }


}