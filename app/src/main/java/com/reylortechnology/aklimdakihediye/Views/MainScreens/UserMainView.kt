package com.reylortechnology.aklimdakihediye.Views.MainScreens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.reylortechnology.aklimdakihediye.R
import com.reylortechnology.aklimdakihediye.AdMob.AdBanner
import com.reylortechnology.aklimdakihediye.ObserverClasses.ForWhoObserver
import com.reylortechnology.aklimdakihediye.ObserverClasses.AlertDialogObserver
import com.reylortechnology.aklimdakihediye.ObserverClasses.ToScreenObserver
import com.reylortechnology.aklimdakihediye.ViewModels.UserMainViewModel
import com.reylortechnology.aklimdakihediye.Compose.AlertDialog
import com.reylortechnology.aklimdakihediye.Compose.LottieAnim
import com.reylortechnology.aklimdakihediye.Compose.MainListDailyButton
import com.reylortechnology.aklimdakihediye.Compose.MainListRowButton
import com.reylortechnology.aklimdakihediye.Compose.NavigationButton
import com.reylortechnology.aklimdakihediye.NavController.LocalNavController
import com.reylortechnology.aklimdakihediye.ObserverClasses.SpecialRowStatus
import com.reylortechnology.aklimdakihediye.Views.PartScreens.LoadingScreen
import com.reylortechnology.aklimdakihediye.models.ComposeModels.UserMainListItems
import com.reylortechnology.aklimdakihediye.models.ComposeModels.UserMainRowListItems



class UserMainView {

    @Composable
    fun UserMainScreen(
        navController: NavController,
        viewModel: UserMainViewModel = hiltViewModel()
    ) {
        val context = LocalContext.current
        val alertDialogState = viewModel.alertDialogState.collectAsState()
        val showAlert = viewModel.showAlert.collectAsState()
        val infoScreen = viewModel.infoScreen.collectAsState()
        val toRowButtonState = viewModel.rowStatus.collectAsState()

        val isLoading = remember { mutableStateOf(true) }
        // Her bir animasyon için rememberLottieComposition kullanıyoruz
        val compositionMain by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.main_anim)
        )
        val compositionExit by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.exit_anim)
        )
        val compositionFather by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.fathers_anim)
        )
        val compositionMother by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.mother)
        )
        val compositionLover by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.lover_anim)
        )
        val compositionWoman by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.woman_anim)
        )

        LaunchedEffect(
           key1 = toRowButtonState.value
        ) {
            toRowButtonState.value.let {
                when(it){
                    SpecialRowStatus.None ->{

                    }
                    is SpecialRowStatus.ToScreen -> {
                        navController.navigate(LocalNavController.SpecialGiftRowScreen(
                            source = it.source,
                            forWho = it.forWho,
                            forAnother = false
                        ))
                    }
                    is SpecialRowStatus.ToTakeInformation -> {
                        navController.navigate(LocalNavController.SpecialGiftRowScreen(
                            source = it.source,
                            forWho = it.forWho,
                            forAnother = it.forAnother
                        ))
                    }
                }
            }
        }

        LaunchedEffect(
            infoScreen.value
        ) {
            if (!isLoading.value) {
                infoScreen.value.let { event->
                    when(event){
                        is ToScreenObserver.ForAnotherInformation -> {
                            viewModel.navigateFromColumn(
                                navController,
                                event.forDay,
                                event.source,
                                "ForAnother",
                            )
                        }
                        is ToScreenObserver.NewInformation -> {
                            viewModel.navigateFromColumn(
                                navController,
                                forDay = event.forDay,
                                source = event.source,
                                forWhat = "SaveInf"
                            )
                        }
                        is ToScreenObserver.WithUserInformation -> {
                            viewModel.navigateFromColumn(
                                navController = navController,
                                forDay = event.forDay,
                                source = event.source,
                                forWhat = "WithInf"
                            )
                        }
                        ToScreenObserver.none -> {
                            Log.e("Error","none")
                        }
                    }
                }
            }
        }

        // Tüm animasyonların yüklenip yüklenmediğini kontrol ediyoruz
        LaunchedEffect(
            compositionMain,
            compositionExit,
            compositionFather,
            compositionMother,
            compositionLover,
            compositionWoman
        ) {
            if (compositionMain != null &&
                compositionExit != null &&
                compositionFather != null &&
                compositionMother != null &&
                compositionLover != null &&
                compositionWoman != null
            ) {
                isLoading.value = false
            }
        }
        alertDialogState.value.let { event ->
            when (event) {
                is AlertDialogObserver.NewAlertDialog -> {
                    if (showAlert.value && !isLoading.value) {
                        AlertDialog(
                            title = event.title,
                            confirmButton = event.onConfirm,
                            dismissButton = event.dismissButton,
                            onDismissRequest = event.onDismiss,
                            dismissText = event.dismissText,
                            confirmText = event.confirmText
                        )
                    }
                }

                AlertDialogObserver.none -> {

                }
            }
        }

        when (isLoading.value) {
            true -> LoadingScreen(modifier = Modifier.fillMaxSize())

            false -> SuccesLoading(navController, infoScreen, viewModel)
        }
    }
}

