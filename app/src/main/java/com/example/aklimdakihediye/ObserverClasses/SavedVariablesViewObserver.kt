package com.example.aklimdakihediye.ObserverClasses

import androidx.lifecycle.ViewModel

sealed class SavedVariablesViewObserver {
    object SavedVariablesView : SavedVariablesViewObserver()
    object None : SavedVariablesViewObserver()

}