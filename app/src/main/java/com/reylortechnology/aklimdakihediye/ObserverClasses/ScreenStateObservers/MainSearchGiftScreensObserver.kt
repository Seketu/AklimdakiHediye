package com.reylortechnology.aklimdakihediye.ObserverClasses.ScreenStateObservers

sealed class MainSearchGiftScreensObserver {
    data class PeopleInformation(
        val screen: PeopleInformationScreen
    ) : MainSearchGiftScreensObserver()

    object GiftInformation : MainSearchGiftScreensObserver()
    object GiftSearchResult : MainSearchGiftScreensObserver()
}

enum class PeopleInformationScreen {
    TakePersonInformation,
    TakePersonHobbies,
    TakeCharacterTraits,
}