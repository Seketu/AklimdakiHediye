package com.reylortechnology.aklimdakihediye.NavController

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.reylortechnology.aklimdakihediye.Views.MainScreens.AboutGiftInformation
import com.reylortechnology.aklimdakihediye.Views.MainScreens.SavedVariablesView
import com.reylortechnology.aklimdakihediye.Views.MainScreens.SpecialDaysScreen
import com.reylortechnology.aklimdakihediye.Views.MainScreens.UserInfoView
import com.reylortechnology.aklimdakihediye.Views.MainScreens.SpecialGiftRowView
import com.reylortechnology.aklimdakihediye.Views.MainScreens.UserMainView
import com.reylortechnology.aklimdakihediye.Views.MainScreens.UserSettingsView
import kotlinx.serialization.Serializable

class LocalNavController {

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun LocalNavHost(modifier: Modifier = Modifier) {
        val navController = rememberNavController()

        NavHost(
            startDestination = MainScreen,
            navController = navController
        ) {

            composable<SpecialDaysScreen> {
                SpecialDaysScreen(navController = navController)
            }

            composable<UserSettingsScreen> {
                UserSettingsView(navController = navController)
            }

            composable<LocalNavController.MainScreen> {
                UserMainView().UserMainScreen(navController = navController)
            }

            composable<SavedVariablesScreen> {
                SavedVariablesView(navController = navController)
            }

            composable<LocalNavController.UserInfoScreen> {
                val forDay = it.toRoute<LocalNavController.UserInfoScreen>()
                UserInfoView().DailyAskUserInfoScreen(
                    navController = navController,
                    args = UserInfoScreen(forDay = forDay.forDay, forDay.source, forDay.forWhat)
                )
            }

            composable<DailyInfoScreen> {
                val forDay = it.toRoute<DailyInfoScreen>()
               AboutGiftInformation(
                    navController = navController,
                    args = DailyInfoScreen(
                        forDay = forDay.forDay,
                        source = forDay.source,
                        forAnother = forDay.forAnother
                    )
                )
            }

            composable<SpecialGiftRowScreen> {
                val args = it.toRoute<SpecialGiftRowScreen>()
                SpecialGiftRowView(args, navController)
            }

            composable<ToFriendGiftView> {
                val args = it.toRoute<ToFriendGiftView>()
                com.reylortechnology.aklimdakihediye.Views.MainScreens.ToFriendGiftView(peopleId = args.peopleId , navController = navController)
            }
        }

    }

    @Serializable
    object SavedVariablesScreen

    @Serializable
    object UserSettingsScreen

    @Serializable
    object MainScreen

    @Serializable
    data class SpecialGiftRowScreen(val forWho: String, val source: Int, val forAnother: Boolean)

    @Serializable
    data class UserInfoScreen(val forDay: String?, val source: Int?, val forWhat: String)

    @Serializable
    object SpecialDaysScreen

    @Serializable
    data class DailyInfoScreen(val forDay: String, val source: Int, val forAnother: Boolean)

    @Serializable
    data class ToFriendGiftView(val peopleId : Int)
}