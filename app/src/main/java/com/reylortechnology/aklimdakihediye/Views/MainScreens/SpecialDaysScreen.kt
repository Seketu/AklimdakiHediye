package com.reylortechnology.aklimdakihediye.Views.MainScreens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.reylortechnology.aklimdakihediye.ObserverClasses.ScreenStateObservers.SpecialDaysScreenObserver
import com.reylortechnology.aklimdakihediye.ViewModels.SpecialDaysViewModel
import com.reylortechnology.aklimdakihediye.Views.PartScreens.SpecialDayMainScreen
import com.reylortechnology.aklimdakihediye.Views.PartScreens.SpecialDayTakeDayInformationScreen
import com.reylortechnology.aklimdakihediye.Views.PartScreens.SpecialDayTakePersonInformationScreen


//Özel günler için hatırlatmaların ayarlandığı sayfa
@Composable
fun SpecialDaysScreen(
    modifier: Modifier = Modifier,
    viewModel : SpecialDaysViewModel = hiltViewModel(),
    navController : NavController
) {

    val screenObserver = remember{
        mutableStateOf(SpecialDaysScreenObserver.MainScreen)
    }

    screenObserver.value.let {
        when(it){
            SpecialDaysScreenObserver.MainScreen -> {
                SpecialDayMainScreen(screenObserver,navController)
            }
            SpecialDaysScreenObserver.TakePersonInformation -> {
                SpecialDayTakePersonInformationScreen(screenObserver)
            }
            SpecialDaysScreenObserver.TakeDayInformation -> {
                SpecialDayTakeDayInformationScreen(screenObserver)
            }
        }
    }

}
