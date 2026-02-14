package com.reylortechnology.aklimdakihediye.Views.PartScreens.AboutGıftInformationsScreen

import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.reylortechnology.aklimdakihediye.ObserverClasses.ScreenStateObservers.GiftScreenObserver
import com.reylortechnology.aklimdakihediye.R
import com.reylortechnology.aklimdakihediye.ViewModels.CelebretadDayViewModel
import com.reylortechnology.aklimdakihediye.Compose.GiftInfoMeaning
import com.reylortechnology.aklimdakihediye.Compose.GiftInfoScreenPrice
import com.reylortechnology.aklimdakihediye.Compose.LottieAnim
import com.reylortechnology.aklimdakihediye.Compose.StepperIndicator
import com.reylortechnology.aklimdakihediye.ObserverClasses.ScreenStateObservers.AboutGiftInformationScreenObserver
import com.reylortechnology.aklimdakihediye.ObserverClasses.GiftMeanStatus
import com.reylortechnology.aklimdakihediye.models.GiftInformationModels.ForGiftInformation
import com.reylortechnology.aklimdakihediye.ui.theme.secondaryLight


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationGiftScreen(
    navController: NavController,
    viewModel: CelebretadDayViewModel = hiltViewModel(),
    giftInformationScreenState: MutableState<AboutGiftInformationScreenObserver>
) {
    // We pass the ViewModel logic as a lambda to InformationGiftContent
    // This allows the Content composable to be previewed without needing a ViewModel instance.
    InformationGiftContent(
        navController = navController,
        giftInformationScreenState = giftInformationScreenState,
        onSaveGiftInformation = { viewModel.saveGiftInformation(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationGiftContent(
    navController: NavController,
    giftInformationScreenState: MutableState<AboutGiftInformationScreenObserver>,
    onSaveGiftInformation: (ForGiftInformation) -> Unit
) {

    val context = LocalContext.current

    val localConfig = LocalConfiguration.current
    val screenHeight = localConfig.screenHeightDp.dp

    val screenStateObserver: MutableState<GiftScreenObserver> = remember {
        mutableStateOf(GiftScreenObserver.Price)
    }

    val priceValueFirst = remember {
        mutableStateOf("")
    }

    val priceValueSecond = remember {
        mutableStateOf("")
    }

    val giftMention = remember {
        mutableStateOf("")
    }

    val labelColor = remember {
        mutableStateOf(Color.White)
    }

    val giftMean = remember { mutableStateOf<GiftMeanStatus>(GiftMeanStatus.none) }

    BackHandler {
        screenStateObserver.value.let {
            when (it) {
                GiftScreenObserver.Price -> giftInformationScreenState.value =
                    AboutGiftInformationScreenObserver.AboutPersonInformation

                GiftScreenObserver.Mention -> screenStateObserver.value =
                    GiftScreenObserver.Price
            }
        }
    }

    Scaffold(
        Modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = {
                        screenStateObserver.value.let {
                            when (it) {
                                GiftScreenObserver.Price -> giftInformationScreenState.value =
                                    AboutGiftInformationScreenObserver.AboutPersonInformation

                                GiftScreenObserver.Mention -> screenStateObserver.value =
                                    GiftScreenObserver.Price
                            }
                        }
                    }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.back_icon),
                            contentDescription = "back button",
                            modifier = Modifier, // Fixes potential overflow by constraining icon size
                            tint = Color.Unspecified // Keeps original icon colors
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )

        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .background(
                    MaterialTheme.colorScheme.surface
                )
                .fillMaxSize()
        ) {
            StepperIndicator(
                modifier = Modifier,
                currentStep = screenStateObserver.value.ordinal + 1, totalSteps = GiftScreenObserver.entries.size
            )
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
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding(),
                ) {
                    item {
                        when (screenStateObserver.value) {
                            GiftScreenObserver.Price -> {
                                GiftInfoScreenPrice(
                                    stateFirst = priceValueFirst,
                                    stateSecond = priceValueSecond,
                                    modifier = Modifier
                                        .height(screenHeight * 0.4f)
                                        .background(MaterialTheme.colorScheme.surface),
                                    label = stringResource(R.string.gift_price_place_label),
                                    labelColor = labelColor.value
                                )
                            }

                            GiftScreenObserver.Mention -> {
                                GiftInfoMeaning(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(screenHeight * 0.5f),
                                    buttonModifier = Modifier
                                        .height(screenHeight * 0.13f),
                                    labelColor = MaterialTheme.colorScheme.onSurface,
                                    label = stringResource(R.string.gift_screen_mention_label),
                                    checkedFirst = giftMean.value == GiftMeanStatus.sentimental,
                                    onClickFirst = {
                                        giftMean.value = if (giftMean.value == GiftMeanStatus.sentimental) GiftMeanStatus.none
                                        else GiftMeanStatus.sentimental
                                    },
                                    onCheckedChangeFirst = {
                                        giftMean.value = if (giftMean.value == GiftMeanStatus.sentimental) GiftMeanStatus.none
                                        else GiftMeanStatus.sentimental
                                    },
                                    imageFirst = R.drawable.gift_mean_sentimental,
                                    buttonTextFirst = stringResource(R.string.gift_mention_button_label_sentimental),
                                    checkedSecond = giftMean.value == GiftMeanStatus.dailyUser,
                                    onClickSecond = {
                                        giftMean.value = if (giftMean.value == GiftMeanStatus.dailyUser) GiftMeanStatus.none
                                        else GiftMeanStatus.dailyUser
                                    },
                                    onCheckedChangeSecond = {
                                        giftMean.value = if (giftMean.value == GiftMeanStatus.dailyUser) GiftMeanStatus.none
                                        else GiftMeanStatus.dailyUser
                                    },
                                    imageSecond = R.drawable.gift_mean_daily_user,
                                    buttonTextSecond = stringResource(R.string.gift_mention_button_label_daily_user),
                                )
                            }
                        }
                    }
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(screenHeight * 0.09f)
                        .background(secondaryLight)
                        .clickable {
                            when (screenStateObserver.value) {
                                GiftScreenObserver.Price -> {
                                    if (priceValueFirst.value.isEmpty() || priceValueSecond.value.isEmpty()) {
                                        labelColor.value = Color.Red
                                    } else {
                                        screenStateObserver.value = GiftScreenObserver.Mention
                                        labelColor.value = Color.Black
                                    }
                                }

                                GiftScreenObserver.Mention -> {
                                    when (giftMean.value) {
                                        GiftMeanStatus.none -> {
                                            labelColor.value = Color.Red
                                        }

                                        GiftMeanStatus.sentimental -> giftMention.value =
                                            "duygusal"

                                        GiftMeanStatus.dailyUser -> giftMention.value =
                                            "günlük kullanım"
                                    }
                                    if (giftMention.value.isEmpty()) {
                                        labelColor.value = Color.Red
                                    } else {
                                        onSaveGiftInformation(
                                            ForGiftInformation(
                                                minPrice = priceValueFirst.value,
                                                maxPrice = priceValueSecond.value,
                                                giftMean = giftMention.value,
                                            )
                                        )
                                        giftInformationScreenState.value =
                                            AboutGiftInformationScreenObserver.SearchResult
                                    }
                                }
                            }
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        if (screenStateObserver.value != GiftScreenObserver.Mention) stringResource(R.string.next_text)
                        else stringResource(R.string.done),
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


@Preview
@Composable
private fun InformationGift() {
    // Use InformationGiftContent instead of InformationGiftScreen in Preview
    // to avoid ViewModel instantiation issues.
    InformationGiftContent(
        navController = rememberNavController(),
        giftInformationScreenState = remember {
            mutableStateOf(AboutGiftInformationScreenObserver.AboutGiftInformation)
        },
        onSaveGiftInformation = {}
    )
}