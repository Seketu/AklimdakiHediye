package com.example.aklimdakihediye.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.aklimdakihediye.ui.theme.ColorDailyUserInfoButton


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
        Image(
            painter = painterResource(source),
            "",
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize(0.7f)
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
    text : String,
    lottieModifier : Modifier,
    cardText : String
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
            color = Color.Black,
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
                fontSize = 8.sp
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
    source: Int
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ){
        Image(
            painter = painterResource(source),
            "",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.fillMaxSize()
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
    ){

        Button(
            onClick = {
                onClick.invoke()
            },
            border = BorderStroke(5.dp, ColorDailyUserInfoButton),
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