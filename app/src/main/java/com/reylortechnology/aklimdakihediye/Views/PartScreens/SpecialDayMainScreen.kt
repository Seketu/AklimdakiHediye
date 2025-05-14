package com.reylortechnology.aklimdakihediye.Views.PartScreens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.reylortechnology.aklimdakihediye.Compose.ReminderCard
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.Notifications
import com.reylortechnology.aklimdakihediye.ObserverClasses.ScreenStateObservers.SpecialDaysScreenObserver
import com.reylortechnology.aklimdakihediye.R
import com.reylortechnology.aklimdakihediye.ViewModels.SpecialDaysViewModel
import com.reylortechnology.aklimdakihediye.models.ComposeModels.SnackBarEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpecialDayMainScreen(
    screenObserver: MutableState<SpecialDaysScreenObserver>,
    navController: NavController,
    viewModel : SpecialDaysViewModel = hiltViewModel()
) {

    val selectedForDelete = remember {
        mutableListOf<Notifications>()
    }

    LaunchedEffect(Unit) {
        viewModel.reminderList.collect { reminderList ->
            val now = System.currentTimeMillis()

            val expiredReminders = reminderList.filter {
                now > it.date + 86_400_000L
            }

            if (expiredReminders.isNotEmpty()) {
                viewModel.deleteReminder(expiredReminders)
            }
        }
    }

    val deleteState = remember {
        mutableStateOf(false)
    }

    val listOfDays = viewModel.reminderList.collectAsState(emptyList())
    val context = LocalContext.current
    val localConfiguration = LocalConfiguration.current
    val localHeight = localConfiguration.screenHeightDp.dp

    val snackbar = remember {
        SnackbarHostState()
    }
    val notificationPermission = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            )
        } else {
            mutableStateOf(
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_NOTIFICATION_POLICY
                )== PackageManager.PERMISSION_GRANTED
            )
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if(isGranted){
                notificationPermission.value = isGranted
            }
        }
    )

    LaunchedEffect(
        key1 = Unit
    ) {
        if (!notificationPermission.value){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }else{
                launcher.launch(Manifest.permission.ACCESS_NOTIFICATION_POLICY)
            }
        }
    }

    LaunchedEffect(
        Unit
    ) {
        viewModel.snackBarEvent.collect { event ->
            when (event) {
                SnackBarEvent.None -> {}
                is SnackBarEvent.ShowSnackBar -> {
                    snackbar.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbar,
                modifier = Modifier
                    .padding(vertical = 32.dp, horizontal = 16.dp)
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.special_day_main_title),
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                },
                modifier = Modifier,
                navigationIcon = {
                    Icon(
                        Icons.AutoMirrored.Default.ArrowBack,
                        "back button",
                        Modifier
                            .clickable {
                                navController.popBackStack()
                            }
                            .size(25.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                },
                actions =  {
                    if (deleteState.value){
                        Icon(
                            Icons.Default.Clear,
                            "back button",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .clickable{
                                    deleteState.value = false
                                }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (notificationPermission.value){
                        screenObserver.value = SpecialDaysScreenObserver.TakePersonInformation
                    }else{
                        viewModel.setSnackBarEvent(event = SnackBarEvent.ShowSnackBar("Bildirim izni vermeniz gerekiyor"))
                    }
                },
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = MaterialTheme.shapes.small
            ) {
                Icon(
                    Icons.Default.Add,
                    "back button",
                    Modifier
                        .size(50.dp),
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(
                    listOfDays.value
                ) {
                    val checked = remember {
                        mutableStateOf(false)
                    }
                    if (checked.value){
                        selectedForDelete.add(it)
                    }else{
                        selectedForDelete.remove(it)
                    }
                    if (!deleteState.value){
                        selectedForDelete.clear()
                    }
                    ReminderCard(
                        title = it.for_who,
                        time = it.date.toLong(),
                        event = it.event,
                        releationship = it.releationship,
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .defaultMinSize(minHeight = localHeight * 0.125f)
                            .background(MaterialTheme.colorScheme.secondary, shape = MaterialTheme.shapes.small)
                            .border(
                                width = if (deleteState.value) 5.dp else 0.dp,
                                color = if (checked.value) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
                                shape = MaterialTheme.shapes.small
                            )
                            .padding(10.dp)
                            .combinedClickable(
                                onLongClick = {
                                    deleteState.value = true
                                    checked.value = !checked.value
                                },
                                onClick = {
                                    if (deleteState.value){
                                        checked.value = !checked.value
                                    }
                                }
                            )
                    )
                    Spacer(Modifier.height(15.dp))
                }
            }
            AnimatedVisibility(
                modifier = Modifier.fillMaxSize(),
                visible = deleteState.value,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Red)
                        .clickable {
                            Log.d("Tag delete button", selectedForDelete.toString())
                            viewModel.deleteReminder(selectedForDelete)
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(stringResource(R.string.delete), fontSize = 26.sp, color = Color.White)
                }
            }
        }
    }
}


@Preview
@Composable
private fun preview() {
    val screenObserver = remember {
        mutableStateOf(SpecialDaysScreenObserver.MainScreen)
    }
    SpecialDayMainScreen(screenObserver,rememberNavController())
}