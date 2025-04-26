package com.example.aklimdakihediye.Views.MainScreens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.aklimdakihediye.ObserverClasses.SavedVariablesViewObserver
import com.example.aklimdakihediye.Views.PartScreens.SavedGiftScreen

@Composable
fun SavedVariablesView(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val screenStepsStatus = remember {
        mutableStateOf<SavedVariablesViewObserver>(SavedVariablesViewObserver.SavedVariablesView)
    }

    when (screenStepsStatus.value) {
        SavedVariablesViewObserver.None -> {

        }
        is SavedVariablesViewObserver.SavedVariablesView -> {
            SavedGiftScreen(navController = navController)
        }
    }

}