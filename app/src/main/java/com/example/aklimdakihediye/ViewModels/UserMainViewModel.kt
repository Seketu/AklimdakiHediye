package com.example.aklimdakihediye.ViewModels

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.aklimdakihediye.NavController.LocalNavController

class UserMainViewModel : ViewModel() {

    fun navigateFromColumn(
        navController: NavController,
        forDay : String,
        source : Int
    ){
        navController.navigate(LocalNavController.DailyInfoScreen)
    }

}