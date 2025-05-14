package com.reylortechnology.aklimdakihediye.ObserverClasses.ScreenStateObservers

sealed class SavedVariablesViewObserver {
    object SavedVariablesView : SavedVariablesViewObserver()
    object None : SavedVariablesViewObserver()

}