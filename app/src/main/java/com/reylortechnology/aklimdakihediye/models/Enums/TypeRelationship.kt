package com.reylortechnology.aklimdakihediye.models.Enums

import androidx.annotation.StringRes
import com.reylortechnology.aklimdakihediye.R

enum class TypeRelationship(
    val id : Int,
    @param:StringRes val stringRes : Int
) {
    Parents(1, stringRes = R.string.type_relationship_parents ),
    Friends(2, stringRes = R.string.friend),
    Partners(3, stringRes = R.string.sweatheart),
    Siblings(4, stringRes = R.string.sibling),
    Coworker(5, stringRes = R.string.main_row_job)
}