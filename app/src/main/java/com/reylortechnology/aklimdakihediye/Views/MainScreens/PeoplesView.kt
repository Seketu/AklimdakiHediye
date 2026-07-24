package com.reylortechnology.aklimdakihediye.Views.MainScreens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.reylortechnology.aklimdakihediye.Compose.AddPeopleDialog
import com.reylortechnology.aklimdakihediye.Compose.ColorButton
import com.reylortechnology.aklimdakihediye.Compose.PeoplePhotoSelector
import com.reylortechnology.aklimdakihediye.ObserverClasses.DialogStateObserver
import com.reylortechnology.aklimdakihediye.ObserverClasses.ScreenStateObservers.PeoplesViewObserver
import com.reylortechnology.aklimdakihediye.R
import com.reylortechnology.aklimdakihediye.ViewModels.PeoplesViewModel
import com.reylortechnology.aklimdakihediye.Views.PartScreens.PeoplesViewScreens.PeopleDetailScreen
import com.reylortechnology.aklimdakihediye.Views.PartScreens.PeoplesViewScreens.PeoplesMainScreen
import com.reylortechnology.aklimdakihediye.models.Enums.ProfileColors

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeoplesView(
    navController: NavController,
    viewModel: PeoplesViewModel = hiltViewModel()
) {

    val peopleScreenObserver = remember {
        mutableStateOf<PeoplesViewObserver>(PeoplesViewObserver.MainScreen)
    }
    val dialogObserver = viewModel.dialogStateObserver.collectAsState()
    val selectedPeople = viewModel.selectedPeople.collectAsState()
    val peoples = viewModel.peoples.collectAsState(emptyList())
    val toastEvent = viewModel.toastEvent.collectAsState(initial = null)
    val context = LocalContext.current

    // Toast mesajlarını göster
    LaunchedEffect(toastEvent.value) {
        toastEvent.value?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    val profileImages = listOf(
            R.drawable.man_1,
            R.drawable.man_2,
            R.drawable.woman_2,
            R.drawable.mom_1,
        R.drawable.mom_2
    )

    // Toast mesajlarını göster
    LaunchedEffect(toastEvent.value) {
        toastEvent.value?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

        when (dialogObserver.value) {
            DialogStateObserver.Open -> {

            }
            DialogStateObserver.None -> {}
            DialogStateObserver.OpenAddPeopleDialog -> {
                AddPeopleDialog(
                    onDismissRequest = { viewModel.openDialog(DialogStateObserver.None) },
                    modifier = Modifier,
                ) {
                    viewModel.addPeople(it)
                }
            }

            is DialogStateObserver.imageSelector -> {

                Dialog(
                    onDismissRequest = { viewModel.openDialog(DialogStateObserver.None) },
                    properties = DialogProperties(
                        decorFitsSystemWindows = true,
                        windowTitle = "Arkadaşınızın Fotoğrafını Değiştirin"
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.6f)

                    ) {
                        val selectedImage = remember {
                            mutableStateOf((dialogObserver.value as DialogStateObserver.imageSelector).imageId)
                        }
                        val selectedColor = remember {
                            mutableStateOf((dialogObserver.value as DialogStateObserver.imageSelector).color)
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 25.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = MaterialTheme.shapes.small
                                ),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            PeoplePhotoSelector(
                                photoList = profileImages,
                                cardSize = 160.dp,
                                photoColor = selectedColor.value,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) { imageId, color ->
                                selectedImage.value = imageId
                                selectedColor.value = color
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(0.95f)
                                    .fillMaxHeight(0.25f)
                                    .horizontalScroll(rememberScrollState()),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ProfileColors.entries.forEach {
                                    ColorButton(
                                        color = it,
                                        modifier = Modifier
                                            .fillMaxHeight(0.7f)
                                            .aspectRatio(1f),
                                        isSelected = selectedColor.value == it.color
                                    ) { selected ->
                                        selectedColor.value = selected
                                    }
                                    Spacer(Modifier.width(10.dp))
                                }
                            }
                            Button(
                                onClick = {
                                    viewModel.setNewColorAndImage(
                                        imageId = selectedImage.value,
                                        color = selectedColor.value
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth(0.3f)
                                    .fillMaxHeight(0.2f)
                            ) {
                                Text(
                                    stringResource(R.string.save_text)
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            modifier = Modifier
                                .padding(10.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.errorContainer,
                                    shape = MaterialTheme.shapes.extraSmall
                                )
                                .align(Alignment.TopEnd)
                                .clickable {
                                    viewModel.openDialog(DialogStateObserver.None)
                                },
                            tint = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.surface
                ),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {

                        if (peopleScreenObserver.value is PeoplesViewObserver.MainScreen) {
                            Text(
                                text = stringResource(R.string.screen_titles_peoples),
                            )
                        } else if (peopleScreenObserver.value is PeoplesViewObserver.PersonDetailScreen) {
                            Text(
                                text = (peopleScreenObserver.value as PeoplesViewObserver.PersonDetailScreen).people.value.peopleName,
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                when (peopleScreenObserver.value) {
                                    PeoplesViewObserver.AddPersonScreen -> {
                                        peopleScreenObserver.value = PeoplesViewObserver.MainScreen
                                    }

                                    PeoplesViewObserver.MainScreen -> {
                                        navController.popBackStack()
                                    }

                                    is PeoplesViewObserver.PersonDetailScreen -> {
                                        peopleScreenObserver.value = PeoplesViewObserver.MainScreen
                                    }
                                }
                            },
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
                if (peopleScreenObserver.value is PeoplesViewObserver.MainScreen) {
                    FloatingActionButton(
                        onClick = {
                            viewModel.openDialog(DialogStateObserver.OpenAddPeopleDialog)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Person Button"
                        )
                    }
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
                            onEdit = { peopleUiModel ->
                                viewModel.setSelectedPeople(peopleUiModel.people)
                                peopleScreenObserver.value = PeoplesViewObserver.PersonDetailScreen(
                                    mutableStateOf(peopleUiModel.people)
                                )
                            }
                        )
                    }

                    is PeoplesViewObserver.PersonDetailScreen -> {
                        if (selectedPeople.value == null) {
                            viewModel.setSelectedPeople(it.people.value)
                        }else {
                            PeopleDetailScreen(
                                modifier = Modifier.padding(paddingValues),
                                peoples = it.people,
                                selectedPeople = selectedPeople.value!!,
                                openImageDialog = { peoples ->
                                    viewModel.setSelectedPeople(peoples)
                                    viewModel.openDialog(
                                        DialogStateObserver.imageSelector(
                                            imageId = peoples.image,
                                            color = peoples.color
                                        )
                                    )
                                },
                                updatePeopleInformation = { people ->
                                    viewModel.setNewPeopleInformation(
                                        people = people,
                                        screenState = peopleScreenObserver
                                    )
                                },
                                context = context
                            )
                        }
                    }

                    is PeoplesViewObserver.AddPersonScreen -> {
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @PreviewScreenSizes
    @Composable
    private fun PeoplesPreview() {
        PeoplesView(navController = rememberNavController())
    }