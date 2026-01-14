package com.reylortechnology.aklimdakihediye.Views.MainScreens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.reylortechnology.aklimdakihediye.Compose.UserSettingsTextField
import com.reylortechnology.aklimdakihediye.R
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.LocalUserInformation
import com.reylortechnology.aklimdakihediye.NavController.LocalNavController
import com.reylortechnology.aklimdakihediye.ViewModels.UserSettingsViewModel
import com.reylortechnology.aklimdakihediye.rememberImeState
import com.reylortechnology.aklimdakihediye.ui.theme.onPrimaryLight
import com.reylortechnology.aklimdakihediye.ui.theme.primaryLight
import com.reylortechnology.aklimdakihediye.ui.theme.surfaceLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSettingsView(
    modifier: Modifier = Modifier,
    viewModel : UserSettingsViewModel = hiltViewModel(),
    navController: NavController
) {


    val userInformation = viewModel.userInformation.collectAsState(emptyList())

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.user_info_user_settings_information),
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.displaySmall
                    )
                },
                modifier = Modifier,
                navigationIcon = {
                    IconButton(
                        onClick = {
                            viewModel.exitScreen(navController = navController)
                        },
                        modifier = Modifier.size(50.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            "Back icon",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            )
        }
    ) {
        when (userInformation.value.isEmpty()) {
            false -> {
                UserSettingsWithDataScreen(
                    modifier = modifier
                        .padding(it)
                        .fillMaxSize(),
                    userInformation
                )
            }
            true -> {
             UserSettingsWithoutDataScreen(
                 modifier = modifier.padding(it).fillMaxSize(), navController = navController
             )
            }
        }


    }

}

@Composable
fun UserSettingsWithoutDataScreen(modifier: Modifier = Modifier,navController : NavController) {
    Column(
        modifier = modifier
                .background(surfaceLight),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                navController.navigate(LocalNavController.UserInfoScreen(null,null,"SaveInf"))
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = primaryLight
            )
        ) {
            Text(
                "Kullanıcı Bilgisi Bulunamadı Eklemek için tıklayın",
                color = onPrimaryLight
                )
        }
    }
}

@Composable
fun UserSettingsWithDataScreen(
    modifier: Modifier = Modifier,
    userInformation: State<List<LocalUserInformation>>,
    viewModel : UserSettingsViewModel = hiltViewModel()
) {

    val originalUserData = userInformation.value.first()

    val localConfiguration = LocalConfiguration.current
    val screenHeight = localConfiguration.screenHeightDp.dp

    val userNameState = remember(originalUserData) {
        mutableStateOf(originalUserData.name)
    }

    val userHobbiesState = remember(originalUserData) {
        mutableStateOf(originalUserData.hobbies)
    }

    val characterState = remember(originalUserData) {
        mutableStateOf(originalUserData.caracter)
    }

    val userOldState = remember(originalUserData) {
        mutableStateOf(originalUserData.old.toString())
    }

    val userZodiacState = remember(originalUserData) {
        mutableStateOf(originalUserData.zodiac)
    }

    val userBestSideState = remember(originalUserData) {
        mutableStateOf(originalUserData.bestSide)
    }

    val isModified = remember(originalUserData) {
        derivedStateOf {
            userNameState.value != originalUserData.name ||
                    userHobbiesState.value != originalUserData.hobbies ||
                    characterState.value != originalUserData.caracter ||
                    userOldState.value != originalUserData.old.toString()||
                    userZodiacState.value != originalUserData.zodiac ||
                    userBestSideState.value != originalUserData.bestSide
        }
    }
    val imeState = rememberImeState()

    val scrollState = rememberScrollState()

    LaunchedEffect(
        imeState.value
    ) {
        if (imeState.value){
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .height(screenHeight * 0.88f)
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //User special fields
            Column(
                modifier = Modifier
                    .heightIn(
                        min = screenHeight * 0.2715f
                        )
                    .fillMaxWidth(0.9320f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Image(
                        painter = painterResource(R.drawable.special_inf_card),
                        "special info card",
                        modifier = Modifier
                            .height(screenHeight * 0.0523f)

                    )

                    Text(
                        text = "Özel Bilgiler",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier
                            .align(Alignment.CenterVertically),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(7.dp))
                UserSettingsTextField(
                    value = userNameState,
                    modifier = Modifier
                        .heightIn(
                            min = screenHeight * 0.0545f,
                            max = screenHeight * 0.15f
                        )
                        .fillMaxWidth(),
                    prefix = stringResource(R.string.user_info_name_label)
                )
                Spacer(modifier = Modifier.height(7.dp))
                UserSettingsTextField(
                    value = userOldState,
                    modifier = Modifier
                        .heightIn(
                            min = screenHeight * 0.0545f,
                            max = screenHeight * 0.15f
                        )
                        .fillMaxWidth(),
                    prefix = stringResource(R.string.user_info_old_label),
                    keyboard = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        autoCorrectEnabled = false,
                        )
                )
                Spacer(modifier = Modifier.height(7.dp))
                UserSettingsTextField(
                    value = userZodiacState,
                    modifier = Modifier
                        .heightIn(
                            min = screenHeight * 0.0545f,
                            max = screenHeight * 0.15f
                        )
                        .fillMaxWidth(),
                    prefix = stringResource(R.string.user_info_zodiac_label)
                    )
            }
            Spacer(Modifier.height(15.dp))


            //User Soccial Inf Fields
            Column(
                modifier = Modifier
                    .heightIn(
                        min = screenHeight * 0.2715f
                    )
                    .fillMaxWidth(0.9320f),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Image(
                        painter = painterResource(R.drawable.social_inf_card),
                        "special info card",
                        modifier = Modifier
                            .height(screenHeight * 0.0523f),
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                    )

                    Text(
                        text = "Sosyal Özellikler",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier
                    )
                }
                Spacer(Modifier.height(7.dp))
                UserSettingsTextField(
                    value = characterState,
                    prefix = stringResource(R.string.user_info_caracter_label),
                    modifier = Modifier
                        .heightIn(
                            min = screenHeight * 0.0545f,
                            max = screenHeight * 0.15f
                        )
                        .fillMaxWidth(),
                )
                Spacer(Modifier.height(7.dp))
                UserSettingsTextField(
                    value = userHobbiesState,
                    prefix = stringResource(R.string.user_info_hobbies_label),
                    modifier = Modifier
                        .heightIn(
                            min = screenHeight * 0.0545f,
                            max = screenHeight * 0.15f
                        )
                        .fillMaxWidth(),
                )
                Spacer(Modifier.height(7.dp))
                UserSettingsTextField(
                    value = userBestSideState,
                    prefix = stringResource(R.string.user_info_best_side_label),
                    modifier = Modifier
                        .heightIn(
                            min = screenHeight * 0.0545f,
                            max = screenHeight * 0.15f
                        )
                        .fillMaxWidth(),
                )
            }
        }
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .align(Alignment.BottomCenter),
                visible = isModified.value,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight * 0.1f)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable {
                            val userInformation = LocalUserInformation(
                                uid = userInformation.value.get(0).uid,
                                name = userNameState.value,
                                hobbies = userHobbiesState.value,
                                caracter = characterState.value,
                                old = userOldState.value.toInt(),
                                zodiac = userZodiacState.value,
                                bestSide = userBestSideState.value
                            )
                            viewModel.setUserInformation(userInformation,isModified)
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Kaydet",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            }
    }
}