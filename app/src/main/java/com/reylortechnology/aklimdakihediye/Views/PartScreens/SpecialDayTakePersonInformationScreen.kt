package com.reylortechnology.aklimdakihediye.Views.PartScreens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.reylortechnology.aklimdakihediye.Compose.SpecialDayTextField
import com.reylortechnology.aklimdakihediye.Compose.specialDayRelationshipCard
import com.reylortechnology.aklimdakihediye.ObserverClasses.ScreenStateObservers.SpecialDaysScreenObserver
import com.reylortechnology.aklimdakihediye.ObserverClasses.SpecialDayCheckModel
import com.reylortechnology.aklimdakihediye.R
import com.reylortechnology.aklimdakihediye.ViewModels.SpecialDaysViewModel
import com.reylortechnology.aklimdakihediye.models.ComposeModels.SnackBarEvent
import com.reylortechnology.aklimdakihediye.models.ComposeModels.SpecialReleationshipsCardItem

@SuppressLint("LocalContextGetResourceValueCall")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpecialDayTakePersonInformationScreen(
    screenObserver: MutableState<SpecialDaysScreenObserver>,
    viewModel: SpecialDaysViewModel = hiltViewModel()
    ) {


    val snackBar = remember {
        SnackbarHostState()
    }

    val context = LocalContext.current

    val localCurrent = LocalConfiguration.current
    val localHeight = localCurrent.screenHeightDp.dp

    val nameField = remember {
        mutableStateOf("")
    }

    val nameError = remember {
        mutableStateOf(false)
    }

    val checkBoxState  = remember {
        mutableStateOf<SpecialDayCheckModel>(SpecialDayCheckModel.None)
    }

    val releationshipCardList : MutableList<SpecialReleationshipsCardItem> = mutableListOf(
        SpecialReleationshipsCardItem(
            title = stringResource(R.string.mother),
            description = stringResource(R.string.mother_short_desc),
            videoSource = R.raw.mother,
            type = SpecialDayCheckModel.Mother
        ),
        SpecialReleationshipsCardItem(
            title = stringResource(R.string.father),
            description = stringResource(R.string.father_desc_short),
            videoSource = R.raw.fathers_anim,
            type = SpecialDayCheckModel.Father
        ),
        SpecialReleationshipsCardItem(
            title = stringResource(R.string.sibling),
            description = stringResource(R.string.sibling_desc_short),
            videoSource = R.raw.sibling,
            type = SpecialDayCheckModel.Sibling
        ),
        SpecialReleationshipsCardItem(
            title = stringResource(R.string.sweatheart),
            description = stringResource(R.string.sweatheart_desc_short),
            videoSource = R.raw.lover_anim,
            type = SpecialDayCheckModel.SweatHeart
        ),
        SpecialReleationshipsCardItem(
            title = stringResource(R.string.teacher),
            description = stringResource(R.string.teacher_desc_short),
            videoSource = R.raw.teacher_row,
            type = SpecialDayCheckModel.Teacher
        ),
        SpecialReleationshipsCardItem(
            title = stringResource(R.string.friend),
            description = stringResource(R.string.friend_desc_short),
            videoSource = R.raw.friend_row,
            type = SpecialDayCheckModel.Friend
        )
    )
    LaunchedEffect(
        Unit
    ) {
        viewModel.snackBarEvent.collect { event->
            when(event){
                SnackBarEvent.None -> {}
                is SnackBarEvent.ShowSnackBar -> {
                    snackBar.showSnackbar(message = event.message)
                    Log.d("Snackbar", "Snackbar gösterildi: ${event.message}")
                }
            }
        }
    }


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.ime),
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
                            "Kişisel Bilgiler",
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                },
                navigationIcon = {
                    Icon(
                        Icons.AutoMirrored.Default.ArrowBack,
                        "back button",
                        Modifier
                            .clickable {
                                screenObserver.value = SpecialDaysScreenObserver.MainScreen
                            }
                            .size(25.dp)
                    )
                },
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(localHeight * 0.05f))
            SpecialDayTextField(
                value = nameField,
                modifier = Modifier
                    .fillMaxWidth(0.85f),
                isError = nameError,
                label = stringResource(R.string.name)
            )
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Yakınlık Dereceniz",
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(25.dp))
                Box{
                    LazyRow{
                        items(releationshipCardList) {item->
                            specialDayRelationshipCard(
                                videoSource = item.videoSource,
                                title = item.title,
                                description = item.description,
                                checked = rememberUpdatedState(checkBoxState.value == item.type),
                                modifier = Modifier
                                    .size(175.dp),
                                onCheckedChange = {
                                    checkBoxState.value = item.type
                                }
                            )
                            Spacer(Modifier.width(20.dp))
                        }
                    }
                    Icon(
                        Icons.AutoMirrored.Default.KeyboardArrowLeft,
                        "",
                        Modifier
                            .align(Alignment.CenterStart)
                            .width(25.dp)
                            .height(50.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        Icons.AutoMirrored.Default.KeyboardArrowRight,
                        "",
                        Modifier
                            .align(Alignment.CenterEnd),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Row(
                modifier = Modifier
                    .height(localHeight * 0.1f)
                    .background(MaterialTheme.colorScheme.primary)
                    .fillMaxWidth()
                    .clickable {
                        when (checkBoxState.value) {
                            SpecialDayCheckModel.None -> {
                                viewModel.setSnackBarEvent(event = SnackBarEvent.ShowSnackBar("Lütfen yakınlık derecenizi seçin"))
                            }
                            SpecialDayCheckModel.Father -> {
                                if (nameField.value.isEmpty()) {
                                    nameError.value = true
                                    viewModel.setSnackBarEvent(event = SnackBarEvent.ShowSnackBar("İsim eksik olamaz"))
                                } else {
                                    viewModel.setPersonInfo(
                                        name = nameField.value,
                                        nearby = context.getString(R.string.father)
                                    )
                                    screenObserver.value =
                                        SpecialDaysScreenObserver.TakeDayInformation
                                }
                            }

                            SpecialDayCheckModel.Mother -> {
                                if (nameField.value.isEmpty()) {
                                    nameError.value = true
                                    viewModel.setSnackBarEvent(event = SnackBarEvent.ShowSnackBar("İsim eksik olamaz"))
                                } else {
                                    viewModel.setPersonInfo(
                                        name = nameField.value,
                                        nearby = context.getString(R.string.mother)
                                    )
                                    screenObserver.value =
                                        SpecialDaysScreenObserver.TakeDayInformation
                                }
                            }

                            SpecialDayCheckModel.SweatHeart -> {
                                if (nameField.value.isEmpty()) {
                                    nameError.value = true
                                    viewModel.setSnackBarEvent(event = SnackBarEvent.ShowSnackBar("İsim eksik olamaz"))

                                } else {
                                    viewModel.setPersonInfo(
                                        name = nameField.value,
                                        nearby = context.getString(R.string.sweatheart)
                                    )
                                    screenObserver.value =
                                        SpecialDaysScreenObserver.TakeDayInformation
                                }
                            }

                            SpecialDayCheckModel.Teacher -> {
                                if(nameField.value.isEmpty()){
                                    nameError.value = true
                                    viewModel.setSnackBarEvent(event = SnackBarEvent.ShowSnackBar("İsim eksik olamaz"))

                                }else{
                                    viewModel.setPersonInfo(
                                        name = nameField.value,
                                        nearby = context.getString(R.string.teacher)
                                        )
                                    screenObserver.value =
                                        SpecialDaysScreenObserver.TakeDayInformation
                                }
                            }

                            SpecialDayCheckModel.Friend -> {
                                if(nameField.value.isEmpty()){
                                    nameError.value = true
                                    viewModel.setSnackBarEvent(event = SnackBarEvent.ShowSnackBar("İsim eksik olamaz"))

                                }else{
                                    viewModel.setPersonInfo(
                                        name = nameField.value,
                                        nearby = context.getString(R.string.friend)
                                    )
                                    screenObserver.value =
                                        SpecialDaysScreenObserver.TakeDayInformation
                                }
                            }
                            SpecialDayCheckModel.Sibling -> {
                                if(nameField.value.isEmpty()){
                                    nameError.value = true
                                    viewModel.setSnackBarEvent(event = SnackBarEvent.ShowSnackBar("İsim eksik olamaz"))
                                }else{
                                    viewModel.setPersonInfo(
                                        name = nameField.value,
                                        nearby = context.getString(R.string.sibling))
                                    screenObserver.value =
                                        SpecialDaysScreenObserver.TakeDayInformation
                                }
                            }
                        }

                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    stringResource(R.string.next_text),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 24.sp
                    )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    val screenState = remember{
        mutableStateOf(SpecialDaysScreenObserver.TakeDayInformation)
    }
    SpecialDayTakePersonInformationScreen(screenObserver = screenState)
}