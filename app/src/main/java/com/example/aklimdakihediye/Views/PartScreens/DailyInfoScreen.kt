package com.example.aklimdakihediye.Views.PartScreens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.aklimdakihediye.Compose.BorderButton
import com.example.aklimdakihediye.Compose.LottieAnim
import com.example.aklimdakihediye.Compose.PersonInfoScreen
import com.example.aklimdakihediye.Compose.ReleationshipStateInfo
import com.example.aklimdakihediye.Compose.ZodiacInfoScreen
import com.example.aklimdakihediye.NavController.LocalNavController
import com.example.aklimdakihediye.ObserverClasses.AboutGiftInformationScreenObserver
import com.example.aklimdakihediye.ObserverClasses.DailyPlacesObserver
import com.example.aklimdakihediye.ObserverClasses.RelationshipStatus
import com.example.aklimdakihediye.ObserverClasses.ZodiacStatus
import com.example.aklimdakihediye.R
import com.example.aklimdakihediye.ViewModels.AboutGiftInformationViewModel
import com.example.aklimdakihediye.ViewModels.SpecialGiftViewModel
import com.example.aklimdakihediye.models.GiftInformationModels.ForPersonInformation
import com.example.aklimdakihediye.ui.theme.ColorUserMainListFathers
import com.example.aklimdakihediye.ui.theme.ColorUserMainListLovers
import com.example.aklimdakihediye.ui.theme.ColorUserMainListMothers
import com.example.aklimdakihediye.ui.theme.ColorUserMainListWomen
import com.example.aklimdakihediye.ui.theme.ColorUserMainPlaceFathers
import com.example.aklimdakihediye.ui.theme.ColorUserMainPlaceLovers
import com.example.aklimdakihediye.ui.theme.ColorUserMainPlaceMothers
import com.example.aklimdakihediye.ui.theme.ColorUserMainPlaceWomen


