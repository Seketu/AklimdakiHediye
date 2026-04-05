package com.reylortechnology.aklimdakihediye.models.ComposeModels

import androidx.annotation.StringRes
import com.reylortechnology.aklimdakihediye.R

enum class PeopleDetailsDividerState(
    @param:StringRes  val stringRes: Int) {
    PersonalAndRelationInformation(
        R.string.people_details_divider_personal_and_relation_information
    ),
    Characteristics(
        R.string.people_details_divider_characteristics
    ),
    Hobbies(
            R.string.people_details_divider_hobbies
    )
}