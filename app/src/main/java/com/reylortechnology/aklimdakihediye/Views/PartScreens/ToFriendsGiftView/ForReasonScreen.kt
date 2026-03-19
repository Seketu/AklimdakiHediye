package com.reylortechnology.aklimdakihediye.Views.PartScreens.ToFriendsGiftView

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.reylortechnology.aklimdakihediye.Compose.ButtonRectangleSelectedBox
import com.reylortechnology.aklimdakihediye.Compose.ColoredCheckBox
import com.reylortechnology.aklimdakihediye.R
import com.reylortechnology.aklimdakihediye.ViewModels.ToFriendsGiftViewModel
import com.reylortechnology.aklimdakihediye.models.Enums.ForWhyGifts
import com.reylortechnology.aklimdakihediye.models.Enums.GiftVibes
import com.reylortechnology.aklimdakihediye.models.Enums.Prices
import com.reylortechnology.aklimdakihediye.models.GiftInformationModels.GiftInformation
import com.reylortechnology.aklimdakihediye.ui.theme.OrelegaOneRegular


@Composable
fun ForReasonScreen(
    modifier: Modifier = Modifier,
    context: Context,
    onActionButton : (GiftInformation) -> Unit,
) {

    val screenConf = LocalConfiguration.current
    val screenHeight = screenConf.screenHeightDp.dp
    val screenWidth = screenConf.screenWidthDp.dp

    val selectedPrices = remember {
        mutableStateOf<Prices?>(null)
    }

    val selectedReason = remember {
        mutableStateOf<ForWhyGifts?>(null)
    }

    val selectedVibe = remember {
        mutableStateOf<GiftVibes?>(null)
    }

    val specialDemand = remember {
        mutableStateOf("")
    }

    Box(
        modifier = modifier,
    ){
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(5.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Column(

                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.idea_icon),
                            "",
                            modifier = Modifier
                                .height(screenHeight * 0.0429f)
                                .aspectRatio(1f),
                            contentScale = ContentScale.FillBounds,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                        )

                        Text(
                            "Bu hediyeyi neden alıyoruz ?",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontFamily = OrelegaOneRegular
                            )
                        )
                    }
                    LazyRow(
                        modifier = Modifier
                            .height(screenHeight * 0.21f)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(ForWhyGifts.entries) { why ->
                            ButtonRectangleSelectedBox(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(screenWidth * 0.31f),
                                isSelected = selectedReason.value == why,
                                imageSource = why.imageRes,
                                text = stringResource(why.textRes),
                                onClick = {
                                    selectedReason.value =
                                        if (selectedReason.value == why) null else why
                                }
                            )
                        }
                    }
                }
            }
            item {
                Column(
                    verticalArrangement = Arrangement
                        .spacedBy(5.dp)
                ){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.grammar_icon),
                            "",
                            modifier = Modifier
                                .height(screenHeight * 0.0429f)
                                .aspectRatio(1f),
                            contentScale = ContentScale.FillBounds,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                        )

                        Text(
                            "Özel İstekler ?",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontFamily = OrelegaOneRegular
                            )
                        )
                    }

                    TextField(
                        value = specialDemand.value,
                        onValueChange = {specialDemand.value = it},
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.secondary,
                            unfocusedContainerColor =  MaterialTheme.colorScheme.secondary,
                            errorIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = MaterialTheme.colorScheme.onSecondary,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSecondary,
                            focusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSecondary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(screenHeight * 0.14f),
                        shape = MaterialTheme.shapes.medium,
                        label = {
                            Text(
                                "Mesela kitap veya akıllı saat istemiyorum vb."
                            )
                        }
                    )
                }

            }
            item {
                Column(
                    verticalArrangement = Arrangement
                        .spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.vibe_icon),
                            "",
                            modifier = Modifier
                                .height(screenHeight * 0.0429f)
                                .aspectRatio(1f),
                            contentScale = ContentScale.FillBounds
                        )

                        Text(
                            "Hediye Ruhu ?",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontFamily = OrelegaOneRegular
                            )
                        )
                    }

                    FlowRow(
                        modifier = Modifier,
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                    ) {
                        GiftVibes.entries.forEach {vibe ->
                            ColoredCheckBox(
                                text = stringResource(vibe.textSource),
                                modifier = Modifier,
                                selectedValue = selectedVibe.value == vibe,
                                selectedColor = MaterialTheme.colorScheme.secondary,
                                onSelectedColor = MaterialTheme.colorScheme.onSecondary,
                                prefixImage = null,
                            ) {
                                if (selectedVibe.value == vibe) selectedVibe.value = null else selectedVibe.value = vibe
                            }
                        }
                    }

                }
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.money_icon),
                            "",
                            modifier = Modifier
                                .height(screenHeight * 0.0429f)
                                .aspectRatio(1f),
                            contentScale = ContentScale.FillBounds
                        )

                        Text(
                            "Bütçemiz Nedir ?",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontFamily = OrelegaOneRegular
                            )
                        )
                    }

                    FlowRow(
                        modifier = Modifier,
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Prices.entries.forEach {prices ->
                            ColoredCheckBox(
                                text = stringResource(prices.textSource),
                                modifier = Modifier,
                                selectedValue = selectedPrices.value == prices,
                                selectedColor = MaterialTheme.colorScheme.secondary,
                                onSelectedColor = MaterialTheme.colorScheme.onSecondary,
                                prefixImage = null,
                            ) {
                                if (selectedPrices.value == prices) selectedPrices.value = null else selectedPrices.value = prices
                            }
                        }
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(screenHeight * 0.07f))
            }
        }

        Button(
            modifier = Modifier
                .padding(bottom = screenHeight * 0.005f)
                .align(
                    Alignment.BottomCenter
                )
                .fillMaxWidth(0.94f)
                .height(screenHeight * 0.05f),
            onClick = {
                    if (
                        selectedPrices.value == null ||
                        selectedReason.value == null ||
                        selectedVibe.value == null
                    ){
                        Toast
                            .makeText(
                                context,
                                "Lütfen Gerekli Alanları Doldurun",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    } else {
                        onActionButton(GiftInformation(
                            forWhy = context.getString(selectedReason.value!!.textRes) ,
                            specialDemand = specialDemand.value,
                            giftVibe = selectedVibe.value!!,
                            price = selectedPrices.value!!
                        ))
                    }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = MaterialTheme.shapes.small,
            border = BorderStroke(
                3.dp,
                MaterialTheme.colorScheme.onPrimary
            ),
            contentPadding = PaddingValues(3.dp)
        ) {
            Text(
                "Hediyeni Bul !",
                style = MaterialTheme.typography.headlineSmall.copy(
                    shadow = Shadow(
                        color = MaterialTheme.colorScheme.onPrimary,
                        offset = Offset(4f, 4f),
                        blurRadius = 10f
                    )
                )
            )
        }
    }
}

@Preview
@Composable
private fun ForReasonPreview() {
    ForReasonScreen(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        LocalContext.current,
        onActionButton = {}
    )
}
