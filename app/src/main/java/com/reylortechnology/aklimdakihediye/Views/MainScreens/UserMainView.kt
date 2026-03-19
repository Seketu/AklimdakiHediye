package com.reylortechnology.aklimdakihediye.Views.MainScreens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.reylortechnology.aklimdakihediye.R
import com.reylortechnology.aklimdakihediye.AdMob.AdBanner
import com.reylortechnology.aklimdakihediye.Compose.AddMainPeopleCard
import com.reylortechnology.aklimdakihediye.Compose.AddPeopleDialog
import com.reylortechnology.aklimdakihediye.ObserverClasses.ForWhoObserver
import com.reylortechnology.aklimdakihediye.ObserverClasses.AlertDialogObserver
import com.reylortechnology.aklimdakihediye.ObserverClasses.ToScreenObserver
import com.reylortechnology.aklimdakihediye.ViewModels.UserMainViewModel
import com.reylortechnology.aklimdakihediye.Compose.AlertDialog
import com.reylortechnology.aklimdakihediye.Compose.AutoResizeText
import com.reylortechnology.aklimdakihediye.Compose.LottieAnim
import com.reylortechnology.aklimdakihediye.Compose.MainListDailyButton
import com.reylortechnology.aklimdakihediye.Compose.MainListRowButton
import com.reylortechnology.aklimdakihediye.Compose.MainPeopleCard
import com.reylortechnology.aklimdakihediye.Compose.NavigationButton
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.LocalUserInformation
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.Peoples
import com.reylortechnology.aklimdakihediye.NavController.LocalNavController
import com.reylortechnology.aklimdakihediye.ObserverClasses.ShowPopUp
import com.reylortechnology.aklimdakihediye.ObserverClasses.SpecialRowStatus
import com.reylortechnology.aklimdakihediye.Views.PartScreens.LoadingScreen
import com.reylortechnology.aklimdakihediye.models.ComposeModels.UserMainListItems
import com.reylortechnology.aklimdakihediye.ui.theme.JustAnotherHandFont
import com.reylortechnology.aklimdakihediye.ui.theme.fatherCardBg
import com.reylortechnology.aklimdakihediye.ui.theme.kanitOzelBaslik
import com.reylortechnology.aklimdakihediye.ui.theme.loverCardBg
import com.reylortechnology.aklimdakihediye.ui.theme.motherCardBg
import com.reylortechnology.aklimdakihediye.ui.theme.stylizedRed
import com.reylortechnology.aklimdakihediye.ui.theme.stylizedYellow
import com.reylortechnology.aklimdakihediye.ui.theme.womenCardBg


