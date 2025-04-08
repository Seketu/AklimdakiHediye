package com.example.aklimdakihediye.Views

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.aklimdakihediye.NavController.LocalNavController
import com.example.aklimdakihediye.ObserverClasses.AboutGiftInformationScreenObserver
import com.example.aklimdakihediye.ObserverClasses.DailyPlacesObserver
import com.example.aklimdakihediye.ObserverClasses.RelationshipStatus
import com.example.aklimdakihediye.R
import com.example.aklimdakihediye.ViewModels.AboutGiftInformationViewModel
import com.example.aklimdakihediye.Compose.BorderButton
import com.example.aklimdakihediye.Compose.LottieAnim
import com.example.aklimdakihediye.Compose.PersonInfoScreen
import com.example.aklimdakihediye.Compose.ZodiacButton
import com.example.aklimdakihediye.ObserverClasses.ZodiacStatus
import com.example.aklimdakihediye.models.GiftInformationModels.ForPersonInformation
import com.example.aklimdakihediye.ui.theme.ColorUserMainListFathers
import com.example.aklimdakihediye.ui.theme.ColorUserMainListLovers
import com.example.aklimdakihediye.ui.theme.ColorUserMainListMothers
import com.example.aklimdakihediye.ui.theme.ColorUserMainListWomen
import com.example.aklimdakihediye.ui.theme.ColorUserMainPlaceFathers
import com.example.aklimdakihediye.ui.theme.ColorUserMainPlaceLovers
import com.example.aklimdakihediye.ui.theme.ColorUserMainPlaceMothers
import com.example.aklimdakihediye.ui.theme.ColorUserMainPlaceWomen

class DailyInfoView {

    @Composable
    fun AboutGiftInformation(
        modifier: Modifier = Modifier,
        navController: NavHostController,
        args: LocalNavController.DailyInfoScreen,
        viewModel: AboutGiftInformationViewModel = hiltViewModel()
    ) {

        if (args.forAnother == false) {
            viewModel.takeInformationFromLocal()
        }

        val giftInformationScreenState = remember {
            mutableStateOf(AboutGiftInformationScreenObserver.AboutPersonInformation)
        }

        when (giftInformationScreenState.value) {
            AboutGiftInformationScreenObserver.AboutPersonInformation -> {
                DailyInfoScreen(
                    navController = navController,
                    args = args,
                    viewModel = viewModel,
                    giftInformationScreenState = giftInformationScreenState
                )
            }

            AboutGiftInformationScreenObserver.AboutGiftInformation -> {
                InformationGiftScreen(
                    navController = navController,
                    viewModel = viewModel,
                    giftInformationScreenState = giftInformationScreenState
                )
            }
        }

    }

}
