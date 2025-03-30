package com.example.aklimdakihediye.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition


@Composable
fun LottieAnim(
    modifier: Modifier = Modifier,
    secondModifier : Modifier = Modifier,
    source : Int,
    contentScale: ContentScale
    ) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(source)
    )

    val progression by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Box(
        modifier = modifier
    ){
        LottieAnimation(
            composition = composition,
            progress = { progression },
            contentScale = contentScale,
            maintainOriginalImageBounds = true,
            modifier = secondModifier
                .fillMaxSize()
        )
    }

}