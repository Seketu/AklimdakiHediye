package com.reylortechnology.aklimdakihediye.models.Enums

import androidx.annotation.StringRes
import com.reylortechnology.aklimdakihediye.R

enum class Genders (
    var id : Int,
    @param:StringRes val titleId : Int
){
    Male(1, R.string.gender_man),
    Female(2, R.string.gender_female),
    NonComment(3,R.string.gender_nc);

    companion object{
        fun getById(id : Int) = entries.find { it.id == id }
    }
}