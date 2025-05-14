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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.reylortechnology.aklimdakihediye.R
import com.reylortechnology.aklimdakihediye.Compose.UserInformationPlace
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.LocalUserInformation
import com.reylortechnology.aklimdakihediye.NavController.LocalNavController
import com.reylortechnology.aklimdakihediye.ViewModels.UserSettingsViewModel
import com.reylortechnology.aklimdakihediye.rememberImeState
import com.reylortechnology.aklimdakihediye.ui.theme.onPrimaryLight
import com.reylortechnology.aklimdakihediye.ui.theme.primaryLight
import com.reylortechnology.aklimdakihediye.ui.theme.surfaceLight

@Composable
fun UserSettingsView(
    modifier: Modifier = Modifier,
    viewModel : UserSettingsViewModel = hiltViewModel(),
    navController: NavController
) {


    val userInformation = viewModel.userInformation.collectAsState(emptyList())

    Scaffold {
        when (userInformation.value.isEmpty()) {
            false -> {
                UserSettingsWithDataScreen(
                    modifier = modifier
                        .padding(it)
                        .fillMaxSize(),
                    userInformation,
                    navController = navController
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
    viewModel : UserSettingsViewModel = hiltViewModel(),
    navController: NavController
) {

    val localConfiguration = LocalConfiguration.current
    val screenHeight = localConfiguration.screenHeightDp.dp

    val userNameState = remember {
        mutableStateOf(userInformation.value.get(0).name)
    }

    val userHobbiesState = remember {
        mutableStateOf(userInformation.value.get(0).hobbies)
    }

    val userCaracterState = remember {
        mutableStateOf(userInformation.value.get(0).caracter)
    }

    val userOldState = remember {
        mutableStateOf(userInformation.value.get(0).old.toString())
    }

    val userZodiacState = remember {
        mutableStateOf(userInformation.value.get(0).zodiac)
    }

    val userBestSideState = remember {
        mutableStateOf(userInformation.value.get(0).bestSide)
    }

    val rowList = mutableListOf<Pair<String, MutableState<String>>>()

    rowList.addAll(
        listOf(
            Pair(
                stringResource(R.string.user_info_name_label),
                userNameState
            ),
            Pair(
                stringResource(R.string.user_info_hobbies_label),
                userHobbiesState
            ),
            Pair(
                stringResource(R.string.user_info_caracter_label),
                userCaracterState
            ),
            Pair(
                stringResource(R.string.user_info_old_label),
                userOldState
            ),
            Pair(
                stringResource(R.string.user_info_zodiac_label),
                userZodiacState
            ),
            Pair(
                stringResource(R.string.user_info_best_side_label),
                userBestSideState
            )
        )
    )
    val imeState = rememberImeState()

    val scrollState = rememberScrollState()

    LaunchedEffect(
        imeState.value
    ) {
        if (imeState.value){
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }
    val editIsEnable = viewModel.isEdit.collectAsState()

    val showInformation = remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier
            .background(surfaceLight),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(R.drawable.icon_back_2),
                "",
                modifier = Modifier
                    .padding(5.dp)
                    .size(50.dp)
                    .clickable{
                        viewModel.exitScreen(navController = navController)
                    }
            )
            Spacer(Modifier.weight(1f))
            Text(
                stringResource(R.string.user_info_user_setting_label),
                fontSize = 24.sp
            )
            Spacer(Modifier.weight(1f))
            Image(
                painter = painterResource(R.drawable.icon_edit),
                "",
                modifier = Modifier
                    .padding(5.dp)
                    .size(50.dp)
                    .clickable{
                        viewModel.setEdit()
                    }
            )
        }
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .imePadding()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth(0.5f),
                shape = CutCornerShape(0.5f),
                onClick = {
                    showInformation.value = !showInformation.value
                }
            ) {
                Text(stringResource(R.string.user_info_button_user_settings_label))
            }

            AnimatedVisibility(
                visible = showInformation.value,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(screenHeight * 0.7f),
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(stringResource(R.string.user_info_user_settings_information))
                    UserInformationPlace(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(top = 10.dp),
                        list = rowList,
                        editEnable = editIsEnable as MutableState<Boolean>
                    )
                }
            }
            Spacer(Modifier.weight(1f))
            AnimatedVisibility(
                modifier = Modifier
                    .fillMaxWidth(),
                visible = editIsEnable.value,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight * 0.1f)
                        .background(Color.Blue)
                        .clickable{
                            val userInformation = LocalUserInformation(
                                uid = userInformation.value.get(0).uid,
                                name = userNameState.value,
                                hobbies = userHobbiesState.value,
                                caracter = userCaracterState.value,
                                old = userOldState.value.toInt(),
                                zodiac = userZodiacState.value,
                                bestSide = userBestSideState.value
                            )
                            viewModel.setUserInformation(userInformation)
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Text("Bilgileri değiştir")
                }
            }

        }
    }
}