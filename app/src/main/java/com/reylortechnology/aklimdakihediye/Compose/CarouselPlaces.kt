package com.reylortechnology.aklimdakihediye.Compose

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PeoplePhotoSelector(
    modifier: Modifier = Modifier,
    photoList: List<Int>,
    photoColor: Color,
    cardSize: Dp = 100.dp,
    onColorAndImageSelected : (
            imageId : Int,
            color : Color)-> Unit
) {

    val density = LocalDensity.current
    val screenWidth = remember { mutableStateOf(0.dp) }



    val pagerState = rememberPagerState(
        pageCount = { photoList.size },
        initialPage = photoList.size / 2
    )
    BoxWithConstraints(
        modifier = modifier
    ) {

        screenWidth.value = with(density) { maxWidth }
        if (screenWidth.value > 0.dp) {
            val horizontalPadding = (screenWidth.value - cardSize) / 2
            HorizontalPager(
                modifier = Modifier
                    .fillMaxSize()
                    .animateContentSize(),
                state = pagerState,
                pageSize = PageSize.Fixed(cardSize),
                contentPadding = PaddingValues(horizontal = horizontalPadding),
                pageSpacing = 5.dp,
                beyondViewportPageCount = 3
            ) { pageIndex ->
                LaunchedEffect(pagerState.currentPage) {
                    onColorAndImageSelected(photoList[pagerState.currentPage], photoColor)
                }
                val pageOffset =
                    (pagerState.currentPage - pageIndex) + pagerState.currentPageOffsetFraction
                val absOffset = kotlin.math.abs(pageOffset)

                val scale = 1f - (absOffset * 0.15f).coerceIn(0f, 0.15f)
                val alpha = 1f - (absOffset * 0.5f).coerceIn(0.15f, 0.6f)
                val isSelected = (pagerState.currentPage == pageIndex)

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .graphicsLayer {
                            scaleY = scale
                            scaleX = scale
                            this.alpha = alpha
                        }
                        .border(
                            width = if (isSelected) 15.dp else 0.dp,
                            brush = Brush.radialGradient(
                                0.7f to photoColor,
                                1f to photoColor.copy(alpha = 0.2f)
                            ),
                            shape = CircleShape
                        )
                        .padding(15.dp)
                        .clip(CircleShape)
                ) {
                    Image(
                        painter = painterResource(photoList[pageIndex]),
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize() // Resmi kutuya doldur
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