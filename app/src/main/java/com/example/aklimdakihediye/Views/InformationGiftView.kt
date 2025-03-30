package com.example.aklimdakihediye.Views

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
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aklimdakihediye.ObserverClasses.GiftScreenObserver
import com.example.aklimdakihediye.ObserverClasses.InfoStepsStatus
import com.example.aklimdakihediye.R
import com.example.aklimdakihediye.compose.GiftInfo
import com.example.aklimdakihediye.compose.GiftInfoScreenPrice
import com.example.aklimdakihediye.compose.LottieAnim
import com.example.aklimdakihediye.compose.StepperIndicator
import com.example.aklimdakihediye.ui.theme.ColorDailyUserInfoTopBarr
import com.example.aklimdakihediye.ui.theme.ColorInformationGiftBc
import com.example.aklimdakihediye.ui.theme.ColorInformationGiftButton

class InformationGiftView {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun InformationGiftScreen(
        modifier: Modifier = Modifier,
        navController: NavController
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

        val giftClass = remember {
            mutableStateOf("")
        }

        Scaffold(
            Modifier
                .fillMaxSize()
                .imePadding(),
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
                    .padding(it)
                    .background(
                        ColorInformationGiftBc
                    )
                    .fillMaxSize()
            ) {
                StepperIndicator( modifier = Modifier,
                    currentStep =  screenStateObserver.value.ordinal + 1 , totalSteps = GiftScreenObserver.entries.size
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
                                            .height(screenHeight * 0.4f),
                                        label = context.getString(R.string.gift_price_place_label)
                                    )
                                }
                                GiftScreenObserver.Mention -> {
                                    GiftInfo(
                                        state = giftMention,
                                        modifier = Modifier
                                            .height(screenHeight * 0.4f),
                                        label = stringResource(R.string.gift_screen_mention_label)
                                    )
                                }
                                GiftScreenObserver.Class -> {
                                    GiftInfo(
                                        state = giftClass,
                                        modifier = Modifier
                                            .height(screenHeight * 0.4f),
                                        label = "Özel Bir Hediye Türünüz Var mı ? (Elbise, Ayakkabı vb)"
                                    )
                                }
                            }
                        }
                    }

                    Row (
                        Modifier
                            .fillMaxWidth()
                            .height(screenHeight * 0.09f)
                            .background(ColorInformationGiftButton)
                            .clickable {
                                when (screenStateObserver.value) {
                                    GiftScreenObserver.Price -> screenStateObserver.value =
                                        GiftScreenObserver.Mention

                                    GiftScreenObserver.Mention -> screenStateObserver.value =
                                        GiftScreenObserver.Class

                                    GiftScreenObserver.Class -> {

                                    }
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ){
                        Text(
                            if (screenStateObserver.value != GiftScreenObserver.Class)context.getString(R.string.next_text)
                            else context.getString(R.string.done),
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