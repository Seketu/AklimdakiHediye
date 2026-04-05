package com.reylortechnology.aklimdakihediye.ObserverClasses

import androidx.compose.ui.graphics.Color

sealed class DialogStateObserver{
        object Open : DialogStateObserver()
        object None: DialogStateObserver()
        data class imageSelector(val imageId : Int, val color : Color) : DialogStateObserver()
        object OpenAddPeopleDialog : DialogStateObserver()
}