package com.reylortechnology.aklimdakihediye.Views.PartScreens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.reylortechnology.aklimdakihediye.R
import com.reylortechnology.aklimdakihediye.Compose.SavedGiftCard
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.SavedGifts
import com.reylortechnology.aklimdakihediye.NavController.LocalNavController
import com.reylortechnology.aklimdakihediye.ViewModels.SavedVariablesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedGiftScreen(
    modifier: Modifier = Modifier,
    viewModel: SavedVariablesViewModel = hiltViewModel(),
    navController: NavController,
) {
    BackHandler {
        navController.navigate(LocalNavController.MainScreen) {
            popUpTo(0) { inclusive = true }
        }
    }

    val giftList = viewModel.savedGifts.collectAsState(initial = emptyList())
    val showDeleteMode = remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.saved_gift_screen_label),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(LocalNavController.MainScreen) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.content_description_back)
                        )
                    }
                },
                actions = {
                    if (giftList.value.isNotEmpty()) {
                        IconButton(
                            onClick = { showDeleteMode.value = !showDeleteMode.value }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(R.string.content_description_delete_mode),
                                tint = if (showDeleteMode.value) MaterialTheme.colorScheme.error
                                      else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        when (giftList.value.isEmpty()) {
            true -> EmptyGiftScreen(
                modifier = Modifier.padding(paddingValues),
                navController = navController
            )
            false -> SuccessGiftScreen(
                modifier = Modifier.padding(paddingValues),
                giftList = giftList.value,
                navController = navController,
                viewModel = viewModel,
                showDeleteMode = showDeleteMode.value,
                onDeleteModeChanged = { showDeleteMode.value = it }
            )
        }
    }
}

@Composable
fun EmptyGiftScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Empty state icon
            Card(
                modifier = Modifier.size(120.dp),
                shape = CircleShape,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            // Empty state text
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.no_gifts_yet),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = stringResource(R.string.track_favorite_gifts),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
            }

            // Call to action button
            Card(
                modifier = Modifier
                    .clickable {
                        navController.navigate(LocalNavController.MainScreen) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Hediye Ara",
                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun SuccessGiftScreen(
    modifier: Modifier = Modifier,
    giftList: List<SavedGifts>,
    navController: NavController,
    viewModel: SavedVariablesViewModel,
    showDeleteMode: Boolean,
    onDeleteModeChanged: (Boolean) -> Unit
) {
    val deleteList = remember { mutableListOf<Int>() }
    val deleteCount = remember { mutableStateOf(0) }

    // Delete mode kapandığında tüm checkbox'ları temizle
    LaunchedEffect(showDeleteMode) {
        if (!showDeleteMode) {
            deleteList.clear()
            deleteCount.value = 0
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = giftList,
                key = { it.giftId }
            ) { gift ->
                val checked = remember(gift.giftId) { mutableStateOf(false) }

                // Delete mode kapandığında checkbox'ı temizle
                LaunchedEffect(showDeleteMode) {
                    if (!showDeleteMode) {
                        checked.value = false
                    }
                }

                AnimatedVisibility(
                    visible = true,
                    enter = scaleIn(
                        animationSpec = spring(),
                        initialScale = 0.8f
                    ) + fadeIn(animationSpec = tween(300)),
                    exit = scaleOut(
                        animationSpec = spring(),
                        targetScale = 0.8f
                    ) + fadeOut(animationSpec = tween(300))
                ) {
                    SavedGiftCard(
                        gift = gift,
                        checked = checked,
                        onCheckedChange = { isChecked ->
                            if (isChecked) {
                                // Checkbox seçildi
                                if (!deleteList.contains(gift.giftId)) {
                                    deleteList.add(gift.giftId)
                                    deleteCount.value = deleteList.size
                                }
                                // İlk seçimde delete mode'u aç
                                if (!showDeleteMode) {
                                    onDeleteModeChanged(true)
                                }
                            } else {
                                // Checkbox seçimi kaldırıldı
                                deleteList.remove(gift.giftId)
                                deleteCount.value = deleteList.size
                            }
                        },
                        enabled = showDeleteMode
                    )
                }
            }

            // Bottom spacing for FAB
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // Floating Delete Action
        AnimatedVisibility(
            visible = showDeleteMode && deleteCount.value > 0,
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = spring()
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = spring()
            ) + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(16.dp)
                    .clickable {
                        viewModel.deleteGift(deleteList.toList())
                        deleteList.clear()
                        deleteCount.value = 0
                        onDeleteModeChanged(false)
                    },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onError,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "${deleteCount.value} öğeyi sil",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onError
                    )
                }
            }
        }

        // Cancel Delete Mode FAB
        if (showDeleteMode) {
            FloatingActionButton(
                onClick = {
                    onDeleteModeChanged(false)
                    deleteList.clear()
                    deleteCount.value = 0
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Cancel"
                )
            }
        }
    }
}
