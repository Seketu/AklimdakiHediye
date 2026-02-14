package com.reylortechnology.aklimdakihediye.Views.MainScreens

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.LocalUserInformation
import com.reylortechnology.aklimdakihediye.NavController.LocalNavController
import com.reylortechnology.aklimdakihediye.ObserverClasses.InfoStepsStatus
import com.reylortechnology.aklimdakihediye.R
import com.reylortechnology.aklimdakihediye.Compose.InfoScreen
import com.reylortechnology.aklimdakihediye.Compose.LottieAnim
import com.reylortechnology.aklimdakihediye.Compose.StepperIndicator
import com.reylortechnology.aklimdakihediye.ViewModels.CelebretadDayViewModel
import com.reylortechnology.aklimdakihediye.ui.theme.secondaryLight
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel


class UserInfoView {
    @SuppressLint("LocalContextGetResourceValueCall")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DailyAskUserInfoScreen(
        modifier: Modifier = Modifier,
        navController: NavController,
        args : LocalNavController.UserInfoScreen,
        viewModel: CelebretadDayViewModel = hiltViewModel()
        ) {
        val context = LocalContext.current
        val nameFocusRequester = remember { FocusRequester() }
        val oldFocusRequester = remember { FocusRequester() }
        val bestSideFocusRequester = remember { FocusRequester() }
        val hobbiesFocusRequester = remember { FocusRequester() }
        val zodiacFocusRequester = remember { FocusRequester() }
        val caracterFocusRequester = remember { FocusRequester() }
        var labelColor by remember { mutableStateOf(Color.Black) }

        val buttonText = remember {
            mutableStateOf(context.getString(R.string.next_text))
        }
        val focusManager = LocalFocusManager.current
        var screenStepState : MutableState<InfoStepsStatus> = remember {
            mutableStateOf(InfoStepsStatus.Name)
        }
        BackHandler {
            when(screenStepState.value){
                InfoStepsStatus.Name -> {
                    viewModel.backMainMenu(navController)
                }
                InfoStepsStatus.Old -> screenStepState.value = InfoStepsStatus.Name

                InfoStepsStatus.BestSide -> screenStepState.value = InfoStepsStatus.Old
                InfoStepsStatus.Hobbies -> screenStepState.value = InfoStepsStatus.BestSide
                InfoStepsStatus.Zodiac -> screenStepState.value = InfoStepsStatus.Hobbies
                InfoStepsStatus.Caracter -> screenStepState.value = InfoStepsStatus.Zodiac
            }
        }
        Log.e("Args" , args.forWhat.toString())

        val nameState = remember {
            mutableStateOf("")
        }

        val oldState = remember {
            mutableStateOf("")
        }

        val bestSideState = remember {
            mutableStateOf("")
        }

        val hobbiesState = remember {
            mutableStateOf("")
        }

        val zodiacState = remember {
            mutableStateOf("")
        }

        val caracterState = remember {
            mutableStateOf("")
        }

        val configuration = LocalConfiguration
        val screenHeight = configuration.current.screenHeightDp.dp


        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
            ,
            topBar = {
                Row {
                    TopAppBar(
                        title = {},
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    viewModel.backMainMenu(navController)
                                }
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.back_icon),
                                    "back button",
                                    modifier = Modifier
                                )
                            }

                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = secondaryLight
                        )
                    )
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .imePadding()
                    .background(Color.LightGray)
                ) {
                Box(
                    Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    LottieAnim(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .height(55.dp),
                        source = R.raw.main_anim,
                        contentScale = ContentScale.FillWidth,
                        secondModifier = Modifier
                            .graphicsLayer(scaleY = 0.6f)
                    )
                    LazyColumn(
                        Modifier
                            .fillMaxSize()
                            .padding(top = 60.dp),
                    ) {
                        item {
                            when(screenStepState.value){
                                InfoStepsStatus.Name -> {
                                    InfoScreen(
                                        modifier = Modifier
                                            .height(screenHeight * 1f),
                                        label = stringResource(R.string.name_label),
                                        state = nameState,
                                        color = labelColor,
                                        focusRequester =  nameFocusRequester,
                                        keyboard = KeyboardOptions(
                                            keyboardType = KeyboardType.Unspecified
                                        )
                                    )
                                    LaunchedEffect(screenStepState.value) {
                                        nameFocusRequester.requestFocus()
                                    }
                                }
                                InfoStepsStatus.Old -> {
                                    InfoScreen(
                                        modifier = Modifier
                                            .height(screenHeight * 1f),
                                        label = stringResource(R.string.old_label),
                                        state = oldState,
                                        color = labelColor,
                                        focusRequester = oldFocusRequester,
                                        keyboard = KeyboardOptions(
                                            keyboardType = KeyboardType.Number
                                        )
                                    )
                                    LaunchedEffect(screenStepState.value) {
                                        oldFocusRequester.requestFocus()
                                    }
                                }
                                InfoStepsStatus.BestSide -> {
                                    InfoScreen(
                                        modifier = Modifier
                                            .height(screenHeight * 1f),
                                        label = stringResource(R.string.best_side_label),
                                        state = bestSideState,
                                        color = labelColor,
                                        focusRequester = bestSideFocusRequester,
                                        keyboard = KeyboardOptions(
                                            keyboardType = KeyboardType.Unspecified
                                        )
                                    )
                                    LaunchedEffect(screenStepState.value) {
                                        bestSideFocusRequester.requestFocus()
                                    }
                                }
                                InfoStepsStatus.Hobbies -> {
                                    InfoScreen(
                                        modifier = Modifier
                                            .height(screenHeight * 1f),
                                        label = stringResource(R.string.hobbies_label),
                                        state = hobbiesState,
                                        color = labelColor,
                                        focusRequester = hobbiesFocusRequester,
                                        keyboard = KeyboardOptions(
                                            keyboardType = KeyboardType.Unspecified
                                        )
                                    )
                                    LaunchedEffect(screenStepState.value) {
                                        hobbiesFocusRequester.requestFocus()
                                    }
                                }
                                InfoStepsStatus.Zodiac -> {
                                    InfoScreen(
                                        modifier = Modifier
                                            .height(screenHeight * 1f),
                                        label = stringResource(R.string.zodiac_label),
                                        state = zodiacState,
                                        color = labelColor,
                                        focusRequester = zodiacFocusRequester,
                                        keyboard = KeyboardOptions(
                                            keyboardType = KeyboardType.Unspecified
                                        )
                                    )
                                    LaunchedEffect(screenStepState.value) {
                                        zodiacFocusRequester.requestFocus()
                                    }
                                }
                                InfoStepsStatus.Caracter -> {
                                    InfoScreen(
                                        modifier = Modifier
                                            .height(screenHeight * 1f),
                                        label = stringResource(R.string.caracter_label),
                                        state = caracterState,
                                        color = labelColor,
                                        focusRequester = caracterFocusRequester,
                                        keyboard = KeyboardOptions(
                                            keyboardType = KeyboardType.Unspecified
                                        )
                                    )
                                    LaunchedEffect(screenStepState.value) {
                                        caracterFocusRequester.requestFocus()
                                    }
                                }
                            }
                        }
                    }
                    StepperIndicator( modifier = Modifier
                        .align(Alignment.TopCenter),
                        currentStep =  screenStepState.value.ordinal + 1 , totalSteps = InfoStepsStatus.entries.size
                    )
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .height(screenHeight * 0.08f)
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable {
                                when (screenStepState.value) {
                                    InfoStepsStatus.Name -> {
                                        if (nameState.value.isEmpty()) {
                                            labelColor = Color.Red
                                        } else {
                                            screenStepState.value = InfoStepsStatus.Old
                                            labelColor = Color.Black
                                        }
                                    }

                                    InfoStepsStatus.Old -> {
                                        if (oldState.value.isEmpty()) {
                                            labelColor = Color.Red
                                        } else {
                                            screenStepState.value = InfoStepsStatus.BestSide
                                            labelColor = Color.Black
                                        }
                                    }

                                    InfoStepsStatus.BestSide -> {
                                        if (bestSideState.value.isEmpty()) {
                                            labelColor = Color.Red
                                        } else {
                                            screenStepState.value = InfoStepsStatus.Hobbies
                                            labelColor = Color.Black
                                        }
                                    }

                                    InfoStepsStatus.Hobbies -> {
                                        if (hobbiesState.value.isEmpty()) {
                                            labelColor = Color.Red
                                        } else {
                                            labelColor = Color.Black
                                            screenStepState.value = InfoStepsStatus.Zodiac
                                        }

                                    }

                                    InfoStepsStatus.Zodiac -> {
                                        if (zodiacState.value.isEmpty()) {
                                            labelColor = Color.Red
                                        } else {
                                            screenStepState.value =
                                                InfoStepsStatus.Caracter
                                            buttonText.value = context.getString(R.string.save_text)
                                            labelColor = Color.Black
                                        }
                                    }

                                    InfoStepsStatus.Caracter -> {

                                        if (caracterState.value.isEmpty()) {
                                            labelColor = Color.Red
                                        } else {

                                            when(args.forWhat){
                                                "ForAnother" ->{
                                                    Log.d("Tag" , args.forWhat.toString())

                                                    viewModel.setForAnotherInformation(
                                                        LocalUserInformation(
                                                            name = nameState.value,
                                                            old = oldState.value.toInt(),
                                                            bestSide = bestSideState.value,
                                                            hobbies = hobbiesState.value,
                                                            zodiac = zodiacState.value,
                                                            character = caracterState.value
                                                        )
                                                    )

                                                    navController.navigate(LocalNavController.DailyInfoScreen(args.forDay!!, source = args.source!!,forAnother = true))
                                                }
                                                "SaveInf" -> {
                                                    viewModel.saveLocalInformation(
                                                        LocalUserInformation(
                                                            name = nameState.value,
                                                            old = oldState.value.toInt(),
                                                            bestSide = bestSideState.value,
                                                            hobbies = hobbiesState.value,
                                                            zodiac = zodiacState.value,
                                                            character = caracterState.value
                                                        )
                                                    )
                                                    navController.navigate(
                                                            LocalNavController.MainScreen
                                                    )
                                                }
                                                else ->{}
                                            }

                                            labelColor = Color.Black
                                        }
                                    }
                                }
                            },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            buttonText.value,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 24.sp,
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
