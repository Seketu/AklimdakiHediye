package com.reylortechnology.aklimdakihediye.Views.PartScreens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.reylortechnology.aklimdakihediye.Compose.GiftLoadingAnimation
import com.reylortechnology.aklimdakihediye.Compose.LoadingDots
import com.reylortechnology.aklimdakihediye.Compose.SearchCardMinWidth
import com.reylortechnology.aklimdakihediye.Compose.SearchCardSkeleton
import com.reylortechnology.aklimdakihediye.R
import com.reylortechnology.aklimdakihediye.ui.theme.AklimdakiHediyeTheme
import com.reylortechnology.aklimdakihediye.ui.theme.ThemePreviews
import kotlinx.coroutines.delay

private const val LOADING_MESSAGE_INTERVAL_MS = 2400L

/**
 * Genel yükleme ekranı. Arka planı Scaffold ile aynı tema rengidir; böylece üst barın
 * yuvarlatılmış köşelerinin arkasında farklı (koyu modda siyah) bir renk görünmez.
 *
 * @param messages Birden fazla verilirse sırayla değişerek gösterilir.
 */
@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    messages: List<String> = listOf(stringResource(R.string.loading)),
    showLogo: Boolean = true
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            if (showLogo) {
                Image(
                    painter = painterResource(id = R.drawable.logo_alpha),
                    contentDescription = stringResource(R.string.content_description_logo),
                    modifier = Modifier.fillMaxWidth(0.6f)
                )
            }

            GiftLoadingAnimation(animationSize = 132.dp)

            RotatingLoadingMessage(messages = messages)

            LoadingDots()
        }
    }
}

/** Hediye önerisi (Gemini) hazırlanırken gösterilen, sırayla değişen mesajlı yükleme ekranı. */
@Composable
fun GiftIdeasLoadingScreen(modifier: Modifier = Modifier) {
    LoadingScreen(
        modifier = modifier,
        showLogo = false,
        messages = listOf(
            stringResource(R.string.loading_thinking_gift_ideas),
            stringResource(R.string.loading_matching_personality),
            stringResource(R.string.loading_almost_ready)
        )
    )
}

/**
 * Ürün kartları yüklenirken gösterilir: üstte küçük animasyonlu durum satırı,
 * altında gerçek kartlarla aynı düzende parıltılı iskelet kartlar.
 */
@Composable
fun SearchResultLoadingScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GiftLoadingAnimation(animationSize = 44.dp)
            Text(
                text = stringResource(R.string.loading_searching_products),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )
            LoadingDots(dotSize = 6.dp)
        }

        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Adaptive(SearchCardMinWidth),
            userScrollEnabled = false,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            items(count = 6) {
                SearchCardSkeleton()
            }
        }
    }
}

@Composable
private fun RotatingLoadingMessage(messages: List<String>) {
    var index by remember(messages) { mutableIntStateOf(0) }

    if (messages.size > 1) {
        LaunchedEffect(messages) {
            while (true) {
                delay(LOADING_MESSAGE_INTERVAL_MS)
                index = (index + 1) % messages.size
            }
        }
    }

    AnimatedContent(
        targetState = messages.getOrElse(index) { "" },
        transitionSpec = {
            (fadeIn(tween(300)) + slideInVertically(tween(300)) { it / 2 }) togetherWith
                    (fadeOut(tween(200)) + slideOutVertically(tween(200)) { -it / 2 })
        },
        label = "Loading Message"
    ) { message ->
        Text(
            text = message,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@ThemePreviews
@Composable
private fun GiftIdeasLoadingPreview() {
    AklimdakiHediyeTheme {
        GiftIdeasLoadingScreen()
    }
}

@ThemePreviews
@Composable
private fun SearchResultLoadingPreview() {
    AklimdakiHediyeTheme {
        SearchResultLoadingScreen()
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingScreenPreview() {
    AklimdakiHediyeTheme {
        LoadingScreen()
    }
}
