package com.example.aklimdakihediye.NavController

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.aklimdakihediye.Views.DailyAskViewUserInfo
import com.example.aklimdakihediye.Views.DailyInfoView
import com.example.aklimdakihediye.Views.InformationGiftView
import com.example.aklimdakihediye.Views.UserMainView
import kotlinx.serialization.Serializable

class LocalNavController {

    @Composable
    fun LocalNavHost(modifier: Modifier = Modifier) {
        val navController = rememberNavController()

        NavHost(
            startDestination = InformationGiftScreen,
            navController = navController
        ){
            composable<MainScreen> {
                UserMainView().UserMainScreen(navController = navController)
            }

            composable<DailyInfoUserScreen> {
                val forDay = it.toRoute<DailyInfoUserScreen>()
                DailyAskViewUserInfo().DailyAskUserInfoScreen(
                    navController = navController,
                    args = DailyInfoUserScreen(forDay = forDay.forDay,forDay.source)
                )
            }

            composable<DailyInfoScreen> {
                val forDay = it.toRoute<DailyInfoScreen>()
                DailyInfoView().DailyInfoScreen(
                    navController = navController,
                    args = DailyInfoScreen(forDay = forDay.forDay,forDay.source)
                )
            }

            composable<InformationGiftScreen> {
                InformationGiftView().InformationGiftScreen(navController = navController)
            }
        }

    }

    @Serializable
    object InformationGiftScreen


    @Serializable
    object MainScreen

    @Serializable
    data class  DailyInfoUserScreen(val forDay : String,val source: Int)


    @Serializable
    data class  DailyInfoScreen(val forDay : String,val source : Int)
}