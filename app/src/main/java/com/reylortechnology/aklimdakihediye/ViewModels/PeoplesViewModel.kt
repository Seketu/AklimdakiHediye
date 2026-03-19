package com.reylortechnology.aklimdakihediye.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reylortechnology.aklimdakihediye.LocalDatabase.Dao.PeoplesDao
import com.reylortechnology.aklimdakihediye.Services.CalculatorSpecialDays
import com.reylortechnology.aklimdakihediye.models.Enums.PeopleUiModel
import com.reylortechnology.aklimdakihediye.models.Enums.TypeRelationship
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PeoplesViewModel
@Inject
constructor(
   val peoplesDao: PeoplesDao,
    val calculatorSpecialDays: CalculatorSpecialDays
) : ViewModel() {

    val peoples = peoplesDao.getAllPeoples().map {list ->
        list.map { people->
            PeopleUiModel(
                people = people,
                isBirthdayNote = calculatorSpecialDays.calculateBirthday(people.birthday),
                isSpecialNote = when(people.relationship){
                    TypeRelationship.Coworker -> null
                    TypeRelationship.Parents -> calculatorSpecialDays.calculateFathersDay() + calculatorSpecialDays.calculateMothersDay()
                    TypeRelationship.Friends -> null
                    TypeRelationship.Partners -> calculatorSpecialDays.calculateValentineDay()
                    TypeRelationship.Siblings -> null
                }
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
}