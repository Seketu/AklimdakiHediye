package com.reylortechnology.aklimdakihediye.Views.MainScreens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.reylortechnology.aklimdakihediye.ObserverClasses.ScreenStateObservers.PeoplesViewObserver
import com.reylortechnology.aklimdakihediye.R
import com.reylortechnology.aklimdakihediye.ViewModels.PeoplesViewModel
import com.reylortechnology.aklimdakihediye.Views.PartScreens.PeoplesViewScreens.PeopleDetailScreen
import com.reylortechnology.aklimdakihediye.Views.PartScreens.PeoplesViewScreens.PeoplesMainScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeoplesView(
    navController: NavController,
    viewModel : PeoplesViewModel = hiltViewModel()
) {

    val peopleScreenObserver = remember {
        mutableStateOf<PeoplesViewObserver>(PeoplesViewObserver.MainScreen)
    }

    val peoples = viewModel.peoples.collectAsState(emptyList())

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.screen_titles_peoples),
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back Button"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                    titleContentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.inverseOnSurface
                ),
                modifier = Modifier.clip(
                    MaterialTheme.shapes.medium.copy(
                        topStart = androidx.compose.foundation.shape.ZeroCornerSize,
                        topEnd = androidx.compose.foundation.shape.ZeroCornerSize
                    )
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {

                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Person Button"
                )
            }
        }
    ) { paddingValues ->
        AnimatedContent(
            targetState = peopleScreenObserver.value,
            transitionSpec = {
                    fadeIn() togetherWith fadeOut()
            }
        ) {
            when (it) {
                is PeoplesViewObserver.MainScreen -> {
                    PeoplesMainScreen(
                        modifier = Modifier.padding(paddingValues),
                        peoplesData = peoples.value,
                        onEdit = {peoples ->
                            peopleScreenObserver.value = PeoplesViewObserver.PersonDetailScreen(peoples)
                        }
                    )
                }
                 is PeoplesViewObserver.PersonDetailScreen -> {
                    PeopleDetailScreen(
                        modifier = Modifier.padding(paddingValues),
                        peoples = it.people
                    )
                }
                is PeoplesViewObserver.AddPersonScreen -> {
                }
        }
    }
    }
}

@Preview
@Composable
private fun PeoplesPreview() {
    PeoplesView(rememberNavController())
}