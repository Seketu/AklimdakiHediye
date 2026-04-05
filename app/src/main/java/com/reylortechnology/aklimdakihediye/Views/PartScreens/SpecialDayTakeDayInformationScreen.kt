package com.reylortechnology.aklimdakihediye.Views.PartScreens

import android.app.AlarmManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.reylortechnology.aklimdakihediye.Compose.AlertDialog
import com.reylortechnology.aklimdakihediye.Compose.DatePickerView
import com.reylortechnology.aklimdakihediye.Compose.SpecialDayTextField
import com.reylortechnology.aklimdakihediye.ObserverClasses.AlertDialogObserver
import com.reylortechnology.aklimdakihediye.ObserverClasses.NotificationTimer
import com.reylortechnology.aklimdakihediye.ObserverClasses.ScreenStateObservers.SpecialDaysScreenObserver
import com.reylortechnology.aklimdakihediye.R
import com.reylortechnology.aklimdakihediye.ViewModels.SpecialDaysViewModel
import com.reylortechnology.aklimdakihediye.models.ComposeModels.SnackBarEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpecialDayTakeDayInformationScreen(
    screenObserver: MutableState<SpecialDaysScreenObserver>,
    viewModel: SpecialDaysViewModel = hiltViewModel()
) {


    val localCurrent = LocalConfiguration.current
    val localHeight = localCurrent.screenHeightDp.dp


    val snackBar = remember {
        SnackbarHostState()
    }

    val context = LocalContext.current

    val exactAlarmPermission = remember {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                alarmManager.canScheduleExactAlarms()
            } else true
        )
    }


    val eventName = remember {
        mutableStateOf("")
    }

    val eventError = remember {
        mutableStateOf(
            false
        )
    }

    val dropDownState = remember {
        mutableStateOf(false)
    }

    val dropDownList = remember {
        mutableStateOf(NotificationTimer.ONE_DAY_BEFORE)
    }

    val alertDialogObserver = viewModel.alertDialogEvent.collectAsState()

    alertDialogObserver.value.let {
        when (it) {
            is AlertDialogObserver.NewAlertDialog -> {
                AlertDialog(
                    confirmText = it.confirmText,
                    dismissText = it.dismissText,
                    title = it.title,
                    confirmButton = it.onConfirm,
                    dismissButton = it.dismissButton,
                    onDismissRequest = it.onDismiss
                )
            }
            AlertDialogObserver.none -> {}
        }
    }


    LaunchedEffect(
        Unit
    ) {
        viewModel.snackBarEvent.collect { event ->
            when (event) {
                SnackBarEvent.None -> {}
                is SnackBarEvent.ShowSnackBar -> {
                    snackBar.showSnackbar(event.message)
                }
            }
        }
    }
    val datePickerState = remember { mutableStateOf<Long?>(null) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        snackbarHost = {
            SnackbarHost(
                hostState = snackBar,
                modifier = Modifier
                    .padding(vertical = 32.dp, horizontal = 16.dp)
            )
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.label_event_information),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
                navigationIcon = {
                    Icon(
                        Icons.AutoMirrored.Default.ArrowBack,
                        stringResource(R.string.content_description_back_button),
                        Modifier
                            .clickable {
                                screenObserver.value =
                                    SpecialDaysScreenObserver.TakePersonInformation
                            }
                            .size(25.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Spacer(Modifier.height(localHeight * 0.05f))

            SpecialDayTextField(
                value = eventName,
                modifier = Modifier
                    .fillMaxWidth(0.9f),
                label = stringResource(R.string.label_event_name),
                isError = eventError
            )

            DatePickerView(
                modifier = Modifier
                    .fillMaxWidth(0.7f),
                selectedDateMillis1 = datePickerState
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(localHeight * 0.06f)
                ) {
                    Button(
                        onClick = {
                            dropDownState.value = !dropDownState.value
                        },
                        modifier = Modifier.fillMaxSize(),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Icon(
                            Icons.Default.Notifications,
                            ""
                        )
                        Text(
                            dropDownList.value.label,
                            fontSize = 24.sp,
                        )
                        Spacer(Modifier.weight(1f))
                        Icon(
                            Icons.AutoMirrored.Default.KeyboardArrowRight,
                            ""
                        )
                    }
                    DropdownMenu(
                        expanded = dropDownState.value,
                        onDismissRequest = { dropDownState.value = !dropDownState.value },
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.secondary)
                            .fillMaxWidth()
                            .height(localHeight * 0.2f)
                            .clip(MaterialTheme.shapes.medium)
                    ) {
                        NotificationTimer.values().forEach { option ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        option.label,
                                        color = MaterialTheme.colorScheme.onSecondary
                                    )
                                },
                                onClick = {
                                    dropDownState.value = !dropDownState.value
                                    dropDownList.value = option
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Notifications,
                                        "",
                                        tint = MaterialTheme.colorScheme.onSecondary
                                    )
                                },
                            )
                        }
                    }
                }
                Text(
                    stringResource(R.string.label_notification_timing),
                    fontSize = 9.sp,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(localHeight * 0.1f)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable {
                        if (eventName.value.isEmpty()) {
                            viewModel.setSnackBarEvent(event = SnackBarEvent.ShowSnackBar(context.getString(R.string.error_enter_event_name)))
                            eventError.value = true
                        } else if (datePickerState.value == null || datePickerState.value == 0L) {
                            viewModel.setSnackBarEvent(event = SnackBarEvent.ShowSnackBar(context.getString(R.string.error_select_date)))
                        } else {
                            Log.e(context.getString(R.string.debug_log_state),exactAlarmPermission.value.toString())
                            if (!exactAlarmPermission.value){
                                viewModel.setAlertDialogEvent(event = AlertDialogObserver.NewAlertDialog(
                                    title = context.getString(R.string.notification_permission_dialog_title),
                                    confirmText = context.getString(R.string.notification_permission_confirm),
                                    dismissText = context.getString(R.string.notification_permission_cancel),
                                    dismissButton = {
                                        viewModel.setAlertDialogEvent(AlertDialogObserver.none)
                                    },
                                    onConfirm = {
                                        viewModel.createReminder(
                                            context = context,
                                            selectedDateMillis = datePickerState.value!!,
                                            notifyBeforeDays = dropDownList.value.daysBefore,
                                            event = eventName.value,
                                            screenState = screenObserver,
                                        )
                                        viewModel.setAlertDialogEvent(AlertDialogObserver.none)
                                    },
                                    onDismiss = {
                                        viewModel.setAlertDialogEvent(AlertDialogObserver.none)
                                    }
                                ))
                            }else{
                                viewModel.createReminder(
                                    context = context,
                                    selectedDateMillis = datePickerState.value!!,
                                    event = eventName.value,
                                    notifyBeforeDays = dropDownList.value.daysBefore,
                                    screenState = screenObserver
                                )
                            }
                        }
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    stringResource(R.string.button_create_reminder),
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    val screenObserver = remember {
        mutableStateOf(SpecialDaysScreenObserver.TakeDayInformation)
    }
    SpecialDayTakeDayInformationScreen(screenObserver)
}
