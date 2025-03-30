package com.example.aklimdakihediye.Views

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.aklimdakihediye.NavController.LocalNavController
import com.example.aklimdakihediye.ObserverClasses.InfoStepsStatus
import com.example.aklimdakihediye.R
import com.example.aklimdakihediye.compose.BorderButton
import com.example.aklimdakihediye.compose.DailyUserInfoFieldsHolder
import com.example.aklimdakihediye.compose.InfoScreen
import com.example.aklimdakihediye.compose.LottieAnim
import com.example.aklimdakihediye.compose.StepperIndicator
import com.example.aklimdakihediye.ui.theme.ColorDailyUserInfoBc
import com.example.aklimdakihediye.ui.theme.ColorDailyUserInfoButton
import com.example.aklimdakihediye.ui.theme.ColorDailyUserInfoFields0
import com.example.aklimdakihediye.ui.theme.ColorDailyUserInfoFields1
import com.example.aklimdakihediye.ui.theme.ColorDailyUserInfoTopBarr

class DailyAskViewUserInfo {


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DailyAskUserInfoScreen(
        modifier: Modifier = Modifier,
        navController: NavController,
        args : LocalNavController.DailyInfoUserScreen
        ) {

        var screenStepState : MutableState<InfoStepsStatus> = remember {
            mutableStateOf(InfoStepsStatus.Name)
        }

        Log.e("Args" , args.forDay)

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

        val context = LocalContext.current

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
                            Image(
                                painter = painterResource(R.drawable.back_icon),
                                "back button",
                                modifier = Modifier.padding(10.dp)
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = ColorDailyUserInfoTopBarr
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
                                        label = context.getString(R.string.name_label),
                                        state = nameState
                                    )
                                }
                                InfoStepsStatus.Old -> {
                                    InfoScreen(
                                        modifier = Modifier
                                            .height(screenHeight * 1f),
                                        label = context.getString(R.string.old_label),
                                        state = oldState
                                    )
                                }
                                InfoStepsStatus.BestSide -> {
                                    InfoScreen(
                                        modifier = Modifier
                                            .height(screenHeight * 1f),
                                        label = context.getString(R.string.best_side_label),
                                        state = oldState
                                    )
                                }
                                InfoStepsStatus.Hobbies -> {
                                    InfoScreen(
                                        modifier = Modifier
                                            .height(screenHeight * 1f),
                                        label = context.getString(R.string.hobbies_label),
                                        state = hobbiesState
                                    )
                                }
                                InfoStepsStatus.Zodiac -> {
                                    InfoScreen(
                                        modifier = Modifier
                                            .height(screenHeight * 1f),
                                        label = context.getString(R.string.zodiac_label),
                                        state = zodiacState
                                    )
                                }
                                InfoStepsStatus.Caracter -> {
                                    InfoScreen(
                                        modifier = Modifier
                                            .height(screenHeight * 1f),
                                        label = context.getString(R.string.caracter_label),
                                        state = caracterState
                                    )
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
                            .background(ColorDailyUserInfoButton)
                            .clickable {
                                when (screenStepState.value) {
                                    InfoStepsStatus.Name -> screenStepState.value =
                                        InfoStepsStatus.Old

                                    InfoStepsStatus.Old -> screenStepState.value =
                                        InfoStepsStatus.BestSide

                                    InfoStepsStatus.BestSide -> screenStepState.value =
                                        InfoStepsStatus.Hobbies

                                    InfoStepsStatus.Hobbies -> screenStepState.value =
                                        InfoStepsStatus.Zodiac

                                    InfoStepsStatus.Zodiac -> screenStepState.value =
                                        InfoStepsStatus.Caracter

                                    InfoStepsStatus.Caracter -> {
                                        navController.navigate(
                                            LocalNavController.DailyInfoScreen(
                                                forDay = args.forDay,
                                                source = args.source
                                            )
                                        )
                                    }
                                }
                            },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            context.getString(R.string.next_text),
                            color = Color.White,
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




/*
*
                        item {
                            DailyUserInfoFieldsHolder(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(ColorDailyUserInfoFields0),
                                state1 = nameState,
                                state2 = oldState,
                                state3 = bestSideState,
                                label1 = context.getString(R.string.name_label),
                                label2 = context.getString(R.string.old_label),
                                label3 = context.getString(R.string.best_side_label),
                                context = context,
                                containerLabel = context.getString(R.string.user_info_label_1)
                            )
                        }

                        item {
                            Spacer(Modifier.height(10.dp))
                        }

                        item {
                            DailyUserInfoFieldsHolder(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(ColorDailyUserInfoFields1),
                                state1 = hobbiesState,
                                state2 = zodiacState,
                                state3 = caracterState,
                                label1 = context.getString(R.string.hobbies_label),
                                label2 = context.getString(R.string.zodiac_label),
                                label3 = context.getString(R.string.caracter_label),
                                context = context,
                                containerLabel = context.getString(R.string.user_info_label_2)
                            )
                        }

                        item {
                            Spacer(Modifier.height(10.dp))
                        }

                        item {
                            BorderButton(
                                Modifier,
                                context.getString(R.string.next_text),
                                onClick = {
                                    navController.navigate(
                                        LocalNavController.DailyInfoScreen(
                                            source = args.source,
                                            forDay = args.forDay
                                        )
                                    )
                                }
                            )
                        }*/