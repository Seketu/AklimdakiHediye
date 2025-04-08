package com.example.aklimdakihediye.NavController

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.aklimdakihediye.LocalDatabase.Models.LocalUserInformation
import com.example.aklimdakihediye.Views.UserInfoView
import com.example.aklimdakihediye.Views.DailyInfoView
import com.example.aklimdakihediye.Views.UserMainView
import kotlinx.serialization.Serializable

class LocalNavController {

    @Composable
    fun LocalNavHost(modifier: Modifier = Modifier) {
        val navController = rememberNavController()

        NavHost(
            startDestination = MainScreen,
            navController = navController
        ){
            composable<LocalNavController.MainScreen> {
                UserMainView().UserMainScreen(navController = navController)
            }

            composable<LocalNavController.UserInfoScreen> {
                val forDay = it.toRoute<LocalNavController.UserInfoScreen>()
                UserInfoView().DailyAskUserInfoScreen(
                    navController = navController,
                    args = UserInfoScreen(forDay = forDay.forDay,forDay.source,forDay.forWhat)
                )
            }

            composable<DailyInfoScreen> {
                val forDay = it.toRoute<DailyInfoScreen>()
                DailyInfoView().AboutGiftInformation(
                    navController = navController,
                    args = DailyInfoScreen(forDay = forDay.forDay, source =  forDay.source, forAnother = forDay.forAnother)
                )
            }
        }

    }



    @Serializable
    object MainScreen

    @Serializable
    data class UserInfoScreen(val forDay : String?, val source: Int?,val forWhat : String)


    @Serializable
    data class DailyInfoScreen(val forDay : String,val source : Int,val forAnother : Boolean)
}