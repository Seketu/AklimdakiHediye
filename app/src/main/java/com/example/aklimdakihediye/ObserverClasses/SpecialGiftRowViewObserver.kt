package com.example.aklimdakihediye.ObserverClasses

sealed class SpecialGiftRowViewObserver {
    object None : SpecialGiftRowViewObserver()
    object TakeInformation : SpecialGiftRowViewObserver()
    object TakeGiftInformation : SpecialGiftRowViewObserver()
    data class TakePersonInformation(val forWho : String, val source : Int) : SpecialGiftRowViewObserver()
}