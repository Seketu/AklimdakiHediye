package com.example.aklimdakihediye.models.GiftInformationModels

import javax.inject.Inject

data class ForPersonInformation
    @Inject constructor(
    val forWho : String,
    val name: String,
    val old : String,
    val zodiac : String,
    val job : String,
    val bestSide : String,
    val hobbies : String,
    val lastGifts : String,
    val relationship : String?
)
