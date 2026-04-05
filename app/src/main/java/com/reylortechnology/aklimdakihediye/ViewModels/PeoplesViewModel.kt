package com.reylortechnology.aklimdakihediye.ViewModels

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reylortechnology.aklimdakihediye.LocalDatabase.Dao.PeoplesDao
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.Peoples
import com.reylortechnology.aklimdakihediye.ObserverClasses.DialogStateObserver
import com.reylortechnology.aklimdakihediye.ObserverClasses.ScreenStateObservers.PeoplesViewObserver
import com.reylortechnology.aklimdakihediye.R
import com.reylortechnology.aklimdakihediye.Repo.PeopleRepo
import com.reylortechnology.aklimdakihediye.Services.CalculatorSpecialDays
import com.reylortechnology.aklimdakihediye.models.Enums.PeopleUiModel
import com.reylortechnology.aklimdakihediye.models.Enums.TypeRelationship
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class PeoplesViewModel
@Inject
constructor(
   val peoplesDao: PeoplesDao,
    val calculatorSpecialDays: CalculatorSpecialDays,
    val peopleRepository : PeopleRepo
) : ViewModel() {

    private val _toastEvent = Channel<String>()
    val toastEvent = _toastEvent.receiveAsFlow()
    private val _dialogStateObserver = MutableStateFlow<DialogStateObserver>(DialogStateObserver.None)
    val dialogStateObserver = _dialogStateObserver.asStateFlow()
    
    private val _selectedPeople = MutableStateFlow<Peoples?>(null)
    val selectedPeople = _selectedPeople.asStateFlow()
    
    fun setSelectedPeople(people: Peoples) {
        _selectedPeople.value = people
    }
    
    fun openDialog(
        newValue : DialogStateObserver
    ) {
        _dialogStateObserver.value = newValue
    }
    
    fun setNewColorAndImage(imageId: Int, color: Color) {
        _selectedPeople.value?.let { currentPeople ->
            val updatedPeople = currentPeople.copy(
                image = imageId,
                color = color
            )
            _selectedPeople.value = updatedPeople

            viewModelScope.launch {
                try {
                    val result = peopleRepository.updatePeople(updatedPeople)
                    if (result.isSuccess) {
                        _dialogStateObserver.value = DialogStateObserver.None
                        _toastEvent.send("Profile image updated successfully")
                    } else {
                        _toastEvent.send("Failed to update image: ${result.exceptionOrNull()?.message}")
                    }
                } catch (e: Exception) {
                    _toastEvent.send("Error: ${e.message}")
                }
            }
        }
    }

    fun setNewPeopleInformation(
        people: Peoples,
        screenState : MutableState<PeoplesViewObserver>
    ){
        _selectedPeople.value?.let {
            viewModelScope.launch {
                try {
                    _selectedPeople.value = people
                    val result = peopleRepository.updatePeople(people)
                    if (result.isSuccess) {
                        _toastEvent.send("People information updated successfully")
                        screenState.value = PeoplesViewObserver.MainScreen
                    } else {
                        _toastEvent.send("Failed to update people information: ${result.exceptionOrNull()?.message}")
                    }
                }
                catch (e : CancellationException){
                    Log.d(
                        "PeoplesViewModel",
                        "Coroutine cancelled: ${e.message}"
                    )
                    throw e
                }
                catch (
                    e: Exception
                ){
                    _toastEvent.send(e.message.toString())
                    Log.e(
                        "PeoplesViewModel",
                        "Error updating people information: ${e.message}"
                    )
                }
            }
        }
    }

    fun addPeople(it: Peoples) {
            viewModelScope.launch {
                try {
                    val result = peopleRepository.addPeople(it)
                    if (result.isSuccess) {
                        _toastEvent.send("New person added successfully")
                    } else {
                        _toastEvent.send("Failed to add new person: ${result.exceptionOrNull()?.message}")
                    }
                }
                catch (e: CancellationException){
                    Log.d(
                        "PeoplesViewModel",
                        "Coroutine cancelled: ${e.message}"
                    )
                    throw e
                }
                catch (e: Exception) {
                    _toastEvent.send("Error: ${e.message}")
                }
            }
    }

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