package com.example.aklimdakihediye.compose

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun GiftInfoScreenPrice(
    modifier: Modifier = Modifier,
    label: String,
    stateFirst: MutableState<String>,
    stateSecond: MutableState<String>
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            label,
            fontSize = 26.sp,
            color = Color.White
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            PriceInformationTextField(
                value1 = stateFirst,
                value2 = stateSecond,
                label1 = "Minimum",
                label2 = "Maximum"
            )
        }
    }
}

@Composable
fun GiftInfo(
    modifier: Modifier = Modifier,
    state : MutableState<String>,
    label: String
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            label,
            fontSize = 22.sp,
            textAlign = TextAlign.Start,
            color = Color.White
        )

        UserInfoTextField(
            modifier = Modifier
                .fillMaxWidth(),
            string = state
        )
    }
}

@Composable
fun InfoScreen(
    modifier: Modifier = Modifier,
    state : MutableState<String>,
    label : String,
    color: Color
) {
    Column(
        modifier = modifier
            .background(Color.Transparent),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
            Text(
                label,
                fontSize = 22.sp,
                textAlign = TextAlign.Start,
                color = color
            )
        Spacer(modifier = Modifier.padding(15.dp))
            UserInfoTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                string = state
            )
    }
}