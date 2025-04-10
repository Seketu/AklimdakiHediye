package com.example.aklimdakihediye.Views.MainScreens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.aklimdakihediye.NavController.LocalNavController
import com.example.aklimdakihediye.ObserverClasses.AboutGiftInformationScreenObserver
import com.example.aklimdakihediye.ViewModels.AboutGiftInformationViewModel
import com.example.aklimdakihediye.Views.PartScreens.DailyInfoScreen
import com.example.aklimdakihediye.Views.PartScreens.InformationGiftScreen
import com.example.aklimdakihediye.Views.PartScreens.SearchResultScreen

class DailyInfoView {

    @Composable
    fun AboutGiftInformation(
        modifier: Modifier = Modifier,
        navController: NavHostController,
        args: LocalNavController.DailyInfoScreen,
        viewModel: AboutGiftInformationViewModel = hiltViewModel()
    ) {

        if (args.forAnother == false) {
            viewModel.takeInformationFromLocal()
        }

        val giftInformationScreenState = remember {
            mutableStateOf(AboutGiftInformationScreenObserver.AboutPersonInformation)
        }

        when (giftInformationScreenState.value) {
            AboutGiftInformationScreenObserver.AboutPersonInformation -> {
                DailyInfoScreen(
                    navController = navController,
                    args = args,
                    giftInformationScreenState = giftInformationScreenState
                )
            }

            AboutGiftInformationScreenObserver.AboutGiftInformation -> {
                InformationGiftScreen(
                    navController = navController,
                    giftInformationScreenState = giftInformationScreenState
                )
            }

            AboutGiftInformationScreenObserver.SearchResult -> {
                SearchResultScreen(
                    navController = navController
                )
            }
        }

    }

}
