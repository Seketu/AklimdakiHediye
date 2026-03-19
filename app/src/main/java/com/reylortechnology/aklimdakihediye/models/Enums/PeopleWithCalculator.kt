package com.reylortechnology.aklimdakihediye.models.Enums

import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.Peoples

data class PeopleUiModel(
    val people: Peoples,
    val isBirthdayNote: String? = null,
    val isSpecialNote : String? = null
)