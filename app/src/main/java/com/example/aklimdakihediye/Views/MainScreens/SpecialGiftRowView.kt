package com.example.aklimdakihediye.Views.MainScreens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.aklimdakihediye.NavController.LocalNavController
import com.example.aklimdakihediye.ObserverClasses.SpecialGiftRowViewObserver
import com.example.aklimdakihediye.ViewModels.SpecialGiftViewModel
import com.example.aklimdakihediye.Views.PartScreens.UserInformationView


@Composable
fun SpecialGiftRowView(
    args: LocalNavController.SpecialGiftRowScreen,
    navController: NavController,
    viewModel: SpecialGiftViewModel = hiltViewModel()
) {
    val screenStateObserver = remember {
        mutableStateOf<SpecialGiftRowViewObserver>(SpecialGiftRowViewObserver.None)
    }

    if(args.forAnother){
        screenStateObserver.value = SpecialGiftRowViewObserver.TakeInformation
    }

    when(screenStateObserver.value){
        SpecialGiftRowViewObserver.None -> TODO()
        SpecialGiftRowViewObserver.TakeInformation -> {
            UserInformationView(
                navController = navController,
                viewModel = viewModel,
                screenStateObserver = screenStateObserver
            )
        }
        SpecialGiftRowViewObserver.TakeGiftInformation -> {

        }
        is SpecialGiftRowViewObserver.TakePersonInformation -> {

        }
    }

}
