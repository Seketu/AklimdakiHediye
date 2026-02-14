package com.reylortechnology.aklimdakihediye.Views.MainScreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.reylortechnology.aklimdakihediye.AdMob.InterstitialAdScreen
import com.reylortechnology.aklimdakihediye.ObserverClasses.ScreenStateObservers.ToFriendGiftViewObserver
import com.reylortechnology.aklimdakihediye.R
import com.reylortechnology.aklimdakihediye.ViewModels.ToFriendsGiftViewModel
import com.reylortechnology.aklimdakihediye.Views.PartScreens.ToFriendsGiftView.ForReasonScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToFriendGiftView(
    peopleId : Int,
    navController: NavController,
    viewModel: ToFriendsGiftViewModel = hiltViewModel()
) {


    val screenConf = LocalConfiguration.current
    val screenHeight = screenConf.screenHeightDp.dp
    val viewerState = remember {
        mutableStateOf<ToFriendGiftViewObserver>(ToFriendGiftViewObserver.ForReasonScreen)
    }
    val context = LocalContext.current
    val showAd = remember {
        mutableStateOf(false)
    }
    BackHandler {
        when (viewerState.value){
            ToFriendGiftViewObserver.ForReasonScreen -> {
                navController.popBackStack()
            }
            ToFriendGiftViewObserver.ShowSearchResults -> {
                viewerState.value = ToFriendGiftViewObserver.ForReasonScreen
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = { CenterAlignedTopAppBar(
            title = { Text(
                text = stringResource(R.string.app_name),
            ) },
            modifier = Modifier
                .clip(
                    shape = RoundedCornerShape(
                        bottomEnd = 16.dp,
                        bottomStart = 16.dp
                    )
                ),
            navigationIcon = {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.back_unbox_icon),
                        "back Button",
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
            ),
            expandedHeight = screenHeight * 0.1f,
        ) }
    ) {
        when(
            viewerState.value
        ){
            ToFriendGiftViewObserver.ForReasonScreen -> {
                ForReasonScreen(modifier = Modifier
                    .padding(it),
                    onActionButton = {
                        viewModel.saveGiftInformation(it)
                    },
                    context = context
                )
            }
            ToFriendGiftViewObserver.ShowSearchResults -> {
                if (showAd.value){
                    InterstitialAdScreen {
                        showAd.value = true
                    }
                }else{

                }
            }
        }
    }
}

@Preview
@Composable
private fun ToFriendPreview() {
    ToFriendGiftView(1, rememberNavController())
}