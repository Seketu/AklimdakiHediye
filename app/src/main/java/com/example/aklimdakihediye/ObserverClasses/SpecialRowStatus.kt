package com.example.aklimdakihediye.ObserverClasses

sealed class SpecialRowStatus {
    object None : SpecialRowStatus()
    data class ToScreen (val forWho : String, val source : Int) : SpecialRowStatus()
    data class ToTakeInformation(val forWho : String,val source: Int, val forAnother : Boolean) : SpecialRowStatus()
}