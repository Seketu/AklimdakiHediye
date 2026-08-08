package com.reylortechnology.aklimdakihediye.Views.MainScreens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.reylortechnology.aklimdakihediye.AdMob.InterstitialAdScreen
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.Peoples
import com.reylortechnology.aklimdakihediye.models.Enums.Genders
import com.reylortechnology.aklimdakihediye.models.Enums.TypeRelationship
import com.reylortechnology.aklimdakihediye.ObserverClasses.ScreenStateObservers.MainSearchGiftScreensObserver
import com.reylortechnology.aklimdakihediye.ObserverClasses.ScreenStateObservers.MainSearchGiftScreensObserver.*
import com.reylortechnology.aklimdakihediye.ObserverClasses.ScreenStateObservers.PeopleInformationScreen
import com.reylortechnology.aklimdakihediye.ObserverClasses.ZodiacStatus
import com.reylortechnology.aklimdakihediye.R
import com.reylortechnology.aklimdakihediye.ViewModels.MainSearchGiftViewModel
import com.reylortechnology.aklimdakihediye.Views.PartScreens.MainSearchGiftScreens.TakePersonCharacterTraitsScreen
import com.reylortechnology.aklimdakihediye.Views.PartScreens.MainSearchGiftScreens.TakePersonHobbiesScreen
import com.reylortechnology.aklimdakihediye.Views.PartScreens.MainSearchGiftScreens.TakePersonInformationScreen
import com.reylortechnology.aklimdakihediye.Views.PartScreens.ToFriendsGiftView.ForGiftMentionSearchScreen
import com.reylortechnology.aklimdakihediye.Views.PartScreens.ToFriendsGiftView.ForReasonScreen
import com.reylortechnology.aklimdakihediye.models.Enums.CharacterTrait
import com.reylortechnology.aklimdakihediye.models.Enums.Hobbies
import androidx.compose.runtime.getValue
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.SavedGifts
import com.reylortechnology.aklimdakihediye.ObserverClasses.QueryProductsStatus
import com.reylortechnology.aklimdakihediye.ObserverClasses.SearchResultStatus
import com.reylortechnology.aklimdakihediye.models.GiftInformationModels.GiftInformation
import com.reylortechnology.aklimdakihediye.models.GiftInformationModels.Products
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreenGiftView(
    navController: NavHostController,
    viewModel : MainSearchGiftViewModel = hiltViewModel()
    ) {
    val geminiQueryState by viewModel.geminiQueryState.collectAsState()
    val searchResultStatus by viewModel.searchResultStatus.collectAsState()

    MainScreenGiftContent(
        navController = navController,
        onSavePeople = viewModel::savePeople,
        onInitPeopleInformation = viewModel::initPeopleInformation,
        onSetGiftInformation = viewModel::setGiftInformation,
        onSaveGift = viewModel.saveGift(),
        geminiQueryState = geminiQueryState,
        searchResultStatus = searchResultStatus,
        onTakeSearchResultStatus = viewModel.takeSearchResultStatus(),
        toastEvent = viewModel.toastEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreenGiftContent(
    navController: NavHostController,
    onSavePeople: (Peoples) -> Unit,
    onInitPeopleInformation: (Peoples) -> Unit,
    onSetGiftInformation: (GiftInformation) -> Unit,
    onSaveGift: (SavedGifts) -> Unit,
    geminiQueryState: QueryProductsStatus,
    searchResultStatus: SearchResultStatus,
    onTakeSearchResultStatus: (List<Products>) -> Unit,
    toastEvent: Flow<String>? = null
) {

    val screenObserver = remember {
        mutableStateOf<MainSearchGiftScreensObserver>(
            PeopleInformation(
                PeopleInformationScreen.TakePersonInformation
            )
        )
    }

    val showAd = remember {
        mutableStateOf(true)
    }
    val context = LocalContext.current

    // TakePersonInformationScreen için state değişkenleri
    val peopleName = remember { mutableStateOf("") }
    val typeOfRelationship = remember { mutableStateOf<TypeRelationship?>(null) }
    val selectedGender = remember { mutableStateOf<Genders?>(null) }
    val today = LocalDate.now()
    val selectedDay = remember { mutableIntStateOf(1) }
    val selectedMonth = remember { mutableIntStateOf(1) }
    val selectedYear = remember { mutableIntStateOf(today.year) }
    val peopleJob  = remember { mutableStateOf("") }
    val peopleHobbies = remember { mutableStateListOf<Hobbies>() }
    val characterTraits = remember { mutableStateListOf<CharacterTrait>() }
    val characterBestSide = remember { mutableStateOf("") }
    val zodiacStatus = remember { mutableStateOf(ZodiacStatus.None) }
    val calculatedAge = remember {
        mutableIntStateOf(0)
    }
    val selectedImageId = remember {
        mutableIntStateOf(R.drawable.man_1)
    }
    val selectedColor = remember {
        mutableStateOf<Color>(Color.Transparent)
    }

    val onBackIcon = remember {
        mutableStateOf<ImageVector>(Icons.Default.Home)
    }
    val onBackAction = remember {
        mutableStateOf<()-> Unit>({
            navController.popBackStack()
        })
    }

    val popBarTitle = remember {
        mutableStateOf("")
    }

    // TopAppBar state'lerini screen'e göre güncelle
    when(screenObserver.value){
        is PeopleInformation -> {
            when((screenObserver.value as PeopleInformation).screen){
                PeopleInformationScreen.TakePersonInformation -> {
                    // Ana ekran - ana sayfaya dön
                    onBackIcon.value = Icons.Default.Home
                    onBackAction.value = {
                        navController.popBackStack()
                    }
                    // Kullanıcı ad girdiğinde title güncelle
                    popBarTitle.value = if (peopleName.value.isNotBlank()) {
                        stringResource(R.string.gift_for_template, peopleName.value)
                    } else {
                        stringResource(R.string.search_gift)
                    }
                }
                PeopleInformationScreen.TakePersonHobbies -> {
                    // Önceki step'e dön
                    onBackIcon.value = Icons.AutoMirrored.Filled.ArrowBack
                    onBackAction.value = {
                        screenObserver.value = PeopleInformation(
                            PeopleInformationScreen.TakePersonInformation
                        )
                    }
                    popBarTitle.value = if (peopleName.value.isNotBlank()) {
                        stringResource(R.string.hobbies_for_template, peopleName.value)
                    } else {
                        stringResource(R.string.hobbies_label)
                    }
                }
                PeopleInformationScreen.TakeCharacterTraits -> {
                    // Önceki step'e dön
                    onBackIcon.value = Icons.AutoMirrored.Filled.ArrowBack
                    onBackAction.value = {
                        screenObserver.value = PeopleInformation(
                            PeopleInformationScreen.TakePersonHobbies
                        )
                    }
                    popBarTitle.value = if (peopleName.value.isNotBlank()) {
                        stringResource(R.string.character_for_template, peopleName.value)
                    } else {
                        stringResource(R.string.label_people_character)
                    }
                }
            }
        }
        GiftInformation -> {
            // Önceki step'e dön
            onBackIcon.value = Icons.AutoMirrored.Filled.ArrowBack
            onBackAction.value = {
                screenObserver.value = PeopleInformation(
                    PeopleInformationScreen.TakeCharacterTraits
                )
            }
            popBarTitle.value = if (peopleName.value.isNotBlank()) {
                stringResource(R.string.character_for_template, peopleName.value) + " - " + stringResource(R.string.gift_criteria)
            } else {
                stringResource(R.string.gift_criteria)
            }
        }
        GiftSearchResult -> {
            // Ana sayfaya dön
            onBackIcon.value = Icons.Default.Home
            onBackAction.value = {
                navController.popBackStack()
            }
            popBarTitle.value = if (peopleName.value.isNotBlank()) {
                stringResource(R.string.gift_for_template, peopleName.value)
            } else {
                stringResource(R.string.gift_suggestions)
            }
        }
    }
    // Toast messages için LaunchedEffect
    LaunchedEffect(Unit) {
        toastEvent?.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        popBarTitle.value,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                    titleContentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.inverseOnSurface,
                ),
                navigationIcon = {
                    IconButton(
                        onClick = onBackAction.value
                    ) {
                        Icon(
                            imageVector = onBackIcon.value,
                            contentDescription = stringResource(R.string.content_description_back),
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        AnimatedContent(
            targetState = screenObserver.value,
            transitionSpec = {
                slideInHorizontally(
                    animationSpec = tween(300),
                    initialOffsetX = { fullWidth -> fullWidth }
                ) togetherWith slideOutHorizontally(
                    animationSpec = tween(300),
                    targetOffsetX = { fullWidth -> -fullWidth }
                )
            },
            label = stringResource(R.string.animation_label_main_search)
        ) {currentState ->

            when(currentState){
                is PeopleInformation -> {
                    when(currentState.screen){
                        PeopleInformationScreen.TakePersonInformation -> {
                            TakePersonInformationScreen(
                                modifier = Modifier.padding(paddingValues),
                                peopleName = peopleName,
                                typeOfRelationship = typeOfRelationship,
                                selectedGender = selectedGender,
                                selectedDay = selectedDay,
                                selectedMonth = selectedMonth,
                                selectedYear = selectedYear,
                                selectedImageId = selectedImageId,
                                selectedColor = selectedColor,
                                onNext = {age->
                                    calculatedAge.intValue = age
                                    screenObserver.value = PeopleInformation(
                                        PeopleInformationScreen.TakePersonHobbies
                                    )
                                }
                            )
                        }
                        PeopleInformationScreen.TakePersonHobbies -> {
                            TakePersonHobbiesScreen(
                                modifier = Modifier
                                    .padding(paddingValues)
                                    .fillMaxSize(),
                                peopleName = peopleName.value,
                                peopleJob = peopleJob,
                                peopleHobbies = peopleHobbies,
                                onNext = {
                                    screenObserver.value = PeopleInformation(
                                        screen = PeopleInformationScreen.TakeCharacterTraits
                                     )
                                }
                            )
                        }

                        PeopleInformationScreen.TakeCharacterTraits -> {
                            TakePersonCharacterTraitsScreen(
                                bestSideState = characterBestSide,
                                characterTraits = characterTraits,
                                modifier = Modifier
                                    .padding(paddingValues)
                                    .fillMaxSize(),
                                zodiacStatus = zodiacStatus,
                                peopleName = peopleName.value
                            ){isSave ->
                                val people = Peoples(
                                    peopleName = peopleName.value,
                                    relationship = typeOfRelationship.value!!,
                                    birthday =  LocalDate.of(selectedYear.intValue, selectedMonth.intValue, selectedDay.intValue),
                                    age = calculatedAge.intValue,
                                    zodiac = zodiacStatus.value,
                                    hobbies = peopleHobbies,
                                    bestSide = characterBestSide.value,
                                    character = characterTraits,
                                    job = peopleJob.value,
                                    image = selectedImageId.intValue,
                                    color = selectedColor.value,
                                    gender = selectedGender.value!!,
                                )
                                // Burada isSave true ise bilgileri kaydet, false ise kaydetme ve direkt olarak arama ekranına geç
                                if (isSave){
                                    onSavePeople(people)
                                }else{
                                    onInitPeopleInformation(people)
                                }
                                screenObserver.value = GiftInformation
                            }
                        }
                    }
                }

                GiftInformation -> {
                    ForReasonScreen(
                        context = context,
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize(),
                        onActionButton = { giftInformation ->
                            onSetGiftInformation(giftInformation)
                            screenObserver.value = GiftSearchResult
                        }
                    )
                }

                GiftSearchResult -> {
                    if (showAd.value){
                        InterstitialAdScreen {
                            showAd.value = false
                        }
                    }else{
                        ForGiftMentionSearchScreen(
                            modifier = Modifier
                                .padding(paddingValues)
                                .fillMaxSize(),
                            context = context,
                            saveGifts = onSaveGift,
                            navController = navController,
                            geminiQueryState = geminiQueryState,
                            searchResultStatus = searchResultStatus,
                            takeSearchResultStatus = onTakeSearchResultStatus,
                        )
                    }

                }
            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun MainSearchGiftViewPrev() {
    MainScreenGiftContent(
        navController = rememberNavController(),
        onSavePeople = {},
        onInitPeopleInformation = {},
        onSetGiftInformation = {},
        onSaveGift = {},
        geminiQueryState = QueryProductsStatus.None,
        searchResultStatus = SearchResultStatus.None,
        onTakeSearchResultStatus = {}
    )
}
