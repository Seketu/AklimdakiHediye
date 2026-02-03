package com.reylortechnology.aklimdakihediye.Compose

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.reylortechnology.aklimdakihediye.ObserverClasses.ZodiacStatus
import com.reylortechnology.aklimdakihediye.R
import com.reylortechnology.aklimdakihediye.models.Enums.ProfileColors
import com.reylortechnology.aklimdakihediye.ui.theme.onSecondaryLight
import com.reylortechnology.aklimdakihediye.ui.theme.outlineLight


@Composable
fun ColorButton(
    modifier: Modifier = Modifier,
    color : ProfileColors,
    isSelected : Boolean,
    onColorChange : (Color)-> Unit
) {
    val border = if (isSelected) BorderStroke(3.dp, Color.White) else BorderStroke(0.dp, Color.Transparent)
    Box(
        modifier = modifier
            .background(
                color = color.color,
                shape = CircleShape
            )
            .clip(CircleShape)
            .border(
                border = border,
                shape = CircleShape,
            )
            .clickable(
                onClick = {
                    onColorChange.invoke(color.color)
                    Log.d("Selected Color " , color.toString())
                }
            )
    )
}
@Composable
fun ColoredCheckBox(
    modifier: Modifier = Modifier,
    text: String,
    selectedValue : Boolean,
    selectedColor : Color = MaterialTheme.colorScheme.primary,
    onSelectedColor : Color = MaterialTheme.colorScheme.onPrimary,
    prefixImage : Int? = null,
    onCheckedChange : (Boolean) -> Unit,
) {
    val animatedContainerColor by animateColorAsState(
        targetValue = if (!selectedValue) Color.White else selectedColor,
        animationSpec = tween(durationMillis = 300),
        label = "Container Color anim"
    )

    val animatedContentColor by animateColorAsState(
        targetValue = if (!selectedValue) Color.Black else onSelectedColor,
        animationSpec = tween(durationMillis = 300),
        label = "Content Color Anim"
    )
        Surface(
            modifier = modifier
                .clip(androidx.compose.material.MaterialTheme.shapes.small)
                .clickable(
                    onClick = {
                        onCheckedChange(!selectedValue)
                    }
                ),
            shape = MaterialTheme.shapes.small,
            color = animatedContainerColor,
            border = BorderStroke(2.dp , color = animatedContentColor),
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 5.dp, horizontal = 5.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (
                    prefixImage != null
                ){
                    Image(
                        painter = painterResource(prefixImage),
                        "",
                        contentScale = ContentScale.FillHeight,
                        modifier = Modifier
                            .size(15.dp)
                            .aspectRatio(1f)
                    )
                }
                Text(
                    text,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    color = animatedContentColor
                )

                if (selectedValue){
                    Icon(
                        painter = painterResource(R.drawable.close_icon),
                        "",
                        tint = androidx.compose.material.MaterialTheme.colors.onPrimary,
                        modifier = Modifier
                            .size(10.dp)
                    )
                }
            }

        }

}

@Composable
fun ButtonWithCheckBox(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    image: Int,
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Button(
        modifier = modifier,
        onClick = {
            onClick.invoke()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Image(
            painter = painterResource(image),
            "Mention Button",
            contentScale = ContentScale.FillHeight
        )

        Text(text, color = androidx.compose.material.MaterialTheme.colors.onSecondary)
        Checkbox(
            checked = checked,
            onCheckedChange = {onCheckedChange.invoke(it)},
        )
    }
}

@Composable
fun ZodiacButton(
    modifier: Modifier = Modifier,
    imageSource: Int,
    zodiacName: String,
    checkBoxState: MutableState<ZodiacStatus>,
    boxZodiac: ZodiacStatus,
    noneZodiacStatus: ZodiacStatus = ZodiacStatus.None
) {
    Button(
        onClick = {
            checkBoxState.value = boxZodiac
        },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White
        ),
    ) {
        Image(
            painter = painterResource(imageSource),
            "zodiac buttons",
            contentScale = ContentScale.FillHeight,
        )
        Text(zodiacName, color = Color.Black)
        Checkbox(
            checked = checkBoxState.value == boxZodiac,
            onCheckedChange = {
                checkBoxState.value = if (checkBoxState.value == boxZodiac) noneZodiacStatus
                else boxZodiac
            },
            colors = CheckboxDefaults.colors(
                checkedColor = Color.Black,
                uncheckedColor = Color.White,
                checkmarkColor = Color.White
            )
        )
    }
}


@Composable
fun MainListRowButton(
    modifier: Modifier = Modifier,
    source: Int,
    text: String
) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LottieAnim(
            modifier = Modifier
                .fillMaxHeight(0.8f)
                .width(75.dp),
            source = source,
            contentScale = ContentScale.FillWidth
        )
        Text(
            text,
            color = Color.White,
            fontSize = 15.sp
        )
    }
}

@Composable
fun MainListDailyButton(
    modifier: Modifier = Modifier,
    source: Int,
    contentScale: ContentScale,
    text: String,
    lottieModifier: Modifier,
    cardText: String
) {

    Column(
        modifier = modifier
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .padding(5.dp),
            color = Color.White,
            fontSize = 24.sp,
            fontFamily = FontFamily.Monospace
        )

        Row(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = cardText,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(1f),
                color = Color.White,
                fontSize = 13.sp
            )

            LottieAnim(
                modifier = lottieModifier,
                contentScale = contentScale,
                source = source
            )
        }
    }
}

@Composable
fun NavigationButton(
    modifier: Modifier = Modifier,
    source: Int,
    text: String
) {

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = modifier
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {

            Image(
                painter = painterResource(source),
                "",
                modifier = Modifier
                    .fillMaxSize(0.8f)
                    .padding(2.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                contentScale = ContentScale.Fit
            )
        }

        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold
        )
    }

}

@Composable
fun BorderButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
    ) {

        Button(
            onClick = {
                onClick.invoke()
            },
            border = BorderStroke(5.dp, MaterialTheme.colorScheme.outline),
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            )
        ) {
            Text(
                text,
                color = Color.Black,
                fontSize = 15.sp
            )
        }
    }
}