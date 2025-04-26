package com.example.aklimdakihediye.Compose

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aklimdakihediye.LocalDatabase.Models.SavedGifts
import com.example.aklimdakihediye.ObserverClasses.RelationshipStatus
import com.example.aklimdakihediye.models.ComposeModels.SearchCardModel
import com.example.aklimdakihediye.R
import com.example.aklimdakihediye.ui.theme.ColorSavedGiftCardBg
import com.example.aklimdakihediye.ui.theme.ColorSearchCardBg
import com.example.aklimdakihediye.ui.theme.ColorSearchCardButton
import com.example.aklimdakihediye.ui.theme.ColorSearchCardTextBg
import com.example.aklimdakihediye.ui.theme.ColorSearchScreenBg


@Composable
fun StepperIndicator(currentStep: Int, modifier: Modifier, totalSteps: Int) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..totalSteps) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(if (i == currentStep) 12.dp else 6.dp)
                    .background(
                        if (i == currentStep) Color.DarkGray else Color.Gray,
                        shape = RoundedCornerShape(50)
                    )
            )
            Spacer(Modifier.width(10.dp))
        }
    }
}

@Composable
fun ReleationshipStateInfo(
    modifier: Modifier = Modifier,
    selectedOption: MutableState<RelationshipStatus>,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = modifier
            .background(Color.Transparent)
            .fillMaxWidth(0.9f)
    ) {
        Text("İlişki Durumu")

        Row(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(5.dp)
                    .clickable(
                        onClick = {
                            selectedOption.value = RelationshipStatus.MARRIED
                        })
                    .background(Color.White, RoundedCornerShape(10.dp))
            ) {
                Checkbox(
                    checked = selectedOption.value == RelationshipStatus.MARRIED,
                    enabled = true,
                    onCheckedChange = {
                        if (it) selectedOption.value =
                            RelationshipStatus.MARRIED else selectedOption.value =
                            RelationshipStatus.NONE
                    })
                Text("Evli",color = Color.Black)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(5.dp)
                    .clickable(
                        onClick = {
                            selectedOption.value = RelationshipStatus.SweatHeart
                        })
                    .background(Color.White, RoundedCornerShape(10.dp))
            ) {

                Checkbox(
                    checked = selectedOption.value == RelationshipStatus.SweatHeart,
                    enabled = true,
                    onCheckedChange = {
                        if (it) selectedOption.value =
                            RelationshipStatus.SweatHeart else RelationshipStatus.NONE
                    })
                Text("Sevgili",color = Color.Black)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(5.dp)
                    .clickable(
                        onClick = {
                            selectedOption.value = RelationshipStatus.FLORT
                        })
                    .background(Color.White, RoundedCornerShape(10.dp))
            ) {
                Checkbox(
                    checked = selectedOption.value == RelationshipStatus.FLORT,
                    enabled = true,
                    onCheckedChange = {
                        if (it) selectedOption.value =
                            RelationshipStatus.FLORT else RelationshipStatus.NONE
                    })
                Text("Flört",color = Color.Black)
            }
        }
    }
    Spacer(Modifier.height(25.dp))
}

@Composable
fun SearchCard(item: SearchCardModel, modifier: Modifier = Modifier,saveGift: () -> Unit) {
    val context = LocalContext.current
    var parentHeight by remember { mutableStateOf(0) }
    val localConfig = LocalConfiguration.current
    val screenHeight = localConfig.screenHeightDp.dp
    Box(
        modifier = modifier
            .onGloballyPositioned {
                parentHeight = it.size.height
            }
            .clip(RoundedCornerShape(20.dp))
            .background(ColorSearchCardBg)

    ) {
        if (parentHeight > 0) {
            val animHeightPx = parentHeight * 0.2f
            val animHeightDp = with(LocalDensity.current) { animHeightPx.toDp() }

            LottieAnim(
                source = R.raw.main_anim,
                modifier = Modifier
                    .height(animHeightDp)
                    .align(Alignment.BottomCenter),
                contentScale = ContentScale.FillWidth,
                secondModifier = Modifier
                    .graphicsLayer(scaleY = 0.2f) // 1f = normal, istersen animasyonla azaltabilirsin
            )
        }

        Column {
            Box(
                modifier = Modifier
                    .background(ColorSearchCardTextBg)
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    item.name,
                    maxLines = 1,
                    fontSize = 24.sp
                )
            }
            Spacer(modifier = Modifier.height(screenHeight * 0.01f))
            Box(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
            ) {
                Text(
                    item.description,
                    maxLines = 3,
                    fontSize = 16.sp
                )
            }
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(5.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        saveGift.invoke()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text(
                        "Kaydet",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ColorSearchCardButton
                    )
                ) {
                    Text(
                        "Sayfaya Git",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}


@Composable
fun SavedGiftCard(
    modifier: Modifier = Modifier,
    gift: SavedGifts,
    checked: MutableState<Boolean>,
    onCheckedChange: (Boolean) -> Unit,
    enabled : MutableState<Boolean>
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .background(ColorSavedGiftCardBg)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                gift.giftName,
            )
            Spacer(Modifier.weight(1f))
            if (enabled.value){
                Checkbox(
                    modifier = Modifier
                        .clickable{
                            checked.value = !checked.value
                        },
                    checked = checked.value,
                    onCheckedChange = {onCheckedChange.invoke(it)},
                )
            }else{

            }

        }
        Row (
            modifier = Modifier.padding(horizontal = 10.dp)
        ){
            Text(gift.giftDescription)
        }
        Row(
            modifier = Modifier
                .padding(end = 10.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {

            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(gift.giftUrl))
                    context.startActivity(intent)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                )
            ) {
                Text(
                    "Sayfaya Git",
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun UserInformationPlace(
    modifier: Modifier = Modifier,
    list : MutableList<Pair<String, MutableState<String>>>,
    editEnable : MutableState<Boolean>
    ) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        list.forEach { (label, value) ->
            UserInformationRow(
                modifier = Modifier.fillMaxWidth(),
                label = label,
                value = value,
                enabled = editEnable
            )
        }
    }
}
