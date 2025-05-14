package com.reylortechnology.aklimdakihediye.Views.MainScreens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.reylortechnology.aklimdakihediye.AdMob.InterstitialAdScreen
import com.reylortechnology.aklimdakihediye.NavController.LocalNavController
import com.reylortechnology.aklimdakihediye.ObserverClasses.ScreenStateObservers.AboutGiftInformationScreenObserver
import com.reylortechnology.aklimdakihediye.ViewModels.AboutGiftInformationViewModel
import com.reylortechnology.aklimdakihediye.Views.PartScreens.DailyInfoScreen
import com.reylortechnology.aklimdakihediye.Views.PartScreens.InformationGiftScreen
import com.reylortechnology.aklimdakihediye.Views.PartScreens.SearchResultScreen



    @Composable
    fun AboutGiftInformation(
        modifier: Modifier = Modifier,
        navController: NavController,
        args: LocalNavController.DailyInfoScreen,
        viewModel: AboutGiftInformationViewModel = hiltViewModel()
    ) {

        val showAds = remember { mutableStateOf(true) }

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
                    giftInformationScreenState = giftInformationScreenState,
                    viewModel = viewModel
                )
            }

            AboutGiftInformationScreenObserver.AboutGiftInformation -> {
                InformationGiftScreen(
                    navController = navController,
                    giftInformationScreenState = giftInformationScreenState,
                )
            }

            AboutGiftInformationScreenObserver.SearchResult -> {
                if (showAds.value){
                    InterstitialAdScreen(
                        onAdClosed = {showAds.value = false }
                    )
                }else{
                    SearchResultScreen(
                        navController = navController,
                    )
                }
            }
        }

    }

