package com.example.aklimdakihediye.Views

import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.aklimdakihediye.NavController.LocalNavController
import com.example.aklimdakihediye.R
import com.example.aklimdakihediye.ViewModels.UserMainViewModel
import com.example.aklimdakihediye.compose.LottieAnim
import com.example.aklimdakihediye.compose.MainListDailyButton
import com.example.aklimdakihediye.compose.MainListRowButton
import com.example.aklimdakihediye.compose.NavigationButton
import com.example.aklimdakihediye.models.ComposeModels.UserMainListItems
import com.example.aklimdakihediye.models.ComposeModels.UserMainRowListItems
import com.example.aklimdakihediye.ui.theme.ColorUserMainBc
import com.example.aklimdakihediye.ui.theme.ColorUserMainListFathers
import com.example.aklimdakihediye.ui.theme.ColorUserMainListLovers
import com.example.aklimdakihediye.ui.theme.ColorUserMainListMothers
import com.example.aklimdakihediye.ui.theme.ColorUserMainListWomen

class UserMainView {

    @Composable
    fun UserMainScreen(
        navController : NavController,
        viewModel : UserMainViewModel = viewModel()
    ) {
        val context = LocalContext.current
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

        when(isLoading.value){
            true ->{
                Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Yükleniyor...", color = Color.Black, fontSize = 20.sp)
                    Spacer(Modifier.height(50.dp))
                    CircularProgressIndicator()
                }
            }}
            false -> SuccesLoading(navController)
        }
    }
}

@Composable
fun SuccesLoading(navController: NavController) {

    val context = LocalContext.current

    val rowList = listOf(
        UserMainRowListItems(
            R.drawable.love_row,
            context.getString(R.string.main_row_love)
        ),
        UserMainRowListItems(
            R.drawable.friend_row,
            context.getString(R.string.main_row_friend)
        ),
        UserMainRowListItems(
            R.drawable.teacher_row,
            context.getString(R.string.main_row_teacher)
        ),
        UserMainRowListItems(
            R.drawable.job_row,
            context.getString(R.string.main_row_job)
        )
    )

    val buttonList = listOf<UserMainListItems>(
        UserMainListItems(
            context.getString(R.string.main_list_father),
            R.raw.fathers_anim,
            ColorUserMainListFathers,
            context.getString(R.string.main_list_father_card)
        ),
        UserMainListItems(
            context.getString(R.string.main_list_mother),
            R.raw.mother,
            ColorUserMainListMothers,
            context.getString(R.string.main_list_mother_card)
        ),
        UserMainListItems(
            context.getString(R.string.main_list_lover),
            R.raw.lover_anim,
            ColorUserMainListLovers,
            context.getString(R.string.main_list_lover_card)
        ),
        UserMainListItems(
            context.getString(R.string.main_list_women),
            R.raw.woman_anim,
            ColorUserMainListWomen,
            context.getString(R.string.main_list_women_card)
        )
    )
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
        ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .background(ColorUserMainBc),
        ){
            Column(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.2f)
                    .padding(top = 10.dp)
                    .paint(
                        painter = painterResource(R.drawable.main_up_row),
                        contentScale = ContentScale.FillBounds
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    "Kim bu şanslı ",
                    color = Color.White
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight(0.8f)
                ) {
                    items(rowList) {row ->
                        Spacer(Modifier.width(20.dp))
                        MainListRowButton(
                            modifier = Modifier
                                .size(90.dp),
                            source = row.source,
                            text = row.text
                        )
                    }
                }
            }


            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ){
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
                        items(buttonList) {button ->
                            Spacer(Modifier.height(50.dp))
                            MainListDailyButton(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(button.color, shape = RoundedCornerShape(15.dp))
                                    .padding(5.dp)
                                    .clickable {
                                        navController.navigate(LocalNavController.DailyInfoUserScreen(
                                            button.text,
                                            button.source
                                        ))
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
                            .paint(painter = painterResource(R.drawable.down_navigation),
                                contentScale = ContentScale.FillWidth)
                            .padding(start = 20.dp, end = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                            NavigationButton(
                                modifier = Modifier
                                    .padding(top = 15.dp)
                                    .size(56.dp),
                                R.drawable.friends
                            )
                            NavigationButton(
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(8.dp),
                                R.drawable.main_navigation
                            )
                            NavigationButton(
                                modifier = Modifier
                                    .padding(top = 15.dp)
                                    .size(56.dp),
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