//Ana kullanıcı sayfası
class UserMainView {

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun UserMainScreen(
        navController: NavController,
        viewModel: UserMainViewModel = hiltViewModel()
    ) {
        val context = LocalContext.current
        val alertDialogState by viewModel.alertDialogState.collectAsState()
        val showAlert by viewModel.showAlert.collectAsState()
        val infoScreen by viewModel.infoScreen.collectAsState()
        val toRowButtonState by viewModel.rowStatus.collectAsState()
        val userInformation by viewModel.userInformation.collectAsState()
        val peopleInformation by  viewModel.peoplesInformation.collectAsState()
        val addPeoplePopUp by viewModel.addPeoplePopUpState.collectAsState()

        UserMainContent(
            navController = navController,
            alertDialogState = alertDialogState,
            showAlert = showAlert,
            infoScreen = infoScreen,
            toRowButtonState = toRowButtonState,
            onUpdateForWho = { forDay, source ->
                viewModel.updateForWhoState(
                    ForWhoObserver.checkState,
                    context,
                    forDay,
                    source
                )
            },
            onNavigateFromColumn = { nav, forDay, source, forWhat ->
                viewModel.navigateFromColumn(nav, forDay, source, forWhat)
            },
            userInformation,
            peopleInformation,
            addNewPeople = { viewModel.addNewPeople(it) },
            addPeoplePopUp = addPeoplePopUp,
            setAddPeoplePopUp = {value ->
                viewModel.setAddPeoplePopup(value)
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun UserMainContent(
        navController: NavController,
        alertDialogState: AlertDialogObserver,
        showAlert: Boolean,
        infoScreen: ToScreenObserver,
        toRowButtonState: SpecialRowStatus,
        onUpdateForWho: (String, Int) -> Unit,
        onNavigateFromColumn: (NavController, String?, Int?, String) -> Unit,
        userInformation: List<LocalUserInformation>,
        peopleInformation : List<Peoples>,
        addNewPeople : (Peoples)-> Unit,
        setAddPeoplePopUp : (Boolean)-> Unit,
        addPeoplePopUp: ShowPopUp
        ) {
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

        when(addPeoplePopUp){
            ShowPopUp.None -> {

            }
            ShowPopUp.Show -> {
                AddPeopleDialog(
                    modifier = Modifier
                        .fillMaxWidth(0.94f)
                        .fillMaxHeight(0.94f),
                    onDismissRequest = {
                        setAddPeoplePopUp(false)
                    }
                ) {peoples ->
                    addNewPeople(peoples)
                }
            }
        }

        LaunchedEffect(
            key1 = toRowButtonState
        ) {
            toRowButtonState.let {
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
            infoScreen
        ) {
            if (!isLoading.value) {
                infoScreen.let { event->
                    when(event){
                        is ToScreenObserver.NewInformation -> {
                            onNavigateFromColumn(
                                navController,
                                event.forDay,
                                event.source,
                                "SaveInf"
                            )
                        }
                        is ToScreenObserver.WithUserInformation -> {
                            onNavigateFromColumn(
                                navController,
                                event.forDay,
                                event.source,
                                "WithInf"
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

        alertDialogState.let { event ->
            when (event) {
                is AlertDialogObserver.NewAlertDialog -> {
                    if (showAlert && !isLoading.value) {
                        AlertDialog(
                            title = event.title,
                            confirmButton = event.onConfirm,
                            dismissButton = event.dismissButton,
                            onDismissRequest = event.onDismiss,
                            dismissText = event.dismissText,
                            confirmText = event.confirmText,
                            withDismiss = event.withDismiss
                        )
                    }
                }

                AlertDialogObserver.none -> {

                }
            }
        }

        when (isLoading.value) {
            true -> LoadingScreen(modifier = Modifier.fillMaxSize())

            false -> SuccessLoading(
                navController =  navController,
                onUpdateForWho =  onUpdateForWho,
                userInformation = userInformation.firstOrNull(),
                peopleInformation = peopleInformation,
                setAddPeoplePopUp = {
                    setAddPeoplePopUp(it)
                }
            )
        }
    }
}

@Composable
fun SuccessLoading(
    navController: NavController,
    onUpdateForWho: (String, Int) -> Unit,
    userInformation: LocalUserInformation? = null,
    peopleInformation: List<Peoples>,
    setAddPeoplePopUp : (Boolean) -> Unit
) {
    val context = LocalContext.current

    val buttonList = listOf(
        UserMainListItems(
            stringResource(R.string.main_list_father),
            R.raw.fathers_anim,
            fatherCardBg,
            stringResource(R.string.main_list_father_card)
        ),
        UserMainListItems(
            stringResource(R.string.main_list_mother),
            R.raw.mother,
            motherCardBg,
            stringResource(R.string.main_list_mother_card)
        ),
        UserMainListItems(
            stringResource(R.string.main_list_lover),
            R.raw.lover_anim,
            loverCardBg,
            stringResource(R.string.main_list_lover_card)
        ),
        UserMainListItems(
            stringResource(R.string.main_list_women),
            R.raw.woman_anim,
            womenCardBg,
            stringResource(R.string.main_list_women_card)
        )
    )
    val screenConfig = LocalConfiguration.current
    val screenHeight = screenConfig.screenHeightDp.dp

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(
                MaterialTheme.colorScheme.surfaceVariant
            )
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AdBanner(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.075f)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text  = buildAnnotatedString {
                        withStyle(
                            style = MaterialTheme.typography.kanitOzelBaslik.copy(
                                color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                                fontSize = 24.sp
                            ).toSpanStyle()
                        ){
                            append("AKLIMDAKİ HEDİYE")
                        }

                        withStyle(
                            style = SpanStyle(
                                color = Color.Red,
                                fontSize = 50.sp
                            )
                        ){
                            append(".")
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 5.dp)
                )
                Text(
                    text  = buildAnnotatedString {
                        withStyle(
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontFamily = JustAnotherHandFont,
                                fontWeight = FontWeight.Thin,
                                color = if (isSystemInDarkTheme()) Color.White else Color.Black,

                            ).toSpanStyle()
                        ){
                            append("Merhaba ${userInformation?.name ?: ""},\nBugün Kimi Mutlu ediyoruz")
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 5.dp)
                )
            }


            Box(modifier = Modifier
                .fillMaxWidth(0.975f)
                .height(screenHeight * 0.2f)
                .paint(
                    painter = painterResource(R.drawable.main_button),
                    contentScale = ContentScale.FillBounds
                )
            ){
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .background(
                            color = stylizedRed,
                            shape = MaterialTheme.shapes.large
                        )
                        .padding(2.dp)
                        .fillMaxWidth(0.3f),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        "AI destekli",
                        color = stylizedYellow
                    )
                }

                AutoResizeText(
                    "Aklındaki Hediyeyi Bulalım",
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .fillMaxWidth(0.7f),
                    style = MaterialTheme.typography.headlineMedium
                )

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 20.dp)
                        .background(stylizedRed, shape = MaterialTheme.shapes.large)
                        .padding(3.dp)
                ){
                    Text(
                        "Aramaya Başla !",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
            Spacer(
                modifier  = Modifier.heightIn(
                    min = 10.dp
                )
            )
            Column(
                modifier = Modifier.fillMaxWidth(0.97f)
                    .heightIn(
                        max = screenHeight * 0.15f
                    )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Arkadaşlarınız İçin",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.W400
                    )

                    Text(
                        "hepsini gör",
                        color = Color.Red
                    )
                }

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.8f)
                ) {
                    items(
                        peopleInformation
                    ){people ->
                        MainPeopleCard(
                            modifier = Modifier
                                .fillMaxHeight()
                                .aspectRatio(1f)
                                .clickable{
                                    navController.navigate(LocalNavController.ToFriendGiftView(people.peopleId))
                                },
                            peoples = people
                        )
                    }
                    item {
                        AddMainPeopleCard(
                            modifier = Modifier
                                .fillMaxHeight(1f)
                                .width(60.dp)
                                .clickable{
                                    setAddPeoplePopUp(true)
                                }
                        )
                    }
                }
            }

            //Depreceted Who is the lucky person
            /*
            Column(
                Modifier
                    .fillMaxWidth()
                    .height(screenHeight * 0.075f)
                    .padding(top = 10.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(mainRowCardBg),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    stringResource(R.string.who_is_lucky),
                    color = Color.White
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight(0.8f)
                ) {
                    items(rowList) { row ->
                        Spacer(Modifier.width(15.dp))
                        MainListRowButton(
                            modifier = Modifier
                                .clickable{
                                    onUpdateForWho(row.text, row.source)
                                },
                            source = row.source,
                            text = row.text
                        )
                        Spacer(Modifier.width(5.dp))
                    }
                }
            }*/
            Spacer(
                modifier  = Modifier.heightIn(
                    min = 10.dp
                )
            )
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
                ) {
                    Text(
                        text = stringResource(R.string.celebrations_list_title),
                        color = if(isSystemInDarkTheme())
                            Color.White
                        else Color.Black,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.W200,
                        textDecoration = TextDecoration.combine(
                            listOf(
                                TextDecoration.Underline
                            )
                        )
                    )
                    Spacer(
                        Modifier.height(3.dp)
                    )

                    //Celebrate Days Fields
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth(0.95f)
                            .fillMaxHeight(0.85f)
                    ) {
                        items(buttonList) { button ->
                            Spacer(Modifier.width(15.dp))
                            MainListDailyButton(
                                modifier = Modifier
                                    .width(  350.dp)
                                    .fillMaxHeight(0.8f)
                                    .background(button.color, shape = RoundedCornerShape(15.dp))
                                    .border(
                                        width = 1.dp,
                                        color = if(isSystemInDarkTheme()) Color.White else Color.Black,
                                        shape = RoundedCornerShape(15.dp)
                                        )
                                    .padding(5.dp)
                                    .clickable {
                                        onUpdateForWho(button.text, button.source)
                                    },
                                source = button.source,
                                lottieModifier = Modifier
                                    .size(100.dp),
                                contentScale = ContentScale.FillHeight,
                                text = button.text,
                                cardText = button.cardText
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(
                            min  = screenHeight * 0.0742f
                        )
                        .padding(
                            vertical = 5.dp,
                            horizontal = 15.dp
                        )
                        .clip(
                            MaterialTheme.shapes.large
                        )
                        .background(
                            color = MaterialTheme.colorScheme.primary
                        )
                        .padding(start = 20.dp, end = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    NavigationButton(
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                navController.navigate(LocalNavController.SavedVariablesScreen)
                            },
                        R.drawable.save_flag_icon,
                        "Hediyeler"
                    )

                    NavigationButton(
                        modifier = Modifier
                            .size(
                                30.dp
                            )
                            .clickable{
                                navController.navigate(LocalNavController.PeoplesScreen)
                            },
                        source = R.drawable.people_icon,
                        text = "İnsanlar"
                    )

                    NavigationButton(
                        modifier = Modifier
                            .size(30.dp)
                            .clickable{
                                navController.navigate(LocalNavController.SpecialDaysScreen)
                            },
                        source = R.drawable.notification_icon,
                        text = "Takvim"
                    )

                    NavigationButton(
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                navController.navigate(LocalNavController.UserSettingsScreen)
                            },
                        R.drawable.user_settings_icon,
                        text = "Profil"
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Prev() {
    SuccessLoading(rememberNavController(),
        onUpdateForWho = { _, _ -> },
        peopleInformation = emptyList(),
        setAddPeoplePopUp = {}
        )
}
