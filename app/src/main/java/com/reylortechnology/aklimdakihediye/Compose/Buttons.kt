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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.CardGiftcard
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextPainter.paint
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.reylortechnology.aklimdakihediye.ObserverClasses.ZodiacStatus
import com.reylortechnology.aklimdakihediye.R
import com.reylortechnology.aklimdakihediye.models.ComposeModels.SearchCardModel
import com.reylortechnology.aklimdakihediye.models.Enums.ProfileColors
import com.reylortechnology.aklimdakihediye.ui.theme.JustAnotherHandFont
import com.reylortechnology.aklimdakihediye.ui.theme.OleoScript
import com.reylortechnology.aklimdakihediye.ui.theme.OrelegaOneRegular


/** Sonuç grid'lerinde (gerçek kart + iskelet) ortak kullanılan minimum kart genişliği. */
val SearchCardMinWidth = 150.dp

private val SearchCardImageShape = RoundedCornerShape(12.dp)

@Composable
fun SearchCard(
    modifier: Modifier = Modifier,
    searchCardModel: SearchCardModel,
    onBuyButton : () -> Unit,
    onSaveButton : () -> Unit,
    isSaved : Boolean = false
) {
    val colors = MaterialTheme.colorScheme

    val saveContainerColor by animateColorAsState(
        targetValue = if (isSaved) colors.primary else colors.surfaceContainerLowest.copy(alpha = 0.92f),
        animationSpec = tween(durationMillis = 300),
        label = "Save Container Color"
    )
    val saveContentColor by animateColorAsState(
        targetValue = if (isSaved) colors.onPrimary else colors.primary,
        animationSpec = tween(durationMillis = 300),
        label = "Save Content Color"
    )

    Card(
        onClick = onBuyButton,
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = colors.surfaceContainerLow,
            contentColor = colors.onSurface
        ),
        border = BorderStroke(1.dp, colors.outlineVariant.copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .aspectRatio(1f)
                .clip(SearchCardImageShape)
                .background(colors.surfaceContainerHighest)
        ) {
            // Görsel yüklenemezse / yoksa arkada hediye ikonu görünür
            Icon(
                imageVector = Icons.Rounded.CardGiftcard,
                contentDescription = null,
                tint = colors.onSurfaceVariant.copy(alpha = 0.4f),
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(40.dp)
            )
            AsyncImage(
                model = searchCardModel.imageUrl,
                contentDescription = searchCardModel.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Surface(
                onClick = onSaveButton,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(34.dp),
                shape = CircleShape,
                color = saveContainerColor,
                contentColor = saveContentColor,
                shadowElevation = 2.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (isSaved) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder,
                        contentDescription = stringResource(R.string.content_description_save_gift),
                        tint = saveContentColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 2.dp, bottom = 10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = searchCardModel.name,
                style = MaterialTheme.typography.titleSmall,
                color = colors.onSurface,
                minLines = 2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = searchCardModel.price.ifBlank { "—" },
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = colors.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            FilledTonalButton(
                onClick = onBuyButton,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(36.dp),
                shape = MaterialTheme.shapes.medium,
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.search_card_browse),
                    style = MaterialTheme.typography.labelLarge,
                    maxLines = 1
                )
                Spacer(Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.OpenInNew,
                    contentDescription = null,
                    tint = colors.onSecondaryContainer,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

/** Ürünler aranırken gösterilen, [SearchCard] ile aynı ölçülerdeki parıltılı iskelet kart. */
@Composable
fun SearchCardSkeleton(modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    val lineShape = RoundedCornerShape(4.dp)

    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = colors.surfaceContainerLow),
        border = BorderStroke(1.dp, colors.outlineVariant.copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .aspectRatio(1f)
                .shimmer(SearchCardImageShape)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp, top = 2.dp, bottom = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(Modifier.fillMaxWidth(0.9f).height(14.dp).shimmer(lineShape))
            Box(Modifier.fillMaxWidth(0.6f).height(14.dp).shimmer(lineShape))
            Box(Modifier.fillMaxWidth(0.4f).height(18.dp).shimmer(lineShape))
            Box(Modifier.fillMaxWidth().height(36.dp).shimmer(MaterialTheme.shapes.medium))
        }
    }
}

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
                    Log.d("Selected Color ", color.toString())
                }
            )
    )
}
@Composable
fun ColoredCheckBox(
    modifier: Modifier = Modifier,
    text: String,
    selectedValue: Boolean,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    onSelectedColor: Color = MaterialTheme.colorScheme.onPrimary,
    prefixImage: Int? = null,
    onCheckedChange: (Boolean) -> Unit,
) {
    val animatedContainerColor by animateColorAsState(
        targetValue = if (!selectedValue) Color.White else selectedColor,
        animationSpec = tween(durationMillis = 300),
        label = "Container Color"
    )

    val animatedContentColor by animateColorAsState(
        targetValue = if (!selectedValue) Color.Black else onSelectedColor,
        animationSpec = tween(durationMillis = 300),
        label = "Content Color"
    )

    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .clickable { onCheckedChange(!selectedValue) }
            .background(animatedContainerColor)
            .border(
                width = 2.dp,
                color = animatedContentColor,
                shape = MaterialTheme.shapes.small
            )
            .padding(vertical = 5.dp, horizontal = 5.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (prefixImage != null) {
            Image(
                painter = painterResource(prefixImage),
                contentDescription = null,
                modifier = Modifier
                    .size(35.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.width(4.dp))
        }

        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            color = animatedContentColor
        )

        if (selectedValue) {
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                painter = painterResource(R.drawable.close_icon),
                contentDescription = null,
                tint = animatedContentColor,
                modifier = Modifier.size(10.dp)
            )
        }
    }
}


