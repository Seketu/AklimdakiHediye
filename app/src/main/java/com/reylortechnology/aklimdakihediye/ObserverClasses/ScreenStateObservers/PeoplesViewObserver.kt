package com.reylortechnology.aklimdakihediye.ObserverClasses.ScreenStateObservers

import androidx.compose.runtime.MutableState
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.Peoples

sealed class PeoplesViewObserver {
    object MainScreen : PeoplesViewObserver()
    data class PersonDetailScreen(val people : MutableState<Peoples>) : PeoplesViewObserver()
    object AddPersonScreen : PeoplesViewObserver()
}