@Composable
fun SuccesLoading(
    navController: NavController,
    infoScreen: State<ToScreenObserver>,
    viewModel: UserMainViewModel
) {
    var showAd by remember { mutableStateOf(true) }
    val userInformation = viewModel.userInformation.collectAsState()
    val context = LocalContext.current
    val forWhoState = viewModel.forWhoState.collectAsState()

    val rowList = listOf(
        UserMainRowListItems(
            source =  R.raw.lover_anim,
            text = context.getString(R.string.main_row_love)
        ),
        UserMainRowListItems(
           source =   R.raw.friend_row,
           text =  context.getString(R.string.main_row_friend)
        ),
        UserMainRowListItems(
            source =  R.raw.teacher_row,
            text =  context.getString(R.string.main_row_teacher)
        ),
        UserMainRowListItems(
            source =  R.raw.job_friend,
            text =  context.getString(R.string.main_row_job)
        )
    )

    val buttonList = listOf<UserMainListItems>(
        UserMainListItems(
            context.getString(R.string.main_list_father),
            R.raw.fathers_anim,
            MaterialTheme.colorScheme.secondary,
            context.getString(R.string.main_list_father_card)
        ),
        UserMainListItems(
            context.getString(R.string.main_list_mother),
            R.raw.mother,
            MaterialTheme.colorScheme.secondary,
            context.getString(R.string.main_list_mother_card)
        ),
        UserMainListItems(
            context.getString(R.string.main_list_lover),
            R.raw.lover_anim,
            MaterialTheme.colorScheme.secondary,
            context.getString(R.string.main_list_lover_card)
        ),
        UserMainListItems(
            context.getString(R.string.main_list_women),
            R.raw.woman_anim,
            MaterialTheme.colorScheme.secondary,
            context.getString(R.string.main_list_women_card)
        )
    )
    val screenConfig = LocalConfiguration.current
    val screenHeight = screenConfig.screenHeightDp.dp

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant),
        ) {
            AdBanner(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.1f)
            )
            Column(
                Modifier
                    .fillMaxWidth()
                    .height(screenHeight * 0.21f)
                    .padding(top = 10.dp)
                    .clip(RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp))
                    .background(MaterialTheme.colorScheme.primary),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    stringResource(R.string.who_is_lucky),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight(0.8f)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    items(rowList) { row ->
                        Spacer(Modifier.width(15.dp))
                        MainListRowButton(
                            modifier = Modifier
                                .clickable{
                                    viewModel.updateForWhoState(
                                        ForWhoObserver.checkState,
                                        forDay = row.text,
                                        source = row.source,
                                        context = context
                                    )
                                },
                            source = row.source,
                            text = row.text
                        )
                        Spacer(Modifier.width(5.dp))
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                LottieAnim(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.2f)
                        .align(Alignment.BottomCenter),
                    source = R.raw.main_anim,
                    contentScale = ContentScale.FillWidth
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .fillMaxHeight(0.85f)
                    ) {
                        items(buttonList) { button ->
                            Spacer(Modifier.height(50.dp))
                            MainListDailyButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(button.color, shape = RoundedCornerShape(15.dp))
                                    .padding(5.dp)
                                    .clickable {
                                        viewModel.updateForWhoState(
                                            ForWhoObserver.checkState,
                                            forDay = button.text,
                                            source = button.source,
                                            context = context
                                        )
                                    },
                                source = button.source,
                                lottieModifier = Modifier
                                    .size(150.dp),
                                contentScale = ContentScale.FillHeight,
                                text = button.text,
                                cardText = button.cardText
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .fillMaxHeight()
                            .paint(
                                painter = painterResource(R.drawable.down_navigation),
                                contentScale = ContentScale.FillWidth
                            )
                            .padding(start = 20.dp, end = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        NavigationButton(
                            modifier = Modifier
                                .padding(top = 15.dp)
                                .size(56.dp)
                                .clickable {
                                    navController.navigate(LocalNavController.SavedVariablesScreen)
                                },
                            R.drawable.saved_variables
                        )
                        Box(
                            modifier = Modifier
                                .size(75.dp)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.logo),
                                "",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .clickable{
                                        navController.navigate(LocalNavController.SpecialDaysScreen)
                                    }
                            )
                        }

                        NavigationButton(
                            modifier = Modifier
                                .padding(top = 15.dp)
                                .size(56.dp)
                                .clickable {
                                    navController.navigate(LocalNavController.UserSettingsScreen)
                                },
                            R.drawable.settings_navigation
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun Prev() {
    UserMainView().UserMainScreen(rememberNavController())
}