@Composable
fun ButtonRectangleSelectedBox(
    modifier: Modifier = Modifier,
    imageSource : Int,
    text : String,
    onClick: () -> Unit,
    isSelected: Boolean
) {
    val containerColor  = if (isSelected) MaterialTheme.colorScheme.primary  else MaterialTheme.colorScheme.secondaryContainer
    val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary  else MaterialTheme.colorScheme.onSecondaryContainer
    val shadowColor = MaterialTheme.colorScheme.onSurface
    Box(
        modifier = modifier
            .drawBehind {
                drawIntoCanvas { canvas ->

                    val paint = Paint().asFrameworkPaint().apply {
                        color = android.graphics.Color.BLACK
                        setShadowLayer(
                            10f,
                            10f,
                            30f,
                            shadowColor.copy(0.2f).toArgb()
                        )
                    }

                    canvas.nativeCanvas.drawRoundRect(
                        0f,
                        0f,
                        size.width,
                        size.height,
                        30f, 30f,
                        paint
                    )
                }
            }
            .clip(MaterialTheme.shapes.medium)
            .border(
                shape = MaterialTheme.shapes.medium,
                color = if (isSelected) Color.Black else Color.Gray,
                width = if (isSelected) 5.dp else 5.dp
            )
            .background(containerColor)
            .clickable {
                onClick()
            }
        ,
    ){
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(imageSource),
                contentDescription = "Button",
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .weight(1f)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = OleoScript
                ),
                color = contentColor
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
        Checkbox(
            onCheckedChange = { onClick() },
            checked = isSelected,
            modifier = Modifier
                .padding(end = 15.dp, top = 15.dp)
                .size(15.dp)
                .align(
                    Alignment.TopEnd
                )
        )
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
            contentDescription = "zodiac buttons",
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
                .fillMaxHeight()
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
                fontSize = 13.sp,
                overflow = TextOverflow.Ellipsis
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

@Preview
@Composable
private fun ButtonsPreview() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SearchCard(
                modifier = Modifier
                    .width(SearchCardMinWidth),
                searchCardModel = SearchCardModel(
                    name = "Hediye Kartı",
                    price = "₺100",
                    imageUrl = "https://productimages.hepsiburada.net/s/49/400-592/10986386358322.jpg",
                    url = "https://www.hepsiburada.com/100-tl-hediye-karti-hediye-kartlari-p-HBV00000KZQG8?magaza=Hepsiburada",
                ),
                onBuyButton = { },
                onSaveButton = { }
            )
        }
}