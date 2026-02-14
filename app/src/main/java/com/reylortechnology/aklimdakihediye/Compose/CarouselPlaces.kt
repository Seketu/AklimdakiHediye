package com.reylortechnology.aklimdakihediye.Compose

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PeoplePhotoSelector(
    modifier: Modifier = Modifier,
    photoList: List<Int>,
    photoColor: Color,
    cardSize: Dp = 100.dp,
    onColorAndImageSelected: (imageId: Int, color: Color) -> Unit
) {
    val pagerState = rememberPagerState(
        pageCount = { photoList.size },
        initialPage = photoList.size / 2
    )

    // Seçim değiştiğinde tetikleme
    LaunchedEffect(pagerState.currentPage, photoColor) {
        if (photoList.isNotEmpty()) {
            onColorAndImageSelected(photoList[pagerState.currentPage], photoColor)
        }
    }

    val context = LocalContext.current
    val density = LocalDensity.current

    // BoxWithConstraints ile parent genişliğine göre dinamik hesaplama
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        // Mevcut genişliği al (Ekran değil, parent genişliği)
        val availableWidth = maxWidth

        // Pager'ın contentPadding değeri: (Toplam Genişlik - Kart Boyutu) / 2
        // Bu formül, seçili öğenin tam ortada durmasını garanti eder.
        val horizontalPadding = (availableWidth - cardSize) / 2

        // Coil için px cinsinden boyut hesabı (Performans için)
        val cardSizePx = with(density) { cardSize.toPx().toInt() }

        HorizontalPager(
            state = pagerState,
            pageSize = PageSize.Fixed(cardSize),
            contentPadding = PaddingValues(horizontal = horizontalPadding),
            pageSpacing = 15.dp, // Biraz daha ferahlık için
            beyondViewportPageCount = 1,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(cardSize + 20.dp) // Border/Gölge taşmaları için güvenli alan
        ) { pageIndex ->

            // Animasyon Hesaplamaları
            val pageOffset = (pagerState.currentPage - pageIndex) + pagerState.currentPageOffsetFraction
            val absOffset = kotlin.math.abs(pageOffset)

            // Scale: Merkezdeki 1f, kenardakiler 0.85f'e kadar düşsün
            val scale = 1f - (absOffset * 0.15f).coerceIn(0f, 0.15f)
            // Alpha: Merkezdeki net, kenardakiler biraz silik
            val alpha = 1f - (absOffset * 0.4f).coerceIn(0f, 0.4f)

            val isSelected = (pagerState.currentPage == pageIndex)

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        this.alpha = alpha
                    }
                    .size(cardSize)
            ) {
                // Seçili Durum Çerçevesi
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(
                                width = 4.dp,
                                color = photoColor,
                                shape = CircleShape
                            )
                    )
                }

                // Resim Alanı
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp) // Çerçeve ile resim arası boşluk
                        .clip(CircleShape)
                        .background(Color.LightGray)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(photoList[pageIndex])
                            .crossfade(false) // Dialog içinde titremeyi önler
                            .size(cardSizePx) // Coil'e tam boyutu vererek bellek tasarrufu yapıyoruz
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CarouselPrev() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        var selectedPhoto by remember { mutableStateOf<Int?>(null) }
        var selectedColor by remember { mutableStateOf(Color.Transparent) }
        PeoplePhotoSelector(
            modifier = Modifier
                .height(75.dp)
                .fillMaxWidth(),
            photoList = listOf(
                com.reylortechnology.aklimdakihediye.R.drawable.man_1,
                com.reylortechnology.aklimdakihediye.R.drawable.man_1,
                com.reylortechnology.aklimdakihediye.R.drawable.man_1,
                com.reylortechnology.aklimdakihediye.R.drawable.man_1,
                com.reylortechnology.aklimdakihediye.R.drawable.man_1,
                com.reylortechnology.aklimdakihediye.R.drawable.man_1,
            ),
            photoColor = Color.Red,
            onColorAndImageSelected = {imageId, color ->
                selectedPhoto = imageId
               selectedColor = color}
        )
    }
}