@Composable
fun DailyInfoScreen(
    navController: NavController,
    args: LocalNavController.DailyInfoScreen,
    viewModel: AboutGiftInformationViewModel = hiltViewModel(),
    giftInformationScreenState: MutableState<AboutGiftInformationScreenObserver>
) {
    Log.e("Error", "Error")
    var themeColor = HashMap<String, Color>()
    val context = LocalContext.current

    val labelListRaw = listOf(
        context.getString(R.string.info_label_name),
        context.getString(R.string.info_label_old),
        context.getString(R.string.info_label_zodiac),
        context.getString(R.string.info_label_job),
        context.getString(R.string.info_label_best_side),
        context.getString(R.string.info_label_hobbies),
        context.getString(R.string.info_label_last_gifts),
        context.getString(R.string.info_label_time)
    )

    when (args.forDay) {

        context.getString(R.string.main_row_love) -> {
            themeColor.apply {
                put("mainColor", ColorUserMainListLovers)
                put("placeColor", ColorUserMainPlaceLovers)
            }
            val labelList = labelListRaw.map {
                "Sevgilinizin " + it
            }
            DailyInfoBakeScreen(
                forWho = args.forDay,
                color = themeColor,
                source = args.source,
                labelList = labelList,
                viewModel = viewModel,
                giftInformationScreenState = giftInformationScreenState,
                navController = navController
            )
        }

        context.getString(R.string.main_row_teacher) -> {
            themeColor.apply {
                put("mainColor", ColorUserMainListFathers)
                put("placeColor", ColorUserMainPlaceFathers)
            }
            val labelList = labelListRaw.map {
                "Öğretmeninin " + it
            }
            DailyInfoBakeScreen(
                forWho = args.forDay,
                color = themeColor,
                source = args.source,
                labelList = labelList,
                viewModel = viewModel,
                giftInformationScreenState = giftInformationScreenState,
                navController = navController
            )
        }

        context.getString(R.string.main_row_job) -> {
            themeColor.apply {
                put("mainColor", ColorUserMainListFathers)
                put("placeColor", ColorUserMainPlaceFathers)
            }
            val labelList = labelListRaw.map {
                "İş Arkadaşınızın " + it
            }
            DailyInfoBakeScreen(
                forWho = args.forDay,
                color = themeColor,
                source = args.source,
                labelList = labelList,
                viewModel = viewModel,
                giftInformationScreenState = giftInformationScreenState,
                navController = navController
            )
        }


        context.getString(R.string.main_row_friend) -> {
            themeColor.apply {
                put("mainColor", ColorUserMainListFathers)
                put("placeColor", ColorUserMainPlaceFathers)
            }
            val labelList = labelListRaw.map {
                "Arkadaşınızın " + it
            }
            DailyInfoBakeScreen(
                forWho = args.forDay,
                color = themeColor,
                source = args.source,
                labelList = labelList,
                viewModel = viewModel,
                giftInformationScreenState = giftInformationScreenState,
                navController = navController
            )
        }

        context.getString(R.string.main_list_father) -> {
            themeColor.apply {
                put("mainColor", ColorUserMainListFathers)
                put("placeColor", ColorUserMainPlaceFathers)
            }
            val labelList = labelListRaw.map {
                "Babanızın " + it
            }
            DailyInfoBakeScreen(
                forWho = args.forDay,
                color = themeColor,
                source = args.source,
                labelList = labelList,
                viewModel = viewModel,
                giftInformationScreenState = giftInformationScreenState,
                navController = navController
            )

        }

        context.getString(R.string.main_list_mother) -> {
            themeColor.apply {
                put("mainColor", ColorUserMainListMothers)
                put("placeColor", ColorUserMainPlaceMothers)
            }
            val labelList = labelListRaw.map {
                "Annenizin " + it
            }
            DailyInfoBakeScreen(
                forWho = args.forDay,
                color = themeColor,
                source = args.source,
                labelList = labelList,
                viewModel = viewModel,
                giftInformationScreenState = giftInformationScreenState,
                navController = navController
            )
        }

        context.getString(R.string.main_list_lover) -> {
            themeColor.apply {
                put("mainColor", ColorUserMainListLovers)
                put("placeColor", ColorUserMainPlaceLovers)
            }
            val labelList = labelListRaw.map {
                "Sevgilinizin " + it
            }
            DailyInfoBakeScreen(
                forWho = args.forDay,
                color = themeColor,
                source = args.source,
                labelList = labelList,
                viewModel = viewModel,
                giftInformationScreenState = giftInformationScreenState,
                navController = navController
            )
        }

        context.getString(R.string.main_list_women) -> {
            themeColor.apply {
                put("mainColor", ColorUserMainListWomen)
                put("placeColor", ColorUserMainPlaceWomen)
            }
            val labelList = labelListRaw.map {
                "Kadının " + it
            }
            DailyInfoBakeScreen(
                forWho = args.forDay,
                color = themeColor,
                source = args.source,
                labelList = labelList,
                viewModel = viewModel,
                giftInformationScreenState = giftInformationScreenState,
                navController = navController
            )
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyInfoBakeScreen(
    forWho: String,
    color: HashMap<String, Color>,
    source: Int,
    context: Context = LocalContext.current,
    labelList: List<String>,
    viewModel: AboutGiftInformationViewModel = hiltViewModel(),
    giftInformationScreenState: MutableState<AboutGiftInformationScreenObserver>,
    navController : NavController
) {

    var labelColor by remember {
        mutableStateOf(Color.Black)
    }

    val valuesState = remember {
        mutableStateOf(DailyPlacesObserver.Name)
    }


    val zodiacStatus = remember {
        mutableStateOf(ZodiacStatus.None)
    }

    val nameRequester = remember { FocusRequester() }
    val oldRequester = remember { FocusRequester() }
    val jobRequester = remember { FocusRequester() }
    val bestSideRequester = remember { FocusRequester() }
    val hobbiesRequester = remember { FocusRequester() }

    val nameState = remember {
        mutableStateOf("")
    }

    val oldState = remember {
        mutableStateOf("")
    }

    val zodiacState = remember {
        mutableStateOf("")
    }

    val hobbiesState = remember {
        mutableStateOf("")
    }

    val bestSideState = remember {
        mutableStateOf("")
    }

    val jobState = remember {
        mutableStateOf("")
    }

    val giftHistory = remember {
        mutableStateOf("")
    }

    var selectedOption = remember { mutableStateOf(RelationshipStatus.NONE) }

    var relationShipState: MutableState<String?> = remember {
        mutableStateOf(null)
    }

    when (selectedOption.value) {
        RelationshipStatus.FLORT -> relationShipState.value =
            stringResource(R.string.relationship_flirt)

        RelationshipStatus.MARRIED -> relationShipState.value =
            stringResource(R.string.relationship_married)

        RelationshipStatus.SweatHeart -> relationShipState.value =
            stringResource(R.string.relationship_sweatheart)

        RelationshipStatus.NONE -> null
    }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(insets = WindowInsets.ime), topBar = {
            TopAppBar(
                title = {
                    Text("")
                }, navigationIcon = {
                    Image(
                        modifier = Modifier
                            .padding(10.dp)
                            .clickable {
                                when (valuesState.value) {
                                    DailyPlacesObserver.Name -> {
                                        navController.popBackStack()
                                    }

                                    DailyPlacesObserver.OldName -> {
                                        valuesState.value = DailyPlacesObserver.Name
                                    }

                                    DailyPlacesObserver.Zodiac -> {
                                        valuesState.value = DailyPlacesObserver.OldName
                                    }

                                    DailyPlacesObserver.Job -> {
                                        valuesState.value = DailyPlacesObserver.Zodiac
                                    }

                                    DailyPlacesObserver.BestSide -> {
                                        valuesState.value = DailyPlacesObserver.Job
                                    }
                                    DailyPlacesObserver.Hobbies -> {
                                        valuesState.value = DailyPlacesObserver.BestSide
                                    }
                                }
                            },
                        painter = painterResource(R.drawable.back_icon),
                        contentDescription = "back button"
                    )
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = color.getValue("placeColor")
                )
            )
        }) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)
                .background(color = color.getValue("mainColor"))
        ) {

            LottieAnim(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(55.dp),
                source = R.raw.main_anim,
                contentScale = ContentScale.FillWidth,
                secondModifier = Modifier.graphicsLayer(scaleY = 0.3f)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                item {
                    LottieAnim(
                        source = source,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.size(150.dp)
                    )
                }

                item {
                    when (valuesState.value) {
                        DailyPlacesObserver.Name -> {
                            PersonInfoScreen(
                                state = nameState,
                                modifier = Modifier.height(screenHeight * 0.2f),
                                color = labelColor,
                                focusRequester = nameRequester,
                                label = labelList[0],
                                keyboard = KeyboardOptions(
                                    keyboardType = KeyboardType.Unspecified
                                )
                            )
                            if (color.getValue("mainColor") == ColorUserMainListLovers && valuesState.value == DailyPlacesObserver.Name) {
                                ReleationshipStateInfo(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    selectedOption = selectedOption
                                )
                            }
                            Spacer(Modifier.height(25.dp))
                        }

                        DailyPlacesObserver.OldName -> {
                            PersonInfoScreen(
                                state = oldState,
                                modifier = Modifier.height(screenHeight * 0.2f),
                                color = labelColor,
                                label = labelList[1],
                                focusRequester = oldRequester,
                                keyboard = KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                )
                            )
                            LaunchedEffect(valuesState.value) {
                                oldRequester.requestFocus()
                            }
                        }

                        DailyPlacesObserver.Zodiac -> {
                            Text(labelList[2], color = labelColor)
                            ZodiacInfoScreen(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(screenHeight * 0.9f),
                                zodiacStatus = zodiacStatus
                            )
                        }

                        DailyPlacesObserver.Job -> {
                            PersonInfoScreen(
                                state = jobState,
                                modifier = Modifier.height(screenHeight * 0.2f),
                                color = labelColor,
                                label = labelList[3],
                                focusRequester = jobRequester,
                                keyboard = KeyboardOptions(
                                    keyboardType = KeyboardType.Unspecified
                                )
                            )
                            LaunchedEffect(valuesState.value) {
                                jobRequester.requestFocus()
                            }
                        }

                        DailyPlacesObserver.BestSide -> {
                            PersonInfoScreen(
                                state = bestSideState,
                                modifier = Modifier.height(screenHeight * 0.2f),
                                color = labelColor,
                                label = labelList[4],
                                focusRequester = bestSideRequester,
                                keyboard = KeyboardOptions(
                                    keyboardType = KeyboardType.Unspecified
                                )
                            )
                            LaunchedEffect(valuesState.value) {
                                bestSideRequester.requestFocus()
                            }
                        }

                        DailyPlacesObserver.Hobbies -> {
                            PersonInfoScreen(
                                state = hobbiesState,
                                modifier = Modifier.height(screenHeight * 0.2f),
                                color = labelColor,
                                label = labelList[5],
                                focusRequester = hobbiesRequester,
                                keyboard = KeyboardOptions(
                                    keyboardType = KeyboardType.Unspecified
                                )
                            )
                            LaunchedEffect(valuesState.value) {
                                hobbiesRequester.requestFocus()
                            }
                        }
                    }
                }



                item {
                    BorderButton(
                        text = context.getString(R.string.next_text), onClick = {
                            when (valuesState.value) {
                                DailyPlacesObserver.Name -> {
                                    if (nameState.value.isEmpty()) {
                                        labelColor = Color.Red
                                    } else {
                                        valuesState.value = DailyPlacesObserver.OldName
                                        labelColor = Color.Black
                                    }
                                }

                                DailyPlacesObserver.OldName -> {
                                    if (oldState.value.isEmpty()) {
                                        labelColor = Color.Red
                                    } else {
                                        valuesState.value = DailyPlacesObserver.Zodiac
                                        labelColor = Color.Black
                                    }
                                }

                                DailyPlacesObserver.Zodiac -> {
                                    if (zodiacStatus.value == ZodiacStatus.None) {
                                        labelColor = Color.Red
                                    } else {
                                        labelColor = Color.Black
                                        valuesState.value = DailyPlacesObserver.Job
                                    }
                                }

                                DailyPlacesObserver.Job -> {
                                    if (jobState.value.isEmpty()) {
                                        labelColor = Color.Red
                                    } else {
                                        valuesState.value = DailyPlacesObserver.BestSide
                                        labelColor = Color.Black
                                    }
                                }

                                DailyPlacesObserver.BestSide -> {
                                    if (bestSideState.value.isEmpty()) {
                                        labelColor = Color.Red
                                    } else {
                                        valuesState.value = DailyPlacesObserver.Hobbies
                                        labelColor = Color.Black
                                    }
                                }

                                DailyPlacesObserver.Hobbies -> {
                                    if (hobbiesState.value.isEmpty()) {
                                        labelColor = Color.Red
                                    } else {
                                        labelColor = Color.Black
                                        when (zodiacStatus.value) {
                                            ZodiacStatus.Libra -> zodiacState.value =
                                                context.getString(R.string.zodiac_name_libra)

                                            ZodiacStatus.Virgo -> zodiacState.value =
                                                context.getString(R.string.zodiac_name_virgo)

                                            ZodiacStatus.Scorpio -> zodiacState.value =
                                                context.getString(R.string.zodiac_name_scorpio)

                                            ZodiacStatus.Sagittarius -> zodiacState.value =
                                                context.getString(R.string.zodiac_name_sagittarius)

                                            ZodiacStatus.Capricorn -> zodiacState.value =
                                                context.getString(R.string.zodiac_name_capricon)

                                            ZodiacStatus.Aquarius -> zodiacState.value =
                                                context.getString(R.string.zodiac_name_aquarius)

                                            ZodiacStatus.Pisces -> zodiacState.value =
                                                context.getString(R.string.zodiac_name_pisces)

                                            ZodiacStatus.Aries -> zodiacState.value =
                                                context.getString(R.string.zodiac_name_aries)

                                            ZodiacStatus.Taurus -> zodiacState.value =
                                                context.getString(R.string.zodiac_name_taurus)

                                            ZodiacStatus.Gemini -> zodiacState.value =
                                                context.getString(R.string.zodiac_name_gemini)

                                            ZodiacStatus.Cancer -> zodiacState.value =
                                                context.getString(R.string.zodiac_name_cancer)

                                            ZodiacStatus.Leo -> zodiacState.value =
                                                context.getString(R.string.zodiac_name_leo)

                                            ZodiacStatus.None -> zodiacState.value = ""
                                        }

                                        viewModel.savePersonInformation(
                                            ForPersonInformation(
                                                forWho = forWho,
                                                name = nameState.value,
                                                old = oldState.value,
                                                zodiac = zodiacState.value,
                                                job = jobState.value,
                                                bestSide = bestSideState.value,
                                                hobbies = hobbiesState.value,
                                                lastGifts = giftHistory.value,
                                                relationship = relationShipState.value
                                            )
                                        )
                                        giftInformationScreenState.value =
                                            AboutGiftInformationScreenObserver.AboutGiftInformation


                                    }
                                }
                            }
                        }, modifier = Modifier
                    )
                }
            }
        }
    }
}

