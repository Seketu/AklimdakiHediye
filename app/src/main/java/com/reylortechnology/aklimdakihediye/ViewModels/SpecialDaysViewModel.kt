package com.reylortechnology.aklimdakihediye.ViewModels

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.Notifications
import com.reylortechnology.aklimdakihediye.ObserverClasses.AlertDialogObserver
import com.reylortechnology.aklimdakihediye.ObserverClasses.ScreenStateObservers.SpecialDaysScreenObserver
import com.reylortechnology.aklimdakihediye.R
import com.reylortechnology.aklimdakihediye.Repo.NotificationsRepo
import com.reylortechnology.aklimdakihediye.models.ComposeModels.SnackBarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import javax.inject.Inject

@HiltViewModel
class SpecialDaysViewModel
@Inject constructor(
    val repo: NotificationsRepo
) : ViewModel() {

    private val _snackBarEvent = MutableSharedFlow<SnackBarEvent>(extraBufferCapacity = 1)
    val snackBarEvent = _snackBarEvent.asSharedFlow()

    private val _alertDialogEvent = MutableStateFlow<AlertDialogObserver>(AlertDialogObserver.none)
    val alertDialogEvent = _alertDialogEvent.asStateFlow()

    val reminderList = repo.notifications

    val personName = MutableStateFlow("")
    val personRelationship = MutableStateFlow("")

    fun setAlertDialogEvent(event: AlertDialogObserver) {
        _alertDialogEvent.value = event
    }

    fun setSnackBarEvent(event: SnackBarEvent) {
        _snackBarEvent.tryEmit(event)
    }

    fun setPersonInfo(
        name: String,
        nearby: String
    ){
        personName.value = name
        personRelationship.value = nearby
    }

    fun deleteReminder(reminder: List<Notifications>){
        viewModelScope.launch {
            repo.deleteReminder(reminder)
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    fun createReminder(
        context: Context,
        selectedDateMillis: Long,
        notifyBeforeDays: Int,
        event: String,
        screenState: MutableState<SpecialDaysScreenObserver>
    ) {
        Log.d("TarihMillis", selectedDateMillis.toString())

        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale("tr"))
        sdf.timeZone = TimeZone.getTimeZone("Europe/Istanbul")
        Log.d("TarihString", sdf.format(Date(selectedDateMillis)))

        val message = buildString {
            append(
                personRelationship.value
            )
            append(" ")
            append(
                personName.value
            )
            append(" ")
            append(context.getString(R.string.reminder_message_text))
        }

        viewModelScope.launch {
            try {
                val result = repo.scheduleAndSaveNotification(
                    context = context,
                    selectedDateMillis = selectedDateMillis,
                    notifyBeforeDays = notifyBeforeDays,
                    event = event,
                    message = message,
                    releationship = personRelationship.value,
                    for_who = personName.value,
                )
                if (result) {
                    _snackBarEvent.tryEmit(
                        SnackBarEvent.ShowSnackBar("Hatırlatıcı Oluşturuldu")
                    )
                    delay(5000)
                    screenState.value = SpecialDaysScreenObserver.MainScreen
                }else{
                    _snackBarEvent.tryEmit(
                        SnackBarEvent.ShowSnackBar("Bir Sorun oluştu")
                    )
                }
            } catch (e: Exception) {
                Log.e("ReminderViewModel", "Hata: ${e.message}", e)
                // istersen bir UI event akışı (Snackbar, Toast vs.) ile de kullanıcıya bildirebilirsin
            }
        }
    }

}