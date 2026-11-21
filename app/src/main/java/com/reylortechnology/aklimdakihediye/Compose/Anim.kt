package com.reylortechnology.aklimdakihediye.Compose

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CardGiftcard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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

/**
 * İskelet (placeholder) alanlar için tema renklerine uyan, soldan sağa akan parıltı efekti.
 */
fun Modifier.shimmer(shape: Shape = RectangleShape): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "Shimmer")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1300, easing = LinearEasing)
        ),
        label = "Shimmer Progress"
    )
    val baseColor = MaterialTheme.colorScheme.surfaceContainerHighest
    val highlightColor = MaterialTheme.colorScheme.surfaceContainerLow

    this
        .clip(shape)
        .drawBehind {
            val width = size.width
            val startX = -width + progress * width * 3f
            drawRect(
                brush = Brush.linearGradient(
                    colors = listOf(baseColor, highlightColor, baseColor),
                    start = Offset(startX, 0f),
                    end = Offset(startX + width, size.height)
                )
            )
        }
}

/**
 * Hediye ikonu zıplayıp sallanırken etrafında dönen bir halka ve nabız gibi atan bir hale çizer.
 * Tüm renkler aktif temadan alınır, açık/koyu modda aynı şekilde uyumludur.
 */
@Composable
fun GiftLoadingAnimation(
    modifier: Modifier = Modifier,
    animationSize: Dp = 120.dp
) {
    val colors = MaterialTheme.colorScheme
    val transition = rememberInfiniteTransition(label = "Gift Loading")

    val ringRotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing)
        ),
        label = "Ring Rotation"
    )
    val haloPulse by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Halo Pulse"
    )
    val giftBounce by transition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1200
                0f at 0
                1f at 300 using FastOutSlowInEasing
                0f at 600 using FastOutSlowInEasing
                0f at 1200
            }
        ),
        label = "Gift Bounce"
    )
    val giftWiggle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1200
                0f at 0
                -8f at 150
                8f at 300
                -4f at 450
                0f at 600
                0f at 1200
            }
        ),
        label = "Gift Wiggle"
    )

    Box(
        modifier = modifier.size(animationSize),
        contentAlignment = Alignment.Center
    ) {
        // Nabız gibi büyüyüp kaybolan hale
        Box(
            modifier = Modifier
                .fillMaxSize(0.62f)
                .graphicsLayer {
                    val scale = 1f + haloPulse * 0.6f
                    scaleX = scale
                    scaleY = scale
                    alpha = (1f - haloPulse) * 0.45f
                }
                .background(colors.primary, CircleShape)
        )

        // Kuyruklu yıldız gibi dönen halka
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { rotationZ = ringRotation }
        ) {
            val strokeWidth = size.minDimension * 0.05f
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(Color.Transparent, colors.tertiary, colors.primary)
                ),
                startAngle = 0f,
                sweepAngle = 300f,
                useCenter = false,
                topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f),
                size = Size(size.width - strokeWidth, size.height - strokeWidth),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        // Ortadaki zıplayan hediye
        Surface(
            modifier = Modifier
                .fillMaxSize(0.6f)
                .graphicsLayer {
                    translationY = -giftBounce * animationSize.toPx() * 0.06f
                    rotationZ = giftWiggle
                },
            shape = CircleShape,
            color = colors.primaryContainer,
            shadowElevation = 4.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Rounded.CardGiftcard,
                    contentDescription = null,
                    tint = colors.onPrimaryContainer,
                    modifier = Modifier.fillMaxSize(0.55f)
                )
            }
        }
    }
}

/** Sırayla zıplayan üç nokta. */
@Composable
fun LoadingDots(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    dotSize: Dp = 8.dp
) {
    val transition = rememberInfiniteTransition(label = "Loading Dots")

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(dotSize * 0.75f),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(3) { index ->
            val progress by transition.animateFloat(
                initialValue = 0f,
                targetValue = 0f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 900
                        0f at 0
                        1f at 225 using FastOutSlowInEasing
                        0f at 450 using FastOutSlowInEasing
                        0f at 900
                    },
                    initialStartOffset = StartOffset(index * 150)
                ),
                label = "Dot $index"
            )
            Box(
                modifier = Modifier
                    .size(dotSize)
                    .graphicsLayer {
                        translationY = -progress * dotSize.toPx() * 0.8f
                        alpha = 0.4f + 0.6f * progress
                    }
                    .background(color, CircleShape)
            )
        }
    }
}
