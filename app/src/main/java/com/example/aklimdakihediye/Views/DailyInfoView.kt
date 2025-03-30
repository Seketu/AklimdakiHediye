package com.example.aklimdakihediye.Views

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aklimdakihediye.NavController.LocalNavController
import com.example.aklimdakihediye.ObserverClasses.RelationshipStatus
import com.example.aklimdakihediye.R
import com.example.aklimdakihediye.compose.BorderButton
import com.example.aklimdakihediye.compose.DailyInfoPlace
import com.example.aklimdakihediye.compose.DailyUserInfoFieldsHolder
import com.example.aklimdakihediye.compose.LottieAnim
import com.example.aklimdakihediye.ui.theme.ColorDailyUserInfoTopBarr
import com.example.aklimdakihediye.ui.theme.ColorUserMainListFathers
import com.example.aklimdakihediye.ui.theme.ColorUserMainListLovers
import com.example.aklimdakihediye.ui.theme.ColorUserMainListMothers
import com.example.aklimdakihediye.ui.theme.ColorUserMainListWomen
import com.example.aklimdakihediye.ui.theme.ColorUserMainPlaceFathers
import com.example.aklimdakihediye.ui.theme.ColorUserMainPlaceLovers
import com.example.aklimdakihediye.ui.theme.ColorUserMainPlaceMothers
import com.example.aklimdakihediye.ui.theme.ColorUserMainPlaceWomen

class DailyInfoView {

    var themeColor = HashMap<String,Color>()

    @Composable
    fun DailyInfoScreen(
        modifier: Modifier = Modifier,
        navController: NavController,
        args: LocalNavController.DailyInfoScreen
    ) {

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

        when(args.forDay) {
            context.getString(R.string.main_list_father) -> {
                themeColor.apply {
                    put("mainColor", ColorUserMainListFathers)
                    put("placeColor", ColorUserMainPlaceFathers)
                }
                val labelList = labelListRaw.map {
                    "Babanızın " + it
                }
                Screen(color = themeColor,source = args.source,labelList = labelList)

            }
            context.getString(R.string.main_list_mother) -> {
                themeColor.apply {
                    put("mainColor", ColorUserMainListMothers)
                    put("placeColor", ColorUserMainPlaceMothers)
                }
                val labelList = labelListRaw.map {
                    "Annenizin " + it
                }
                Screen(color = themeColor, source = args.source, labelList = labelList)
            }
            context.getString(R.string.main_list_lover) -> {
                themeColor.apply {
                    put("mainColor", ColorUserMainListLovers)
                    put("placeColor", ColorUserMainPlaceLovers)
                }
                val labelList = labelListRaw.map {
                    "Sevgilinizin " + it
                }
                Screen(color = themeColor, source = args.source, labelList = labelList)
            }
            context.getString(R.string.main_list_women) -> {
                themeColor.apply {
                    put("mainColor", ColorUserMainListWomen)
                    put("placeColor", ColorUserMainPlaceWomen)
                }
                val labelList = labelListRaw.map {
                    "Kadının " + it
                }
                Screen(color = themeColor, source = args.source, labelList = labelList)
            }

        }

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Screen(
        modifier: Modifier = Modifier,
        color: HashMap<String, Color>,
        source: Int,
        context: Context = LocalContext.current,
        labelList: List<String>
    ) {

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

        val giftHistory = remember{
            mutableStateOf("")
        }

        var selectedOption by remember { mutableStateOf(RelationshipStatus.NONE) }


        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp.dp

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(insets = WindowInsets.ime),
            topBar = {
                TopAppBar(
                    title = {
                        Text("")
                    },
                    navigationIcon = {
                        Image(
                            modifier = Modifier
                                .padding(10.dp),
                            painter = painterResource(R.drawable.back_icon),
                            contentDescription =  "back button",
                            )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = color.getValue("placeColor")
                    )
                )
            }
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(it)
                    .background(color = color.getValue("mainColor"))
            ) {
                LazyColumn (
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceAround
                ){
                    item {
                        LottieAnim(
                            source = source,
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .size(250.dp)
                        )
                    }

                    item {
                        DailyInfoPlace(
                            modifier = Modifier
                                .fillMaxWidth(0.95f)
                                .background(color.getValue("placeColor"), shape = RoundedCornerShape(15.dp)),
                            labels = labelList.take(n = 3),
                            state = listOf(nameState,oldState,zodiacState)
                        )
                        Spacer(Modifier.height(25.dp))
                    }

                    item {
                        DailyInfoPlace(
                            modifier = Modifier
                                .fillMaxWidth(0.95f)
                                .background(color.getValue("placeColor"), shape = RoundedCornerShape(15.dp)),
                            labels = labelList.subList(3,7),
                            state = listOf(jobState,bestSideState,hobbiesState,giftHistory)
                        )
                        Spacer(Modifier.height(25.dp))
                    }

                    item {
                        if (color.getValue("mainColor") == ColorUserMainListLovers) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceAround
                            ) {
                                Text("İlişki Durumu")

                            Row(
                                modifier = Modifier
                                    .height(IntrinsicSize.Max)
                                    .background(Color.LightGray, shape = RoundedCornerShape(25.dp))
                                    .fillMaxWidth(0.7f)
                                    .padding(15.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clickable(
                                            onClick = {
                                                selectedOption = RelationshipStatus.MARRIED
                                            }
                                        )
                                ) {
                                    Checkbox(
                                        checked = selectedOption == RelationshipStatus.MARRIED,
                                        enabled = true,
                                        onCheckedChange = {
                                            if (it) selectedOption =
                                                RelationshipStatus.MARRIED else selectedOption =
                                                RelationshipStatus.NONE
                                        }
                                    )
                                    Text("Evli")
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clickable(
                                            onClick = {
                                                selectedOption = RelationshipStatus.SINGLE
                                            }
                                        )
                                ) {

                                    Checkbox(
                                        checked = selectedOption == RelationshipStatus.SINGLE,
                                        enabled = true,
                                        onCheckedChange = {
                                            if (it) selectedOption =
                                                RelationshipStatus.SINGLE else RelationshipStatus.NONE
                                        }
                                    )
                                    Text("Sevgili")
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clickable(
                                            onClick = {
                                                selectedOption = RelationshipStatus.FLORT
                                            }
                                        )
                                ) {
                                    Checkbox(
                                        checked = selectedOption == RelationshipStatus.FLORT,
                                        enabled = true,
                                        onCheckedChange = {
                                            if (it) selectedOption =
                                                RelationshipStatus.FLORT else RelationshipStatus.NONE
                                        }
                                    )
                                    Text("Flört")
                                }
                            }
                        }
                        }
                        Spacer(Modifier.height(25.dp))
                    }

                    item{
                        BorderButton(
                            text =  context.getString(R.string.next_text),
                            onClick = {

                            },
                            modifier = Modifier
                        )
                    }
                }
            }
        }
    }
}

