package com.reylortechnology.aklimdakihediye.ObserverClasses.ScreenStateObservers

import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.Peoples

sealed class PeoplesViewObserver {
    object MainScreen : PeoplesViewObserver()
    data class PersonDetailScreen(val people : Peoples) : PeoplesViewObserver()
    object AddPersonScreen : PeoplesViewObserver()
}